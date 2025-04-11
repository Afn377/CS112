package minecraft;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;

public class Driver {

    public static MinecraftLP probing = new MinecraftLP();
    public static Font customFont;
    public static boolean useCustomFont = false;
    private static String defaultFont = new JLabel().getFont().getFontName();
    private static JPanel inventoryPanel;
    private static JScrollPane scrollPane;
    public static JFrame frame;

    private static void loadCustomFont() {
        try {
            File fontFile = new File("assets/Monocraft-SemiBold.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
        } catch (Exception e) {
            customFont = new Font("Wingdings", Font.PLAIN, 14);
        }
    }

    public static void updateFont(Component component) {
        Font fontToUse = useCustomFont && customFont != null
                ? customFont.deriveFont(component.getFont().getSize2D())
                : new Font(defaultFont, Font.PLAIN, component.getFont().getSize());

        component.setFont(fontToUse);
        if (component instanceof InventoryCell) {
            ((InventoryCell) component).updateToolTipFont();
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateFont(child);
            }
        }
    }

    public static void showToast(JFrame parentFrame, String message) {
        JDialog dialog = new JDialog(parentFrame);
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout());

        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setSize(400, 50);
        textArea.setOpaque(true);

        dialog.add(textArea);

        dialog.getContentPane().setBackground(Color.DARK_GRAY);
        dialog.pack();

        int dialogWidth = dialog.getWidth();
        int dialogHeight = dialog.getHeight();

        int x = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
        int y = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;

        dialog.setLocation(x, y);
        dialog.setAlwaysOnTop(true);
        dialog.setFocusableWindowState(false);

        ComponentListener moveListener = new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                int newX = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
                int newY = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;
                dialog.setLocation(newX, newY);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                int newX = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
                int newY = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;
                dialog.setLocation(newX, newY);
            }
        };
        parentFrame.addComponentListener(moveListener);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
                parentFrame.removeComponentListener(moveListener);
            }
        }, 2000);

        dialog.setVisible(true);
    }

    private static void showFailureToast(JFrame parentFrame, String message) {
        JDialog dialog = new JDialog(parentFrame);
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout());

        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.PLAIN, 24));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.RED);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setSize(400, 50);
        textArea.setOpaque(false);

        dialog.add(textArea);

        dialog.getContentPane().setBackground(Color.RED);
        dialog.pack();

        int dialogWidth = dialog.getWidth();
        int dialogHeight = dialog.getHeight();

        int x = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
        int y = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;

        dialog.setLocation(x, y);
        dialog.setAlwaysOnTop(true);
        dialog.setFocusableWindowState(false);

        ComponentListener moveListener = new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                int newX = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
                int newY = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;
                dialog.setLocation(newX, newY);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                int newX = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
                int newY = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 100;
                dialog.setLocation(newX, newY);
            }
        };
        parentFrame.addComponentListener(moveListener);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, 5000);

        dialog.setVisible(true);
    }

    private static String[] getAssetFilenames() {
        File iconsDir = new File("assets/icons");
        if (iconsDir.exists() && iconsDir.isDirectory()) {
            String[] filenames = iconsDir.list((dir, name) -> name.endsWith(".png"));
            for (int i = 0; i < filenames.length; i++) {
                filenames[i] = filenames[i].replace(".png", ""); // Removes extension
            }
            return filenames;
        }
        return new String[0];
    }

    public static void updateInventory() {
        inventoryPanel.removeAll();
        inventoryPanel.setLayout(new GridBagLayout());
        inventoryPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        int columns = 9;
        for (int i = 0; i < probing.getST().length; i++) {
            InventoryCell cell = new InventoryCell(i);

            gbc.gridx = i % columns;
            gbc.gridy = i / columns;

            inventoryPanel.add(cell, gbc);
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    public static void main(String[] args) {
        loadCustomFont();
        frame = new JFrame("Minecraft Linear Probing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(200, 200, 200));

        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
        northContainer.setBackground(new Color(192, 192, 192));

        JCheckBox fontToggleCheckbox = new JCheckBox("Use Minecraft Font");
        fontToggleCheckbox.setFont(new Font(defaultFont, Font.PLAIN, 12));
        fontToggleCheckbox.setBackground(new Color(192, 192, 192));
        fontToggleCheckbox.setHorizontalAlignment(SwingConstants.RIGHT);
        useCustomFont = true;
        fontToggleCheckbox.addActionListener(e -> {
            useCustomFont = fontToggleCheckbox.isSelected();
            updateFont(frame.getContentPane());
            frame.revalidate();
            frame.repaint();
        });
        fontToggleCheckbox.setSelected(true);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkboxPanel.setBackground(new Color(192, 192, 192));
        checkboxPanel.add(fontToggleCheckbox);
        northContainer.add(checkboxPanel, BorderLayout.EAST);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(192, 192, 192));

        JLabel titleLabel = new JLabel("ProbeCraft", SwingConstants.CENTER);
        titleLabel.setFont(new Font(defaultFont, Font.PLAIN, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        northContainer.add(titlePanel);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(new Color(192, 192, 192));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] filenames = getAssetFilenames();
        Arrays.sort(filenames);
        JComboBox<String> searchBox = new JComboBox<>(filenames);
        searchBox.setEditable(true);
        searchBox.setFont(new Font(defaultFont, Font.PLAIN, 14));
        searchBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        searchBox.setBackground(Color.LIGHT_GRAY);
        searchBox.setPreferredSize(new Dimension(200, 30));
        searchBox.setOpaque(false);
        JTextField boxField = (JTextField) searchBox.getEditor().getEditorComponent();
        boxField.setBackground(Color.LIGHT_GRAY);
        boxField.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font(defaultFont, Font.PLAIN, 14));
        searchButton.setBackground(new Color(34, 139, 34));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        searchButton.setUI(new MetalButtonUI());
        searchButton.setPreferredSize(new Dimension(100, 40));
        searchButton.addActionListener(e -> {
            String query = ((String) searchBox.getSelectedItem()).trim();
            try {
                int res = probing.search(query);
                if (res != -1) {
                    showToast(frame, "Found item at index " + res + "! Name: " + probing.getST()[res].getName()
                            + ", Count: " + probing.getST()[res].getCount());
                } else {
                    showToast(frame, "Couldn't find that item.");
                }
                searchBox.setSelectedItem("Apple");
            } catch (Exception ex) {
                showFailureToast(frame, "Something went wrong; a " + ex.getClass().getSimpleName() + " happened.");
            }
        });

        searchPanel.add(new JLabel("Search Items:"), BorderLayout.WEST);
        searchPanel.add(searchBox, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        northContainer.add(searchPanel, BorderLayout.SOUTH);

        // Instruction Label
        JTextArea instructionLabel = new JTextArea(
                "In the search/add/drop menus, hit the selectors (with up/down arrows) to choose an item. "
                        + "In the inventory, click an item to drop one of it. Hover to see its name.");
        instructionLabel.setFont(new Font(defaultFont, Font.PLAIN, 14));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBackground(Color.DARK_GRAY);
        instructionLabel.setLineWrap(true);
        instructionLabel.setWrapStyleWord(true);
        instructionLabel.setEditable(false);
        instructionLabel.setOpaque(false);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0)); // Add padding

        JPanel instructionPanel = new JPanel(new BorderLayout());
        instructionPanel.setBackground(Color.DARK_GRAY);
        instructionPanel.add(instructionLabel, BorderLayout.CENTER);

        northContainer.add(instructionPanel);

        frame.add(northContainer, BorderLayout.NORTH);

        // Is student in the right folder?
        File sanityIconFile = new File("assets/icons/" + "Lead" + ".png");

        if (!sanityIconFile.exists()) {
            showFailureToast(frame,
                    "STOP! \nRerun and make sure you opened the MinecraftLP directory that directly contains the bin, src, assets and lib folders.");
        }

        inventoryPanel = new JPanel();
        updateInventory();

        scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);

        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel addItemPanel = new JPanel(new BorderLayout(10, 10));
        addItemPanel.setBackground(new Color(192, 192, 192));
        addItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        JLabel addItemLabel = new JLabel("Add/Drop Item");
        addItemLabel.setFont(new Font(defaultFont, Font.PLAIN, 14));
        addItemPanel.add(addItemLabel, BorderLayout.WEST);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(new Color(192, 192, 192));

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.insets = new Insets(5, 10, 5, 5);
        fieldGbc.anchor = GridBagConstraints.CENTER;

        fieldGbc.gridx = 0;
        fieldGbc.gridy = 0;
        fieldGbc.weightx = 0;
        fieldGbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font(defaultFont, Font.PLAIN, 12));
        fieldsPanel.add(nameLabel, fieldGbc);

        fieldGbc.gridx = 1;
        fieldGbc.weightx = 1;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> nameField = new JComboBox<>(filenames);
        nameField.setEditable(true);
        nameField.setFont(new Font(defaultFont, Font.PLAIN, 12));
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        nameField.setBackground(Color.LIGHT_GRAY);
        nameField.setMinimumSize(new Dimension(100, 40));
        nameField.setPreferredSize(new Dimension(200, 40));
        nameField.setOpaque(false);

        JComponent editorComponent = (JComponent) nameField.getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField) {
            JTextField textField = (JTextField) editorComponent;
            textField.setOpaque(true);
            textField.setBackground(Color.LIGHT_GRAY);
            textField.setForeground(Color.BLACK);
            textField.setBorder(BorderFactory.createEmptyBorder());
        }
        fieldsPanel.add(nameField, fieldGbc);

        fieldGbc.gridx = 2;
        fieldGbc.weightx = 0;
        fieldGbc.fill = GridBagConstraints.NONE;
        JLabel countLabel = new JLabel("Count");
        countLabel.setFont(new Font(defaultFont, Font.PLAIN, 12));
        fieldsPanel.add(countLabel, fieldGbc);

        fieldGbc.gridx = 3;
        fieldGbc.weightx = 1;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        JSpinner countSpinner = new JSpinner();
        countSpinner.setFont(new Font(defaultFont, Font.PLAIN, 12));
        countSpinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        countSpinner.setBackground(Color.LIGHT_GRAY);
        countSpinner.setMinimumSize(new Dimension(50, 40));
        countSpinner.setPreferredSize(new Dimension(50, 40));
        countSpinner.setModel(new SpinnerNumberModel(0, 0, 64, 1));
        countSpinner.setValue(1);

        JComponent editorComponent2 = countSpinner.getEditor();

        if (editorComponent2 instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editorComponent2;
            JTextField textField = spinnerEditor.getTextField();
            textField.setOpaque(true);
            textField.setBackground(Color.LIGHT_GRAY);
            textField.setForeground(Color.BLACK);
            textField.setBorder(BorderFactory.createEmptyBorder());
        }

        fieldsPanel.add(countSpinner, fieldGbc);

        addItemPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(192, 192, 192));

        GridBagConstraints addButtonGbc = new GridBagConstraints();
        addButtonGbc.insets = new Insets(5, 5, 5, 5);
        addButtonGbc.gridx = 0;
        addButtonGbc.gridy = 0;
        addButtonGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addButton = new JButton("Add");
        addButton.setFont(new Font(defaultFont, Font.PLAIN, 14));
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        addButton.setUI(new MetalButtonUI());
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.addActionListener(e -> {
            try {
                File iconFile = new File("assets/icons/" + ((String) nameField.getSelectedItem()).trim() + ".png");

                if (!iconFile.exists()) {
                    showFailureToast(frame, "Item not found. Item names are case-sensitive.");
                    return;
                }

                String nameToAdd = ((String) nameField.getSelectedItem()).trim();
                int count = (Integer) countSpinner.getValue();
                probing.put(nameToAdd, count);
                updateInventory(); // Refresh inventory cells
                // workaround to update fonts based on user choice
                updateFont(frame.getContentPane());
                showToast(frame, count + " of " + nameToAdd + " were added!");
            } catch (Exception except) {
                showFailureToast(frame, "Something went wrong; a " + except.getClass().getSimpleName() + " happened.");
                except.printStackTrace();
            }
            nameField.setSelectedItem("Apple");
            countSpinner.setValue(1);
        });

        buttonPanel.add(addButton, addButtonGbc);

        GridBagConstraints dropButtonGbc = new GridBagConstraints();
        dropButtonGbc.insets = new Insets(5, 5, 5, 2);
        dropButtonGbc.gridx = 1;
        dropButtonGbc.gridy = 0;
        dropButtonGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton dropButton = new JButton("Drop");
        dropButton.setFont(new Font(defaultFont, Font.PLAIN, 14));
        dropButton.setBackground(new Color(220, 20, 60));
        dropButton.setForeground(Color.WHITE);
        dropButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        dropButton.setUI(new MetalButtonUI());
        dropButton.setPreferredSize(new Dimension(100, 40));
        dropButton.addActionListener(e -> {
            try {
                File iconFile = new File("assets/icons/" + ((String) nameField.getSelectedItem()).trim() + ".png");

                if (!iconFile.exists()) {
                    showFailureToast(frame, "Item not found. Item names are case-sensitive.");
                    return;
                }
                String nameToDelete = ((String) nameField.getSelectedItem());
                int count = (Integer) countSpinner.getValue();
                probing.delete(nameToDelete, count);
                updateInventory();
                updateFont(frame.getContentPane());
                showToast(frame, count + " of " + nameToDelete + " were dropped!");
            } catch (Exception except) {
                showFailureToast(frame, "Something went wrong; a " + except.getClass().getSimpleName() + " happened.");
                except.printStackTrace();
            }
            nameField.setSelectedItem("Apple");
            countSpinner.setValue(1);
        });

        buttonPanel.add(dropButton, dropButtonGbc);

        addItemPanel.add(buttonPanel, BorderLayout.EAST);

        JScrollPane addDropScrollPane = new JScrollPane(addItemPanel);
        addDropScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addDropScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        addDropScrollPane.setBorder(BorderFactory.createEmptyBorder());

        frame.add(addDropScrollPane, BorderLayout.SOUTH);
        updateFont(frame.getContentPane());
        frame.setVisible(true);
    }
}

class InventoryCell extends JPanel {
    private static String defaultFont = new JLabel().getFont().getFontName();
    private int index;
    private Item item;
    private JWindow tooltipWindow;
    private JLabel tooltipLabel;

    public InventoryCell(int index) {
        this.index = index;
        this.item = Driver.probing.getST()[index];

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(70, 90));

        setupGraphics();
        setupCustomTooltip();
    }

    public void updateToolTipFont() {
        if (tooltipLabel == null)
            return;
        Font fontToUse = Driver.useCustomFont && Driver.customFont != null
                ? Driver.customFont.deriveFont(12f)
                : new Font(defaultFont, Font.PLAIN, 12);
        tooltipLabel.setFont(fontToUse);
    }

    private void setupGraphics() {
        JPanel topRectangle = new JPanel();
        topRectangle.setBackground(new Color(50, 50, 50));
        topRectangle.setBounds(0, 0, 70, 4);
        add(topRectangle);

        JPanel mainSquare = new JPanel();
        mainSquare.setBackground(new Color(120, 120, 120));
        mainSquare.setBounds(0, 4, 70, 55);
        mainSquare.setLayout(null);
        add(mainSquare);

        JPanel bottomRectangle = new JPanel();
        bottomRectangle.setBackground(new Color(180, 180, 180));
        bottomRectangle.setBounds(0, 59, 70, 3);
        add(bottomRectangle);

        JLabel indexLabel = new JLabel(String.valueOf(index), SwingConstants.CENTER);
        indexLabel.setFont(new Font(defaultFont, Font.PLAIN, 14));
        indexLabel.setForeground(Color.WHITE);
        indexLabel.setBounds(0, 62, 70, 28);
        add(indexLabel);

        if (item != null) {
            File iconFile = new File("assets/icons/" + item.getName() + ".png");
            if (iconFile.exists()) {
                ImageIcon icon = new ImageIcon(iconFile.getPath());
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setBounds(10, 0, 50, 50);

                if (item.getCount() > 1) {
                    JLabel countLabel = new JLabel(String.valueOf(item.getCount()));
                    countLabel.setFont(new Font(defaultFont, Font.PLAIN, 10));
                    countLabel.setForeground(Color.WHITE);
                    countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                    countLabel.setBounds(38, 32, 20, 15);
                    mainSquare.add(countLabel);
                }
                mainSquare.add(iconLabel);

            } else {
                JLabel missingLabel = new JLabel("?");
                missingLabel.setFont(new Font(defaultFont, Font.PLAIN, 24));
                missingLabel.setForeground(Color.RED);
                missingLabel.setHorizontalAlignment(SwingConstants.CENTER);
                missingLabel.setBounds(10, 5, 50, 50);
                mainSquare.add(missingLabel);
            }
        }
    }

    private void setupCustomTooltip() {
        setToolTipText(null);

        tooltipWindow = new JWindow();
        tooltipLabel = new JLabel();
        tooltipLabel.setOpaque(true);
        tooltipLabel.setBackground(new Color(50, 50, 50));
        tooltipLabel.setForeground(Color.WHITE);
        updateToolTipFont();
        tooltipLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tooltipWindow.getContentPane().add(tooltipLabel);

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (item != null) {
                    String tooltipText = item.getName();
                    tooltipLabel.setText(tooltipText);
                    Point locationOnScreen = e.getLocationOnScreen();
                    tooltipWindow.setLocation(locationOnScreen.x + 10, locationOnScreen.y + 10);
                    tooltipWindow.pack();
                    tooltipWindow.setVisible(true);
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (item != null) {
                    tooltipWindow.setVisible(false);
                    Driver.probing.delete(item.getName(), 1);
                    Driver.updateInventory();
                    Driver.updateFont(Driver.frame.getContentPane());
                    Driver.showToast(Driver.frame, item.getName() + " was just dropped.");
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                tooltipWindow.setVisible(false);
            }
        });
    }
}