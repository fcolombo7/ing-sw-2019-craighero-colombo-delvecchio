package it.polimi.ingsw.model;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {

    private Square[][] map;
    private int rowLength;
    private int colLength;
    private ArrayList<Player> killshotTrack;
    private ArrayList<Boolean> overkillTrack;
    private int skullNumber;

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
    }

    private int composeRow(Node rowNode, int rowCount) {
        if(rowCount>=rowLength) throw new IllegalArgumentException("RowLength does not correspond to rows number.");
        int colCount=0;
        NodeList squares=rowNode.getChildNodes();
        for(int j=0;j<colLength;j++){
            Node square=squares.item(j);
            if(square.getNodeType()!=Node.TEXT_NODE){
                colCount=composeColumn(square,colCount,rowCount);
            }
        }
        return rowCount+1;
    }

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
        if(type.equalsIgnoreCase("Weaopn"))
            this.map[rowCount][colCount]=new WeaponSquare(roomColor,doors);
        else if(type.equalsIgnoreCase("Ammo"))
            this.map[rowCount][colCount]=new AmmoSquare(roomColor,doors);
        else
            this.map[rowCount][colCount]=null;
        return colCount+1;
    }

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

    private RoomColor getRoomColor(String stringColor) {
        if(stringColor.equalsIgnoreCase("RED")) return RoomColor.RED;
        if(stringColor.equalsIgnoreCase("YELLOW")) return RoomColor.YELLOW;
        if(stringColor.equalsIgnoreCase("BLUE")) return RoomColor.BLUE;
        if(stringColor.equalsIgnoreCase("GREEN")) return RoomColor.GREEN;
        if(stringColor.equalsIgnoreCase("WHITE")) return RoomColor.WHITE;
        if(stringColor.equalsIgnoreCase("PURPLE")) return RoomColor.PURPLE;
        throw  new IllegalArgumentException("RoomColor error");
    }

    public List<Boolean> getOverkillTrack() {
        return overkillTrack;
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    public List<Player> getKillshotTrack() {
        return killshotTrack;
    }

    public void updateTrack(Player player, boolean hasOverkilled){}

    //public MatrixHelper getVisibilityMatrix(int x,int y){}

    //public MatrixHelper getVisibilityMatrix(int x,int y,int distance){}
}
