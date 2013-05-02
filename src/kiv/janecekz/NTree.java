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

/**
 * Utility class for the tasks with the N-tree. Imports external data file and
 * prepares instances of the nodes.
 * 
 * @author Zdeněk Janeček
 */
public class NTree {
    private static final int POS_ID = 0;
    private static final int POS_NAME = 1;
    private static final int POS_MOTHER = 2;
    private static final int POS_FATHER = 3;
    private static final int POS_MALE = 4;
    private static final int POS_PARTNER = 5;

    private Node[] instances;

    public NTree(String file) {
        boolean res = processInput(file);
        
        if (!res)
            System.out.println("Došlo k chybě při čtení souboru "+file);
    }
    
    private boolean processInput(String file) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Soubor nenalezen!");
            return false;
        }

        String curLine;
        try {
            // We add one virtual nod to save end of line.
            int count = Integer.parseInt(input.readLine().trim()) + 1;

            // first line shows count of input data
            instances = new Node[count];

            // virtual root node
            instances[0] = new Node(0, "Super Mother", null, null, false, null, this);

            for (int i = 1; i < count; i++) {
                curLine = input.readLine().trim();
                if ((curLine.charAt(0) != '[')
                        || (curLine.charAt(curLine.length() - 1) != ']')) {
                    // TODO: be angry on user

                    continue;
                }

                Node newNod = createNod(curLine);
                instances[newNod.getId()] = newNod;
            }

            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
        System.out.println("Načteno: ");
        for (Node nod : instances) {
            System.out.println(nod.toString());
        }

        System.out.println("\nPotomci Super Matky: ");
        for (Node nod : instances[0].listOfChilds()) {
            System.out.println(nod.toString());
        }
         */
        return true;
    }

    public Node getId(int id) {
        if (id >= 0 && id < instances.length)
            return instances[id];
        else
            return null;
    }

    public int size() {
        return instances.length;
    }
    
    /**
     * Format is [id, name, mother, father, male, partner]
     * 
     * @param s
     * @return
     */
    private Node createNod(String s) {
        Node newNod;

        int id;
        String name;
        Node mother = null;
        Node father = null;
        boolean male;
        Node partner = null;

        String[] tokens = Starter.getTokens(s);

        male = Boolean.parseBoolean(tokens[POS_MALE]);
        name = tokens[POS_NAME];
        id = Integer.parseInt(tokens[POS_ID]);

        for (int i : new int[] { POS_MOTHER, POS_FATHER, POS_PARTNER }) {
            int index = tokens[i].equals("nil") ? 0 : Integer
                    .parseInt(tokens[i]);

            switch (i) {
            case POS_MOTHER:
                if (instances[index] == null) {
                    mother = new Node(index, null, null, null, false, null, this);
                    instances[index] = mother;
                } else {
                    mother = instances[index];
                }

                mother.addChild(id);
                break;
            case POS_FATHER:
                if (index > 0) {
                    if (instances[index] == null) {
                        father = new Node(index, null, null, null, true, null, this);
                        instances[index] = father;
                    } else
                        father = instances[index];

                    father.addChild(id);
                }
                break;

            case POS_PARTNER:
                if (index > 0) {
                    if (instances[index] == null) {
                        partner = new Node(index, null, null, null, !male, null, this);
                        instances[index] = partner;
                    } else
                        partner = instances[index];
                }
                break;

            default:

                break;
            }
        }

        if (instances[id] == null) {
            newNod = new Node(id, name, mother, father, male, partner, this);
            instances[id] = newNod;
        } else {
            newNod = instances[id];
            newNod.updateInfo(name, mother, father, partner);
        }

        return newNod;
    }
    
    public void printTree() {
        instances[0].printPretty("", true);
    }
}
