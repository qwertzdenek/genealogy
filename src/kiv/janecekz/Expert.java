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
    
    private final String P = "P";
    private final String C = "C";

    private NTree tree;
    
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

    public Expert(String file, NTree tree) {
        this.tree = tree;  
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
    public Collection<Node> parentOf(Collection<Node> a) {
        Collection<Node> b = new HashSet<Node>();
        
        for (Node node : a) {
            b.add(node.getFather());
            b.add(node.getMother());
        }
        
        return b;
    }

    /**
     * Function for Predicate logic expression. Method fills {@code b} with parents
     * of all elements of {@code a}.
     * 
     * @param a children nodes
     * @param b parent nodes
     */
    public Collection<Node> childrenOf(Collection<Node> a) {
        Collection<Node> b = new HashSet<Node>();

        for (Node node : a) {
            b.addAll(node.listOfChilds());
        }
        
        return b;
    }

    /**
     * Function for Predicate logic expression. Method fills b with
     * all objects that are in {@code a} but without objects in {@code b}.
     * 
     * @param a source nodes
     * @param b nodes to exclude from a
     */
    public Collection<Node> notThis(Collection<Node> a, Collection<Node> b) {
        Collection<Node> newSet = new HashSet<Node>();
        for (Node node : a) {
            if (!(b.contains(node)))
                newSet.add(node);
        }
        
        return newSet;
    }
    
    /**
     * Compares complex rule with general path. Like PxaPabCbcNca with PPC gives true.
     * @param rule1
     * @param rule2
     * @return
     */
    private boolean compareRel(String rule1, String rule2) {
        StringBuilder rule1strip = new StringBuilder();
        // clean rule1
        for (int i = 0; i < rule1.length(); i++) {
            if (rule1.charAt(i) == 'P' || rule1.charAt(i) == 'C') {
                rule1strip.append(rule1.charAt(i));
                i += 2;
            } else {
                i++;
            }
        }
        
        return rule1strip.toString().equals(rule2);
    }
    
    public int getRealationId(String rule) {
        for (int i = 0; i < relations.length; i++) {
            if (compareRel(relations[i].rule,rule)) {
                return i;
            }
        }
        
        return -1;
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

    // TODO: hledání osoby
    public String findPerson(Node from, Node target) {
        String result = "Nenalezen";
        LinkedList<Node> q = new LinkedList<Node>();
        boolean[] visited = new boolean[tree.size()];
        
        String[] paths = new String[tree.size()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = "";
        }
        
        visited[from.getId()] = true;
        q.add(from);
        
        while (!q.isEmpty()) {
            Node v = q.pollLast();
            
            if (v.equals(target)) {
                int id = getRealationId(paths[v.getId()]);
                if (id == -1) {
                    result = "Nenalezen vztah";
                } else {
                    if (v.isMale())
                        result = "Nalezen "+v.getName()+" ve vztahu "+relations[id].maleName;
                    else
                        result = "Nalezena "+v.getName()+" ve vztahu "+relations[id].femaleName;
                }
            }
            
            for (Node w : v.listOfChilds()) {
                if (!visited[w.getId()]) {
                    visited[w.getId()] = true;
                    paths[w.getId()] = paths[v.getId()] + C;
                    q.add(w);
                }
            }
            
            Node[] parents = {v.getFather(), v.getMother()};
            
            for (Node w : parents) {
                if (w == null)
                    continue;
                
                if (!visited[w.getId()]) {
                    visited[w.getId()] = true;
                    paths[w.getId()] = paths[v.getId()] + P;
                    q.add(w);
                }
            }
        }
        
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
                onerel.rule = curLine.trim();

                relations[i] = onerel;
            }

            input.close();
        } catch (IOException e) {
            System.out.println("Chyba při čtení!");
        }

        return true;
    }
}
