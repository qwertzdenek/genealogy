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

import kiv.janecekz.DataManipulation;
import kiv.janecekz.NTree;
import kiv.janecekz.Node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LevelSort {
    NTree ntree;
    Node[] tree;

    @Before
    public void setUp() throws Exception {
        ntree = new NTree(new File("familyData"));
        tree = new Node[8];

        tree[0] = new Node(0, "0", Node.NONE, Node.NONE, false, 3, ntree);
        tree[1] = new Node(1, "1", Node.NONE, Node.NONE, false, 6, ntree);
        tree[2] = new Node(2, "2", Node.NONE, Node.NONE, false, 7, ntree);
        tree[3] = new Node(3, "3", Node.NONE, Node.NONE, false, 0, ntree);
        tree[4] = new Node(4, "4", Node.NONE, Node.NONE, false, 5, ntree);
        tree[5] = new Node(5, "5", Node.NONE, Node.NONE, false, 4, ntree);
        tree[6] = new Node(6, "6", Node.NONE, Node.NONE, false, 1, ntree);
        tree[7] = new Node(7, "7", Node.NONE, Node.NONE, false, 2, ntree);

        ntree.nodes = tree; // HACK: temporary fix
    }

    @Test
    public void test() {
        DataManipulation.sort(tree);

        for (int i = 0; i < tree.length; i++) {
            if (tree[i].getPartner() == null)
                continue;
            else {
                Assert.assertEquals(tree[i].getPartner(), tree[i + 1]);
                i++;
            }
        }
    }
}
