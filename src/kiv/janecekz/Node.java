/*
 * Genealogy expert system.
 * 
 * Written by Zdeněk Janeček, 2014
 * Share it freely under conditions of GNU GPL v3
 * 
 * version 2.0
 * last change in June 2014
 */

package kiv.janecekz;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
    public static final int NONE = -1;

    private LinkedList<Node> childrens;

    private String name;
    private int id;
    private int mother;
    private int father;
    private boolean male;
    private int partner;
    private NTree tree;

    public Node(int id, String name, int mother, int father, boolean male,
            int partner, NTree tree) {
        super();
        this.name = name;
        this.id = id;
        this.mother = mother;
        this.father = father;
        this.male = male;
        this.partner = partner;
        this.tree = tree;

        childrens = new LinkedList<Node>();
    }

    public Node getPartner() {
        if (partner == NONE)
            return null;
        else
            return tree.nodes.get(partner);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Node getMother() {
        if (mother == NONE)
            return null;
        else
            return tree.nodes.get(mother);
    }

    public Node getFather() {
        if (father == NONE)
            return null;
        else
            return tree.nodes.get(father);
    }

    public boolean isMale() {
        return male;
    }

    public NTree getTree() {
        return tree;
    }

    public void addChild(Node id) {
        childrens.add(id);
    }

    public ArrayList<Node> getChilds() { // FIXME: it is really necessary
        return new ArrayList<Node>(childrens);
    }

    public int childsCount() {
        return childrens.size();
    }
    
    @Override
    public String toString() {
        return "Node " + id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
