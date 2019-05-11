package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messages.matchmessages.MatchCreationMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchMessage;
import it.polimi.ingsw.model.messages.matchmessages.MatchUpdateMessage;
import it.polimi.ingsw.utils.Costants;
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

        Node root = parsingXMLFile(Costants.GAME_CONFIG_FILEPATH);
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
                this.gameBoard = new GameBoard(parsingXMLFile(Costants.BOARD1_FILEPATH), skullNumber,mapNumber);
                break;
            case 2:
                this.gameBoard = new GameBoard(parsingXMLFile(Costants.BOARD2_FILEPATH), skullNumber,mapNumber);
                break;
            case 3:
                this.gameBoard = new GameBoard(parsingXMLFile(Costants.BOARD3_FILEPATH), skullNumber,mapNumber);
                break;
            case 4:
                this.gameBoard = new GameBoard(parsingXMLFile(Costants.BOARD4_FILEPATH), skullNumber,mapNumber);
                break;
            default: throw new IllegalArgumentException("Wrong map number chosen: game board not initialized");
        }
        fillGameboard();
        MatchMessage update=new MatchUpdateMessage(players,new GameBoard(gameBoard));
        notify(update);
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

    protected Player getLastPlayerBeforeFrenzy() {
        return lastPlayerBeforeFrenzy;
    }

    protected Player getCurrentPlayer() {
        return currentPlayer;
    }

    protected void fillGameboard(){
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
                while(!this.canDrawWeapon()||weaponSquare.isFull()){
                    weaponSquare.addWeapon(this.drawWeapon());
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
}
