package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.PlayerStatus;
import it.polimi.ingsw.model.enums.TurnStatus;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.matchanswer.RespawnAnswer;
import it.polimi.ingsw.network.controller.messages.matchmessages.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;
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

public class Game extends Observable<MatchMessage> {

    private Player currentPlayer;
    private Turn currentTurn;
    private int ammoIndex = 0;

    /**
     * This attribute contains the all the ammo tiles
     */
    private List<AmmoTile> ammoTileDeck;
    private int weaponIndex = 0;
    private List<Card> weaponDeck;
    private int powerupIndex = 0;
    private boolean powerupShuffled = false;
    private List<Card> powerupDeck;
    private List<Player> players;
    private GameBoard gameBoard;
    private int skullNumber;

    private boolean frenzyMode = false;

    private Player lastPlayerBeforeFrenzy =null;

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
                        setSkullNumber(cardNode);
                        break;
                        default: break;
                }
        }
        shuffleDeck(weaponDeck);
        shuffleDeck(powerupDeck);
        shuffleDeck(ammoTileDeck);
    }

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

    private void setSkullNumber(Node node){
        NodeList nodeList = node.getChildNodes();
        for(int i=0; i<nodeList.getLength();i++){
            Node sNode = nodeList.item(i);
            if(sNode.getNodeType()!=Node.TEXT_NODE){
                skullNumber=Integer.parseInt(sNode.getFirstChild().getNodeValue());
                return;
            }
        }

    }

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

    private AmmoTile createAmmo(String id, boolean powerup, String colors){
        AmmoTile a;
        if(powerup)
            a = new AmmoTile(parseColor(colors.charAt(0)), parseColor(colors.charAt(1)), null, true);
        else
            a = new AmmoTile(parseColor(colors.charAt(0)), parseColor(colors.charAt(1)), parseColor(colors.charAt(2)), false);
        a.setId(id);
        return a;
    }

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

    public List<AmmoTile> getAmmoTileDeck() {
        return new ArrayList<>(ammoTileDeck);
    }

    public List<Card> getWeaponDeck() {
        return new ArrayList<>(weaponDeck);
    }

    public List<Card> getPowerupDeck() {
        return new ArrayList<>(powerupDeck);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int mapNumber) {
        switch(mapNumber){
            case 1:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD1_FILEPATH), skullNumber,mapNumber);
                break;
            case 2:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD2_FILEPATH), skullNumber,mapNumber);
                break;
            case 3:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD3_FILEPATH), skullNumber,mapNumber);
                break;
            case 4:
                this.gameBoard = new GameBoard(parsingXMLFile(Constants.BOARD4_FILEPATH), skullNumber,mapNumber);
                break;
            default: throw new IllegalArgumentException("Wrong map number chosen: game board not initialized");
        }
        fillGameboard();
        BoardUpdateMessage createdMessage=new BoardUpdateMessage(new GameBoard(gameBoard));
        Logger.log("Sending board created message...");
        notify(createdMessage);
        for (Player p:players) {
            if(p.isFirst()) {
                currentPlayer=p;
                return;
            }
        }
    }

    public Weapon drawWeapon(){
        if(weaponIndex>weaponDeck.size()-1)
            throw new IndexOutOfBoundsException("All the weapons are already on the boards");
        return new Weapon(weaponDeck.get(weaponIndex++));
    }

    private boolean canDrawWeapon(){
        return (weaponIndex<=weaponDeck.size()-1);
    }

    //TODO NEED TEST
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

    public AmmoTile drawAmmoTile(){
        if(ammoIndex>ammoTileDeck.size()-1){
            shuffleDeck(ammoTileDeck);
            ammoIndex = 0;
        }
        return ammoTileDeck.get(ammoIndex++);
    }

    private void shuffleDeck(List<?> deck){
        Collections.shuffle(deck);
    }

    public void addPlayer(Player player) {
        if(getPlayers().size()>4)
            throw new IndexOutOfBoundsException("Maximum number of player reached [5]");
        players.add(player);
    }

    public Map<Player, Integer> calcWinner() {
        Map<Player, Integer> map = new HashMap<>();
        for (Player player : players) map.put(player, player.getScore());
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public boolean isFrenzy() {
        return frenzyMode;
    }

    public void setFrenzy(Player lastPlayer){
        frenzyMode=true;
        this.lastPlayerBeforeFrenzy =lastPlayer;
    }

    Player getLastPlayerBeforeFrenzy() {
        return lastPlayerBeforeFrenzy;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    void fillGameboard(){
        int[] boardDimension=gameBoard.getBoardDimension();
        for(int i=0;i<boardDimension[0];i++){
            for(int j=0;j<boardDimension[1];j++){
                if(gameBoard.hasSquare(i,j)){
                    fillSquare(i,j);
                }
            }
        }
    }

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

    public void startMessage() {
        for(int i=0;i<players.size();i++) {
            //first turn number is 1 (not 0)!
            MatchMessage msg= new MatchCreationMessage(players.get(i).getNickname(),i+1,players);
            notify(msg);
        }
    }

    public void createTurn() {
        if (currentPlayer == null)
            throw new IllegalStateException("Cannot create turn without having instantiated the Gameboard.");
        if (currentPlayer.getStatus() == PlayerStatus.FIRST_SPAWN)
            throw new IllegalStateException("Current player need to be Spawned.");
        if (currentPlayer.getStatus() != PlayerStatus.WAITING)
            throw new IllegalStateException("Cannot create turn.[Illegal player status: " + currentPlayer.getStatus().name() + "]");
        if (currentTurn!=null&&currentTurn.getStatus()!= TurnStatus.END)
            throw new IllegalStateException("Cannot create turn. [Another one is still being played]");
        //TODO create turn
    }

    public void endTurn() {

    }

    public void respawnPlayerRequest(Player player, boolean firstSpawn) {
        if(player.getStatus()!=PlayerStatus.DEAD&&player.getStatus()!=PlayerStatus.FIRST_SPAWN) throw new IllegalStateException("Cannot respawn the player '"+player.getNickname()+"'. ["+player.getStatus().name()+"]");
        ArrayList<Card> drawnPowerups= new ArrayList<>(2);
        if(firstSpawn) {
            drawnPowerups.add(new Card(drawPowerup()));
        }
        drawnPowerups.add(new Card(drawPowerup()));
        for(Card card:drawnPowerups)
            player.addPowerup(new Powerup(card));
        MatchMessage message=new RespawnRequestMessage(player.getNickname(),drawnPowerups);
        notify(message);
    }

    public void respawnPlayer(Player player, RespawnAnswer answer){
        if(player.getStatus()!=PlayerStatus.DEAD&&player.getStatus()!=PlayerStatus.FIRST_SPAWN){
            Logger.log("Invalid answer received form player "+player.getNickname()+". [RESPAWN: status]");
            MatchMessage message= new InvalidAnswerMessage(player.getNickname(),"Cannot respawn the player. ["+player.getStatus().name()+"]");
            notify(message);
            return;
        }
        Card recPowerup=answer.getPowerup();
        for (Powerup powerup:player.getPowerups()) {
            if(powerup.getId().equals(recPowerup.getId())){
                player.popPowerup(powerup);
                for (WeaponSquare square:gameBoard.getSpawnPoints()){
                    if(powerup.getColor().name().equalsIgnoreCase(square.getRoomColor().name()))
                        player.setPosition(square);
                }
                MatchMessage message=new RespawnMessage(new SimplePlayer(player),powerup);
                notify(message);
                return;
            }
        }
        Logger.log("Invalid answer received form player "+player.getNickname()+". [RESPAWN: missing powerup]");
        MatchMessage message= new InvalidAnswerMessage(player.getNickname(),"Cannot respawn the player. ["+player.getStatus().name()+"]");
        notify(message);
    }

    void routineMessage(TurnRoutineMessage message){
        notify(message);
    }

    void sendAvailableActions(List<String> actions) {
        MatchMessage message=new TurnActionsMessage(currentPlayer.getNickname(),actions);
        notify(message);
    }

    void sendInvalidAction(String msg){
        MatchMessage message=new InvalidAnswerMessage(currentPlayer.getNickname(),msg);
        notify(message);
    }

    void sendUsablePowerups(List<Card> powerups){
        MatchMessage message=new AvailablePowerupsMessage(currentPlayer.getNickname(),powerups);
        notify(message);
    }

    void sendLoadableWeapons(List<Card> weapons){
        MatchMessage message=new LoadableWeaponsMessage(currentPlayer.getNickname(),weapons);
        notify(message);
    }

    void sendReloadMessage(List<Card> discardedPowerups){
        MatchMessage message = new WeaponReloadMessage(new SimplePlayer(getCurrentPlayer()),discardedPowerups);
        notify(message);
    }

    void sendDamageMessage(List<Player> selected, int value) {
        MatchMessage message=new DamageMessage(currentPlayer.getNickname(),selected,value);
        notify(message);
    }

    void sendMarkMessage(List<Player> selected, int value) {
        MatchMessage message=new MarkMessage(currentPlayer.getNickname(),selected,value);
        notify(message);
    }

    void sendMoveRequestMessage(String player, MatrixHelper matrix){
        MatchMessage message= new MoveRequestMessage(getCurrentPlayer().getNickname(), player, matrix);
        notify(message);

    }

    void sendMoveMessage(String player, int[] position) {
        MatchMessage message=new MoveMessage(player, position);
        notify(message);
    }

    void sendSelectedPowerup(Powerup powerup) {
        MatchMessage message=new SelectedPowerupMessage(currentPlayer.getNickname(),powerup);
        notify(message);
    }
}
