package kiv.janecekz;

import java.util.Collection;
import java.util.HashSet;

public class Genealogy {

    /**
     * @param args
     */
    public static void main(String[] args) {
        NTree familyTree = new NTree();
        
        familyTree.processInput("data");
        
        System.out.println("\nSourozenci čísla 3:");
        
        for (Node person : listSiblings(familyTree.getId(1))) {
            System.out.println(person.toString());
        }
        
        System.out.println("\nPokrevní linie od 3:");
        
        for (Node person : listBlodLine(familyTree.getId(3))) {
            System.out.println(person.toString());
        }
    }

    public static Node[] listSiblings(Node person) {
        HashSet<Node> res = new HashSet<Node>();
        for (Node node : person.getMother().listOfChilds()) {
            res.add(node);
        }
        
        for (Node node : person.getFather().listOfChilds()) {
            res.add(node);
        }
        
        res.remove(person);
        
        return res.toArray(new Node[res.size()]);
    }
    
    public static Collection<Node> listBlodLine(Node person) {
        HashSet<Node> res = new HashSet<Node>();
        
        listAllChilds(person, res);
        listAllParents(person, res);
        
        res.remove(person.getTree().getId(0));
        
        return res;
    }
    
    public static void listAllChilds(Node person, HashSet<Node> set) {
        Collection<Node> list = person.listOfChilds();
        
        if (list == null)
            return;
        
        set.addAll(list);
        for (Node node : list) {
            listAllChilds(node, set);
        }
    }
    
    public static void listAllParents(Node person, HashSet<Node> set) {
        if (person.getMother() != null) {
            set.add(person.getMother());
            listAllParents(person.getMother(), set);
        }
        
        if (person.getFather() != null) {
            set.add(person.getFather());
            listAllParents(person.getFather(), set);
        }
    }
}
