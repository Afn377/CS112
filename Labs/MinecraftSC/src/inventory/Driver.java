package inventory;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Driver {

    public static MinecraftSC craftingTable = new MinecraftSC();
    public static Font customFont;
    public static boolean useCustomFont = false;
    private static String defaultFont = new JLabel().getFont().getFontName();
    private static JPanel recipeBookPanel;
    public static JFrame frame;

    private static void loadCustomFont() {
        try {
            File fontFile = new File("assets/Monocraft-SemiBold.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
        } catch (Exception e) {
            customFont = new Font("Arial", Font.PLAIN, 14); // Fallback
        }
    }

    public static void updateFont(Component component) {
        Font fontToUse = useCustomFont && customFont != null
                ? customFont.deriveFont(component.getFont().getSize2D())
                : new Font(defaultFont, Font.PLAIN, component.getFont().getSize());
        
        //if (component instanceof JComboBox || component instanceof JTextField) return;

        component.setFont(fontToUse);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateFont(child);
            }
        }
        component.validate();
    }

    public static void showRecipe(Item item) {
        if (item == null) return;
    
        JDialog recipeDialog = new JDialog(frame, "Recipe for " + item.getName(), true);
        recipeDialog.setLayout(new BorderLayout());
        recipeDialog.setSize(500, 400);
    
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(192, 192, 192));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
    
        JPanel recipePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        recipePanel.setBackground(new Color(192, 192, 192));
    
        String[][] recipe = item.getRecipe().getItems();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String ingredient = recipe[i][j];
                DialogCell cell = ingredient != null
                    ? new DialogCell(new Item(ingredient, craftingTable.findUnknownRecipe(ingredient, "all_recipes.txt"), null))
                    : new DialogCell(new Item(" ", null, null));
                recipePanel.add(cell);
            }
        }
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapperPanel.add(recipePanel, gbc);
    
        // Arrow Label
        JLabel arrowLabel = new JLabel();
        File arrowFile = new File("assets/arrow.png");
        if (arrowFile.exists()) {
            ImageIcon arrowIcon = new ImageIcon(arrowFile.getPath());
            arrowLabel.setIcon(arrowIcon);
        } else {
            arrowLabel.setText("→");
            arrowLabel.setFont(new Font("Arial", Font.BOLD, 36));
            arrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        gbc.gridx = 1;
        wrapperPanel.add(arrowLabel, gbc);
    
        JPanel resultGridPanel = new JPanel(new GridLayout(3, 1));
        resultGridPanel.setBackground(wrapperPanel.getBackground());

        resultGridPanel.add(new JLabel());
        ResultCell resultCell = new ResultCell(item, true);
        resultGridPanel.add(resultCell); 
        resultGridPanel.add(new JLabel()); 

        gbc.gridx = 2;
        wrapperPanel.add(resultGridPanel, gbc);

        recipeDialog.add(wrapperPanel, BorderLayout.CENTER);
    
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(34, 139, 34));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        closeButton.setUI(new MetalButtonUI());
        closeButton.addActionListener(e -> recipeDialog.dispose());
        closeButton.setPreferredSize(new Dimension(100, 40));
        closeButton.setFocusable(false);

        Font fontToUse = Driver.useCustomFont && Driver.customFont != null
        ? Driver.customFont.deriveFont(14f)
        : new Font(defaultFont, Font.PLAIN, 14);
        closeButton.setFont(fontToUse);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.setBackground(recipePanel.getBackground());
        buttonPanel.add(closeButton);
        
        recipeDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        recipeDialog.setLocationRelativeTo(frame);
        recipeDialog.setVisible(true);
    }

    public static void updateRecipeBook() {
        recipeBookPanel.removeAll();
        recipeBookPanel.setLayout(new GridBagLayout());
        recipeBookPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = new String[26];
        for (int i = 0; i < 26; i++) {
            labels[i] = Character.toString((char) ('A' + i));
        }

        Item[] table = craftingTable.getTable();
        for (int i = 0; i < 26; i++) {
            JPanel bucketPanel = new JPanel();
            bucketPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            bucketPanel.setBackground(Color.DARK_GRAY);

            JLabel bucketLabel = new JLabel(labels[i] + ":");
            bucketLabel.setForeground(Color.WHITE);
            bucketPanel.add(bucketLabel);

            if (i < table.length) {
                Item curr = table[i];
                while (curr != null) {
                    RecipeCell cell = new RecipeCell(curr);
                    bucketPanel.add(cell);
                    curr = curr.getNext();
                }
            }

            gbc.gridy = i;
            recipeBookPanel.add(bucketPanel, gbc);
        }

        gbc.weighty = 1.0;
        gbc.gridy = 26;
        recipeBookPanel.add(Box.createVerticalGlue(), gbc);

        recipeBookPanel.revalidate();
        recipeBookPanel.repaint();
        updateFont(recipeBookPanel);
    }

    public static void showToast(JFrame parentFrame, String message) {
        JDialog dialog = new JDialog(parentFrame);
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout());

        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.GRAY);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        int toastWidth = Math.max(200, message.length() * 7); // Adjust width based on message length
        textArea.setPreferredSize(new Dimension(toastWidth, textArea.getPreferredSize().height));

        dialog.add(textArea);
        dialog.getContentPane().setBackground(Color.DARK_GRAY);
        dialog.pack();

        int dialogWidth = dialog.getWidth();
        int dialogHeight = dialog.getHeight();

        int x = parentFrame.getX() + (parentFrame.getWidth() - dialogWidth) / 2;
        int y = parentFrame.getY() + parentFrame.getHeight() - dialogHeight - 112;

        dialog.setLocation(x, y);
        dialog.setAlwaysOnTop(true);
        dialog.setFocusableWindowState(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, 3000);

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
                filenames[i] = filenames[i].replace(".png", ""); 
            }
            return filenames;
        }
        return new String[0];
    }

    public static void main(String[] args) {
        loadCustomFont();
        frame = new JFrame("Minecraft Recipe Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 800);
        frame.setLayout(new BorderLayout());

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
            updateRecipeBook();
            frame.revalidate();
            frame.repaint();
        });
        fontToggleCheckbox.setSelected(true);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkboxPanel.setBackground(new Color(192, 192, 192));
        checkboxPanel.add(fontToggleCheckbox);
        northContainer.add(checkboxPanel, BorderLayout.EAST);
        // Title Panel for centering the title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(192, 192, 192));

        JLabel titleLabel = new JLabel("Minecraft Separate Chaining", SwingConstants.CENTER);
        titleLabel.setFont(new Font(defaultFont, Font.PLAIN, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        northContainer.add(titlePanel);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(new Color(192, 192, 192));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // New Search Panel: ComboBox
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
        boxField.setBackground(Color.WHITE);
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
                Item res = craftingTable.search(query);
                if (res != null) {
                    showToast(frame, "Found item: " + res.getName());
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

        JTextArea instructionLabel = new JTextArea(
                "In the search/add/drop menus, hit the selectors (with up/down arrows) to choose an item. "
                    + "You can also add all items (ONLY WITH RECIPES) by selecting Add All. "
                    + "Hover to see an item's name; click to view its recipe (if one exists). ");
        instructionLabel.setFont(new Font(defaultFont, Font.PLAIN, 14));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBackground(Color.DARK_GRAY);
        instructionLabel.setLineWrap(true); // Enable line wrapping
        instructionLabel.setWrapStyleWord(true); 
        instructionLabel.setEditable(false); 
        instructionLabel.setOpaque(false); 
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0)); 

        JPanel instructionPanel = new JPanel(new BorderLayout());
        instructionPanel.setBackground(Color.DARK_GRAY);
        instructionPanel.add(instructionLabel, BorderLayout.CENTER);

        northContainer.add(instructionPanel);

        frame.add(northContainer, BorderLayout.NORTH);

        // Is student in the right folder?
        File sanityIconFile = new File("assets/icons/" + "Apple" + ".png");

        if (!sanityIconFile.exists()) {
            showFailureToast(frame,
                    "STOP! \nRerun and make sure you opened the MinecraftSC directory that directly contains the bin, src, assets and lib folders.");
        }

        frame.add(northContainer, BorderLayout.NORTH);

        recipeBookPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(recipeBookPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        frame.add(scrollPane, BorderLayout.CENTER);
        updateRecipeBook();
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
        nameField.setBackground(Color.WHITE);
        nameField.setMinimumSize(new Dimension(100, 40));
        nameField.setPreferredSize(new Dimension(200, 40));
        nameField.setOpaque(true);

        JComponent editorComponent = (JComponent) nameField.getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField) {
            JTextField textField = (JTextField) editorComponent;
            textField.setBackground(Color.WHITE);
            textField.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            textField.setForeground(Color.BLACK);
        }
        fieldsPanel.add(nameField, fieldGbc);

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
        addButton.setBackground(new Color(34, 139, 34)); // Minecraft green
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
                Recipe found = craftingTable.findUnknownRecipe(nameToAdd, "all_recipes.txt");
                craftingTable.put(nameToAdd, found.getItems(), found.getResultingCount()); 
                updateRecipeBook();
                updateFont(frame.getContentPane());
                showToast(frame, nameToAdd + " was added!");
            } catch (Exception except) {
                showFailureToast(frame, "Something went wrong; a " + except.getClass().getSimpleName() + " happened.");
                except.printStackTrace();
            }
        });

        buttonPanel.add(addButton, addButtonGbc);

        GridBagConstraints dropButtonGbc = new GridBagConstraints();
        dropButtonGbc.insets = new Insets(5, 5, 5, 2);
        dropButtonGbc.gridx = 1;
        dropButtonGbc.gridy = 0;
        dropButtonGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton dropButton = new JButton("Drop");
        dropButton.setFont(new Font(defaultFont, Font.PLAIN, 14));
        dropButton.setBackground(new Color(220, 20, 60)); // Red
        dropButton.setForeground(Color.WHITE);
        dropButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        dropButton.setUI(new MetalButtonUI());
        dropButton.setPreferredSize(new Dimension(100, 40));
        dropButton.addActionListener(e -> {
            try {
                File iconFile = new File("assets/icons/" + ((String) nameField.getSelectedItem()).trim() + ".png");

                String nameToDelete = ((String) nameField.getSelectedItem()).trim();
                craftingTable.delete(nameToDelete); // Drop one item
                updateRecipeBook();
                updateFont(frame.getContentPane());
                showToast(frame, nameToDelete + " was dropped!");
            } catch (Exception except) {
                showFailureToast(frame, "Something went wrong; a " + except.getClass().getSimpleName() + " happened.");
                except.printStackTrace();
            }
            nameField.setSelectedItem("Apple"); 
        });

        buttonPanel.add(dropButton, dropButtonGbc);

        // putAllCraftableItems
        GridBagConstraints addAllButtonGbc = new GridBagConstraints();
        addAllButtonGbc.insets = new Insets(5, 5, 5, 5);
        addAllButtonGbc.gridx = 2;
        addAllButtonGbc.gridy = 0;
        addAllButtonGbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] fileOptions = {"small_input.txt", "all_recipes.txt"};
        JComboBox<String> fileDropdown = new JComboBox<>(fileOptions);
        fileDropdown.setFont(new Font(defaultFont, Font.PLAIN, 14));
        fileDropdown.setBackground(Color.LIGHT_GRAY);
        fileDropdown.setPreferredSize(new Dimension(150, 30));
        fileDropdown.setEditable(true);
        fileDropdown.setFont(new Font(defaultFont, Font.PLAIN, 12));
        fileDropdown.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        fileDropdown.setBackground(Color.WHITE);
        fileDropdown.setMinimumSize(new Dimension(100, 40));
        fileDropdown.setPreferredSize(new Dimension(200, 40));

        JComponent editorComponent3 = (JComponent) fileDropdown.getEditor().getEditorComponent();
        if (editorComponent3 instanceof JTextField) {
            JTextField textField = (JTextField) editorComponent3;
            textField.setBackground(Color.WHITE);
            textField.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            textField.setForeground(Color.BLACK);
        }
        
        JButton addAllButton = new JButton("Put All");
        addAllButton.setFont(new Font(defaultFont, Font.PLAIN, 14));
        addAllButton.setBackground(Color.BLUE); 
        addAllButton.setForeground(Color.WHITE);
        addAllButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        addAllButton.setUI(new MetalButtonUI());
        addAllButton.setPreferredSize(new Dimension(100, 40));
        
        addAllButton.addActionListener(e -> {
            String selectedFile = fileDropdown.getSelectedItem().toString();
            String fileName = selectedFile.equals("small_input.txt") ? "small_input.txt" : "all_recipes.txt";
            craftingTable.putAllCraftableItems(fileName);
            updateRecipeBook();
            showToast(frame, "All craftable items from " + fileName + " were added!");
        });
        
        JPanel putAllPanel = new JPanel(new FlowLayout());
        putAllPanel.setBackground(new Color(192, 192, 192)); 
        putAllPanel.add(fileDropdown);
        putAllPanel.add(addAllButton);
        
        buttonPanel.add(putAllPanel, addAllButtonGbc);

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

class RecipeCell extends JPanel {
    private Item item;
    private static String defaultFont = new JLabel().getFont().getFontName();
    private JWindow tooltipWindow;
    private JLabel tooltipLabel;

    public RecipeCell(Item item) {
        this.item = item;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(40, 40));
        setBackground(Color.DARK_GRAY);
        updateToolTipFont();

        File iconFile = new File("assets/icons/" + item.getName() + ".png");
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (iconFile.exists()) {
            iconLabel.setIcon(new ImageIcon(iconFile.getPath()));
        } else {
            iconLabel.setText(item.getName());
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        add(iconLabel, BorderLayout.CENTER);
        setupCustomTooltip();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Driver.showRecipe(item);
            }
        });
    }

    public void updateToolTipFont() {
        if (tooltipLabel == null)
            return;
        Font fontToUse = Driver.useCustomFont && Driver.customFont != null
                ? Driver.customFont.deriveFont(12f)
                : new Font(defaultFont, Font.PLAIN, 12);
        tooltipLabel.setFont(fontToUse);
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
            public void mouseExited(java.awt.event.MouseEvent e) {
                tooltipWindow.setVisible(false);
            }
        });
    }
}

class DialogCell extends JPanel {
    private static String defaultFont = new JLabel().getFont().getFontName();
    private final Item item;
    private JWindow tooltipWindow;
    private JLabel tooltipLabel;
    private boolean isResult;

    public DialogCell(Item item) {
        this.item = item;

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(70, 70));

        setupGraphics();
        if (!item.getName().equals(" ")) {
            setupCustomTooltip();
            tooltipWindow.setAlwaysOnTop(true);
            updateToolTipFont();
        }
    }

    public DialogCell(Item item, boolean isResult) {
        this.item = item;
        this.isResult = isResult;

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(70, 70));

        setupGraphics();
        if (!item.getName().equals(" ")) {
            setupCustomTooltip();
            tooltipWindow.setAlwaysOnTop(true);
            updateToolTipFont();
        }
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
        JPanel mainSquare;
        if (!isResult) {
        JPanel topRectangle = new JPanel();
        topRectangle.setBackground(new Color(50, 50, 50));
        topRectangle.setBounds(0, 0, 70, 4);
        add(topRectangle);

        mainSquare = new JPanel();
        mainSquare.setBackground(new Color(120, 120, 120));
        mainSquare.setBounds(0, 4, 70, 55);
        mainSquare.setLayout(null);
        add(mainSquare);

        JPanel bottomRectangle = new JPanel();
        bottomRectangle.setBackground(new Color(180, 180, 180));
        bottomRectangle.setBounds(0, 59, 70, 3);
        add(bottomRectangle);
    }
    else {

        mainSquare = new JPanel();
        mainSquare.setBackground(new Color(192, 192, 192));
        mainSquare.setBounds(0, 4, 70, 70);
        mainSquare.setLayout(null);
        add(mainSquare);

        JPanel bottomRectangle = new JPanel();
        bottomRectangle.setBackground(new Color(192, 192, 192));
        bottomRectangle.setBounds(0, 59, 70, 3);
        add(bottomRectangle);
    }

        if (item != null) {
            File iconFile = new File("assets/icons/" + item.getName() + ".png");
            if (iconFile.exists()) {
                ImageIcon icon = new ImageIcon(iconFile.getPath());
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setBounds(8, 2, 50, 50);
                mainSquare.add(iconLabel);
            }
        }
    }

    private void setupCustomTooltip() {
        tooltipWindow = new JWindow();
        tooltipLabel = new JLabel();
        tooltipLabel.setOpaque(true);
        tooltipLabel.setBackground(new Color(50, 50, 50));
        tooltipLabel.setForeground(Color.WHITE);
        tooltipLabel.setFont(new Font(defaultFont, Font.PLAIN, 12));
        tooltipLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tooltipWindow.getContentPane().add(tooltipLabel);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (item != null && item.getName() != null && !item.getName().isEmpty()) {
                    tooltipLabel.setText(item.getName());
                    Point locationOnScreen = e.getLocationOnScreen();
                    tooltipWindow.setLocation(locationOnScreen.x + 10, locationOnScreen.y + 10);
                    tooltipWindow.pack();
                    tooltipWindow.setVisible(true);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                tooltipWindow.setVisible(false);
            }
        });
    }
}



class ResultCell extends JPanel {
    private static String defaultFont = new JLabel().getFont().getFontName();
    private final Item item;
    private JWindow tooltipWindow;
    private JLabel tooltipLabel;
    private boolean isResult;

    public ResultCell(Item item) {
        this(item, false);
    }

    public ResultCell(Item item, boolean isResult) {
        this.item = item;
        this.isResult = isResult;

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(70, 50));

        setupGraphics();
        if (!item.getName().equals(" ")) {
            setupCustomTooltip();
            tooltipWindow.setAlwaysOnTop(true);
            updateToolTipFont();
        }
    }

    public void updateToolTipFont() {
        if (tooltipLabel == null) return;

        Font fontToUse = Driver.useCustomFont && Driver.customFont != null
                ? Driver.customFont.deriveFont(12f)
                : new Font(defaultFont, Font.PLAIN, 12);
        tooltipLabel.setFont(fontToUse);
    }

    private void setupGraphics() {
        Font fontToUse = Driver.useCustomFont && Driver.customFont != null
        ? Driver.customFont.deriveFont(12f)
        : new Font(defaultFont, Font.PLAIN, 12);

        int padding = 5;

        JPanel topRectangle = new JPanel();
        topRectangle.setBackground(new Color(50, 50, 50));
        topRectangle.setBounds(0, padding, 70, 4);
        add(topRectangle);

        JPanel mainSquare = new JPanel();
        mainSquare.setBackground(new Color(120, 120, 120));
        mainSquare.setBounds(0, padding + 4, 70, 55);
        mainSquare.setLayout(null);
        add(mainSquare);

        JPanel bottomRectangle = new JPanel();
        bottomRectangle.setBackground(new Color(180, 180, 180));
        bottomRectangle.setBounds(0, padding + 59, 70, 3);
        add(bottomRectangle);

        if (item != null) {
            File iconFile = new File("assets/icons/" + item.getName() + ".png");
            if (iconFile.exists()) {
                ImageIcon icon = new ImageIcon(iconFile.getPath());
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setBounds(10, 0, 50, 50);

                if (item.getRecipe().getResultingCount() > 1) {
                    JLabel countLabel = new JLabel(String.valueOf(item.getRecipe().getResultingCount()));
                    countLabel.setFont(fontToUse);
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
        tooltipWindow = new JWindow();
        tooltipLabel = new JLabel();
        tooltipLabel.setOpaque(true);
        tooltipLabel.setBackground(new Color(50, 50, 50));
        tooltipLabel.setForeground(Color.WHITE);
        tooltipLabel.setFont(new Font(defaultFont, Font.PLAIN, 12));
        tooltipLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tooltipWindow.getContentPane().add(tooltipLabel);
    
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (item != null && item.getName() != null && !item.getName().isEmpty()) {
                    tooltipLabel.setText(item.getName());
                    Point locationOnScreen = e.getLocationOnScreen();
                    tooltipWindow.setLocation(locationOnScreen.x + 10, locationOnScreen.y + 10);
                    tooltipWindow.pack();
                    tooltipWindow.setVisible(true);
                }
            }
        });
    
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                tooltipWindow.setVisible(false);
            }
        });
    }
     
}