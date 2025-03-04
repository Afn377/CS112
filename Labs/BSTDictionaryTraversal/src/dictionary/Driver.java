package dictionary;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    private JFrame display;

    private JTabbedPane tabs;

    private JPanel dictionaryPanel, controls;
    private JScrollPane wordsAndDefs;

    private BSTDictionary dictionary;
    private BSTDisplay bstDisplay;

    private JTextField preorderDisplay;
    private JTextField postorderDisplay;
    private JTextField inorderDisplay;
    private JTextField levelorderDisplay;

    private static int PANEL_WIDTH = 700;
    private static int PANEL_HEIGHT = 600;

    public Driver() {
        display = new JFrame("BST Dictionary");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(new BorderLayout());

        tabs = new JTabbedPane();

        dictionary = new BSTDictionary();

        dictionaryPanel = createDictionaryPanel();
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridBagLayout());

        JPanel bstPanel = new JPanel();
        bstPanel.setLayout(new BoxLayout(bstPanel, BoxLayout.PAGE_AXIS));
        bstPanel.add(createControls());
        bstDisplay = new BSTDisplay(dictionary);
        bstPanel.add(bstDisplay);

        JPanel traversalPanel = createTraversalPanel();

        tabs.addTab("Dictionary", dictionaryPanel);
        tabs.addTab("Traversals", traversalPanel);
        tabs.addTab("BST Display", bstPanel);
        tabs.setSelectedIndex(1);

        GridBagConstraints tabConstraints = new GridBagConstraints();
        tabConstraints.gridx = 0; // first column
        tabConstraints.gridy = 0; // first row
        tabConstraints.fill = GridBagConstraints.HORIZONTAL; // make the tabs panel stretches horizontally
        tabConstraints.weightx = 1.0; // expand horizontally
        displayPanel.add(tabs, tabConstraints);

        GridBagConstraints controlConstraints = new GridBagConstraints();
        controlConstraints.gridx = 0; // first column
        controlConstraints.gridy = 1; // below the tabs panel (in the next row)
        controlConstraints.fill = GridBagConstraints.HORIZONTAL; // make controls panel fill horizontally
        controlConstraints.weightx = 0;
        controlConstraints.weighty = 0;

        displayPanel.add(controls, controlConstraints);
        displayPanel.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        display.add(displayPanel);
        display.pack();
        display.setVisible(true);
    }

    private JPanel createDictionaryPanel() {

        JPanel dictionaryPanel = new JPanel();
        dictionaryPanel.setLayout(new BoxLayout(dictionaryPanel, BoxLayout.PAGE_AXIS));

        wordsAndDefs = createDictionary();

        dictionaryPanel.add(wordsAndDefs);

        return dictionaryPanel;
    }

    private JScrollPane createDictionary() {
        JScrollPane wordsAndDefs = new JScrollPane();

        JPanel wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.PAGE_AXIS));

        ArrayList<WordNode> inorder = dictionary.inOrder();
        try {
            for (WordNode word : inorder) {

                JPanel wordPanel = new JPanel();
                wordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                wordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel label = new JLabel(word.getWord() + " -");
                Font font = label.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                label.setFont(font.deriveFont(attributes));

                JLabel def = new JLabel(word.getDefinition());

                wordPanel.add(label);
                wordPanel.add(def);

                wordsPanel.add(wordPanel);
            }
        } catch (Exception e) {
            JPanel wordPanel = new JPanel();
            wordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            wordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("ERROR!");
            JLabel err = new JLabel("Your inorder traversal method resulted in a runtime error.");
            JPanel wordPanel2 = new JPanel();

            wordPanel.add(label);
            wordsPanel.add(wordPanel);
            wordPanel2.add(err);
            wordsPanel.add(wordPanel2);

            JTextArea textArea = new JTextArea(e.getMessage());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            wordsPanel.add(textArea);

            wordsAndDefs.add(wordsPanel);
            wordsAndDefs.setViewportView(wordsPanel);
        }

        wordsAndDefs.add(wordsPanel);
        wordsAndDefs.setViewportView(wordsPanel);
        return wordsAndDefs;
    }

    private void updateDictionary() {
        this.dictionaryPanel.remove(this.wordsAndDefs);
        this.wordsAndDefs = createDictionary();
        this.dictionaryPanel.add(wordsAndDefs);
        this.dictionaryPanel.revalidate();
        this.dictionaryPanel.repaint();
    }

    private JPanel createTraversalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel preorderLabel = new JLabel("preorder:");
        preorderDisplay = new JTextField();
        preorderDisplay.setEditable(false);
        JLabel postorderLabel = new JLabel("postorder:");
        postorderDisplay = new JTextField();
        postorderDisplay.setEditable(false);
        JLabel inorderLabel = new JLabel("inorder:");
        inorderDisplay = new JTextField();
        inorderDisplay.setEditable(false);
        JLabel levelorderLabel = new JLabel("level-order:");
        levelorderDisplay = new JTextField();
        levelorderDisplay.setEditable(false);

        panel.add(preorderLabel);
        panel.add(preorderDisplay);

        panel.add(postorderLabel);
        panel.add(postorderDisplay);

        panel.add(inorderLabel);
        panel.add(inorderDisplay);

        panel.add(levelorderLabel);
        panel.add(levelorderDisplay);

        return panel;
    }

    private JPanel createControls() {
        controls = new JPanel();

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.PAGE_AXIS));

        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.LINE_AXIS));
        JTextField wordEntry = new JTextField();
        wordEntry.setPreferredSize(new Dimension(150, 20));
        wordPanel.add(new JLabel("Word Entry: "));
        wordPanel.add(wordEntry);

        JPanel defPanel = new JPanel();
        defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.LINE_AXIS));
        JTextField defEntry = new JTextField();
        defEntry.setPreferredSize(new Dimension(150, 20));
        defPanel.add(new JLabel("Definition: "));
        defPanel.add(defEntry);

        JButton addButton = new JButton("Add Word");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (wordEntry.getText() == null || wordEntry.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a word to add!",
                            "Word Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else if (wordEntry.getText().length() > 20) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter 20 characters or less!",
                            "Word Length Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (defEntry.getText() == null || defEntry.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a definition to add!",
                            "Definition Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (containsWord(wordEntry.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "This word already exists in the dictionary!",
                            "Word Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                dictionary.addWord(wordEntry.getText(), defEntry.getText());
                bstDisplay.refresh();
                updateDictionary();
                refreshPanels();
            }
        });

        entryPanel.add(wordPanel);
        entryPanel.add(defPanel);

        controls.add(entryPanel);
        controls.add(addButton);

        controls.setMaximumSize(new Dimension(PANEL_WIDTH, 30));

        return controls;
    }

    private boolean containsWord(String word) {
        WordNode node = this.dictionary.getRoot();
        return containsWordHelper(node, word);
    }

    private boolean containsWordHelper(WordNode curr, String word) {
        if (curr == null) {
            return false;
        }
        return curr.getWord().equals(word) || containsWordHelper(curr.getLeft(), word)
                || containsWordHelper(curr.getRight(), word);
    }

    private void refreshPanels() {

        StringBuilder preText = new StringBuilder();
        ArrayList<WordNode> pre = dictionary.preOrder();
        boolean first = true;

        if (pre != null && pre.size() > 0) {
            for (WordNode node : pre) {
                if (!first) {
                    preText.append(", ");
                }
                first = false;
                preText.append(node.getWord());
            }
        } else {
            preText.append("Preorder list is empty or null");
        }
        preorderDisplay.setText(preText.toString());

        StringBuilder postText = new StringBuilder();
        ArrayList<WordNode> post = dictionary.postOrder();
        first = true;
        if (post != null && post.size() > 0) {
            for (WordNode node : post) {
                if (!first) {
                    postText.append(", ");
                }
                first = false;
                postText.append(node.getWord());
            }
        } else {
            postText.append("Postorder list is empty or null");
        }
        postorderDisplay.setText(postText.toString());

        StringBuilder inText = new StringBuilder();
        ArrayList<WordNode> in = dictionary.inOrder();
        first = true;
        if (in != null && in.size() > 0) {
            for (WordNode node : in) {
                if (!first) {
                    inText.append(", ");
                }
                first = false;
                inText.append(node.getWord());
            }
        } else {
            inText.append("Inorder list is empty or null");
        }
        inorderDisplay.setText(inText.toString());

        StringBuilder levelText = new StringBuilder();
        ArrayList<WordNode> level = dictionary.levelOrder();
        first = true;
        if (level != null && level.size() > 0) {
            for (WordNode node : level) {
                if (!first) {
                    levelText.append(", ");
                }
                first = false;
                levelText.append(node.getWord());
            }
        } else {
            levelText.append("Level-order list is empty or null");
        }
        levelorderDisplay.setText(levelText.toString());
    }

    private static class BSTDisplay extends JScrollPane {

        private BSTDictionary dict;

        private BSTDisplay(BSTDictionary bst) {
            super();
            dict = bst;

            TreeDisplay<String> myTreeViewer = new TreeDisplay<String>(bst.getRoot(), this.getWidth(),
                    this.getHeight());
            JPanel frame = myTreeViewer;

            setViewportView(frame);

        }

        public void refresh() {
            setViewportView(new TreeDisplay<String>(dict.getRoot(), this.getWidth(), this.getHeight()));

            if (getHorizontalScrollBar() != null) {
                getHorizontalScrollBar().setValue(getHorizontalScrollBar().getMaximum());
            }

            this.repaint();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Driver();

        });
    }

}