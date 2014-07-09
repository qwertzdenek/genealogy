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

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

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

    public ArrayList<Node> nodes;

    public NTree(File file) throws Exception {
        DataLoader dl = new DataLoader(file);
        int count = dl.getCount();
        nodes = new ArrayList<Node>(count + 1);

        nodes.add(new Node(0, "Super Mother", Node.NONE, Node.NONE, false,
                Node.NONE, this));

        String[] e;
        int id = 1;
        while ((e = dl.next()) != null) {
            id = Integer.parseInt(e[POS_ID]);

            if (id >= count + 1)
                throw new Exception("File not consistent");

            nodes.add(id, new Node(id, e[POS_NAME], e[POS_MOTHER].toLowerCase()
                    .equals("nil") ? Node.NONE
                    : Integer.parseInt(e[POS_MOTHER]), e[POS_FATHER]
                    .toLowerCase().equals("nil") ? Node.NONE
                    : Integer.parseInt(e[POS_FATHER]),
                    Boolean.parseBoolean(e[POS_MALE]), e[POS_PARTNER]
                            .toLowerCase().equals("nil") ? Node.NONE
                            : Integer.parseInt(e[POS_PARTNER]), this));
        }

        Node n;
        for (int j = 1; j < nodes.size(); j++) {
            if (nodes.get(j) == null)
                throw new Exception("File not consistent");

            n = nodes.get(j).getMother();
            if (n != null)
                n.addChild(nodes.get(j));
            else
                nodes.get(0).addChild(nodes.get(j));

            n = nodes.get(j).getFather();
            if (n != null)
                n.addChild((nodes.get(j)));
        }
    }

    public void draw(Graphics g) {
        NTreeDraw.drawTree(g, nodes.get(0).getChilds(), 0, 10);

        g.drawString("Size is " + nodes.size(), 10, 20);
    }
}
