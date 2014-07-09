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
import java.util.ArrayList;
import java.util.Collection;

public class NTreeDraw {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 25;
    private static final int SEP = 20;

    public static int drawTree(Graphics g, Collection<Node> tree, int level,
            int startx) {
        if (tree.isEmpty())
            return 0;

        ArrayList<Node> nodes = (ArrayList<Node>) tree;

        DataManipulation.sort(nodes);

        int x = startx;
        int y = level * (HEIGHT + SEP) + SEP;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getPartner() == null) {
                g.drawString(Integer.toString(nodes.get(i).getId()), x + WIDTH
                        / 2, y + HEIGHT / 2 + 5);
                g.drawRect(x, y, WIDTH, HEIGHT);

                x += WIDTH + SEP;
            } else {
                // draw subnodes first
                int ch = NTreeDraw.drawTree(g, nodes.get(i).getChilds(),
                        level + 1, startx);

                g.drawString(Integer.toString(nodes.get(i).getId()), x + WIDTH
                        / 2, y + HEIGHT / 2 + 5);
                g.drawRect(x, y, WIDTH, HEIGHT);

                g.drawString(Integer.toString(nodes.get(i + 1).getId()), x
                        + WIDTH / 2 + WIDTH + SEP, y + HEIGHT / 2 + 5);
                g.drawRect(x + WIDTH + SEP, y, WIDTH, HEIGHT);

                x += Math.max(2, ch) * (WIDTH + SEP);

                i++; // skip partner
            }
        }

        return nodes.size();
    }
}
