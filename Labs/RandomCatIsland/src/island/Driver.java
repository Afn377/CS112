package island;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Flow;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import island.std.StdOut;

public class Driver {

    private JFrame display;

    private RandomCatIsland isle;

    private JButton infoButton;

    private JButton runButton;
    private JPanel controls;
    private JTextField netIDinput;

    private JPanel questionPanel;

    public Driver() {
        display = new JFrame("Random Cat Island");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(new BorderLayout());

        controls = createControls();
        display.add(controls, BorderLayout.NORTH);

        display.setResizable(false);
        display.pack();
        display.setVisible(true);

        refresh();
    }

    private JPanel createControls() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(Color.lightGray);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.lightGray);
        infoButton = new JButton();
        infoButton();
        infoPanel.add(infoButton);

        JPanel controls = new JPanel();
        controls.setBackground(Color.lightGray);
        JLabel netIDLabel = new JLabel("Enter NetID:");
        netIDinput = new JTextField();
        netIDinput.setPreferredSize(new Dimension(75, 19));
        runButton = new JButton("Run Island");

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (netIDinput.getText() != null && RandomCatIsland.validNetID(netIDinput.getText()) == null) {
                    runIsland();
                } else if (RandomCatIsland.validNetID(netIDinput.getText()) != null) {
                    JOptionPane.showMessageDialog(null,
                            RandomCatIsland.validNetID(netIDinput.getText()),
                            "NetID Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        controls.add(netIDLabel);
        controls.add(netIDinput);
        controls.add(runButton);

        controlPanel.add(infoPanel);
        controlPanel.add(controls);
        return controlPanel;
    }

    private void infoButton() {
        infoButton.setText("Read Me Before Running!");
        for (int i = infoButton.getActionListeners().length - 1; i >= 0; i--) {
            infoButton.removeActionListener(infoButton.getActionListeners()[i]);
        }
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                StringBuilder sb = new StringBuilder();
                sb.append("RandomCatIsland is a modified cat island, which is randomly generated\n")
                        .append(" based on your netID.").append("\n").append("\n");
                sb.append("When it is run, tiles will be generated and cats will spawn, who will then\n")
                        .append(" randomly move around for 250-300 turns.").append("\n").append("\n");
                sb.append("Yarn will also be placed around the board at random intervals, for the\n")
                        .append("cats to collect.").append("\n").append("\n");
                sb.append("After the island finishes running, you will be presented questions about\n")
                        .append(" the simulation, which you should use the debugger to solve.").append("\n")
                        .append("\n");
                sb.append("Then you can enter your answers, click the print button, and submit the\n")
                        .append(" generated \"answers.out\" file to autolab.").append("\n").append("\n");

                JOptionPane.showMessageDialog(null, sb.toString(), "Welcome to RandomCatIsland!",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    private void debugButton() {
        infoButton.setText("Need help starting?");
        for (int i = infoButton.getActionListeners().length - 1; i >= 0; i--) {
            infoButton.removeActionListener(infoButton.getActionListeners()[i]);
        }
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "To find the answers to your given questions, you will need ").append("\n")
                        .append("to use the VScode debugger to investigate the code as it runs.").append("\n")
                        .append("You only need to debug the nextTurn() method in RandomCatIsland.").append("\n\n");
                sb.append("You should place breakpoints and conditional breakpoints, then ").append("\n")
                        .append("click \"Rerun Island\" to run again. This will pause running the ").append("\n")
                        .append("code at those breakpoints. You can then use the Debug Console to ").append("\n")
                        .append("view the values of relevant variables.").append("\n\n");

                sb.append(
                        "Try to determine key parts of each question (i.e. when, who, what).").append("\n")
                        .append("For example, to find information about a cat at a certain time, ").append("\n")
                        .append("you will want to pause the program at that time using a conditional").append("\n")
                        .append("breakpoint, then use the debug console to view variable values.").append("\n\n");

                sb.append("View the assignment description for more info on debugging.");

                JOptionPane.showMessageDialog(null, sb.toString(), "Welcome to RandomCatIsland!",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    private void runIsland() {
        this.netIDinput.setEditable(false);
        isle = new RandomCatIsland(netIDinput.getText());
        while (isle.nextTurn()) {
            continue;
        }
        createQuestionPanel(isle.generateQuestions());
    }

    private void createQuestionPanel(String[] questions) {
        if (questionPanel != null) {
            return;
        }
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(Color.lightGray);
        ArrayList<JTextField> answerInputs = new ArrayList<>();
        for (String s : questions) {
            JPanel question = new JPanel();
            question.setLayout(new FlowLayout(FlowLayout.RIGHT));
            question.setBackground(Color.lightGray);
            question.add(new JLabel(s));
            JTextField input = new JTextField();
            input.setPreferredSize(new Dimension(50, 20));
            input.setMaximumSize(new Dimension(50, 20));
            answerInputs.add(input);
            question.add(input);
            questionPanel.add(question);
        }

        this.runButton.setText("Rerun Island");
        debugButton();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton printAnswers = new JButton("Print Answers.out");
        printAnswers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                StdOut.setFile("answers.out");
                String netID = netIDinput.getText();
                StdOut.println(netID);
                StdOut.println(RandomCatIsland.hash(netID));
                for (JTextField text : answerInputs) {
                    StdOut.println(text.getText());
                }
            }
        });

        JButton restartButton = new JButton("Quit and Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                runButton.setText("Run Island");
                netIDinput.setEditable(true);
                infoButton();

                display.remove(questionPanel);
                questionPanel = null;
                display.pack();
            }
        });

        buttonPanel.add(printAnswers);
        buttonPanel.add(restartButton);

        questionPanel.add(buttonPanel);

        display.add(questionPanel, BorderLayout.SOUTH);
        display.pack();
    }

    private void refresh() {
        display.revalidate();
        display.repaint();
    }

    private void clearActionListeners(JButton button) {
        if (button.getActionListeners().length > 0) {
            for (int i = button.getActionListeners().length - 1; i >= 0; i--) {
                button.removeActionListener(button.getActionListeners()[i]);
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Driver();
            }
        });
    }
}