package island;

import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import island.Cat;
import island.Island;
import island.SmartCat;
import island.Tile;
import island.constants.*;
import test.SmartCatTest;

import java.awt.Graphics;
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
    private JButton runButton;
    private JSlider speedSlider;

    public Driver() {
        display = new JFrame("Smart Cat Island");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        mainPanel.setBackground(Color.lightGray);

        controls = makeControls();
        mainPanel.add(controls);

        JPanel cats = new JPanel();
        cats.setBackground(Color.lightGray);
        catDisplay = new CatDisplay();
        cats.add(catDisplay);
        mainPanel.add(cats);

        JPanel bottomBar = new JPanel();
        bottomBar.setBackground(Color.lightGray);
        runButton = new JButton("Run Island");

        this.speedSlider = new JSlider(1, 10);
        this.speedSlider.setSnapToTicks(true);
        this.speedSlider.setMajorTickSpacing(1);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> ht = new Hashtable<>() {
            {
                put(1, new JLabel("Slow"));
                put(10, new JLabel("Fast"));
            }
        };
        this.speedSlider.setLabelTable(ht);
        this.speedSlider.setBackground(Color.lightGray);

        runButton.setVisible(false);
        speedSlider.setVisible(false);
        bottomBar.add(speedSlider);
        bottomBar.add(runButton);
        mainPanel.add(bottomBar);

        // SET SIZES
        controls.setPreferredSize(new Dimension(520, 40));
        cats.setPreferredSize(new Dimension(520, 520));
        catDisplay.setPreferredSize(new Dimension(500, 500));
        display.setPreferredSize(new Dimension(550, 615));

        display.add(mainPanel);
        display.setResizable(false);
        display.pack();
        display.setVisible(true);

        refresh();
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

    public JPanel makeControls() {
        JPanel controls = new JPanel();
        controls.setBackground(Color.lightGray);

        JButton walkPathButton1 = new JButton("pathIsland");
        walkPathButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                walkPath1();
            }
        });
        controls.add(walkPathButton1);

        JButton collectYarnButton1 = new JButton("yarnIsland");
        controls.add(collectYarnButton1);
        collectYarnButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                collectYarn1();
            }
        });

        JButton solveMazeButton1 = new JButton("mazeIsland");
        controls.add(solveMazeButton1);
        solveMazeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catDisplay.stopTimer();
                solveMaze1();
            }
        });

        return controls;
    }

    public void walkPath1() {
        catDisplay.stopTimer();
        runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island path = Island.copyIsland(SmartCatTest.pathIsland);
        SmartCat pathCat = new SmartCat("PathWalker", path, 0, 0, randColor());
        catDisplay.displayIsland = path;
        catDisplay.revalidate();
        catDisplay.repaint();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(path, pathCat, SmartCat.class.getDeclaredMethod("walkPath", new Class[] {}),
                            new Object[] {}, speedSlider.getValue());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void collectYarn1() {
        catDisplay.stopTimer();
        runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island island = Island.copyIsland(SmartCatTest.yarnIsland);
        SmartCat cat = new SmartCat("YarnCollector", island, 0, 0, randColor());
        catDisplay.displayIsland = island;
        catDisplay.revalidate();
        catDisplay.repaint();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    controls.revalidate();
                    controls.repaint();
                    catDisplay.runIsland(island, cat,
                            SmartCat.class.getDeclaredMethod("collectAllYarn", new Class[] {}), new Object[] {},
                            speedSlider.getValue());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void solveMaze1() {
        catDisplay.stopTimer();
        runButton.setVisible(true);
        speedSlider.setVisible(true);
        Island island = Island.copyIsland(SmartCatTest.mazeIsland);
        SmartCat cat = new SmartCat("MazeSolver", island, 0, 0, randColor());
        catDisplay.displayIsland = island;
        catDisplay.revalidate();
        catDisplay.repaint();

        clearActionListeners(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runButton.setVisible(false);
                    speedSlider.setVisible(false);
                    catDisplay.runIsland(island, cat, SmartCat.class.getDeclaredMethod("solveMaze", new Class[] {}),
                            new Object[] {}, speedSlider.getValue());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private island.constants.Color randColor() {
        return (new island.constants.Color[] { island.constants.Color.ORANGE,
                island.constants.Color.GREY, island.constants.Color.WHITE,
                island.constants.Color.BROWN,
                island.constants.Color.BLACK })[new Random().nextInt(5)];
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Driver();
            }
        });
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

            SmartCat copy = (SmartCat) this.displayIsland.getTiles()[c.getRow()][c.getCol()].cat;

            try {
                m.invoke(c, args);
            } catch (Exception we) {
                we.printStackTrace();
                System.out.println(
                        "Exception found when running island animation. First redownload to make sure you have not modified any provided classes or methods, review the exception's stack trace (the 'Caused by: ' might be more helpful), and reach out on Piazza if you have further questions.");
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