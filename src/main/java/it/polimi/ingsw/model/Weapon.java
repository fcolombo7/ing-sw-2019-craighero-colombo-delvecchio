package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardNotInitializedException;
import it.polimi.ingsw.exceptions.WeaponEffectException;
import it.polimi.ingsw.exceptions.WeaponLoadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a weapon card
 */
public class Weapon extends Card{

    /**
     * This attribute contains a list of TreeNode that determines the tree of the possible using orders of the effects of a weapon
     */
    private List<TreeNode<Integer>> effectOrder;

    /**
     * This attribute contains a list of color that determines the ammo of the weapon
     */
    private List<Color> ammo;

    /**
     * This attribute specifies if the weapon is initialized or not
     */
    private boolean initialized;

    /**
     * This attribute specifies the status of the weapon (loaded or unloaded)
     */
    private boolean loaded;

    /**
     * This attribute contains the list of the effects of the weapon
     */
    private List<Effect> effects;

    /**
     * This constructor instantiates a weapon calling the constructor of Card (Weapon extends Card)
     * @param id representing the id of the weapon
     * @param name representing the name of the weapon
     */
    public Weapon(String id, String name, String initXML){
        super(id, name, initXML);
        initialized=false;
        loaded=false;
        this.effects=null;
        this.ammo=null;
    }

    /**
     * This method returns true if the weapon is initialized
     * @return boolean as true if the weapon is initialized
     */
    public boolean isInit(){
        return initialized;
    }

    /**
     * TODO
     */
    public void init() throws ParserConfigurationException, IOException, SAXException {
        this.effects=new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        /*
        factory.setValidating(true);

        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) throws SAXException {
                System.out.println("WARNING : " + e.getMessage()); // do nothing
                throw e;
            }

            @Override
            public void error(SAXParseException e) throws SAXException {
                System.out.println("ERROR : " + e.getMessage());
                throw e;
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                System.out.println("FATAL : " + e.getMessage());
                throw e;
            }
        });
        */
        Document document = builder.parse(new File(getInitXML()));
        document.normalizeDocument();
        Element root = document.getDocumentElement();
        root.normalize();
        /*TODO: Controllo per la verifica che id e name siano giusti?
        * String id=root.getAttribute("id");
        * if(!id.equals(getId())) throw new InvalidInitializationException;
        * */
        setAmmo(root.getElementsByTagName("ammo"));
        NodeList effectsList=root.getElementsByTagName("effects").item(0).getChildNodes();
        int count=0;
        while(count<effectsList.getLength()){
            Node effectNode= effectsList.item(count);
            if(effectNode.getNodeType()!=Node.TEXT_NODE)
                addEffect(effectNode);
            count++;
        }
        effectOrder=new ArrayList<>();
        setEffectOrder(root.getElementsByTagName("order").item(0).getChildNodes());

        //da controllare
        initialized=true;
        loaded=true;
    }

    private void setEffectOrder(NodeList sequences) {
        for(int i=0;i<sequences.getLength();i++){
            if(sequences.item(i).getNodeType()!=Node.TEXT_NODE) {
                List<Integer> idSequence = getIdSequence(sequences.item(i).getChildNodes());
                updateTree(idSequence);
            }
        }
    }

    private void updateTree(List<Integer> idSequence) {
        List<TreeNode<Integer>> orderLevel=effectOrder;
        int count=0;
        TreeNode<Integer> parent=null;
        while(count<idSequence.size()){
            int x=0;
            boolean found=false;
            while(x<orderLevel.size()&&!found){
                if(orderLevel.get(x).getValue().equals(idSequence.get(count))) found=true;
                else x++;
            }
            if(!found) {
                TreeNode<Integer> node= new TreeNode<>(idSequence.get(count));
                if(parent!=null)
                    node.setParent(parent);
                else
                    orderLevel.add(node);
                parent=node;
            }else{
                parent=orderLevel.get(x);
            }
            count++;
            orderLevel=parent.getChildren();
        }
    }

    private List<Integer> getIdSequence(NodeList childNodes) {
        List<Integer> list =new ArrayList<>();
        for(int i=0;i<childNodes.getLength();i++){
            if(childNodes.item(i).getNodeType()!=Node.TEXT_NODE) {
                int value=Integer.parseInt(childNodes.item(i).getFirstChild().getNodeValue());
                boolean valid=false;
                for (Effect effect: effects){
                     if(effect.getRefId()==value){
                         valid=true; break;
                     }
                }
                if(!valid) throw new IllegalArgumentException("Invalid sequence in the weapon effect order (incompatible id)");
                list.add(value);
            }
        }
        list.add(-1); //aggiungo il terminatore dell'effetto
        return list;
    }

    private void addEffect(Node effectNode) {
        Effect effect=new Effect(effectNode);
        for (Effect e:effects)
            if(effect.getRefId()==e.getRefId()) throw new IllegalArgumentException("Weapon '"+getName()+"' has two effect with the same ref_id attribute");
        effects.add(effect);
    }

    private void setAmmo(NodeList ammoNodeList) {
        Node ammoNode=null;
        int count=0;
        while(ammoNode==null&&count<ammoNodeList.getLength()){
            if(ammoNodeList.item(count).getNodeType()!=Node.TEXT_NODE) ammoNode=ammoNodeList.item(count);
            count++;
        }
        if(ammoNode==null) throw new IllegalArgumentException("Ammo of the weapon '"+getName()+"' is not set.");
        String ammoStr=ammoNode.getFirstChild().getNodeValue();
        ammo=new ArrayList<>(ammoStr.length());
        for(int i=0;i<ammoStr.length();i++){
            char car=ammoStr.charAt(i);
            switch (car){
                case 'R': ammo.add(Color.RED); break;
                case 'B': ammo.add(Color.BLUE); break;
                case 'Y': ammo.add(Color.YELLOW); break;
                default: throw new IllegalArgumentException("Weapon '"+getName()+"' contains an invalid ammo (color) in ammo element.");
            }
        }
    }

    /**
     * This method returns the tree of the possible using orders of the effects of the weapon
     * @return List containing TreeNode representing the tree of the possible using orders of the effects of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     */
    public List<TreeNode<Integer>> getOrder() {
        if(!initialized) throw new CardNotInitializedException("The order of the effect is not set: the card is not initialized.");
        return effectOrder;
    }

    /**
     * This method returns an effect of the weapon
     * @param name representing  the name of the weapon
     * @return Effect representing an effect of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponEffectException when the weapon doesn't have that effect
     */
    public Effect getEffect(String name) {
        if(!initialized) throw new CardNotInitializedException("The effects list of the weapon is not set: the card is not initialized.");
        for(Effect e: effects)
            if(name.equals(e.getName()))
                return e;
        throw new WeaponEffectException ("The weapon does not have the effect '"+name+"'.");

    }

    /**
     * This method returns all the effects of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return List containing Effect representing all the effects of the weapon
     */
    public List<Effect> getEffects() {
        if(!initialized) throw new CardNotInitializedException("The effects list of the weapon is not set: the card is not initialized.");
        return effects;
    }

    /**
     * This method returns the ammo of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return List containing Color representing the ammo of the weapon
     */
    public List<Color> getAmmo() {
        if(!initialized) throw new CardNotInitializedException("The ammo of the weapon is not set: the card is not initialized.");
        return ammo;
    }

    /**
     * This method return the status of the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @return boolean as true if the weapon is loaded
     */
    public boolean isLoaded() {
        if(!initialized) throw new CardNotInitializedException("Can't check if the weapon is loaded: the card is not initialized.");
        return loaded;
    }

    /**
     * This method loads the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponLoadException when the weapon is already loaded
     */
    public void load(){
        if(!initialized) throw new CardNotInitializedException("Can't load the weapon: the card is not initialized.");
        if(isLoaded())
            throw new WeaponLoadException("The weapon is already loaded.");
        this.loaded=true;
    }

    /**
     * This method unloads the weapon
     * @throws CardNotInitializedException when the card is not initialized
     * @throws WeaponLoadException when the weapon is already unloaded
     */
    public void unload() {
        if(!initialized) throw new CardNotInitializedException("The card is not initialized.");
        if(!isLoaded())
            throw new WeaponLoadException("The weapon is already unloaded.");
        this.loaded=false;
    }
}
