package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a tree
 * @param <E> representing the type of the nodes of the tree
 */
public class TreeNode<E> {

    /**
     * This attribute contains a list of the children of the tree
     */
    private List<TreeNode<E>> children = new ArrayList<>();

    /**
     * This attribute contains the value of a node
     */
    private E value = null;

    /**
     * This attribute represents a parent node in the tree
     */
    private TreeNode<E> parent= null;

    /**
     * This constructor instantiates a tree
     * @param value representing the value of the node of the tree
     */
    public TreeNode(E value) {
        this.value = value;
    }

    /**
     * This method returns the children of the tree
     * @return List containing TreeNode representing the children of the tree
     */
    public List<TreeNode<E>> getChildren() {
        return children;
    }

    /**
     * This method sets a parent of the tree
     * @param parent representing the parent to set
     */
    public void setParent(TreeNode<E> parent) {
        parent.addChild(this);
        this.parent = parent;
    }


    /**
     * This method adds a child in the tree
     * @param value representing the value of the child to add
     */
    public void addChild(E value) {
        TreeNode<E> child = new TreeNode<>(value);
        this.children.add(child);
    }

    /**
     * This method adds a child in the tree
     * @param child representing the child to add
     */
    public void addChild(TreeNode<E> child) {
        this.children.add(child);
    }

    /**
     * This method returns the value of the node
     * @return E representing the value of the node
     */
    public E getValue() {
        return this.value;
    }

    /**
     * this method sets the value of a node
     * @param value representing the value
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * This method returns true if the node is the root of the tree
     * @return boolean as true if the node is the root of the tree
     */
    public boolean isRoot() {
        return (this.parent == null);
    }

    /**
     * This method returns true is  the node is a leaf
     * @return boolean as true if the node is a leaf
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    /**
     * This method removes the parent of the tree
     */
    public void removeParent() {
        this.parent = null;
    }

    @Override
    public String toString() {
        StringBuilder msg=new StringBuilder();
        msg.append("TreeNode<").append(value.getClass().getSimpleName()).append(">: {value: ").append(value).append(", children {");
        if(children.isEmpty())
            msg.append("none");
        else{
            for(int i=0;i<children.size()-1;i++)
                msg.append(children.get(i).toString()).append(", ");
            msg.append(children.get(children.size()-1).toString());
        }
        msg.append("}}");
        return msg.toString();
    }
}
