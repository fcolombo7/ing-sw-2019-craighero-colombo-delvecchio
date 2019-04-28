package it.polimi.ingsw.model;

import org.w3c.dom.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class represents the game board
 */
public class GameBoard {

    /**
     * This attribute represents the map of the game board
     */
    private static Square[][] map;

    /**
     * This attribute represents the number of row of square contained in the map
     */
    private static int rowLength;

    /**
     * This attribute represents the number of column of square contained in the map
     */
    private static int colLength;

    /**
     * This attribute represents the player killed contained in the killshot track on the board
     */
    private ArrayList<Player> killshotTrack;

    /**
     * This attribute represents if the players on the killshot track have overkilled
     */
    private ArrayList<Boolean> overkillTrack;

    private int skullNumber;

    /**
     * This constructor instantiates a GameBoard object
     * @param map representing the Node read from the XML file
     * @param skullNumber representing the number of the skull placed on the board
     */
    public GameBoard(Node map, int skullNumber){
        NamedNodeMap attributes=map.getAttributes();
        rowLength=Integer.parseInt(attributes.getNamedItem("rowLength").getNodeValue());
        colLength=Integer.parseInt(attributes.getNamedItem("colLength").getNodeValue());
        if(rowLength<=0) throw new IllegalArgumentException("Invalid attribute: rowLength must be >0.");
        if(colLength<=0) throw new IllegalArgumentException("Invalid attribute: colLength must be >0.");

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
        initMatrixes();
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
        if(type.equalsIgnoreCase("Weapon"))
            this.map[rowCount][colCount]=new WeaponSquare(roomColor,doors,new int[]{rowCount,colCount});
        else if(type.equalsIgnoreCase("Ammo"))
            this.map[rowCount][colCount]=new AmmoSquare(roomColor,doors,new int[]{rowCount,colCount});
        else
            this.map[rowCount][colCount]=null;
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
    public static MatrixHelper getVisibilityMatrix(int x,int y){
        if(!hasSquare(x,y)) throw new IllegalArgumentException("Can't get the visibility of the selected square (null).");
        return map[x][y].getVisibilityMatrix();
    }

    public static boolean hasSquare(int x,int y){
        return map[x][y]!=null;
    }

    public static Square getSquare(int x,int y){
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
    public static MatrixHelper getDistanceMatrix(int x,int y,int distance){
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
    private static List<Square> checkAround(Square co, boolean[][] matrix){
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
    private static boolean checkAdjacentAccessibility(Square curr, Square access, Direction d){
        return access != null && (curr.getRoomColor() == access.getRoomColor() || curr.hasDoor(d));
    }

    public static MatrixHelper getGameboardMatrix(){
        boolean[][] mat=new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++) mat[i][j]=map[i][j]!=null;
        }
        return new MatrixHelper(mat);
    }
}
