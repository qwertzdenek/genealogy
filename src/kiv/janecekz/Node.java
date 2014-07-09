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

import java.util.Collection;
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
            return tree.nodes[partner];
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
            return tree.nodes[mother];
    }

    public Node getFather() {
        if (father == NONE)
            return null;
        else
            return tree.nodes[father];
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

    public Collection<Node> getChilds() {
        return childrens;
    }

    @Override
    public String toString() {
        return "Node " + id;
    }
}
