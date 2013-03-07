/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Node {
    private boolean[] childrens;
    
    private String name;
    private int id;
    private Node mother;
    private Node father;
    private boolean male;
    private Node partner;
    private NTree tree;

    public Node(int id, String name, Node mother, Node father, boolean male,
            Node partner, NTree tree) {
        super();
        this.name = name;
        this.id = id;
        this.mother = mother;
        this.father = father;
        this.male = male;
        this.partner = partner;
        this.tree = tree;
    }

    public Node getPartner() {
        return partner;
    }

    public void setPartner(Node partner) {
        this.partner = partner;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Node getMother() {
        return mother;
    }

    public Node getFather() {
        return father;
    }

    public NTree getTree() {
        return tree;
    }
    
    public boolean isMale() {
        return male;
    }

    public boolean updateInfo(String name, Node mother, Node father, Node partner) {
        if (this.name == null)
            this.name = name;
        else
            return false;
        
        if (this.mother == null)
            this.mother = mother;
        else
            return false;
        
        if (this.father == null)
            this.father = father;
        else
            return false;
        
        if (this.partner == null)
            this.partner = partner;
        else
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return "Man [id=" + id + ", name=" + name + ", male=" + male + "]";
    }

    public void addChild(int id) {
        if (childrens == null)
            childrens = new boolean[tree.size()];
        
        if (id >= childrens.length)
            Arrays.copyOf(childrens, tree.size());
        
        if (id > 0)
            childrens[id] = true;
    }
    
    public Collection<Node> listOfChilds() {
        if (childrens == null)
            return null;
        
        LinkedList<Node> childs = new LinkedList<Node>();
        for (int i = 0; i < childrens.length; i++) {
            if (childrens[i]) {
                childs.add(tree.getId(i));
            }
        }
        
        return childs;
    }
}
