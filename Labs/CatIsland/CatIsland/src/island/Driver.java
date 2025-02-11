package island;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import island.constants.CatInWaterException;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Random;

public class Driver {

    private CatDisplay catDisplay;
    private JFrame display;

    public JPanel controls;
    public JPanel bottomBar;

    private JPanel buttonPanel;
    private JButton runButton;
    private JSlider speedSlider;
    private JTextArea infoText;

    public Driver() {
        display = new JFrame("Cat Island");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBackground(Color.lightGray);

        controls = makeControls();
        mainPanel.add(controls);

        JPanel cats = new JPanel();
        cats.setBackground(Color.lightGray);
        catDisplay = new CatDisplay();
        cats.add(catDisplay);
        mainPanel.add(cats);

        this.bottomBar = new JPanel();
        this.bottomBar.setBackground(Color.lightGray);
        this.bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.Y_AXIS));
        this.bottomBar.add(Box.createHorizontalStrut(10));
        this.bottomBar.add(Box.createHorizontalGlue());

        this.runButton = new JButton("Run Island");

        this.speedSlider = new JSlider(1, 10);
        this.speedSlider.setSnapToTicks(true);
        this.speedSlider.setMajorTickSpacing(1);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>() {
            {
                put(1, new JLabel("Slow"));
                put(10, new JLabel("Fast"));
            }
        };
        this.speedSlider.setLabelTable(ht);
        this.speedSlider.setBackground(Color.lightGray);

        this.infoText = new JTextArea();
        this.infoText.setBackground(Color.white);
        this.infoText.setPreferredSize(new Dimension(450, 80));
        this.infoText.setLineWrap(true);
        this.infoText.setWrapStyleWord(true);
        this.infoText.setEditable(false);

        this.buttonPanel = new JPanel();
        this.buttonPanel.setBackground(Color.lightGray);
        this.buttonPanel.setLayout(new GridBagLayout());
        this.buttonPanel.add(speedSlider);
        this.buttonPanel.add(this.runButton);

        bottomBar.add(infoText);
        this.bottomBar.add(buttonPanel);

        this.bottomBar.setVisible(false);
        mainPanel.add(bottomBar);

        display.add(mainPanel);
        display.setResizable(false);
        display.setMinimumSize(new Dimension(550, 1));
        display.setMaximumSize(new Dimension(550, 700));
        display.pack();
        display.setVisible(true);

        refresh();
    }

    private void refresh() {
        display.revalidate();
        display.repaint();
    }

    public JPanel makeControls() {
        JPanel controls = new JPanel();
        controls.setBackground(Color.lightGray);
        controls.setPreferredSize(new Dimension(570, 80));

        JButton intro = new JButton("Intro");
        intro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                introIsland();
            }
        });
        controls.add(intro);
        JButton i1 = new JButton("Island 1");
        controls.add(i1);
        i1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                island1();
            }
        });
        JButton i2 = new JButton("Island 2");
        controls.add(i2);
        i2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                island2();
            }
        });
        JButton i3 = new JButton("Island 3");
        controls.add(i3);
        i3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                island3();
            }
        });
        JButton i4 = new JButton("Island 4");
        controls.add(i4);
        i4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                island4();
            }
        });
        JButton i5 = new JButton("Island 5");
        controls.add(i5);
        i5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                island5();
            }
        });

        return controls;
    }

    private void introIsland() {
        this.bottomBar.setVisible(true);
        this.runButton.setVisible(false);
        this.speedSlider.setVisible(false);
        this.controls.setVisible(false);
        this.infoText.setVisible(true);
        infoText.setPreferredSize(new Dimension(450, 80));
        SwingUtilities.invokeLater(() -> {
            this.infoText.setText("Welcome to Cat Island! A mini-world made using OOP.");
            display.pack();
        });

        Island s = new Island(new String[][] {
                { "L", "L", "L", "L" },
                { "L", "L", "L", "L" },
                { "L", "L", "L", "L" },
                { "L", "L", "L", "L" }
        });
        catDisplay.displayIsland = s;

        final int[] index = { 0 }; // use array for final constraint of action listener
        final String[] text = new String[] {
                "Here, we represent everything as objects. Including Cats, Islands, and each Tile on an island. Above is a 4x4 square of empty Grass tiles. We use (row,col) as indices and (0,0) is the tile in the top left.",
                "Here's what the same island would look like surrounded by tiles of water. (0,0) is now a water Tile. ",
                "To create a new cat and add it to the island, we call the Cat class constructor method, with the keyword 'new'. This constructor contains any code needed to 'setup' the object. This constructor can take parameters (next page).",
                "The Cat constructor takes the following parameters: name, island, row, col, and color. The constructor code initializes the cats attributes, and then places itself on the given island at the given coordinates.",
                "Every object (including Cat) can contain methods, which contain code that are related to the class type. For example, to move our cat left we call its holmes.moveLeft() method. ",
                "",
                "",
                "",
                "Information about a cat (like the cats name, island, color, row, and col) are stored as Class attributes (also known as fields). Every time we instantiate a 'new' cat, it gets its own set of attribute variables.",
                "Classes can also have static methods/attributes, which are shared by all instances of the class. They are accessed through the class type itself, instead of any object of that class type. i.e. Cat.species or Cat.getSpecies(), Cat is the type, not a new cat object.",
                "Each Island has a 2D array of Tile objects which makes up the island. Each Tile object contains a Cat attribute (holding the cat which is on it, or null if none is), as well as a boolean hasYarn attribute, which is true if the tile has yarn on it. If a cat steps on a yarn, the cat will collect it.",
                "",
                "",
                "If your cat falls in the water, the program throws an error, known in Java as a Runtime Exception. The specific exception name for this is CatInWaterException.",
                "",
                "",
                "",
                "To solve this lab, you will need to control a cat on a given island, to complete certain objectives. Good luck!",
        };

        JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index[0] < text.length) {
                    SwingUtilities.invokeLater(() -> {
                        infoText.setText(text[index[0]]);

                        if (index[0] == 1) {
                            catDisplay.displayIsland = (new Island(new String[][] {
                                    { "W", "W", "W", "W", "W", "W" },
                                    { "W", "L", "L", "L", "L", "W" },
                                    { "W", "L", "L", "L", "L", "W" },
                                    { "W", "L", "L", "L", "L", "W" },
                                    { "W", "L", "L", "L", "L", "W" },
                                    { "W", "W", "W", "W", "W", "W" }
                            }));
                            catDisplay.revalidate();
                            catDisplay.repaint();
                        } else if (index[0] == 3) {
                            next.setText("Cat holmes = new Cat(\"Sherlock\", island, 2, 3, Color.GREY);");
                        } else if (index[0] == 4) {
                            infoText.setText("");
                            new Cat("Sherlock", catDisplay.displayIsland, 2, 3, island.constants.Color.GREY);
                            next.setText("holmes.moveLeft();");
                        } else if (index[0] == 5) {
                            try {
                                catDisplay.displayIsland.getTiles()[2][3].cat.moveLeft();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("holmes.moveDown();");
                        } else if (index[0] == 6) {
                            try {
                                catDisplay.displayIsland.getTiles()[2][2].cat.moveDown();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("holmes.moveRight();");
                        } else if (index[0] == 7) {
                            try {
                                catDisplay.displayIsland.getTiles()[3][2].cat.moveRight();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("holmes.moveUp();");
                        } else if (index[0] == 8) {
                            try {
                                catDisplay.displayIsland.getTiles()[3][3].cat.moveUp();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("Next");
                        } else if (index[0] == 9) {
                            next.setText("Next");
                        } else if (index[0] == 10) {
                            next.setText("tiles[2][2].hasYarn = true;");
                        } else if (index[0] == 11) {
                            catDisplay.displayIsland.getTiles()[2][2].hasYarn = true;
                            infoText.setText("");
                            next.setText("holmes.moveLeft();");
                        } else if (index[0] == 12) {
                            try {
                                catDisplay.displayIsland.getTiles()[2][3].cat.moveLeft();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("holmes.moveDown();");
                        } else if (index[0] == 13) {
                            try {
                                catDisplay.displayIsland.getTiles()[2][2].cat.moveDown();
                            } catch (CatInWaterException we) {

                            }
                            next.setText("Next");
                        } else if (index[0] == 14) {
                            infoText.setText("");
                            next.setText("holmes.moveRight();");
                        } else if (index[0] > 14 && index[0] <= 17) {
                            try {
                                catDisplay.displayIsland.getTiles()[3][index[0] - 13].cat.moveRight();
                            } catch (CatInWaterException we) {
                                JOptionPane.showMessageDialog(null, "Your cat fell in the water!",
                                        "CatInWaterException", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }

                        index[0]++;
                        if (index[0] == text.length) {
                            infoText.setPreferredSize(new Dimension(430, 80));
                            next.setText("End Intro");
                        }
                    });
                    display.pack();
                    display.revalidate();
                    display.repaint();
                } else {
                    SwingUtilities.invokeLater(() -> {
                        bottomBar.remove(next);
                        bottomBar.setVisible(false);
                        runButton.setVisible(true);
                        speedSlider.setVisible(true);
                        buttonPanel.remove(next);
                        infoText.setText("");
                        controls.setVisible(true);
                    });
                    return;
                }
            }
        });
        this.buttonPanel.add(next);

    }

    public void visibleControls() {
        controls.setVisible(true);
    }

    private void clearActionListeners(JButton button) {
        if (button.getActionListeners().length > 0) {
            for (int i = button.getActionListeners().length - 1; i >= 0; i--) {
                button.removeActionListener(button.getActionListeners()[i]);
            }
        }
    }

    private void island1() {
        this.bottomBar.setVisible(true);
        this.infoText.setVisible(false);
        this.runButton.setVisible(true);
        this.speedSlider.setVisible(true);
        this.controls.setVisible(true);
        Island s = new Island(new String[][] {
                { "W", "W", "W", "W" },
                { "W", "L", "Y", "W" },
                { "W", "Y", "L", "W" },
                { "W", "W", "W", "W" }
        });
        Cat tutorCat = new Cat("TutorCat", s, 1, 1, randColor());
        catDisplay.displayIsland = (s);
        refresh();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    speedSlider.setVisible(false);
                    runButton.setVisible(false);
                    System.out.println(speedSlider.getValue());
                    catDisplay.runIsland(s, tutorCat,
                            CatIsland.class.getDeclaredMethod("solveIsland1", new Class[] { Cat.class }),
                            new Object[] { tutorCat }, speedSlider.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void island2() {
        this.bottomBar.setVisible(true);
        controls.setVisible(true);
        this.infoText.setVisible(false);
        this.runButton.setVisible(true);
        this.speedSlider.setVisible(true);
        Island s = new Island(new String[][] {
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "L", "L", "L", "L", "L", "W" },
                { "W", "L", "L", "L", "L", "L", "W" },
                { "W", "L", "L", "L", "L", "L", "W" },
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "W", "W", "W", "W", "W", "W" }
        });
        Cat tutorCat = new Cat("TutorCat", s, 3, 2, randColor());
        catDisplay.displayIsland = (s);
        refresh();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(s, tutorCat,
                            CatIsland.class.getDeclaredMethod("solveIsland2", new Class[] { Cat.class }),
                            new Object[] { tutorCat }, speedSlider.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void island3() {
        this.bottomBar.setVisible(true);
        controls.setVisible(true);
        this.infoText.setVisible(false);
        this.runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island s = new Island(new String[][] {
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "L", "Y", "W", "L", "Y", "W" },
                { "W", "L", "L", "L", "L", "L", "W" },
                { "W", "Y", "L", "W", "L", "Y", "W" },
                { "W", "W", "W", "W", "W", "W", "W" },
                { "W", "W", "W", "W", "W", "W", "W" }
        });
        Cat tutorCat = new Cat("TutorCat", s, 3, 3, randColor());
        catDisplay.displayIsland = (s);
        refresh();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(s, tutorCat,
                            CatIsland.class.getDeclaredMethod("solveIsland3", new Class[] { Cat.class }),
                            new Object[] { tutorCat }, speedSlider.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void island4() {
        this.bottomBar.setVisible(true);
        controls.setVisible(true);
        this.infoText.setVisible(false);
        this.runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island s = new Island(new String[][] {
                { "W", "W", "W", "W", "W", },
                { "L", "L", "W", "L", "L", },
                { "L", "L", "W", "L", "L", },
                { "L", "L", "W", "L", "L", },
                { "W", "W", "W", "W", "W", },
        });

        Cat tutorCat = new Cat("TutorCat", s, 2, 1, randColor());
        Cat lonelyCat = new Cat("LonelyCat", s, 2, 3, randColor());
        catDisplay.displayIsland = (s);
        refresh();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(s, tutorCat,
                            CatIsland.class.getDeclaredMethod("solveIsland4", new Class[] { Island.class }),
                            new Object[] { s }, speedSlider.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void island5() {
        this.bottomBar.setVisible(true);
        controls.setVisible(true);
        this.infoText.setVisible(false);
        this.runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island s = new Island(randomPath());
        Cat tutorCat = new Cat("TutorCat", s, 0, 0, randColor());
        catDisplay.displayIsland = s;
        refresh();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(s, tutorCat,
                            CatIsland.class.getDeclaredMethod("solveIsland5", new Class[] { Cat.class, Island.class }),
                            new Object[] { tutorCat, s }, speedSlider.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private String[][] randomPath() {
        String[][][] paths = new String[][][] { new String[][] {
                { "L", "L", "W", "L", "L", "L", "L", "W", "L" },
                { "W", "L", "W", "L", "W", "W", "L", "W", "L" },
                { "L", "L", "W", "L", "W", "L", "L", "W", "L" },
                { "L", "W", "L", "L", "W", "L", "W", "L", "L" },
                { "L", "W", "L", "W", "W", "L", "W", "L", "W" },
                { "L", "W", "L", "L", "W", "L", "W", "L", "L" },
                { "L", "L", "W", "L", "W", "L", "W", "W", "L" },
                { "W", "L", "W", "L", "W", "L", "W", "L", "L" },
                { "W", "L", "L", "L", "W", "L", "L", "L", "W" }
        }, new String[][] {
                { "L", "L", "L", "L", "L", "L", "W", "L", "L" },
                { "W", "W", "W", "W", "W", "L", "W", "L", "W" },
                { "W", "L", "L", "L", "L", "L", "W", "L", "L" },
                { "L", "L", "W", "W", "W", "W", "W", "W", "L" },
                { "L", "W", "W", "L", "L", "L", "L", "W", "L" },
                { "L", "W", "W", "L", "W", "W", "L", "W", "L" },
                { "L", "L", "W", "L", "L", "W", "L", "W", "L" },
                { "W", "L", "L", "W", "L", "W", "L", "W", "L" },
                { "W", "W", "L", "L", "L", "W", "L", "L", "L" }
        }, new String[][] {
                { "L", "L", "W", "W", "L", "L", "L", "W", "L" },
                { "W", "L", "W", "L", "L", "W", "L", "W", "L" },
                { "L", "L", "W", "L", "W", "W", "L", "L", "L" },
                { "L", "W", "W", "L", "W", "W", "W", "W", "W" },
                { "L", "L", "W", "L", "L", "L", "L", "W", "W" },
                { "W", "L", "W", "W", "W", "W", "L", "L", "W" },
                { "L", "L", "W", "L", "L", "L", "W", "L", "L" },
                { "L", "W", "W", "L", "W", "L", "W", "W", "L" },
                { "L", "L", "L", "L", "W", "L", "L", "L", "L" }
        }, new String[][] {
                { "L", "L", "L", "L", "L", "L", "L", "W", "L" },
                { "W", "W", "W", "W", "W", "W", "L", "W", "L" },
                { "L", "L", "L", "L", "L", "W", "L", "W", "L" },
                { "L", "W", "W", "W", "L", "W", "L", "W", "L" },
                { "L", "L", "W", "L", "L", "W", "L", "W", "L" },
                { "W", "L", "W", "L", "W", "W", "L", "W", "L" },
                { "L", "L", "W", "L", "L", "L", "L", "W", "L" },
                { "L", "W", "W", "W", "W", "W", "W", "W", "L" },
                { "L", "L", "L", "L", "L", "L", "L", "L", "L" }
        }, new String[][] {
                { "L", "L", "L", "L", "W", "W", "W", "L", "L" },
                { "W", "W", "W", "L", "L", "L", "W", "L", "W" },
                { "L", "L", "L", "W", "W", "L", "W", "L", "L" },
                { "L", "W", "L", "L", "W", "L", "L", "W", "L" },
                { "L", "L", "W", "L", "W", "W", "L", "W", "L" },
                { "W", "L", "W", "L", "L", "W", "L", "W", "L" },
                { "L", "L", "W", "W", "L", "L", "L", "W", "L" },
                { "L", "W", "W", "W", "W", "W", "W", "W", "L" },
                { "L", "L", "L", "L", "L", "L", "L", "L", "L" }
        } };
        return paths[(new Random().nextInt(paths.length))];
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Driver();
            }
        });
    }

    private island.constants.Color randColor() {
        return (new island.constants.Color[] { island.constants.Color.ORANGE,
                island.constants.Color.GREY, island.constants.Color.WHITE,
                island.constants.Color.BROWN,
                island.constants.Color.BLACK })[new Random().nextInt(5)];
    }

    private static class CatDisplay extends JPanel {

        public BufferedImage grassImage;
        public BufferedImage waterImage;
        public BufferedImage yarnImage;

        private Island displayIsland;
        private Timer timer;

        private CatDisplay() {
            super();

            try {
                grassImage = ImageIO.read(new File("res/grass.png"));
            } catch (Exception e) {
                System.out.println(
                        "Grass Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
            }
            try {
                waterImage = ImageIO.read(new File("res/water.png"));
            } catch (Exception e) {
                System.out.println(
                        "Water Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
            }
            try {
                yarnImage = ImageIO.read(new File("res/yarn.png"));
            } catch (Exception e) {
                System.out.println(
                        "Yarn Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
            }

            this.setBackground(Color.lightGray);
            this.setPreferredSize(new Dimension(500, 500));
        }

        public void stopTimer() {
            if (timer != null) {
                timer.stop();
            }
        }

        public void runIsland(Island s, Cat c, Method m, Object[] args, int speed) {
            this.displayIsland = Island.copyIsland(s);
            stopTimer();

            Cat copy = this.displayIsland.getTiles()[c.getRow()][c.getCol()].cat;

            try {
                m.invoke(CatIsland.class, args);
            } catch (Exception we) {
                we.printStackTrace();
                System.out.println(
                        "Exception found when running island animation. Make sure you have not modified any provided classes or methods, review the exception's stack trace (the 'Caused by: ' might be more helpful), and use the debugger to investigate further.");
            }
            animateCatWalk(c, copy, s, speed);
        }

        private void animateCatWalk(Cat moved, Cat toMove, Island s, int speed) {
            if (moved.getCoord().getLocationHistory().isEmpty()) {
                return;
            }
            int[] current = moved.getCoord().getLocationHistory().get(0);
            final int[] i = { 1 };
            int delay = 50 * (11 - speed); // speed the cat will walk
            CatDisplay display = this;
            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (i[0] >= moved.getCoord().getLocationHistory().size()) {
                        ((Timer) e.getSource()).stop();
                        display.displayIsland = s;
                        revalidate();
                        repaint();
                        return;
                    }
                    int[] next = moved.getCoord().getLocationHistory().get(i[0]++);
                    try {
                        if (current[0] == next[0] && current[1] < next[1]) {
                            toMove.moveRight();
                        } else if (current[0] == next[0] && current[1] > next[1]) {
                            toMove.moveLeft();
                        } else if (current[1] == next[1] && current[0] < next[0]) {
                            toMove.moveDown();
                        } else if (current[1] == next[1] && current[0] > next[0]) {
                            toMove.moveUp();
                        }
                    } catch (CatInWaterException we) {
                        revalidate();
                        repaint();
                        JOptionPane.showMessageDialog(null,
                                "Your cat fell in the water! Check your .move...() methods calls and reevaluate your logic for moving the cat.",
                                "CatInWaterException", JOptionPane.INFORMATION_MESSAGE);
                        ((Timer) e.getSource()).stop();
                        return;
                    }
                    revalidate();
                    repaint();
                    current[0] = next[0];
                    current[1] = next[1];
                }
            });
            timer.setRepeats(true);
            timer.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (displayIsland == null) {
                return;
            }
            Tile[][] tiles = displayIsland.getTiles();
            int rows = tiles.length;
            int cols = tiles[0].length;

            int height = this.getSize().height / rows;
            int width = this.getSize().width / cols;

            int x = 0;
            int y = 0;
            g.setColor(Color.BLACK);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (tiles[i][j].type.equals(Tile.LAND)) {
                        g.drawImage(grassImage, x, y, width, height, getFocusCycleRootAncestor());
                        if (tiles[i][j].cat != null) {
                            if (tiles[i][j].cat.getColor() == island.constants.Color.ORANGE) {
                                try {
                                    g.drawImage(ImageIO.read(new File("res/orangecat.png")), x, y, width, height,
                                            getFocusCycleRootAncestor());
                                } catch (Exception e) {
                                    System.out.println(
                                            "Orange Cat Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
                                }
                            } else if (tiles[i][j].cat.getColor() == island.constants.Color.WHITE) {
                                try {
                                    g.drawImage(ImageIO.read(new File("res/whitecat.png")), x, y, width, height,
                                            getFocusCycleRootAncestor());
                                } catch (Exception e) {
                                    System.out.println(
                                            "White Cat Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
                                }

                            } else if (tiles[i][j].cat.getColor() == island.constants.Color.BROWN) {
                                try {
                                    g.drawImage(ImageIO.read(new File("res/browncat.png")), x, y, width, height,
                                            getFocusCycleRootAncestor());
                                } catch (Exception e) {
                                    System.out.println(
                                            "Pink Cat Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
                                }

                            } else if (tiles[i][j].cat.getColor() == island.constants.Color.BLACK) {
                                try {
                                    g.drawImage(ImageIO.read(new File("res/blackcat.png")), x, y, width, height,
                                            getFocusCycleRootAncestor());
                                } catch (Exception e) {
                                    System.out.println(
                                            "Red Cat Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
                                }
                            } else {
                                // Cat is grey if color is null or not matched
                                try {
                                    g.drawImage(ImageIO.read(new File("res/greycat.png")), x, y, width, height,
                                            getFocusCycleRootAncestor());
                                } catch (Exception e) {
                                    System.out.println(
                                            "Grey Cat Image File not found! Make sure you've opened the correct 'CatIsland' directory in VSCode.");
                                }
                            }

                        } else if (tiles[i][j].hasYarn) {
                            g.drawImage(yarnImage, x, y, width, height, getFocusCycleRootAncestor());
                        }
                    } else {
                        g.drawImage(waterImage, x, y, width, height, getFocusCycleRootAncestor());
                    }
                    g.drawRect(x, y, width, height);
                    x += width;
                }
                y += height;
                x = 0;
            }

        }
    }

}
