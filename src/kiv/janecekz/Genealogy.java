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

public class Genealogy {
    private Expert ex;
    
    private Node me;

    private class Question {
        public String desc;
        public String rule;
        
        public Question(String desc, String rule) {
            this.desc = desc;
            this.rule = rule;
        }
    }
    
    private Question[] questions;
    
    public Genealogy() {
        ex = new Expert("expertData");
        processInput("questions");
    }
    
    public String answer(Node person, String q) {
        q = q.toLowerCase();
        
        String rule = null;
        for (Question oneq : questions) {
            if (q.equals(oneq.desc)) {
                System.out.println("Budu řešit: "+oneq.rule+" od osoby "+me.toString());
                rule = oneq.rule;
            }
        }
        
        String val = ex.getRelationInfo(Integer.parseInt(rule))[Expert.POS_RULE];
        
        boolean testRelVal = true;
        int symIt = 0;
        
        while (testRelVal && symIt >= val.length()) {
            switch (val.charAt(symIt++)) {
            case 'P':
                testRelVal &= ex.isParentOf(me, person);
                break;
            case 'C':
                testRelVal &= ex.isChildrenOf(person, me);
                break;
            default:
                break;
            }
        }
        
        System.out.println(testRelVal);
        
        return null;
    }
    
    public void listChilds(Collection<Node> persons, Collection<Node> result) {
        result.clear();
        
        if (result instanceof HashSet) {
            for (Node p : persons) {
                result.addAll(p.listOfChilds());
            }
        } else {
            for (Node p : persons) {
                for (Node c : p.listOfChilds()) {
                    if (!result.contains(c))
                        result.add(c);
                }
                
            }
        }
    }
    
    public void listParents(Collection<Node> persons, Collection<Node> result) {
        result.clear();
        
        if (result instanceof HashSet) {
            for (Node p : persons) {
                result.add(p.getFather());
                result.add(p.getMother());
            }
        } else {
            for (Node p : persons) {
                if (!result.contains(p.getFather()))
                    result.add(p.getFather());
                if (!result.contains(p.getMother()))
                    result.add(p.getMother());
            }
        }
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
            questions = new Question[count];

            for (int i = 0; i < count; i++) {
                curLine = input.readLine();
                String[] values = Starter.getTokens(curLine);
                curLine = input.readLine();

                Question oneq = new Question(values[0], curLine);
                
                oneq.rule = curLine.trim();
                
                questions[i] = oneq;
            }

            input.close();
        } catch (IOException e) {
            System.out.println("Chyba při čtení!");
        }

        return true;
    }
    
    public void setMe(Node me) {
        this.me = me;
    }
}
