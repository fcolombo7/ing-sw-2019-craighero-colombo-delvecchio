package it.polimi.ingsw.model;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * @param name String representing the name of the effect to be instantiated
     * @param cost List<Color> representing the cost of the effect to be instantiated
     * @param requirements NodeList representing the preconditions of the effect to be instantiated
     * @param actions List<Action> representing all the actions of the effect to be instantiated
     * @param target Target of the effect to be instantiated
     */
    public Effect(int refId, String name, List<Color> cost, List<String[]> requirements, List<Action> actions, List<String[]> extra, Target target) {
        this.refId = refId;
        this.name = name;
        this.cost = new ArrayList<>(cost);
        this.requirements = new ArrayList<>(requirements);
        this.actions = new ArrayList<>(actions);
        this.target = target;
        if(extra!=null)
            this.extra=new ArrayList<>(extra);
        else
            this.extra=new ArrayList<>();
    }

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

    private List<String> getTargetConstraints(NodeList childNodes) {
        List<String> constraints=new ArrayList<>();
        for(int j=0;j<childNodes.getLength();j++){
            if(childNodes.item(j).getNodeType()!=Node.TEXT_NODE)
                constraints.add(childNodes.item(j).getFirstChild().getNodeValue());
        }
        return constraints;
    }

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
     * TODO
     */
    public static void initRequirements(){
        //TODO
        requirementsMap=new HashMap<>();
    }
}
