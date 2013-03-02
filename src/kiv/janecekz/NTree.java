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
 * prepares instances of the nods.
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
    
    private Nod[] instances;

    public boolean processInput(String file) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return false;
        }

        String curLine;
        try {
            // first line shows count of input data
            instances = new Nod[Integer.parseInt(input.readLine())];

            // virtual root node
            instances[0] = new Nod(0, "Root", null, null, true, null);

            while ((curLine = input.readLine()) != null) {
                curLine = curLine.trim();
                if ((curLine.charAt(0) != '[')
                        || (curLine.charAt(curLine.length() - 1) == ']')) {
                    // TODO: be angry on user

                    continue;
                }

                Nod newNod = createNod(curLine);
            }

            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Format is [id, name, mother, father, male, partner]
     * [1, Petr Janeček, Jitka Janečková, Jiří Janeček, true, nil]
     * @param s
     * @return
     */
    private Nod createNod(String s) {
        Nod newNod;
        
        String name;
        Nod mother;
        Nod father;
        boolean male;
        Nod partner;

        String[] tokens = new String[6];

        int tokenNum = 0;
        int spos = 1;
        int endPos = 1;

        for (int i = 1; i < s.length() - 1; i++) {
            spos = endPos + 1;
            
            for ( ; i < s.length() - 1; i++) {
                if ((s.charAt(i) == ',')) {
                    break;
                }
            }
            
            endPos = i;
            
            tokens[tokenNum++] = s.substring(spos, endPos).trim();
        }
        
        male = Boolean.parseBoolean(tokens[POS_MALE]);
        name = tokens[POS_NAME];
        
        int index = Integer.parseInt(tokens[POS_MOTHER]);
        if (instances[index] == null) {
            mother = new Nod(index, null, null, null, false, null);
            instances[index] = mother;
        } else
            mother = instances[index];
        
        index = Integer.parseInt(tokens[POS_FATHER]);
        if (instances[index] == null) {
            father = new Nod(index, null, null, null, true, null);
            instances[index] = father;
        } else
            father = instances[index];
        
        index = Integer.parseInt(tokens[POS_PARTNER]);
        if (instances[index] == null) {
            partner = new Nod(index, null, null, null, !male, null);
            instances[index] = partner;
        } else
            partner = instances[index];
        
        index = Integer.parseInt(tokens[POS_ID]);
        
        if (instances[index] == null) {
            newNod = new Nod(index, name, mother, father, male, partner);
            instances[index] = newNod;
        } else {
            newNod = instances[index];
            newNod.updateInfo(name, mother, father, partner);
        }
        
        return newNod;
    } 
}
