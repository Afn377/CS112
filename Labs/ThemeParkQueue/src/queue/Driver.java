package queue;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class Driver {

    private ThemeParkQueue queue;

    private RideQueueDisplay rideQueue;
    private final Color[] possibleColors = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK,
            Color.ORANGE };
    private final String[] colorNames = { "RED", "BLUE", "GREEN", "YELLOW", "PINK", "ORANGE" };

    public Driver() {

        JFrame display = new JFrame("Theme Park Queue Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        queue = new ThemeParkQueue();

        rideQueue = new RideQueueDisplay();
        JScrollPane scroll = new JScrollPane(rideQueue, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel controls = new JPanel();
        JLabel numberRiders = new JLabel("Line Length: " + queue.getLineLength());
        JButton enqueueRider = new JButton("Enqueue Rider");
        enqueueRider.addActionListener((ActionEvent e) -> {
            int rand = (int) (Math.random() * (possibleColors.length));
            queue.enqueue(colorNames[rand]);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberRiders.setText("Line Length: " + queue.getLineLength());
                    rideQueue.revalidate();
                    rideQueue.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        JButton dequeueRider = new JButton("Dequeue Rider");
        dequeueRider.addActionListener((ActionEvent e) -> {
            queue.dequeue();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberRiders.setText("Line Length: " + queue.getLineLength());
                    rideQueue.revalidate();
                    rideQueue.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });
        controls.add(enqueueRider);
        controls.add(dequeueRider);
        controls.add(numberRiders);
        controls.setPreferredSize(new Dimension(300, 37));
        window.add(controls);
        window.add(scroll);

        display.add(window);
        display.setResizable(false);
        display.pack();
        display.setVisible(true);
    }

    private class RideQueueDisplay extends JPanel {
        private RideQueueDisplay() {
            super();
            this.setPreferredSize(new Dimension(420, 200));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = 50;
            int y = 100;
            for (Rider ptr = queue.getFront(); ptr != null; ptr = ptr.next) {
                for (int i = 0; i < colorNames.length; i++) {
                    if (colorNames[i].equals(ptr.name)) {
                        g.setColor(possibleColors[i]);
                    }
                }
                g.fillOval(x - 40, (y + 50) - 100, 50, 120);
                g.fillOval(x - 40, (y + 10) - 100, 50, 50);
                x += 60;
            }
            this.setPreferredSize(new Dimension(60 * queue.getLineLength() + 20, 200));
        }
    }

    public static void main(String args[]) {
        ThemeParkQueue queue = new ThemeParkQueue();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Driver();
            }
        });
    }
}