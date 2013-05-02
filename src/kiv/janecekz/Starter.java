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
    private static NTree familyTree;
    private static Expert ex;

    private static final char ESC = 27;

    public static void dieWithError(String s) {
        System.out.println(s);
        try {
            if (br != null)
                br.close();
        } catch (IOException e) {
            System.out.println("Ooops");
        }
        System.exit(-1);
    }

    private static void printMenu() {
        System.out.println("Otázky:");
        System.out.println("1)  kdo všechno je");
        System.out.println("2)  test vztahu");
        System.out.println("3)  v jakém vztahu jsou");
        System.out.println("4)  vypsat strom\n");
    }

    private static String doAnswer() {
        Node from;
        Node target;
        int[] r;
        boolean male;

        System.out.print(ESC + "[0;0H");
        System.out.print(ESC + "[2J");
        printMenu();

        int q = 1;
        try {
            q = Integer.parseInt(prompt(">> "));

            switch (q) {
            case 1:
                from = promptNode();
                r = promptRel();
                male = r[1] == 0 ? false : true;
                System.out.println(ex.findPersons(from, r[0], male));
                break;
            case 2:
                from = promptNode();
                target = promptNode();
                r = promptRel();
                male = r[1] == 0 ? false : true;
                System.out.println(ex.testRelation(from, target, r[0], male));
                break;
            case 3:
                from = promptNode();
                target = promptNode();

                System.out.println(ex.findPerson(from, target));
                break;
            case 4:
                System.out.println("Načten strom:");
                familyTree.printTree();
                break;
            default:
                break;
            }
        } catch (NumberFormatException e) {
            dieWithError("(EE) Neplatné číslo");
        }
        return null;
    }
    
    private static String prompt(String prompt) {
        System.out.print(prompt);
        String res = null;
        try {
            res = br.readLine();
        } catch (IOException e) {
            dieWithError("(EE) chyba vstupu");
        }
        return res;
    }

    private static Node promptNode() {
        System.out.print(ESC + "[0;0H");
        System.out.print(ESC + "[2J");
        System.out.flush();

        for (int i = 1; i < familyTree.size(); i++) {
            Node p = familyTree.getId(i);
            System.out.println(String.format(" (%d) %s", i, p.getName()));
        }
        Node res = null;
        try {
            System.out.println("Zadejte id osoby: ");
            int ch = Integer.parseInt(br.readLine());
            res = familyTree.getId(ch);

            if (res == null)
                dieWithError("(EE) Neexistující osoba");

        } catch (IOException e) {
            dieWithError("(EE) chyba vstupu");
        }
        return res;
    }

    private static int[] promptRel() {
        System.out.print(ESC + "[0;0H");
        System.out.print(ESC + "[2J");
        System.out.flush();

        int[] res = new int[2];
        String[] p;

        String[][] rel = ex.getRelations();
        for (int i = 0; i < rel.length; i++) {
            p = rel[i];
            String line = String.format(" (%s) %s, %s", p[Expert.POS_ID],
                    p[Expert.POS_MALENAME], p[Expert.POS_FEMALENAME]);
            System.out.println(line);
        }

        try {
            boolean found = false;

            do {
                System.out.print("\nZadejte vztah: ");
                String ch = br.readLine();

                int male = 0;

                for (int i = 0; i < rel.length; i++) {
                    p = rel[i];
                    if (ch.equals(p[Expert.POS_FEMALENAME])) {
                        res[0] = i;
                        res[1] = male;
                        break;
                    }
                    male++;
                    if (ch.equals(p[Expert.POS_MALENAME])) {
                        res[0] = i;
                        res[1] = male;
                        break;
                    }
                    male--;
                }
            } while (found == false);

        } catch (IOException e) {
            dieWithError("(EE) chyba vstupu");
        }
        return res;
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
    
    public static void main(String[] args) {
        br = new BufferedReader(new InputStreamReader(System.in));
        familyTree = new NTree("familyData");
        ex = new Expert(familyTree, br);

        do {
            String r = doAnswer();

            System.out.println("\n>> " + r);
        } while (prompt("Další otázku? a/n").charAt(0) == 'a');

        try {
            br.close();
        } catch (IOException e) {
            dieWithError("(EE) chyba při vypínání");
        }
    }
}
