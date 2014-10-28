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
import java.util.Arrays;

public class NTreeDraw {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 26;
    private static final int SEP = 20;
    private static final int MOVE = WIDTH + SEP;

    public static void drawTree(Graphics g, ArrayList<Node> nodes) {
        int x = 0, y = 25;

        int[] parentPos = new int[nodes.get(0).getTree().nodes.size()];
        int[] siblingCount = new int[nodes.get(0).getTree().nodes.size()];
        
        ArrayList<Node> newNodes = new ArrayList<Node>();
        ArrayList<Node> swapPointer;

        DataManipulation.sort(nodes);

        int i = 0;
        while (!nodes.isEmpty()) {
            Node person = nodes.get(i);

            x = parentPos[person.getId()] + siblingCount[person.getId()] * MOVE;
            
            if (person.getPartner() == null) { // doesn't have a partner
                
                
                g.drawString(Integer.toString(person.getId()), x + WIDTH / 2, y
                        + HEIGHT / 2 + 5);
                g.drawRect(x, y, WIDTH, HEIGHT);

                if (i < nodes.size() - 1) {
                    g.drawLine(x + WIDTH, y + 13, x + MOVE, y + 13);
                }

//                x += MOVE;
            } else { // node has a partner
                g.drawString(Integer.toString(person.getId()), x + WIDTH / 2,
                        y + HEIGHT / 2 + 5);
                g.drawRect(x, y, WIDTH, HEIGHT);

                g.drawString(Integer.toString(person.getPartner().getId()),
                        x + MOVE + WIDTH / 2, y + HEIGHT / 2 + 5);
                g.drawRect(x + MOVE, y, WIDTH, HEIGHT);

                g.drawLine(x + WIDTH, y + 10, x + MOVE, y + 10);
                g.drawLine(x + WIDTH, y + 16, x + MOVE, y + 16);

                // prepare next level
                ArrayList<Node> childs = person.getChilds(); 
                newNodes.addAll(childs);

                int pos = 0;
                for (Node node : childs) {
                    parentPos[node.getId()] = x;
                    siblingCount[node.getId()] = pos++;
                }

                x += Math.max(2, person.childsCount()) * MOVE;
                i++; // skip partner
            }

            i++;

            // create next level
            if (i == nodes.size()) {
                y += HEIGHT + SEP;
                i = 0;

                nodes.clear();
                
                swapPointer = nodes;
                nodes = newNodes;
                newNodes = swapPointer;

                DataManipulation.sort(nodes);
            }
        }
    }
}
