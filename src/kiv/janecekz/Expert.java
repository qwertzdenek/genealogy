package kiv.janecekz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Expert {
    public static final int POS_ID = 0;
    public static final int POS_DESC = 1;
    public static final int POS_MALENAME = 2;
    public static final int POS_FEMALENAME = 3;

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

    public Expert(String file) {
        boolean res = processInput(file);

        if (!res)
            System.out.println("Došlo k chybě při čtení souboru " + file);
    }
    
    public String[] getRelationInfo(String r) {
        for (Relation rel : relations) {
            if (rel.rule.equals(r)) {
                String[] result = new String[4];
                result[POS_ID] = Integer.toString(rel.id);
                result[POS_DESC] = rel.desc;
                result[POS_MALENAME] = rel.maleName;
                result[POS_FEMALENAME] = rel.femaleName;
                
                return result;
            }
        }
        
        return null;
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
                onerel.rule = curLine.trim().toUpperCase();
                
                relations[i] = onerel;
            }

            input.close();
        } catch (IOException e) {
            System.out.println("Chyba při čtení!");
        }

        return true;
    }
}
