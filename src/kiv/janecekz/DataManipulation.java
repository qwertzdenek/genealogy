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

import java.util.Collection;
import java.util.LinkedList;

public class DataManipulation {
    /*
     * private static final int POS_ID = 0; private static final int POS_NAME =
     * 1; private static final int POS_MOTHER = 2; private static final int
     * POS_FATHER = 3; private static final int POS_MALE = 4; private static
     * final int POS_PARTNER = 5;
     */

    /**
     * For example [aa,bb,cc] returns {"aa", "bb", "cc"}
     * 
     * @param s
     *            String to tokenize
     * @return array with result
     */
    public static Collection<String> getTokens(String s) {
        int spos = 0;
        int endPos = 0;

        if (s == null)
            return null;

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

        return list;
    }

    public static void sort(Node[] el) {
        Node par;
        Node tmp;

        for (int i = 0; i < el.length; i++) {
            par = el[i].getPartner();

            if (par == null)
                continue;

            for (int j = i + 1; j < el.length; j++) {
                if (el[j].equals(par)) {
                    tmp = el[i + 1];
                    el[i + 1] = el[j];
                    el[j] = tmp;
                    i++;
                    break;
                }
            }
        }
    }
}
