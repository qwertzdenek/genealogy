/*
 * Genealogy expert system.
 * 
 * Written by Zdeněk Janeček, 2014
 * Share it freely under conditions of GNU GPL v3
 * 
 * version 2.0
 * last change in June 2014
 */

package kiv.janecekz.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import kiv.janecekz.DataManipulation;
import kiv.janecekz.NTree;
import kiv.janecekz.Node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LevelSort {
    NTree ntree;
    ArrayList<Node> tree;

    @Before
    public void setUp() throws Exception {
        ntree = new NTree(new File("familyData"));
        tree = new ArrayList<Node>();

        tree.add(new Node(0, "0", Node.NONE, Node.NONE, false, 3, ntree));
        tree.add(new Node(1, "1", Node.NONE, Node.NONE, false, 6, ntree));
        tree.add(new Node(2, "2", Node.NONE, Node.NONE, false, 7, ntree));
        tree.add(new Node(3, "3", Node.NONE, Node.NONE, false, 0, ntree));
        tree.add(new Node(4, "4", Node.NONE, Node.NONE, false, 5, ntree));
        tree.add(new Node(5, "5", Node.NONE, Node.NONE, false, 4, ntree));
        tree.add(new Node(6, "6", Node.NONE, Node.NONE, false, 1, ntree));
        tree.add(new Node(7, "7", Node.NONE, Node.NONE, false, 2, ntree));

        ntree.nodes = new ArrayList<Node>(tree);
    }

    @Test
    public void test() {
        DataManipulation.sort(tree);

        for (int i = 0; i < tree.size(); i++) {
            if (tree.get(i).getPartner() == null) {
                continue;
            } else {
                Assert.assertEquals(tree.get(i).getPartner(), tree.get(i + 1));
                i++;
            }
        }
    }
}
