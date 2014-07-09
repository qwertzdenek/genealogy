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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class creates window.
 * 
 * @author Zdeněk Janeček
 */
public class Starter extends JPanel implements ActionListener {
    private static final String EXPERT_DIALOG = "Expert data";
    private static final String FAMILY_DIALOG = "Family tree";
    private static final String FILE_DIALOG = "File";
    private static final String HELP_DIALOG = "Help";
    private static final String ABOUT_DIALOG = "About";

    private static NTree familyTree;
    // private static Expert ex;
    private DefaultListModel<String> listModel;
    private Panel2 chart;

    private class Panel2 extends JPanel {
        Panel2() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(420, 420));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (familyTree != null)
                familyTree.draw(g);
            else
                g.drawString("Family tree not loaded", 10, 20);
        }
    }

    private static JMenu getMenu(String name, String[] values, ActionListener l) {
        JMenu myMenu = new JMenu(name);

        JMenuItem item = null;
        for (String val : values) {
            item = new JMenuItem(val);
            item.addActionListener(l);
            myMenu.add(item);
        }
        return myMenu;
    }

    public Starter() {
        chart = new Panel2();
        chart.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(String.format("pressed %dx%d", e.getX(),
                        e.getY()));
            }
        });

        listModel = new DefaultListModel<String>();
        JList<String> relations = new JList<String>(listModel);

        listModel.add(0, "Bratr/sestra");

        // selected entity info
        JLabel info = new JLabel("Ahoj");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(relations);
        panel.add(info);

        setLayout(new BorderLayout());
        add(chart, BorderLayout.LINE_START);
        add(panel, BorderLayout.LINE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        // chooser.setSelectedFile(new File(fileName));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String name = ((JMenuItem) e.getSource()).getText();
        int code;

        try {
            switch (name) {
            case EXPERT_DIALOG:
                code = chooser.showOpenDialog(getRootPane());
                if (code == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();

                    // ex = new Expert(selectedFile);

                    // updateModel(ex.getRelations());
                }
                break;
            case FAMILY_DIALOG:
                code = chooser.showOpenDialog(getRootPane());
                if (code == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();

                    familyTree = new NTree(selectedFile);
                    chart.repaint();
                }
                break;
            default:
                break;
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

    }

    private void updateModel(String[][] relations) {
        listModel.clear();
        for (String[] strings : relations) {
            // listModel.addElement(strings[Expert.POS_DESC]);
        }
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Genealogy expert system");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        JComponent newContentPane = new Starter();
        newContentPane.setOpaque(true); // content panes must be opaque

        // Main menu
        JMenuBar top = new JMenuBar();
        JMenu file = getMenu(FILE_DIALOG, new String[] { EXPERT_DIALOG,
                FAMILY_DIALOG }, (Starter) newContentPane);
        JMenu help = getMenu(HELP_DIALOG, new String[] { ABOUT_DIALOG },
                (Starter) newContentPane);
        top.add(file);
        top.add(help);

        frame.setContentPane(newContentPane);
        frame.setJMenuBar(top);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        ;
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
