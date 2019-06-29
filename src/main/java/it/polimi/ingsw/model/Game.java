package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.GameStatus;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Observable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the abstract representation of the Adrenaline match. It is the Model class of the MVC pattern
 */
public class Game extends Observable<MatchMessage> {

    /**
     * This attributes contains the current player of the match
     */
    private Player currentPlayer;

    /**
     * This attributes contains the current turn of the match
     */
    private Turn currentTurn;

    /**
     * This attributes contains the index of the ammo deck
     */
    private int ammoIndex = 0;

    /**
     * This attributes contains the status of the match
     */
    private GameStatus status;

    /**
     * This attribute contains the all the ammo tiles
     */
    private List<AmmoTile> ammoTileDeck;

    /**
     * This attributes contains the index of the weapon deck
     */
    private int weaponIndex = 0;

    /**
     * This attributes represents the deck of the weapon. So it contains all the Adrenaline weapons.
     */
    private List<Card> weaponDeck;

    /**
     * This attributes contains the index of the powerup deck.
     */
    private int powerupIndex = 0;

    /**
     * This attributes is used to check if the powerup deck is shuffled.
     */
    private boolean powerupShuffled = false;

    /**
     * This attributes represents the deck of the powerup. So it contains all the Adrenaline powerups.
     */
    private List<Card> powerupDeck;

    /**
     * This attributes contains the players of the match.
     */
    private List<Player> players;

    /**
     * This attribute contains the game board of the match.
     */
    private GameBoard gameBoard;

    /**
     * This attribute contains the skulls number of the match.
     */
    private int skullsNumber;

    /**
     * This attribute is used to check if the game is, or is not, in frenzy mode.
     */
    private boolean frenzyMode = false;

    /**
     * This attribute is used to check if the game is, or is not, ended.
     */
    private boolean gameEnded = false;

    /**
     * This attribute contains the last player who takes the turn in normal mode, before the game turns in frenzy.
     */
    private Player lastPlayerBeforeFrenzy =null;

    /**
     * This constructor instantiates the Game object
     */
    public Game(){
        weaponDeck = new ArrayList<>();
        powerupDeck = new ArrayList<>();
        ammoTileDeck = new ArrayList<>();
        players = new ArrayList<>();

        Node root = parsingXMLFile(Constants.GAME_CONFIG_FILEPATH);
        NodeList nodeList = root.getChildNodes();
        for(int i=0; i<nodeList.getLength();i++) {
            Node cardNode = nodeList.item(i);
            if (cardNode.getNodeType() != Node.TEXT_NODE)
                switch(cardNode.getNodeName()){
                    case "weapons":
                        buildCardDeck(weaponDeck, cardNode);
                        break;
                    case "powerups":
                        buildCardDeck(powerupDeck, cardNode);
                        break;
                    case "ammos":
                        buildAmmoDeck(ammoTileDeck, cardNode);
                        break;
                    case "skull":
                        setSkullsNumber(cardNode);
                        break;
                        default: break;
                }
        }
        shuffleDeck(weaponDeck);
        shuffleDeck(powerupDeck);
        shuffleDeck(ammoTileDeck);
    }

    /**
     * This method is used to build the card deck.
     * @param deck representing the deck (the container which will be update).
     * @param card representing the XML Node read from the configuration file.
     */
    private void buildCardDeck(List<Card> deck, Node card){
        NodeList cardNode = card.getChildNodes();
        String id = "";
        String name = "";
        String path = "";
        int count=0;
        int quantity = 1;
        Element e = (Element) card;
        if(!e.getAttribute("quantity").equals(""))
            quantity = Integer.parseInt(e.getAttribute("quantity"));
        for(int i=0; i<cardNode.getLength();i++) {
            Node wpNode = cardNode.item(i);
            NodeList stringNode = wpNode.getChildNodes();
            while (count < stringNode.getLength()) {
                Node node = stringNode.item(count);
                if (node.getNodeType() != Node.TEXT_NODE) {
                    if (node.getNodeName().equalsIgnoreCase("id"))
                        id = node.getFirstChild().getNodeValue();
                    else if (node.getNodeName().equalsIgnoreCase("name"))
                        name = node.getFirstChild().getNodeValue();
                    else if (node.getNodeName().equalsIgnoreCase("path"))
                        path = node.getFirstChild().getNodeValue();
                }
                count++;
            }
            if(stringNode.getLength()!=0)
                for(int j=0; j<quantity; j++)
                    deck.add(new Card(id, name, path));
            count = 0;
        }
    }

    /**
     * This method sets the skull number of the match
     * @param node representing the node read from the XML file, which contains the skull number.
     */
    private void setSkullsNumber(Node node){
        skullsNumber =Integer.parseInt(node.getFirstChild().getNodeValue());
    }

    /**
     * This method builds the Ammo deck.
     * @param deck representing the deck (the container which will be update).
     * @param ammo representing the XML Node read from the configuration file.
     */
    private void buildAmmoDeck(List<AmmoTile> deck, Node ammo){
        NodeList ammoNode = ammo.getChildNodes();
        String id = "";
        boolean powerup = false;
        String colors = "";
        int count=0;
        for(int i=0; i<ammoNode.getLength();i++) {
            Node aNode = ammoNode.item(i);
            NodeList stringNode = aNode.getChildNodes();
            while (count < stringNode.getLength()) {
                Node node = stringNode.item(count);
                if (node.getNodeType() != Node.TEXT_NODE) {
                    if (node.getNodeName().equalsIgnoreCase("id"))
                        id = node.getFirstChild().getNodeValue();
                    else if (node.getNodeName().equalsIgnoreCase("pow"))
                        powerup = Boolean.parseBoolean(node.getFirstChild().getNodeValue());
                    else if (node.getNodeName().equalsIgnoreCase("colors"))
                        colors = node.getFirstChild().getNodeValue();
                }
                count++;
            }
            if(stringNode.getLength()!=0)
                deck.add(createAmmo(id, powerup, colors));
            count = 0;
        }
    }

    /**
     * This method instantiates an ammo
     * @param id representing the id of the ammo
     * @param powerup a boolean which is true if the ammo has a powerup
     * @param colors representing the color of the ammo
     * @return an AmmoTile representing the ammo instantiated
     */
    private AmmoTile createAmmo(String id, boolean powerup, String colors){
        AmmoTile a;
        if(powerup)
            a = new AmmoTile(parseColor(colors.charAt(0)), parseColor(colors.charAt(1)), null, true);
        else
            a = new AmmoTile(parseColor(colors.charAt(0)), parseColor(colors.charAt(1)), parseColor(colors.charAt(2)), false);
        a.setId(id);
        return a;
    }

    /**
     * This method is used to parse a character value representing a Color
     * @param c char representing the the value you want to parse
     * @return the corresponding color
     */
    private Color parseColor(char c){
        switch(c){
            case 'y':
                return Color.YELLOW;
            case 'b':
                return Color.BLUE;
            case 'r':
                return Color.RED;
            default:
                return null;
        }
    }

    /**
     * This method instantiates a XML parser and parse the XML file you passed as parameter
     * @param path a string representing the XML file you want to parse
     * @return a Node representing the root of the XML file
     */
    private Node parsingXMLFile(String path){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                    Logger.logErr("WARNING : " + e.getMessage()); // do nothing
                    throw e;
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    Logger.logErr("ERROR : " + e.getMessage());
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    Logger.logErr("FATAL : " + e.getMessage());
                    throw e;
                }
            });
            Document document = builder.parse(new File(path));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            root.normalize();
            return root;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.logErr(e.getMessage());
            throw new NullPointerException("Parsing failed");
        }
    }

    /**
     * This method return the ammo tile deck
     * @return a Collection representing the ammo tile deck
     */
    public List<AmmoTile> getAmmoTileDeck() {
        return new ArrayList<>(ammoTileDeck);
    }

    /**
     * This method return the weapon deck
     * @return a Collection representing the weapon deck
     */
    public List<Card> getWeaponDeck() {
        return new ArrayList<>(weaponDeck);
    }

    /**
     * This method return the powerup deck
     * @return a Collection representing the powerup deck
     */
    public List<Card> getPowerupDeck() {
        return new ArrayList<>(powerupDeck);
    }

    /**
     * This method return all the players of the game
     * @return a collection containing all the players of the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * This method return the gameboard
     * @return the bameboard of the game
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * This method return the skull number of the game
     * @return an integer representing the skull number of the match
     */
    public int getSkullsNumber(){return skullsNumber;}

    /**
     * This method force the skull number of the match to the value you put as parameter
     * @param skullNumber representing the skull number of the game
     */
    public void forceSkullsNumber(int skullNumber){
        this.skullsNumber =skullNumber;
    }

    /**
     * This method is used to draw a weapon
     * @return a Weapon card drawn from the Weapon deck
     */
    public Weapon drawWeapon(){
        if(weaponIndex>weaponDeck.size()-1)
            throw new IndexOutOfBoundsException("All the weapons are already on the boards");
        return new Weapon(weaponDeck.get(weaponIndex++));
    }

    /**
     * This method checks if you can draw a weapon
     * @return true if you can draw a weapon
     */
    private boolean canDrawWeapon(){
        return (weaponIndex<=weaponDeck.size()-1);
    }

    /**
     * This method is used to draw a powerup
     * @return a Weapon card drawn from the powerup deck
     */
    public Powerup drawPowerup(){
        if(powerupIndex>powerupDeck.size()-1) {
            shuffleDeck(powerupDeck);
            powerupShuffled = true;
            powerupIndex = 0;
        }
        if(powerupShuffled)
            return (Powerup) powerupDeck.get(powerupIndex++);
        else {
            powerupDeck.set(powerupIndex, new Powerup(powerupDeck.get(powerupIndex)));
            return (Powerup) powerupDeck.get(powerupIndex++);
        }
    }

    /**
     * This method is used to draw an ammo tile
     * @return a Weapon card drawn from the ammo tile deck
     */
    public AmmoTile drawAmmoTile(){
        if(ammoIndex>ammoTileDeck.size()-1){
            shuffleDeck(ammoTileDeck);
            ammoIndex = 0;
        }
        return ammoTileDeck.get(ammoIndex++);
    }

    /**
     * This method is used to shuffle a Deck
     * @param deck the deck ypu want to shuffle
     */
    private void shuffleDeck(List<?> deck){
        Collections.shuffle(deck);
    }

    /**
     * This method is used to add a player to the game
     * @param player a Player obj representing the player you want to add to the game.
     */
    public void addPlayer(Player player) {
        if(getPlayers().size()>4)
            throw new IndexOutOfBoundsException("Maximum number of player reached [5]");
        players.add(player);
    }

    /**
     * This method is used to compute the winner of the game
     * @return a Map containing the Players and their points
     */
    public Map<Player, Integer> calcWinner() {
        Map<Player, Integer> map = new HashMap<>();
        for (Player player : players) map.put(player, player.getScore());
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * This method checks if the game is in frenzy mode
     * @return true if the game is in frenzy mode
     */
    public boolean isFrenzy() {
        return frenzyMode;
    }

    /**
     * This method returns the game status
     * @return the game status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * This method sets who is the last player who plays in the normal mode, before the Frenzy mode is activated
     * @param lastPlayer representing the last player who plays in the normal mode, before the Frenzy mode is activated
     */
    public void setFrenzy(Player lastPlayer){
        frenzyMode=true;
        this.lastPlayerBeforeFrenzy =lastPlayer;
    }

    /**
     * This method return the last player who plays before the frenzy mode
     * @return the last player who plays before the frenzy mode
     */
    Player getLastPlayerBeforeFrenzy() {
        return lastPlayerBeforeFrenzy;
    }

    /**
     * This method sets the current player
     * @param playerIndex index representing the position where the player you want to set as current is in the collection of players
     */
    public void setCurrentPlayer(int playerIndex) {
        this.currentPlayer = players.get(playerIndex);
    }

    /**
     * This method return the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * This method fill the game board: all the square which are not empty, if possible, will be filled
     */
    private void fillGameboard(){
        int[] boardDimension=gameBoard.getBoardDimension();
        for(int i=0;i<boardDimension[0];i++){
            for(int j=0;j<boardDimension[1];j++){
                if(gameBoard.hasSquare(i,j)){
                    fillSquare(i,j);
                }
            }
        }
    }

    /**
     * This method fill a square (if possible)
     * @param x representing the row index of the gameboard
     * @param y representing the y index of the gameboard
     */
    private void fillSquare(int x, int y) {
        Square square=gameBoard.getSquare(x,y);
        if(!square.isFull()){
            if(!gameBoard.isSpawnPoint(x,y)){
                AmmoSquare ammoSquare=(AmmoSquare)square;
                ammoSquare.setAmmoTile(this.drawAmmoTile());
            }
            else{
                WeaponSquare weaponSquare=(WeaponSquare)square;
                while(this.canDrawWeapon()&&!weaponSquare.isFull()){
                    try {
                        weaponSquare.addWeapon(this.drawWeapon());
                    }catch(Exception e) {
                        Logger.logErr(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * This method is used to send to the players a notification in order to advise them that the match is started
     */
    public void startMessage() {
        status=GameStatus.CREATED;
        for(int i=0;i<players.size();i++) {
            //first turn number is 1 (not 0)!
            MatchMessage msg= new MatchCreationMessage(players.get(i).getNickname(),i+1,players);
            notify(msg);
        }
    }

    /**
     * This method is used to choose the gameboard type
     * @param mapNumber representing the number of the map you want to set as gameboard
     */
    public void setGameBoard(int mapNumber) {
        switch(mapNumber){
            case 1:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD1_FILEPATH), skullsNumber,mapNumber);
                break;
            case 2:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD2_FILEPATH), skullsNumber,mapNumber);
                break;
            case 3:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD3_FILEPATH), skullsNumber,mapNumber);
                break;
            case 4:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD4_FILEPATH), skullsNumber,mapNumber);
                break;
            default: throw new IllegalArgumentException("An invalid map number has been chosen: game board not initialized");
        }
        fillGameboard();
        BoardUpdateMessage boardUpdateMessage=new BoardUpdateMessage(new GameBoard(gameBoard));
        Logger.logServer("Sending board created message...");
        notify(boardUpdateMessage);
        for (Player p:players) {
            if(p.isFirst()) {
                currentPlayer=p;
                break;
            }
        }
        status=GameStatus.READY;
    }

    /**
     * This method is used get the current turn
     * @return the current turn
     */
    public Turn getCurrentTurn(){
        return currentTurn;
    }

    /**
     * This method create a turn and notify all the players which are playing
     */
    public void createTurn() {
        if(gameEnded)
            throw new IllegalStateException("Game is ended");
        if (currentPlayer == null)
            throw new IllegalStateException("Cannot create turn without having instantiated the Gameboard.");
        if (currentPlayer.getStatus() == PlayerStatus.FIRST_SPAWN)
            throw new IllegalStateException("Current player need to be Spawned.");
        if (currentPlayer.getStatus() != PlayerStatus.PLAYING)
            throw new IllegalStateException("Cannot create turn.[Illegal player status: " + currentPlayer.getStatus().name() + "]");
        if (currentTurn!=null&&currentTurn.getStatus()!= TurnStatus.END)
            throw new IllegalStateException("Cannot create turn. [Another one is still being played]");
        status=GameStatus.PLAYING_TURN;
        notify(new TurnCreationMessage(currentPlayer.getNickname()));
        currentTurn= new Turn(this);
    }

    /**
     * This method ends the turn
     */
    public void endTurn() {
        if(currentTurn==null)  throw new IllegalStateException("No turn is playing now.");
        if(currentTurn.getStatus()!= TurnStatus.END) throw new IllegalStateException("TurnStatus is not END");
        status=GameStatus.CLOSING_TURN;
        fillGameboard();
        //SISTEMO I MORTI
        int deathNumber=0;
        for(Player p:players){
            if(p.getStatus()==PlayerStatus.ALMOST_DEAD||p.getStatus()==PlayerStatus.DEAD) {
                cashDamages(p);
                deathNumber++;
                if(isFrenzy()&&!p.getBoard().isSwitched())
                    p.getBoard().switchBoard();
            }
        }
        //double kill
        if(deathNumber>=2){
            currentPlayer.updateScore(1);
        }
        boolean frenzyInTurn=false;

        if(!isFrenzy()&&gameBoard.getKillshotTrack().size()>= skullsNumber){
            enableFrenzy();
            frenzyInTurn=true;
        }
        if(!frenzyInTurn&&lastPlayerBeforeFrenzy==currentPlayer){
            gameEnded=true;
            status=GameStatus.END;
        }else
            notify(new MatchUpdateMessage(players,gameBoard,frenzyMode));
    }

    /**
     * This method is used to assign the last point to the player and end the game.
     */
    private void endGame() {
        for(Player player:players){
            List<Integer> values=player.getBoard().getBoardScoreValues();
            List<Player> selectedPlayers=player.getBoard().getDamage();
            List<Player> healthBar=player.getBoard().getHealthBar();
            for(Player p:selectedPlayers){
                p.updateScore(values.get(0));
                values.remove(0);
            }
            //first blood?
            if(!player.getBoard().isSwitched()&&!healthBar.isEmpty())
                healthBar.get(0).updateScore(1);
            player.getBoard().clearDamage();
        }
        List<Integer> values=GameBoard.getDefaultScoreValues();
        List<Player> selectedPlayers=gameBoard==null?new ArrayList<>():gameBoard.getKillShotDamage();
        for(Player p:selectedPlayers){
            p.updateScore(values.get(0));
            values.remove(0);
        }
        notify(new GameEndMessage(players));
    }

    /**
     * This method force the the end of the game
     */
    public void forceEndGame(){
        gameEnded=true;
        status=GameStatus.END;
        endGame();
    }

    /**
     * This method assigns the kill points to all the players that damaged the dead Player passed as parameter
     * @param player representing the dead Player
     */
    private void cashDamages(Player player) {
        List<Integer> values=player.getBoard().getBoardScoreValues();
        List<Player> selectedPlayers=player.getBoard().getDamage();
        List<Player> healthBar=player.getBoard().getHealthBar();
        for(Player p:selectedPlayers){
            p.updateScore(values.get(0));
            values.remove(0);
        }
        //first blood?
        if(!player.getBoard().isSwitched())
            healthBar.get(0).updateScore(1);

        player.getBoard().incDeathCounter();
        gameBoard.updateTrack(healthBar.get(10),healthBar.size()>=12);
        //overkill
        if(healthBar.size()>=12){
            healthBar.get(11).getBoard().addMarks(player,1);
        }
        if(player.getStatus()==PlayerStatus.ALMOST_DEAD) {
            player.setStatus(PlayerStatus.DEAD);
            player.setPosition(null);
        }
        player.getBoard().clearDamage();
    }

    /**
     * This method enable the frenzy mode and update all the player boards
     */
    private void enableFrenzy() {
        setFrenzy(currentPlayer);
        for(Player p:players){
            if(p.getBoard().getHealthBar().isEmpty())
                p.getBoard().switchBoard();
        }
    }

    /**
     * This method send a respawn request to the player passed as parameter
     * @param player representing the player to respawn
     * @param firstSpawn true if is the first respawn
     */
    public void respawnPlayerRequest(Player player, boolean firstSpawn) {
        if(player.getStatus()!=PlayerStatus.DEAD&&player.getStatus()!=PlayerStatus.FIRST_SPAWN) throw new IllegalStateException("Cannot respawn the player '"+player.getNickname()+"'. ["+player.getStatus().name()+"]");
        ArrayList<Card> drawnPowerups;
        if(firstSpawn) {
            player.addPowerup(new Powerup(drawPowerup()));
        }
        player.addPowerup(new Powerup(drawPowerup()));
        drawnPowerups=new ArrayList<>(player.getPowerups());
        MatchMessage message=new RespawnRequestMessage(player.getNickname(),drawnPowerups);
        notify(message);
    }

    /**
     * This method complete the player respawn
     * @param player the player to respawn
     * @param discardedPowerup the powerup discarded for the respawn
     */
    public void respawnPlayer(Player player, Card discardedPowerup){
        if(player.getStatus()!=PlayerStatus.DEAD&&player.getStatus()!=PlayerStatus.FIRST_SPAWN){
            Logger.logServer("Invalid answer received form player "+player.getNickname()+". [RESPAWN: status]");
            MatchMessage message= new InvalidAnswerMessage(player.getNickname(),"Cannot respawn the player. ["+player.getStatus().name()+"]");
            notify(message);
            return;
        }
        for (Powerup powerup:player.getPowerups()) {
            if(powerup.getId().equals(discardedPowerup.getId())){
                player.popPowerup(powerup);
                for (WeaponSquare square:gameBoard.getSpawnPoints()){
                    if(powerup.getColor().name().equalsIgnoreCase(square.getRoomColor().name()))
                        player.setPosition(square);
                }
                player.setStatus(player.getStatus()==PlayerStatus.FIRST_SPAWN?PlayerStatus.PLAYING:PlayerStatus.WAITING);
                MatchMessage message=new RespawnMessage(new SimplePlayer(player),powerup);
                notify(message);
                return;
            }
        }
        Logger.logServer("Invalid answer received form player "+player.getNickname()+". [RESPAWN: missing powerup]");
        MatchMessage message= new InvalidAnswerMessage(player.getNickname(),"Cannot respawn the player. ["+player.getStatus().name()+"]");
        notify(message);
        throw new IllegalArgumentException("MISSING POWERUP");
    }

    /**
     * This method is used to wake up a player who was disconnected
     * @param player representing the player that need to be waken up
     */
    public void recoverPlayer(Player player) {
        notify(new RecoveringPlayerMessage(player.getNickname(),players,gameBoard,frenzyMode));
    }

    public void sendLeaderBoard(String nickname, List<Player> disconnected){
        Map<Player, Integer> map=calcWinner();
        List<String> nicknames;
        List<Integer> points;
        for(Player p:disconnected)  map.remove(p);
        nicknames=new ArrayList<>(map.size());
        points=new ArrayList<>(map.size());
        for(Player player: map.keySet()){
            int value=map.get(player);
            int i=0;
            while(i<points.size()&&points.get(i)<value)i++;
            points.add(i,value);
            nicknames.add(i, player.getNickname());
        }

        notify(new LeaderboardMessage(nickname,nicknames,points));
    }
}
