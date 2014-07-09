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

import kiv.janecekz.NTree;
import kiv.janecekz.Node;

import org.junit.Assert;
import org.junit.Test;

public class NodeManipulation {
    Node node;

    @Test
    public void testCreation() throws Exception {
        NTree ntree = new NTree(new File("familyData"));
        node = new Node(0, "0", Node.NONE, Node.NONE, false, Node.NONE, ntree);
        Assert.assertEquals(0, node.getId());
        Assert.assertEquals("0", node.getName());
        Assert.assertEquals(null, node.getMother());
        Assert.assertEquals(null, node.getFather());
        Assert.assertEquals(null, node.getPartner());
    }

    @Test
    public void testChild() throws Exception {
        NTree ntree = new NTree(new File("familyData"));
        node = new Node(0, "0", Node.NONE, Node.NONE, false, 1, ntree);
        // Node n1 = new Node(1, "1", Node.NONE, Node.NONE, true, 0, ntree);
        Node n2 = new Node(2, "2", Node.NONE, Node.NONE, true, Node.NONE, ntree);
        Node n3 = new Node(3, "3", Node.NONE, Node.NONE, true, Node.NONE, ntree);
        Node n4 = new Node(4, "4", Node.NONE, Node.NONE, true, Node.NONE, ntree);

        Node[] test = new Node[] { n2, n3, n4 };

        for (int i = 0; i < test.length; i++) {
            node.addChild(test[i]);
        }

        ArrayList<Node> ch = node.getChilds();

        for (int i = 0; i < test.length; i++) {
            Assert.assertEquals(test[i], ch.get(i));
        }
    }
}
