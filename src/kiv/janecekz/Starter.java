/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

import java.util.LinkedList;

public class Starter {
    public static void main(String[] args) {
        NTree familyTree = new NTree("familyData");
        Genealogy gen = new Genealogy();

        gen.setMe(familyTree.getId(4));
        gen.answer(familyTree.getId(1), "můj potomek");
    }
    
    /**
     * For example [aa,bb,cc] returns {"aa", "bb", "cc"}
     * @param s String to tokenize
     * @return array with result
     */
    public static String[] getTokens(String s) {
        int spos = 0;
        int endPos = 0;

        LinkedList<String> list = new LinkedList<String>();

        for (int i = 1; i < s.length() - 1; i++) {
            spos = endPos + 1;

            for (; i < s.length() - 1; i++) {
                if ((s.charAt(i) == ',')) {
                    break;
                }
            }

            endPos = i;

            list.add(s.substring(spos, endPos).trim());
        }
        
        return list.toArray(new String[list.size()]);
    }
}
