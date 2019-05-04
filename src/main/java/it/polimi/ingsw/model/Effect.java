package it.polimi.ingsw.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * This class represents a single effect of a card (powerup or weapon)
 * */
public class Effect {
    /**
     * TODO
     */
    private static HashMap<String,Requirement> requirementsMap=initRequirements();

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
                Node extraNode=extraNodeList.item(i);
                element[0]=extraNode.getAttributes().getNamedItem("name").getNodeValue();
                if(extraNode.getFirstChild()!=null)
                    element[1]=extraNode.getFirstChild().getNodeValue();
                else
                    element[1]="none";
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
     * This method initialized the HashMap used to check the Effect Requirements
     */
    private static HashMap<String,Requirement> initRequirements(){
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
        - IF_FIRST_CHECK_VISIBILITY_AFTER_SHIFT
        - IF_FIRST_CHECK_IN_SQUARE_AFTER_SHIFT
         */
        HashMap<String,Requirement> requirementsMap=new HashMap<>();

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
            return matrix.bitWiseAnd(GameBoard.getDistanceMatrix(curPos[0],curPos[1],val-1)
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
        requirementsMap.put("PREV_PLAYER_POSITION",(value, curPos, lastPos, matrix) -> {
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

        /*
        This requirement check is used when target is 'ME' and it update the matrix adding all the squares which are visible from the current matrix.
         */
        requirementsMap.put("IF_FIRST_CHECK_VISIBILITY_AFTER_SHIFT", (value, curPos, lastPos, matrix) -> requirementsMap.get("VISIBLE").checkRequirement(value,curPos,lastPos,matrix));

        /*
        This requirement check is used when target is 'ME' and it update the matrix setting/not setting the curPos
         */
        requirementsMap.put("IF_FIRST_CHECK_IN_SQUARE_AFTER_SHIFT", (value, curPos, lastPos, matrix) -> {
            if(Boolean.parseBoolean(value))
                return requirementsMap.get("MAX_DISTANCE").checkRequirement("0",curPos,lastPos,matrix);
            else
                return requirementsMap.get("MIN_DISTANCE").checkRequirement("1",curPos,lastPos,matrix);
        });

        return requirementsMap;
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
        if(nodeValue.equalsIgnoreCase("ME")) return TargetType.ME;
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
     * TODO: NEED TO BE TESTED
     * This method return true if using this Effect the current player could shoot a player
     * @param currentPlayer representing the player whose playing the Turn
     * @param players representing all the players (current player excluded)
     * @param shotPlayers represent all the last player shooted using the Weapon
     * @return boolean representing if if using this Effect the current player could shoot a player
     */
    public boolean canUse(Player currentPlayer, List<Player> players, Deque<Player> shotPlayers){
        if(target.getType().name().equalsIgnoreCase(TargetType.ME.name())){
            boolean[][] mat=getMovementMatrix(currentPlayer,players,shotPlayers).toBooleanMatrix();
            for(int i=0;i<mat.length;i++){
                for(int j=0;j<mat[0].length;j++) if(mat[i][j]) return true;
            }
            return false;
        }
        List<List<Player>> shootablePlayers=getShootablePlayers(currentPlayer, players, shotPlayers);
        return !shootablePlayers.isEmpty();
    }

    /** TODO: NEED TO BE TESTED
     * This method is used to obtain a matrix representing where the current player can move to
     * @param currentPlayer representing the player whose playing the Turn
     * @param players representing all the players (current player excluded)
     * @param shotPlayers represent all the last player shot using the Weapon
     * @return MatrixHelper representing where the current player can move to
     */
    public MatrixHelper getMovementMatrix(Player currentPlayer, List<Player> players, Deque<Player> shotPlayers) {
        if(!target.getType().name().equalsIgnoreCase(TargetType.ME.name())) return MatrixHelper.allFalseMatrix(GameBoard.getGameboardMatrix().getRowLength(),GameBoard.getGameboardMatrix().getColLength());
        int[] lastPos=null;
        if(shotPlayers.peek()!=null && shotPlayers.peek().getPosition() != null)
            lastPos=shotPlayers.peek().getPosition().getBoardIndexes();
        return calcMovementMatrix(currentPlayer.getPosition().getBoardIndexes(),lastPos,players,shotPlayers.isEmpty());
    }

    /**
     * This method is used by the effect to get the Matrix of the effect for the situation described by the parameters passed
     * @param curPos representing the indexes of the Gameboard of the current position
     * @param lastPos representing the indexes of the Gameboard of the last position
     * @param players representing all the players (current player excluded)
     * @return MatrixHelper object representing the Matrix of the effect for the situation described by the parameters passed
     */
    private MatrixHelper calcMovementMatrix(int[] curPos, int[] lastPos, List<Player> players,boolean first) {
        List<String[]> eRequirements=new ArrayList<>(this.requirements);
        String shiftableValue="";
        for (String[] requirement:eRequirements) {
            if(requirement[0].equalsIgnoreCase("SHIFTABLE")){
                shiftableValue=requirement[1];
                eRequirements.remove(requirement);
                break;
            }
        }
        MatrixHelper matrix=GameBoard.getGameboardMatrix();
        //SET UP MATRIX
        matrix=requirementsMap.get("MAX_DISTANCE").checkRequirement("0",curPos,lastPos,matrix);
        matrix=requirementsMap.get("SHIFTABLE").checkRequirement(shiftableValue,curPos,lastPos,matrix);
        //if there are shot players you can move everywhere without check
        if(!first) return matrix;

        List<MatrixHelper> decMatrixes=matrix.decompose();
        List<MatrixHelper> selMatrixes=new ArrayList<>();
        for(MatrixHelper mat:decMatrixes){
            MatrixHelper curMatrix=GameBoard.getGameboardMatrix();
            int x=0;
            int y=0;
            for(int i=0;i<mat.getRowLength();i++){
                for(int j=0;j<mat.getColLength();j++){
                    if(mat.toBooleanMatrix()[i][j]){x=i;y=j;break;}
                }
            }
            for (String[] requirement:eRequirements) {
                curMatrix=requirementsMap.get(requirement[0]).checkRequirement(requirement[1],new int[]{x,y},lastPos,curMatrix);
            }
            if(!getAvailablePlayer(players,new ArrayDeque<>(),curMatrix).isEmpty())
                selMatrixes.add(mat);
        }
        MatrixHelper retMatrix=MatrixHelper.allFalseMatrix(matrix.getRowLength(),matrix.getColLength());
        for (MatrixHelper mat:selMatrixes){
            retMatrix=retMatrix.bitWiseOr(mat);
        }

        return retMatrix;
    }

    /**
     * This method is used to obtain all the players which can be shot group by the effect target type
     * @param currentPlayer representing the player whose playing the Turn
     * @param players representing all the players (current player excluded)
     * @param shotPlayers represent all the last player shot using the Weapon
     * @return List<List<Player>> representing all the players which can be shot group by the effect target type
     */
    public List<List<Player>> getShootablePlayers(Player currentPlayer, List<Player> players, Deque<Player> shotPlayers) {
        //TODO: Last position is the last shooted player position
        if(target.getType().name().equalsIgnoreCase(TargetType.ME.name())) return new ArrayList<>();
        int[] lastPos=null;
        if(shotPlayers.peek()!=null && shotPlayers.peek().getPosition() != null)
            lastPos=shotPlayers.peek().getPosition().getBoardIndexes();
        MatrixHelper effectMatrix=calcEffectMatrix(currentPlayer.getPosition().getBoardIndexes(),lastPos);
        List<Player> availablePlayer=getAvailablePlayer(players,shotPlayers,effectMatrix);
        return composeTargetPlayers(currentPlayer,availablePlayer);
    }

    /**
     * This method is used by the effect to get the Matrix of the effect for the situation described by the parameters passed
     * @param curPos representing the indexes of the Gameboard of the current position
     * @param lastPos representing the indexes of the Gameboard of the last position
     * @return MatrixHelper object representing the Matrix of the effect for the situation described by the parameters passed
     */
    private MatrixHelper calcEffectMatrix(int[] curPos, int[] lastPos) {
        MatrixHelper matrix=GameBoard.getGameboardMatrix();
        for (String[] requirement:requirements) {
            //TODO:This line is used to set the shiftable matrix in turn class
            //if(requirement[0].equalsIgnoreCase("SHIFTABLE")) Turn.setShiftableMatrix(matrix);
            matrix=requirementsMap.get(requirement[0]).checkRequirement(requirement[1],curPos,lastPos,matrix);
        }
        return matrix;
    }

    /**
     * This method is used to obtain all the player which can be shot using the effect
     * @param players representing all the players (current player excluded)
     * @param shotPlayers represent all the last player shot using the Weapon
     * @param effectMatrix representing the Matrix of the effect for the situation described by the parameters passed
     * @return List<Player> representing the players which can be shot using the effect
     */
    private List<Player> getAvailablePlayer(List<Player> players, Deque<Player> shotPlayers, MatrixHelper effectMatrix) {
        ArrayList<Player> availablePlayer=new ArrayList<>();
        for (Player p:players) {
            int[] pos=p.getPosition().getBoardIndexes();
            if(effectMatrix.toBooleanMatrix()[pos[0]][pos[1]])
                availablePlayer.add(p);
        }
        ArrayList<Player> realAvailablePlayer=new ArrayList<>(availablePlayer);
        for (String constraint:target.getPrevConstraints()) {
            for(Player p:availablePlayer) {
                if(!Target.checkConstraint(constraint,p,shotPlayers)) realAvailablePlayer.remove(p);
            }
        }
        return realAvailablePlayer;
    }

    /**
     * This method is used to group the players which can be shot by the effect target type
     * @param currentPlayer representing the player whose playing the Turn
     * @param availablePlayer representing the players which can be shot using the effect
     * @return List<List<Player>> representing all the players which can be shot group by the effect target type
     */
    private List<List<Player>> composeTargetPlayers(Player currentPlayer, List<Player> availablePlayer) {
        if(availablePlayer.isEmpty()) return new ArrayList<>();
        if(target.getType().equals(TargetType.PLAYER)) return composePlayer(availablePlayer);
        else if(target.getType().equals(TargetType.SQUARE)) return composeSquare(availablePlayer);
        else if(target.getType().equals(TargetType.DIRECTION)) return composeDirection(currentPlayer,availablePlayer);
        else if(target.getType().equals(TargetType.ROOM)) return composeRoom(availablePlayer);
        return new ArrayList<>();
    }

    /**
     * This method is used to group the players which can be shot by room
     * @param availablePlayer representing the players which can be shot using the effect
     * @return List<List<Player>> representing all the players which can be shot group by room
     */
    private List<List<Player>> composeRoom(List<Player> availablePlayer) {
        List<List<Player>> list= new ArrayList<>();
        for (Player p:availablePlayer) {
            boolean found=false;
            for (List<Player> l:list) {
                if(l.get(0).getPosition().getRoomColor()==p.getPosition().getRoomColor()) {
                    l.add(p);
                    found=true;
                }
            }
            if(!found) {
                ArrayList<Player> l=new ArrayList<>();
                l.add(p);
                list.add(l);
            }
        }
        return list;
    }

    /**
     * This method is used to group the players which can be shot by direction
     * @param currentPlayer representing the player whose playing the Turn
     * @param availablePlayer representing the players which can be shot using the effect
     * @return List<List<Player>> representing all the players which can be shot group by direction
     */
    private List<List<Player>> composeDirection(Player currentPlayer, List<Player> availablePlayer) {
        List<List<Player>> list= new ArrayList<>();
        ArrayList<Player> arrayList=new ArrayList<>();
        for (Player p:availablePlayer) {
            boolean found=false;
            if(currentPlayer.getPosition()==p.getPosition()) {
                arrayList.add(p);
                found=true;
            }
            else for (List<Player> l:list) {
                if(currentPlayer.isInDirection(l.get(0))==currentPlayer.isInDirection(p)) {
                    l.add(p);
                    found=true;
                }
            }
            if(!found) {
                ArrayList<Player> l=new ArrayList<>();
                l.add(p);
                list.add(l);
            }
        }
        if(list.isEmpty())list.add(new ArrayList<>(arrayList));
        else for (List<Player> l:list) {
            l.addAll(arrayList);
        }
        return list;
    }

    /**
     * This method is used to group the players which can be shot by square
     * @param availablePlayer representing the players which can be shot using the effect
     * @return List<List<Player>> representing all the players which can be shot group by square
     */
    private List<List<Player>> composeSquare(List<Player> availablePlayer) {
        List<List<Player>> list= new ArrayList<>();
        for (Player p:availablePlayer) {
            boolean found=false;
            for (List<Player> l:list) {
                if(l.get(0).getPosition().getBoardIndexes()[0]==p.getPosition().getBoardIndexes()[0]&&
                        l.get(0).getPosition().getBoardIndexes()[1]==p.getPosition().getBoardIndexes()[1]) {
                    l.add(p);
                    found=true;
                }
            }
            if(!found) {
                ArrayList<Player> l=new ArrayList<>();
                l.add(p);
                list.add(l);
            }
        }
        return list;
    }

    /**
     * This method is used to group the players which can be shot by player
     * @param availablePlayer representing the players which can be shot using the effect
     * @return List<List<Player>> representing all the players which can be shot group by player
     */
    private List<List<Player>> composePlayer(List<Player> availablePlayer) {
        List<List<Player>> list= new ArrayList<>();
        list.add(new ArrayList<>(availablePlayer));
        return list;
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
