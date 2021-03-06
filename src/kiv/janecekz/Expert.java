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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This is an expert class that gives you an answers.
 * 
 * @author Zdeněk Janeček
 */
public class Expert {
    public static final int POS_ID = 0;
    public static final int POS_DESC = 1;
    public static final int POS_MALENAME = 2;
    public static final int POS_FEMALENAME = 3;
    public static final int POS_RULE = 4;

    private static final String P = "P";
    private static final String C = "C";

    private Relation[] relations;

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

    /**
     * You can have as much instances you want with the different family
     * trees.
     * 
     * @param tree Family tree you want to get results from.
     */
    public Expert(File expertFile) {
        try {
	    processExpInput(expertFile);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Function for Predicate logic expression. Method returns Collection with
     * parents of all elements from {@code a}.
     * 
     * @param a child nodes
     * @return parent nodes
     */
    private Collection<Node> parentOf(Collection<Node> a, NTree tree) {
        Collection<Node> b = new HashSet<Node>();

        for (Node node : a) {
            b.add(node.getFather());
            b.add(node.getMother());
        }

        return b;
    }

    /**
     * Function for Predicate logic expression. Method returns Collection with
     * childrens of all elements from {@code a}.
     * 
     * @param a parent nodes
     * @return child nodes
     */
    private Collection<Node> childrenOf(Collection<Node> a) {
        Collection<Node> b = new HashSet<Node>();

        for (Node node : a) {
            b.addAll(node.getChilds());
        }

        return b;
    }

    /**
     * Function for Predicate logic expression. Method retuns new HashSet
     * with all objects that are in {@code a} but without objects in {@code b}.
     * 
     * @param a source nodes
     * @param b nodes to exclude from a
     */
    private Collection<Node> notThis(Collection<Node> a, Collection<Node> b) {
        Collection<Node> newSet = new HashSet<Node>();
        for (Node node : a) {
            if (!(b.contains(node)))
                newSet.add(node);
        }

        return newSet;
    }

    /**
     * Compares complex rule with general path. Like PxaPabCbcNca with PPC gives
     * true.
     * 
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
                i += 2;
            }
        }

        return rule1strip.toString().equals(rule2);
    }

    /**
     * Get from the general rule (PPC) index to the {@code relations} table.
     * 
     * @param rule rule you want to find
     * @return index to the {@code relations} table.
     */
    public int getRealationId(String rule) {
        for (int i = 0; i < relations.length; i++) {
            if (compareRel(relations[i].rule, rule)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Description array of one relation.
     * 
     * @param id relation id
     * @return string array. Indexes are described by the Expert
     * constants POS_*.
     */
    private String[] getRelationInfo(int id) {
        String[] result = new String[5];
        result[POS_ID] = Integer.toString(id);
        result[POS_DESC] = relations[id].desc;
        result[POS_MALENAME] = relations[id].maleName;
        result[POS_FEMALENAME] = relations[id].femaleName;
        result[POS_RULE] = relations[id].rule;

        return result;
    }

    /**
     * Simple method to get whole library of relations.
     * @return twodimensional array with description.
     */
    public String[][] getRelations() {
        String[][] result = new String[relations.length][5];
        for (int i = 0; i < relations.length; i++) {
            result[i] = getRelationInfo(i);
        }

        return result;
    }

    /**
     * Searches for some person and returns their relation.
     * 
     * @param from Person to search from.
     * @param target Person to search for.
     * @return result of search
     */
    public String findPerson(Node from, Node target) {
        if (from == null || target == null) {
            return "(II) Can't continue to find person";
        }

        String result = "Not found";
        LinkedList<Node> q = new LinkedList<Node>();
        boolean[] visited = new boolean[tree.size()];

        String[] paths = new String[tree.size()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = "";
        }

        visited[from.getId()] = true;
        q.add(from);

        while (!q.isEmpty()) {
            Node v = q.pollFirst();

            if (v.equals(target)) {
                int id = getRealationId(paths[v.getId()]);
                if (id == -1) {
                    result = "Relation not found";
                } else {
                    if (v.isMale())
                        result = "Found " + v.getName() + " in relation: "
                                + relations[id].maleName;
                    else
                        result = "Found " + v.getName() + " in relation "
                                + relations[id].femaleName;
                }
                
                break;
            }

            for (Node w : v.getChilds()) {
                if (!visited[w.getId()]) {
                    visited[w.getId()] = true;
                    paths[w.getId()] = paths[v.getId()] + C;
                    q.add(w);
                }
            }

            Node[] parents = { v.getFather(), v.getMother() };

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

    /**
     * Finds all persons in specified relation.
     * 
     * @param from Person to search from.
     * @param rel wanted relation
     * @param male is male or female
     * @return LinkedList of person instances
     */
    public Collection<Node> findPersons(Node from, int rel, boolean male) {
        if (from == null)
            return null;

        String rule = relations[rel].rule;
        LinkedList<Node> q = new LinkedList<Node>();
        boolean[] visited = new boolean[tree.size()];
        Collection<Node> result = new LinkedList<Node>();

        String[] paths = new String[tree.size()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = "";
        }

        visited[from.getId()] = true;
        q.add(from);

        while (!q.isEmpty()) {
            Node v = q.pollFirst();

            if (v.isMale() == male && compareRel(rule, paths[v.getId()])) {
                result.add(v);
            }

            for (Node w : v.getChilds()) {
                if (!visited[w.getId()]) {
                    visited[w.getId()] = true;
                    paths[w.getId()] = paths[v.getId()] + C;
                    q.add(w);
                }
            }

            Node[] parents = { v.getFather(), v.getMother() };

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

    /**
     * @param from From which person is done an comparison.
     * @param target To which person is done an comparison.
     * @param rel relation id we want to test
     * @param male true if I look for male, false if it is female
     * 
     * @return true if it founds the person and the false in the other case
     */
    public boolean testRelation(Node from, Node target, int rel, boolean male) {
        boolean result = false;
        HashMap<Character, Collection<Node>> terms;
        terms = new HashMap<Character, Collection<Node>>();

        HashSet<Node> first = new HashSet<Node>();
        HashSet<Node> second = null;
        Collection<Node> test = null;

        Collection<Node> initial = new HashSet<Node>();
        initial.add(from);
        terms.put('x', initial);

        String val = relations[rel].rule;

        for (int symIt = 0; symIt < val.length(); symIt++) {
            switch (val.charAt(symIt)) {
            case 'P':
                first = (HashSet<Node>) terms.get(new Character(val
                        .charAt(++symIt)));
                test = parentOf(first);
                if (test.contains(tree.getId(0)) || test == null)
                    return false;
                else
                    second = (HashSet<Node>) test;
                terms.put(val.charAt(++symIt), new HashSet<Node>(second));
                break;
            case 'C':
                first = (HashSet<Node>) terms.get(new Character(val
                        .charAt(++symIt)));
                test = childrenOf(first);
                if (test == null)
                    return false;
                else
                    second = (HashSet<Node>) test;
                terms.put(val.charAt(++symIt), new HashSet<Node>(second));
                break;
            case 'N':
                first = (HashSet<Node>) terms.get(new Character(val
                        .charAt(++symIt)));
                second = (HashSet<Node>) terms.get(new Character(val
                        .charAt(++symIt)));
                second = (HashSet<Node>) notThis(first, second);
                break;
            default:
                break;
            }
        }

        if (male == target.isMale() && second.contains(target))
            result = true;
        else
            result = false;

        return result;
    }

    /**
     * Get your blood line. They are all parents of parents and so on.
     * @param from Person to search from.
     * @param deep how deep you want to search
     * @return all persons that are my ascendants
     */
    public Collection<Node> bloodline(Node from, int deep) {
        Collection<Node> res = new HashSet<Node>();
        LinkedList<Node> q = new LinkedList<Node>();

        q.add(from);

        int d = 0;
        int timeToDepthIncrease = 1;
        boolean pendingDepthIncrease = false;
        while (!q.isEmpty()) {
            if (d > deep)
                break;

            Node v = q.pollFirst();
            res.add(v);
            timeToDepthIncrease--;
            
            if (timeToDepthIncrease == 0) {
                d++;
                pendingDepthIncrease = true;
            }

            Node[] parents = { v.getFather(), v.getMother() };

            for (Node w : parents) {
                if (w == null || w.equals(tree.getId(0)))
                    continue;

                q.add(w);
            }
            
            if (pendingDepthIncrease) {
                timeToDepthIncrease = q.size();
                pendingDepthIncrease = false;
            }
        }

        res.remove(from);
        
        return res;
    }

    private void processExpInput(File file) throws IOException {
        BufferedReader input = null;
        input = new BufferedReader(new FileReader(file));
        
        String curLine;

        // We add one virtual nod to save end of line.
        int count = Integer.parseInt(input.readLine().trim());

        // first line shows count of input data
        relations = new Relation[count];

        for (int i = 0; i < count; i++) {
            curLine = input.readLine();
            String[] values = DataManipulation.getTokens(curLine);
            int id = Integer.parseInt(values[POS_ID]);

            Relation onerel = new Relation(id, values[POS_DESC],
                    values[POS_MALENAME], values[POS_FEMALENAME]);

            curLine = input.readLine();
            onerel.rule = curLine.trim();

            relations[i] = onerel;
        }

        input.close();
    }
}
