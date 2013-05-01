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
import java.util.HashMap;
import java.util.HashSet;

public class Genealogy {
    private Expert ex;
    private Node me;
    private HashMap<Character, Collection<Node>> terms;

    private class Question {
        public String desc;
        public String rule;

        public Question(String desc, String rule) {
            this.desc = desc;
            this.rule = rule;
        }
    }

    private Question[] questions;

    public Genealogy(NTree tree) {
        ex = new Expert("expertData", tree);
        processInput("questions");
    }

    // TODO: Oddělit otázky. V jakém jsou vztahu a zda jsou v tomto stavu.

    public String answer(Node person, String q) {
        if (person == null || me == null) {
            return "(II) Nemůžu pokračovat";
        }
        
        
        String result = "(EE) Neumím odpovědět";
        q = q.toLowerCase();

        String rule = "n";
        for (Question oneq : questions) {
            if (q.equals(oneq.desc)) {
                rule = oneq.rule;
            }
        }
        
        if (rule.charAt(0) == 'n') {
            result = "(EE) Neznámá otázka";
        } else if (rule.charAt(0) == 'f') {
            result = ex.findPerson(me, person);
        } else if (rule.charAt(0) == 'i'){
            boolean male = rule.charAt(1) == 'm' ? true : false;
            rule = rule.substring(2);
            String val = ex.getRelationInfo(Integer.parseInt(rule))[Expert.POS_RULE];

            HashSet<Node> first = new HashSet<Node>();
            HashSet<Node> second = null;
            terms = new HashMap<Character, Collection<Node>>();

            Collection<Node> initial = new HashSet<Node>();
            initial.add(me);
            terms.put('x', initial);
            
            for (int symIt = 0; symIt < val.length(); symIt++) {
                switch (val.charAt(symIt)) {
                case 'P':
                    first = (HashSet<Node>) terms.get(new Character(val.charAt(++symIt)));
                    second = (HashSet<Node>) ex.parentOf(first);
                    terms.put(val.charAt(++symIt), new HashSet<Node>(second));
                    break;
                case 'C':
                    first = (HashSet<Node>) terms.get(new Character(val.charAt(++symIt)));
                    second = (HashSet<Node>) ex.childrenOf(first);
                    terms.put(val.charAt(++symIt), new HashSet<Node>(second));
                    break;
                case 'N':
                    first = (HashSet<Node>) terms.get(new Character(val.charAt(++symIt)));
                    second = (HashSet<Node>) terms.get(new Character(val.charAt(++symIt)));
                    second = (HashSet<Node>) ex.notThis(first, second);
                    break;
                default:
                    break;
                }
            }

            if (male == person.isMale() && second.contains(person))
                result = "Pravda";
            else
                result = "Nepravda";
        }
        return result;
    }

    public Collection<Node> listChilds(Collection<Node> persons,
            Collection<Node> result) {
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

        return result;
    }

    public Collection<Node> listParents(Collection<Node> persons,
            Collection<Node> result) {
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
        if (me == null)
            System.out.println("(EE) Osoba nenalezena");
        else
            this.me = me;
    }
}
