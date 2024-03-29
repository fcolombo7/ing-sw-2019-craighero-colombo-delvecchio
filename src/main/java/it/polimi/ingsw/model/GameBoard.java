package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Direction;
import it.polimi.ingsw.model.enums.RoomColor;
import it.polimi.ingsw.utils.MatrixHelper;
import org.w3c.dom.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * This class represents the game board
 */
public class GameBoard {

    /**
     * This attribute represents the map of the game board
     */
    private Square[][] map;

    private int sourceReference;

    /**
     * This attribute represents the number of row of square contained in the map
     */
    private int rowLength;

    /**
     * This attribute represents the number of column of square contained in the map
     */
    private int colLength;

    /**
     * This attribute represents the player killed contained in the killshot track on the board
     */
    private List<Player> killshotTrack;

    /**
     * This attribute represents if the players on the killshot track have overkilled
     */
    private List<Boolean> overkillTrack;

    /**
     * This attribute contains the starting skull number
     */
    private int skullNumber;

    private List<WeaponSquare> spawnPoints;

    /**
     * This constructor instantiates a GameBoard object
     * @param map representing the Node read from the XML file
     * @param skullNumber representing the number of the skull placed on the board
     */
    public GameBoard(Node map, int skullNumber, int sourceReference){
        this.spawnPoints=new ArrayList<>(Color.values().length);
        NamedNodeMap attributes=map.getAttributes();
        rowLength=3;
        colLength=4;

        this.map=new Square[rowLength][colLength];
        NodeList nodeList=map.getChildNodes();
        int rowCount=0;
        for(int i=0; i<nodeList.getLength();i++){
            Node rowNode=nodeList.item(i);
            if(rowNode.getNodeType()!=Node.TEXT_NODE)
                rowCount=composeRow(rowNode,rowCount);
        }
        this.skullNumber=skullNumber;
        this.killshotTrack=new ArrayList<>(skullNumber);
        this.overkillTrack=new ArrayList<>(skullNumber);
        this.sourceReference=sourceReference;
        initMatrixes();
    }

    public GameBoard(GameBoard other){
        this.sourceReference=other.sourceReference;
        this.rowLength=other.rowLength;
        this.colLength=other.colLength;
        this.overkillTrack=new ArrayList<>(other.overkillTrack);
        this.killshotTrack=new ArrayList<>(other.killshotTrack);
        this.skullNumber=other.skullNumber;
        this.spawnPoints=new ArrayList<>(Color.values().length);
        this.map=new Square[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++){
                if(other.map[i][j]!=null){
                    boolean weaponS=false;
                    for(WeaponSquare w:other.spawnPoints){
                        if(w.getBoardIndexes()[0]==i&&w.getBoardIndexes()[1]==j)
                            weaponS=true;
                    }
                    if(weaponS){
                        map[i][j]=new WeaponSquare((WeaponSquare) other.map[i][j]);
                        spawnPoints.add((WeaponSquare)map[i][j]);
                    }
                    else map[i][j]=new AmmoSquare((AmmoSquare) other.map[i][j]);
                }
            }
        }
    }

    /**
     * This method construct the row of the map at index rowNode.
     * @param rowNode index of the row the method constructs
     * @param rowCount index of the row which is constructed
     * @return integer representing the next row of the map
     */
    private int composeRow(Node rowNode, int rowCount) {
        if(rowCount>=rowLength) throw new IllegalArgumentException("RowLength does not correspond to rows number.");
        int colCount=0;
        NodeList squares=rowNode.getChildNodes();
        for(int j=0;j<squares.getLength();j++){
            Node square=squares.item(j);
            if(square.getNodeType()!=Node.TEXT_NODE){
                colCount=composeColumn(square,colCount,rowCount);
            }
        }
        return rowCount+1;
    }

    /**
     * This method construct the square at rowCount row in colCount position
     * @param square represents square to be constructed
     * @param colCount index of the column where the square is added
     * @param rowCount index of the row where the square is added
     * @return integer representing the next column of the map
     */
    private int composeColumn(Node square, int colCount, int rowCount) {
        if(colCount>=colLength) throw new IllegalArgumentException("ColLength does not correspond to columns number.");
        boolean []doors=null;
        RoomColor roomColor=null;
        String type="";
        int count=0;
        NodeList elements=square.getChildNodes();
        while (count<elements.getLength()){
            Node node=elements.item(count);
            if(node.getNodeType()!=Node.TEXT_NODE){
                if(node.getNodeName().equalsIgnoreCase("type"))
                    type=node.getFirstChild().getNodeValue();
                else if(node.getNodeName().equalsIgnoreCase("color"))
                    roomColor=getRoomColor(node.getFirstChild().getNodeValue());
                else if(node.getNodeName().equalsIgnoreCase("doors"))
                    doors=getDoors(node.getChildNodes());
            }
            count++;
        }
        if(type.equalsIgnoreCase("Weapon")) {
            WeaponSquare weaponSquare=new WeaponSquare(roomColor, doors, new int[]{rowCount, colCount});
            map[rowCount][colCount] = weaponSquare;
            spawnPoints.add(weaponSquare);
        }
        else if(type.equalsIgnoreCase("Ammo"))
            map[rowCount][colCount]=new AmmoSquare(roomColor,doors,new int[]{rowCount,colCount});
        else
            map[rowCount][colCount]=null;
        return colCount+1;
    }

    /**
     * This method returns the array representing the cardinal coordinates of the doors read from the XML
     * @param nodeList represents the doors of the square
     * @return an array of boolean representing the cardinal coordinates of the doors
     */
    private boolean[] getDoors(NodeList nodeList) {
        boolean []door=new boolean[4];
        int count=0;
        for(int i =0;i<nodeList.getLength();i++){
            Node node=nodeList.item(i);
            if(node.getNodeType()!=Node.TEXT_NODE){
                String s=node.getFirstChild().getNodeValue();
                if(!(s.equalsIgnoreCase("true")||s.equalsIgnoreCase("false"))) throw  new IllegalArgumentException("Doors error: door doesn't contain true/false value.");
                door[count]=Boolean.parseBoolean(s);
                count++;
            }
        }
        return door;
    }

    /**
     * This method returns the room color read from the XML file
     * @param stringColor represents the color read
     * @return RoomColor parsed from the String parameter
     */
    private RoomColor getRoomColor(String stringColor) {
        if(stringColor.equalsIgnoreCase("RED")) return RoomColor.RED;
        if(stringColor.equalsIgnoreCase("YELLOW")) return RoomColor.YELLOW;
        if(stringColor.equalsIgnoreCase("BLUE")) return RoomColor.BLUE;
        if(stringColor.equalsIgnoreCase("GREEN")) return RoomColor.GREEN;
        if(stringColor.equalsIgnoreCase("WHITE")) return RoomColor.WHITE;
        if(stringColor.equalsIgnoreCase("PURPLE")) return RoomColor.PURPLE;
        throw new IllegalArgumentException("RoomColor error");
    }

    private void composeMatrix(int x,int y){
        boolean[][] mat=new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++)
            for (int j=0;j<colLength;j++)
                mat[i][j]=false;
        ArrayBlockingQueue<RoomColor> queue=new ArrayBlockingQueue<>(RoomColor.values().length);
        ArrayList<RoomColor> checked=new ArrayList<>(RoomColor.values().length);
        queue.add(map[x][y].getRoomColor());
        incQueue(x,y,queue,checked);
        while(!queue.isEmpty()){
            RoomColor curColor=queue.poll();
            checked.add(curColor);
            for(int r=0;r<rowLength;r++){
                for(int c =0;c<colLength;c++){
                    if(map[r][c]!=null&&map[r][c].getRoomColor()==curColor) mat[r][c]=true;
                }
            }
        }
        map[x][y].setVisibilityMatrix(new MatrixHelper(mat));
    }

    private void incQueue(int r, int c, ArrayBlockingQueue<RoomColor> queue, ArrayList<RoomColor> checked) {
        for (Direction direction:Direction.values()) {
            if(map[r][c].hasDoor(direction)){
                int rx;
                int cx;
                switch(direction){
                    case NORTH: rx=r-1;cx=c; break;
                    case EAST: rx=r;cx=c+1; break;
                    case SOUTH: rx=r+1;cx=c; break;
                    case WEST: rx=r;cx=c-1; break;
                    default: rx=r;cx=r;
                }

                if(!checked.contains(map[rx][cx].getRoomColor()))
                    queue.add(map[rx][cx].getRoomColor());
            }
        }
    }

    /**
     * This method returns the number of skulls still on the board
     * @return integer representing the skull number still on the board
     */
    public int getSkullNumber() {
        return skullNumber;
    }

    /**
     * This method returns the players having killed at least once
     * @return List of Player representing the killshot track on the board
     */
    public List<Player> getKillshotTrack() {
        return new ArrayList<>(killshotTrack);
    }

    /**
     * This method returns the positions on the killshot track where an overkill death has happened
     * @return List of Boolean representing which kills are overkills
     */
    public List<Boolean> getOverkillTrack() {
        return new ArrayList<>(overkillTrack);
    }

    /**
     * This method returns the visibility matrix based on a position
     * @param x represents the x coordinate of the position
     * @param y represents the y coordinate of the position
     * @return MatrixHelper representing the visibility matrix of the square chosen
     */
    public MatrixHelper getVisibilityMatrix(int x,int y){
        if(!hasSquare(x,y)) throw new IllegalArgumentException("Can't get the visibility of the selected square (null).");
        return map[x][y].getVisibilityMatrix();
    }

    public List<WeaponSquare> getSpawnPoints() {
        return new ArrayList<>(spawnPoints);
    }

    public boolean isSpawnPoint(int x,int y){
        if(!hasSquare(x,y)) return false;
        for(WeaponSquare ws:spawnPoints){
            if(ws.getBoardIndexes()[0]==x&&ws.getBoardIndexes()[1]==y)
                return true;
        }
        return false;
    }

    public int getSourceReference(){return sourceReference;}

    public boolean hasSquare(int x, int y){
        return map[x][y]!=null;
    }

    int[] getBoardDimension(){
        return new int[]{rowLength,colLength};
    }

    public Square getSquare(int x,int y){
        if(!hasSquare(x,y)) throw new NullPointerException("Gameboard map has not square at "+x+" - "+y+".");
        return map[x][y];
    }

    private void initMatrixes(){
        for(int i=0;i<rowLength;i++){
            for (int j=0;j<colLength;j++){
                if(map[i][j]!=null)
                    composeMatrix(i,j);
            }
        }
    }

    /**
     * This method update the killshot track when someone is killed
     * @param player represents the killer player
     * @param hasOverkilled represents if the player has overkilled
     */
    public void updateTrack(Player player, boolean hasOverkilled){
        killshotTrack.add(player);
        overkillTrack.add(hasOverkilled);
    }

    /**
     * This method returns a matrix that represent where a player could move if it has a given number of movement
     * The method implements a sort of breadth first search with maximum depth
     * @param x represents the x coordinate of the square the player is at
     * @param y represents the y coordinate of the square the player is at
     * @param distance represents the maximum number of movement the player can do
     * @return MatrixHelper representing the distance matrix as described above
     */
    public MatrixHelper getDistanceMatrix(int x,int y,int distance){
        Queue<Square> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(map[x][y]);

        boolean[][] matrix = new boolean[rowLength][colLength];
        int currentDepth = 0;
        int elementsToDepthIncrease = 1;
        int nextElementsToDepthIncrease = 0;
        List<Square> checkAroundList;

        while (!nodeQueue.isEmpty()) {
            Square current = nodeQueue.poll();
            matrix[current.getBoardIndexes()[0]][current.getBoardIndexes()[1]]=true;
            checkAroundList = checkAround(current, matrix);
            nextElementsToDepthIncrease += checkAroundList.size();
            if (--elementsToDepthIncrease == 0) {
                if (++currentDepth > distance) return new MatrixHelper(matrix);
                elementsToDepthIncrease = nextElementsToDepthIncrease;
                nextElementsToDepthIncrease = 0;
            }
            nodeQueue.addAll(checkAroundList);
        }
        return new MatrixHelper(matrix);
    }

    /**
     * This method check which square around the chosen one is accessible
     * @param co represents the square chosen for the checking
     * @param matrix represents which squares were already visited
     * @return List of Square representing which squares is accessible
     */
    private List<Square> checkAround(Square co, boolean[][] matrix){
        List<Square> retList= new ArrayList<>();
        for(Direction d: Direction.values()){
            int x = co.getBoardIndexes()[0];
            int y = co.getBoardIndexes()[1];
            switch(d){
                case NORTH:
                    if(x>0)
                        x--;
                    break;
                case EAST:
                    if(y<colLength-1)
                        y++;
                    break;
                case SOUTH:
                    if(x<rowLength-1)
                        x++;
                    break;
                case WEST:
                    if(y>0)
                        y--;
                    break;
            }
            if(map[x][y]!=null && !matrix[x][y] && checkAdjacentAccessibility(co, map[x][y], d)){
                    retList.add(map[x][y]);
            }
        }
        return retList;
    }

    /**
     * This method check if a access square is accessible from curr square with one "step"
     * @param curr represents the square start
     * @param access represents the square destination
     * @param d represents the direction where the access square is, compared to the curr one
     * @return boolean representing if access is accessibility
     */
    private boolean checkAdjacentAccessibility(Square curr, Square access, Direction d){
        return access != null && (curr.getRoomColor() == access.getRoomColor() || curr.hasDoor(d));
    }

    MatrixHelper getGameboardMatrix(){
        boolean[][] mat=new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++) mat[i][j]=map[i][j]!=null;
        }
        return new MatrixHelper(mat);
    }

    public Square[][] getMap(){
        Square[][] mat=new Square[rowLength][colLength];
        for(int i=0;i<rowLength;i++) {
            for(int j=0;j<colLength;j++){
                if(map[i][j]!=null){
                    if(isSpawnPoint(i,j))
                        mat[i][j]=new WeaponSquare((WeaponSquare) map[i][j]);
                    else
                        mat[i][j]=new AmmoSquare((AmmoSquare) map[i][j]);
                }
                else mat[i][j]=null;
            }
        }
        return mat;
    }

    /**
     * This method returns a list of player sorted by dealt damages in descending order (in case of equals sum of dealt damages, the one who hit first appears first)
     * @return List containing Player representing the ranking of dealt damages
     */
    public List<Player> getKillShotDamage(){
        Map<Player, Integer> orderDamages = new HashMap<>();
        Integer i = 0;
        for (Player p: killshotTrack) {
            if(!orderDamages.containsKey(p)){
                orderDamages.put(p, i);
                i++;
            }
        }

        Map<Player, Long> counterDamage = killshotTrack.stream().collect(Collectors.groupingBy((p -> p), Collectors.counting()));
        GameBoard.GameComparator tbc = new GameBoard.GameComparator(counterDamage, orderDamages);
        TreeMap<Player, Long> sortedMap = new TreeMap<>(tbc);
        sortedMap.putAll(counterDamage);
        return new ArrayList<>(sortedMap.keySet());
    }

    /**
     *This private class is a player comparator, it compares them basing on two different maps
     */
    private class GameComparator implements Comparator<Player>{

        /**
         * This attribute contains the map primary used for comparing
         */
        Map<Player, Long> base;

        /**
         * This attribute contains the map used to break the tie in case of equal value in base map
         */
        Map<Player, Integer> order;

        /**
         * This constructor initializes the maps of the comparator
         * @param base representing the base map for comparing player
         * @param order representing the map that breaks the tie
         */
        GameComparator(Map<Player, Long> base, Map<Player, Integer> order){
            this.base=base;
            this.order=order;
        }

        /**
         * This method overrides the compare method of Comparator interface
         * @param a representing the first player to compare
         * @param b representing the second player to compare
         * @return int representing if parameter a is bigger/smaller than or equal to parameter b
         */
        @Override
        public int compare(Player a, Player b){
            if(base.get(a) < base.get(b))
                return 1;
            else if(base.get(a) > base.get(b))
                return -1;
            else if(base.get(a).equals(base.get(b))){
                if(order.get(a) < order.get(b))
                    return -1;
                else return 1;
            }
            else return 0;
        }
    }

    /**
     * This method return the default values used to increase the players score
     * @return List of integers representing the default values used to increase the players score
     */
    public static List<Integer> getDefaultScoreValues(){
        List<Integer> values=new ArrayList<>(6);
        values.add(8);
        values.add(6);
        values.add(4);
        values.add(2);
        values.add(1);
        values.add(1);
        return values;
    }
}
