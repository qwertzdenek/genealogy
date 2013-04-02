/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

import java.util.Collection;
import java.util.HashSet;

public class Genealogy {
    private final int GEN_ME = 9;
    private final int GEN_BROTHER = 10;
    private final int GEN_SISTER = 11;
    private final int GEN_MOTHER = 12;
    private final int GEN_FATHER = 13;
    private final int GEN_GRAND_FATHER = 14;
    private final int GEN_GRAND_MOTHER = 15;
    
    private NTree tree;

    public Genealogy(NTree tree) {
        super();
        this.tree = tree;
    }

    public Collection<Node> listSiblings(Node person) {
        HashSet<Node> res = new HashSet<Node>();
        for (Node node : person.getMother().listOfChilds()) {
            res.add(node);
        }
        
        for (Node node : person.getFather().listOfChilds()) {
            res.add(node);
        }
        
        res.remove(person);
        
        return res;
    }
    
    public Collection<Node> listBlodLine(Node person) {
        HashSet<Node> res = new HashSet<Node>();
        
        listAllChilds(person, res);
        listAllParents(person, res);
        
        res.remove(person.getTree().getId(0));
        
        return res;
    }
    
    public void listAllChilds(Node person, HashSet<Node> set) {
        Collection<Node> list = person.listOfChilds();
        
        if (list == null)
            return;
        
        set.addAll(list);
        for (Node node : list) {
            listAllChilds(node, set);
        }
    }
    
    public void listAllParents(Node person, HashSet<Node> set) {
        if (person.getMother() != null) {
            set.add(person.getMother());
            listAllParents(person.getMother(), set);
        }
        
        if (person.getFather() != null) {
            set.add(person.getFather());
            listAllParents(person.getFather(), set);
        }
    }
    
    public int getRelation(Node person, Node second) {
        int state = GEN_ME;
        
        // TODO: nějaký stavový automat rozpoznávající stav mezi dvěma osobama.
        return state;
    }
}
