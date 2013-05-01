/*
 * Genealogy expert system.
 * Semestral work for the University of West Bohemia.
 * 
 * Written by Zdeněk Janeček, 2013
 * Share it freely under conditions of GNU GPL v3
 */

package kiv.janecekz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Starter {
    private static BufferedReader br;

    private static void dieWithError(String s) {

        try {
            if (br != null)
                br.close();
        } catch (IOException e) {
            System.out.println("Ooops");
        }
        System.exit(-1);
    }

    public static void main(String[] args) {
        br = new BufferedReader(new InputStreamReader(System.in));
        NTree familyTree = new NTree("familyData");
        Genealogy gen = new Genealogy(familyTree);

        String q = null;
        Node me = null;
        Node tar = null;
        try {
            do {
                System.out.print("Výchozí člověk: ");
                int m = Integer.parseInt(br.readLine());
                me = familyTree.getId(m);
            } while (me == null);
            System.out.println(">> "+me.toString());
            
            do {
                System.out.print("Cílová osoba: ");
                int t = Integer.parseInt(br.readLine());
                tar = familyTree.getId(t);
            } while (tar == null);
            System.out.println(">> "+tar.toString());
            
            System.out.print("Otázka: ");
            q = br.readLine();
        } catch (NumberFormatException e1) {
            dieWithError("Neplatné číslo");
        } catch (IOException e1) {
            dieWithError("Chyba vstupu výstupu");
        }
        
        gen.setMe(me);
        String r = gen.answer(tar, q);
        
        System.out.println("\n>> "+r);
        
        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * For example [aa,bb,cc] returns {"aa", "bb", "cc"}
     * 
     * @param s
     *            String to tokenize
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
