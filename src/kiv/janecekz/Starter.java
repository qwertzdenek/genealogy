/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

public class Starter {
    public static void main(String[] args) {
        NTree familyTree = new NTree();
        
        Genealogy gen = new Genealogy(familyTree);
        
        familyTree.processInput("data");
        
        System.out.println("\nSourozenci čísla 3:");
        
        for (Node person : gen.listSiblings(familyTree.getId(1))) {
            System.out.println(person.toString());
        }
        
        System.out.println("\nPokrevní linie od 3:");
        
        for (Node person : gen.listBlodLine(familyTree.getId(3))) {
            System.out.println(person.toString());
        }
    }
}
