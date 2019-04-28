package it.polimi.ingsw.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * This class represents a single effect of a card (powerup or weapon)
 * */
public class Effect {
    /**
     * TODO
     */
    private static HashMap<String,Requirement> requirementsMap;

    /**
     * This This attribute contains the effect reference id
     */
    private int refId;

    /**
     * This attribute contains the effect name
     * */
    private String name;

    /**
     * This attribute contains the cost of the effect
     * */
    private List<Color> cost;

    /**
     * This attribute contains all the requirements of the effect
     * */
    private List<String[]> requirements;

    /**
     * This attribute contains all the actions of the effect which can be executed only if all the requirements are satisfied
     * */
    private List<Action> actions;

    /**
     * This attribute contains the target of the effect
     * */
    private Target target;

    /**
     * this attribute contains all the additional conditions that must be evaluated after the execution of the effect
     * */
    private List<String[]> extra;

    /**
     * This constructor instantiates an Effect
     * @param node representing the xml structure used to declare the Effect parameters
     */
    public Effect(Node node){
        if(node==null) throw new NullPointerException("Effect constructor requires a not null node parameter.");
        refId=Integer.parseInt(node.getAttributes().getNamedItem("ref_id").getNodeValue());
        if(refId<=0) throw new IllegalArgumentException("The ref_id value can not bw lesser then 1.");
        name=node.getAttributes().getNamedItem("name").getNodeValue();
        NodeList children=node.getChildNodes();
        for(int i=0; i<children.getLength();i++){
            if(children.item(i).getNodeType()!=Node.TEXT_NODE){
                String nodeName=children.item(i).getNodeName();
                if(nodeName.equalsIgnoreCase("target")) setTarget(children.item(i));
                else if(nodeName.equalsIgnoreCase("cost")) setCost(children.item(i));
                else if(nodeName.equalsIgnoreCase("requirements")) setRequirements(children.item(i));
                else if(nodeName.equalsIgnoreCase("actions")) setActions(children.item(i));
                else if(nodeName.equalsIgnoreCase("extras")) setExtras(children.item(i));
            }
        }
    }

    /**
     * This method instantiates the requirements of the Effect
     * @param item representing the XML node to read
     */
    private void setRequirements(Node item) {
        requirements=new ArrayList<>();
        NodeList requirementsNodeList=item.getChildNodes();
        for(int i=0;i<requirementsNodeList.getLength();i++){
            if(requirementsNodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                String[] requirement=new String[2];
                Node requirementNode=requirementsNodeList.item(i);
                requirement[0]=requirementNode.getAttributes().getNamedItem("name").getNodeValue();
                requirement[1]=requirementNode.getFirstChild().getNodeValue();
                requirements.add(requirement);
            }
        }
    }

    /**
     * This method instantiates the cost of the Effect
     * @param item representing the XML node to read
     */
    private void setCost(Node item) {
        NodeList ammoNodeList=item.getChildNodes();
        Node ammoNode=null;
        int count=0;
        while(ammoNode==null&&count<ammoNodeList.getLength()){
            if(ammoNodeList.item(count).getNodeType()!=Node.TEXT_NODE) ammoNode=ammoNodeList.item(count);
            count++;
        }
        if(ammoNode==null){
            cost=new ArrayList<>();
            return;
        }
        String ammoStr=ammoNode.getFirstChild().getNodeValue();
        cost=new ArrayList<>(ammoStr.length());
        for(int i=0;i<ammoStr.length();i++){
            char car=ammoStr.charAt(i);
            switch (car){
                case 'R': cost.add(Color.RED); break;
                case 'B': cost.add(Color.BLUE); break;
                case 'Y': cost.add(Color.YELLOW); break;
                default: throw new IllegalArgumentException("Effect '"+name+"' contains an invalid ammo (color) in cost element.");
            }
        }
    }

    /**
     * This method instantiates the extra of the Effect
     * @param item representing the XML node to read
     */
    private void setExtras(Node item) {
        extra=new ArrayList<>();
        NodeList extraNodeList=item.getChildNodes();
        for(int i=0;i<extraNodeList.getLength();i++){
            if(extraNodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                String[] element=new String[2];
                Node requirementNode=extraNodeList.item(i);
                element[0]=requirementNode.getAttributes().getNamedItem("name").getNodeValue();
                element[1]=requirementNode.getFirstChild().getNodeValue();
                extra.add(element);
            }
        }
    }

    /**
     * This method instantiates the actions of the Effect
     * @param item representing the XML node to read
     */
    private void setActions(Node item) {
        actions=new ArrayList<>();
        NodeList actionsNodeList=item.getChildNodes();
        for(int i=0;i<actionsNodeList.getLength();i++){
            if(actionsNodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                Node actionNode=actionsNodeList.item(i);
                ActionType aType=null;
                int value;
                String type=actionNode.getAttributes().getNamedItem("type").getNodeValue();
                if(type.equalsIgnoreCase(ActionType.DAMAGE.name())) aType=ActionType.DAMAGE;
                else if(type.equalsIgnoreCase(ActionType.MARK.name())) aType=ActionType.MARK;
                else if(type.equalsIgnoreCase(ActionType.MOVE.name())) aType=ActionType.MOVE;

                NodeList actionValueNodeList=actionNode.getChildNodes();
                Node actionValueNode=null;
                int count=0;
                while(actionValueNode==null&&count<actionValueNodeList.getLength()){
                    if(actionValueNodeList.item(count).getNodeType()!=Node.TEXT_NODE) actionValueNode=actionValueNodeList.item(count);
                    count++;
                }
                if(actionValueNode==null) throw new NullPointerException("Action tag is empty.");
                value=Integer.parseInt(actionValueNode.getFirstChild().getNodeValue());
                if(value<=0) throw new IllegalArgumentException("The value of the action "+type.toUpperCase()+" can not be lesser than 1.");
                Action action= new Action(aType,value);
                actions.add(action);
            }
        }
    }

    /**
     * This method instantiates the target of the Effect
     * @param node representing the XML node to read
     */
    private void setTarget(Node node) {
        NodeList targetNodeList=node.getChildNodes();
        TargetType type=null;
        int minNumber=0;
        int maxNumber=0;
        int minPlayerIn=0;
        int maxPlayerIn=0;
        List<String> prevConstraints=null;
        for(int i=0;i<targetNodeList.getLength();i++){
            if(targetNodeList.item(i).getNodeType()!=Node.TEXT_NODE){
                Node element=targetNodeList.item(i);
                if(element.getNodeName().equalsIgnoreCase("minNumber")) minNumber=Integer.parseInt(element.getFirstChild().getNodeValue());
                else if(element.getNodeName().equalsIgnoreCase("maxNumber")) maxNumber=Integer.parseInt(element.getFirstChild().getNodeValue());
                else if(element.getNodeName().equalsIgnoreCase("minPlayerIn")) minPlayerIn=Integer.parseInt(element.getFirstChild().getNodeValue());
                else if(element.getNodeName().equalsIgnoreCase("maxPlayerIn")) maxPlayerIn=Integer.parseInt(element.getFirstChild().getNodeValue());
                else if(element.getNodeName().equalsIgnoreCase("prevConstraints")){
                    prevConstraints= getTargetConstraints(element.getChildNodes());
                }
                else if(element.getNodeName().equalsIgnoreCase("type")) type=getTargetType(element.getFirstChild().getNodeValue());
            }
        }

        target=new Target(type,minNumber,maxNumber,minPlayerIn,maxPlayerIn,prevConstraints);
    }

    /**
     * This method instantiates the constraints of the Effect target
     * @param childNodes representing the XML nodeList to read
     */
    private List<String> getTargetConstraints(NodeList childNodes) {
        List<String> constraints=new ArrayList<>();
        for(int j=0;j<childNodes.getLength();j++){
            if(childNodes.item(j).getNodeType()!=Node.TEXT_NODE)
                constraints.add(childNodes.item(j).getFirstChild().getNodeValue());
        }
        return constraints;
    }
    /**
     * This method return the TargetType of the target associated to the Target Name (string)
     * @param nodeValue representing the name of the TargetType you want
     */
    private TargetType getTargetType(String nodeValue) {
        if(nodeValue.equalsIgnoreCase("PLAYER")) return TargetType.PLAYER;
        if(nodeValue.equalsIgnoreCase("ROOM")) return TargetType.ROOM;
        if(nodeValue.equalsIgnoreCase("SQUARE")) return TargetType.SQUARE;
        if(nodeValue.equalsIgnoreCase("DIRECTION")) return TargetType.DIRECTION;
        throw  new IllegalArgumentException("TargetType error");
    }

    /**
     * This method returns the effect reference id used during the navigation in the weapon tree
     * @return int representing the reference id of the effect
     * */
    public int getRefId() {
        return refId;
    }


    /**
     * This method returns the effect name
     * @return String representing the name of the effect
     * */
    public String getName() {
        return name;
    }

    /**
     * This method returns the cost of the effect
     * @return List<Color> representing the cost of the effect
     * */
    public List<Color> getCost() {
        return new ArrayList<>(cost);
    }

    /**
     * This method returns all the actions performed by the current effect
     * @return List<Action> representing all the actions performed by the current effect
     * */
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    /**
     * This method returns the target of the effect
     * @return Target of the effect
     * */
    public Target getTarget() {
        return target;
    }

    /**
     * this method returns the additional conditions that must be evaluated after the execution of the effect
     * @return List<String> representing all the additional conditions that must be evaluated after the execution of the effect
     * */
    public List<String[]> getExtra(){
        return new ArrayList<>(extra);
    }

    /**
     * This method initialized the HashMap used to check the Effect Requirements
     */
    public static void initRequirements(){
        /*
        TODO
        SONO TUTTI DA VERIFICARE
        - VISIBLE       x
        - MIN_DISTANCE  x
        - MAX_DISTANCE  x
        - PREV_VISIBLE  x
        - PREV_POSITION x
        - ON_DIRECTION  x
        - SHIFTABLE     x
        - NOT_IN_ROOM   x
         */
        requirementsMap=new HashMap<>();

        /*
        This requirement check update the matrix with the only squares visible/not visible from the current position (not if value is false)
         */
        requirementsMap.put("VISIBLE",(value, curPos, lastPos, matrix) -> {
            if(Boolean.parseBoolean(value)) return matrix.bitWiseAnd(GameBoard.getVisibilityMatrix(curPos[0],curPos[1]));
            else return matrix.bitWiseAnd(GameBoard.getGameboardMatrix()
                    .bitWiseAnd(GameBoard.getVisibilityMatrix(curPos[0],curPos[1]).bitWiseNot()));
        });

        /*
        This requirement check update the matrix with the only squares which distance from the current position is at least 'value'
         */
        requirementsMap.put("MIN_DISTANCE",(value, curPos, lastPos, matrix) -> {
            int val=Integer.parseInt(value);
            return matrix.bitWiseAnd(GameBoard.getDistanceMatrix(curPos[0],curPos[1],val)
                    .bitWiseNot()
                    .bitWiseAnd(GameBoard.getGameboardMatrix()));
        });

        /*
        This requirement check update the matrix with the only squares which distance from the current position is at last 'value'
         */
        requirementsMap.put("MAX_DISTANCE",(value, curPos, lastPos, matrix) -> {
            int val=Integer.parseInt(value);
            return matrix.bitWiseAnd(GameBoard.getDistanceMatrix(curPos[0],curPos[1],val));
        });

        /*
        This requirement check update the matrix with the only squares visible (not) from the last position (not if value is false)
         */
        requirementsMap.put("PREV_VISIBLE",(value, curPos, lastPos, matrix) -> {
            if(Boolean.parseBoolean(value)) return matrix.bitWiseAnd(GameBoard.getVisibilityMatrix(lastPos[0],lastPos[1]));
            else return matrix.bitWiseAnd(GameBoard.getGameboardMatrix()
                    .bitWiseAnd(GameBoard.getVisibilityMatrix(lastPos[0],lastPos[1]).bitWiseNot()));
        });

        /*
        This requirement check update the matrix with the only square which correspond to the last position
         */
        requirementsMap.put("PREV_POSITION",(value, curPos, lastPos, matrix) -> {
            boolean val=Boolean.parseBoolean(value);
            int colLength=GameBoard.getGameboardMatrix().getColLength();
            int rowLength=GameBoard.getGameboardMatrix().getRowLength();
            boolean [][]mat=new boolean[rowLength][colLength];
            for(int i=0;i<rowLength;i++){
                for(int j=0;j<colLength;j++)
                    mat[i][i]=val==(i==lastPos[0]&&j==lastPos[1]);
            }
            return matrix.bitWiseAnd(new MatrixHelper(mat));
        });

        /*
        This requirement check update the matrix with the only squares which are (not) NORTH|EAST|SOUTH|WEST of the current position. (not if value is false)
         */
        requirementsMap.put("ON_DIRECTION",(value, curPos, lastPos, matrix) -> {
            boolean val=Boolean.parseBoolean(value);
            int colLength=GameBoard.getGameboardMatrix().getColLength();
            int rowLength=GameBoard.getGameboardMatrix().getRowLength();
            boolean [][]mat=new boolean[rowLength][colLength];
            for(int i=0;i<rowLength;i++){
                for(int j=0;j<colLength;j++)
                    mat[i][j]=(i==curPos[0]||j==curPos[1]);
            }
            MatrixHelper directionMatrix=new MatrixHelper(mat);
            if(!val) directionMatrix=directionMatrix.bitWiseNot();
            return matrix.bitWiseAnd(GameBoard.getGameboardMatrix()
                    .bitWiseAnd(directionMatrix));
        });

        /*
        This requirement check update the matrix with the only squares which are not (are) in the same room of the current position. (are if value is false)
         */
        requirementsMap.put("NOT_IN_ROOM",(value, curPos, lastPos, matrix) -> {
            boolean val=Boolean.parseBoolean(value);
            int colLength=GameBoard.getGameboardMatrix().getColLength();
            int rowLength=GameBoard.getGameboardMatrix().getRowLength();
            boolean [][]mat=new boolean[rowLength][colLength];
            RoomColor roomColor=GameBoard.getSquare(curPos[0],curPos[1]).getRoomColor();

            for(int i=0;i<rowLength;i++){
                for(int j=0;j<colLength;j++)
                    mat[i][j]=GameBoard.hasSquare(i,j)&&!GameBoard.getSquare(i,j).getRoomColor().equals(roomColor);
            }
            MatrixHelper notInRommMatrix=new MatrixHelper(mat);
            if(val) return matrix.bitWiseAnd(notInRommMatrix);
            else return matrix.bitWiseAnd(GameBoard.getGameboardMatrix()
                    .bitWiseAnd(notInRommMatrix.bitWiseNot()));
        });

        /*
        This requirement check update the matrix adding all the squares which are at distance 'value' from the current matrix.
         */
        requirementsMap.put("SHIFTABLE",(value, curPos, lastPos, matrix) -> {
            int val=Integer.parseInt(value);
            int colLength=GameBoard.getGameboardMatrix().getColLength();
            int rowLength=GameBoard.getGameboardMatrix().getRowLength();
            boolean[][] curMat=matrix.toBooleanMatrix();
            MatrixHelper retMatrix=new MatrixHelper(curMat);
            for(int i=0;i<rowLength;i++){
                for(int j=0;j<colLength;j++) {
                    if (curMat[i][j])
                        retMatrix=retMatrix.bitWiseOr(GameBoard.getDistanceMatrix(i, j, val));
                }
            }
            return retMatrix;
        });

    }

    /**
     * This method return the string representation of the Effect object
     * @return String representing the Effect object
     */
    @Override
    public String toString() {
        StringBuilder msg= new StringBuilder();
        msg.append("Effect {\n");
        //id
        msg.append("ref_id: ").append(refId).append("\n");
        //name
        msg.append("name: ").append(name).append("\n");
        //cost
        msg.append("cost: ");
        if(!cost.isEmpty()) {
            for(int i=0;i<cost.size()-1;i++)
                msg.append(cost.get(i).name()).append(", ");
            msg.append(cost.get(cost.size()-1).name()).append("\n");
        }
        else msg.append("none").append("\n");
        //target
        msg.append(target.toString()).append("\n");
        //Requirements
        msg.append("requirements: ");
        if(!requirements.isEmpty()) {
            msg.append("{");
            for(int i=0;i<requirements.size()-1;i++)
                msg.append(requirements.get(i)[0]).append(": ").append(requirements.get(i)[1]).append(", ");
            msg.append(requirements.get(requirements.size()-1)[0]).append(": ").append(requirements.get(requirements.size()-1)[1]).append("}\n");
        }
        else msg.append("none").append("\n");
        //actions
        msg.append("actions: ");
        if(!actions.isEmpty()) {
            msg.append("{");
            for(int i=0;i<actions.size()-1;i++)
                msg.append(actions.get(i).getActionType().name()).append(": ").append(actions.get(i).getValue()).append(", ");
            msg.append(actions.get(actions.size()-1).getActionType().name()).append(": ").append(actions.get(actions.size()-1).getValue()).append("}\n");
        }
        else msg.append("none").append("\n");
        //extras
        msg.append("extra: ");
        if(!extra.isEmpty()) {
            msg.append("{");
            for(int i=0;i<extra.size()-1;i++)
                msg.append(extra.get(i)[0]).append(": ").append(extra.get(i)[1]).append(", ");
            msg.append(extra.get(extra.size()-1)[0]).append(": ").append(extra.get(extra.size()-1)[1]).append("}\n");
        }
        else msg.append("none").append("\n");
        msg.append("}");
        return msg.toString();
    }
}
