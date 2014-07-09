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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class DataLoader {
    private BufferedReader br;

    private int loaded;
    private int count;

    public DataLoader(File file) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(file));

        try {
            count = Integer.parseInt(br.readLine().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] next() throws IOException {
        if (loaded++ == count) {
            return null;
        }

        String desc = br.readLine();
        Collection<String> values = DataManipulation.getTokens(desc);

        if (values == null)
            return null;

        br.mark(5);

        int ch = br.read();
        if (ch == '[' || ch == -1) {
            br.reset();
        } else {
            br.reset();
            values.add(br.readLine());
        }

        return values.toArray(new String[values.size()]);
    }

    public int getCount() {
        return count;
    }

    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
