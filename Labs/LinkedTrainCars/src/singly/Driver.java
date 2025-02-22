package singly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class Driver {

    private TrainCarDisplay trainDisplay;
    private LinkedTrainCars cars;
    private static String[] possibleCars = { "Engine", "Coal", "Passenger", "Flat", "Box", "Tank", };

    public Driver() {

        JFrame display = new JFrame("Linked Train List Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        cars = new LinkedTrainCars();

        trainDisplay = new TrainCarDisplay();
        JScrollPane scroll = new JScrollPane(trainDisplay, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));

        JPanel text = new JPanel();
        JLabel numberCars = new JLabel("Number Cars: " + cars.numCars());
        text.add(numberCars);

        JPanel buttons = new JPanel();
        JComboBox<String> carsBox = new JComboBox<>();
        for (int i = 0; i < possibleCars.length; i++) {
            carsBox.addItem(possibleCars[i]);
        }

        JSpinner indexBox = new JSpinner(new SpinnerNumberModel(cars.numCars() + 1, 0, 100, 1));
        JButton addCar = new JButton("Add Car at Index");
        addCar.addActionListener((ActionEvent e) -> {
            int index = (int) indexBox.getValue();
            String chosen = (String) (carsBox.getSelectedItem());
            cars.insertAt(chosen, index);

            int numcars = cars.numCars();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    numberCars.setText("Number Cars: " + numcars); 
                    trainDisplay.revalidate();
                    trainDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });

        JButton removeCarIndex = new JButton("Remove Index");
        removeCarIndex.addActionListener((ActionEvent e) -> {
            int index = (int) indexBox.getValue();
            String removed = cars.remove(index);
            int numcars = cars.numCars();
            int selected = (numcars - 1 > 0) ? ((removed != null) ? (numcars) : (numcars + 1)) : (1);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() { 
                    numberCars.setText("Number Cars: " + numcars);
                    trainDisplay.revalidate();
                    trainDisplay.repaint();
                    scroll.revalidate();
                    scroll.repaint();
                }
            });
        });

        // JButton removeCarName = new JButton("Remove Car");
        // removeCarName.addActionListener((ActionEvent e) -> {
        // String chosen = (String) (carsBox.getSelectedItem());
        // String removed = cars.remove(chosen);
        // int numcars = cars.numCars();
        // SwingUtilities.invokeLater(new Runnable() {
        // @Override
        // public void run() {
        // indexBox.setModel(new SpinnerNumberModel(numcars + 1, 1, numcars + 1, 1));
        // numberCars.setText("Number Cars: " + numcars);
        // trainDisplay.revalidate();
        // trainDisplay.repaint();
        // scroll.revalidate();
        // scroll.repaint();
        // }
        // });
        // });

        buttons.add(carsBox);
        buttons.add(addCar);
        buttons.add(indexBox);
        buttons.add(removeCarIndex);
        // buttons.add(removeCarName);
        buttons.setPreferredSize(new Dimension(525, 40));

        controls.add(buttons);
        controls.add(text);
        controls.setPreferredSize(new Dimension(525, 60));

        window.add(controls);
        window.add(scroll);

        display.add(window);
        display.setPreferredSize(new Dimension(525, 250));
        display.setResizable(false);
        display.pack();
        display.setVisible(true);
    }

    private class TrainCarDisplay extends JPanel {
        private TrainCarDisplay() {
            super();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int[] carDim = { 120, 80 };
            int trainY = (this.getHeight() / 2) - 80;
            int currX = 15;
            int position = 1;
            for (TrainCar ptr = cars.getFront(); ptr != null; ptr = ptr.next) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(currX, trainY, carDim[0], carDim[1]);
                g.setColor(Color.WHITE);
                g.drawString(ptr.name, currX + 35, trainY + 45);
                g.drawString("" + position++, currX + 5, trainY + 15);
                int wheelX = currX;
                for (int j = 0; j < 4; j++) {
                    g.setColor(Color.BLACK);
                    g.fillOval(wheelX, trainY + carDim[1], carDim[0] / 4, carDim[1] / 3);
                    g.setColor(Color.WHITE);
                    g.fillOval(wheelX + 3, trainY + carDim[1] + 3, carDim[0] / 4 - 6, carDim[1] / 3 - 6);
                    wheelX += carDim[0] / 4;
                }
                if (ptr != cars.getFront()) {
                    g.setColor(Color.DARK_GRAY);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4, currX - 15, trainY + (3 * carDim[1]) / 4);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4 - 5, currX - 15, trainY + (3 * carDim[1]) / 4 - 5);

                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4, currX - 6, trainY + (3 * carDim[1]) / 4 + 5);
                    g.drawLine(currX, trainY + (3 * carDim[1]) / 4 - 5, currX - 6, trainY + (3 * carDim[1]) / 4 - 10);
                }
                currX += carDim[0] + 15;
            }
            int panelWidth = (15 + (position * (carDim[0] + 25)) < 450) ? (450) : (15 + (position * (carDim[0] + 25)));
            this.setPreferredSize(new Dimension(panelWidth, 175));
        }

        
    }

    public static void main(String args[]) {
        LinkedTrainCars list = new LinkedTrainCars();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Driver();
            }
        });
    }
}
