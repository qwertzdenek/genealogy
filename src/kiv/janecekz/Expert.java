/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Expert {
    public static final int POS_ID = 0;
    public static final int POS_DESC = 1;
    public static final int POS_MALENAME = 2;
    public static final int POS_FEMALENAME = 3;
    public static final int POS_RULE = 4;

    private class Relation {
        public final int id;
        public final String desc;
        public final String maleName;
        public final String femaleName;

        public String rule;

        public Relation(int id, String desc, String maleName, String femaleName) {
            this.id = id;
            this.desc = desc;
            this.maleName = maleName;
            this.femaleName = femaleName;
        }
    }

    private Relation[] relations;

    public Expert(String file) {
        boolean res = processInput(file);

        if (!res)
            System.out.println("Došlo k chybě při čtení souboru " + file);
    }

    /**
     * Function for Predicate logic expression. Method fills {@code b} with childrens
     * of all elements of {@code a}.
     * 
     * @param a parent nodes
     * @param b child nodes
     */
    public void parentOf(Collection<Node> a, Collection<Node> b) {
        if (!(b instanceof HashSet))
            b = new HashSet<Node>();

        b.clear();
        
        for (Node node : a) {
            b.add(node.getFather());
            b.add(node.getMother());
        }
    }

    /**
     * Function for Predicate logic expression. Method fills {@code b} with parents
     * of all elements of {@code a}.
     * 
     * @param a children nodes
     * @param b parent nodes
     */
    public void childrenOf(Collection<Node> a, Collection<Node> b) {
        if (!(b instanceof HashSet))
            b = new HashSet<Node>();

        b.clear();
        
        for (Node node : a) {
            b.addAll(node.listOfChilds());
        }
    }

    /**
     * Function for Predicate logic expression. Method fills b with
     * all objects that are in {@code a} but without objects in {@code b}.
     * 
     * @param a source nodes
     * @param b nodes to exclude from a
     */
    public void notThis(Collection<Node> a, Collection<Node> b) {
        Collection<Node> toDelete = new LinkedList<Node>();
        toDelete.addAll(b);
        b.clear();
        for (Node node : a) {
            if (!(toDelete.contains(node)))
                b.add(node);
        }
    }
    
    public String[] getRelationInfo(int id) {
        String[] result = new String[5];
        result[POS_ID] = Integer.toString(id);
        result[POS_DESC] = relations[id].desc;
        result[POS_MALENAME] = relations[id].maleName;
        result[POS_FEMALENAME] = relations[id].femaleName;
        result[POS_RULE] = relations[id].rule;

        return result;
    }

    private boolean processInput(String file) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Soubor nenalezen!");
            return false;
        }

        try {
            String curLine;

            // We add one virtual nod to save end of line.
            int count = Integer.parseInt(input.readLine().trim());

            // first line shows count of input data
            relations = new Relation[count];

            for (int i = 0; i < count; i++) {
                curLine = input.readLine();
                String[] values = Starter.getTokens(curLine);
                int id = Integer.parseInt(values[POS_ID]);

                Relation onerel = new Relation(id, values[POS_DESC],
                        values[POS_MALENAME], values[POS_FEMALENAME]);

                curLine = input.readLine();
                onerel.rule = curLine.trim().toUpperCase();

                relations[i] = onerel;
            }

            input.close();
        } catch (IOException e) {
            System.out.println("Chyba při čtení!");
        }

        return true;
    }
}
