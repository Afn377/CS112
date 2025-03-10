package dictionary;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane; 
import javax.swing.JTextField; 
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

    private static int PANEL_WIDTH = 500;
    private static int PANEL_HEIGHT = 400;

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

        tabs.addTab("Dictionary", dictionaryPanel); 
        tabs.addTab("BST Display", bstPanel);
        tabs.setSelectedIndex(1);

        GridBagConstraints tabConstraints = new GridBagConstraints();
        tabConstraints.gridx = 0; // first column
        tabConstraints.gridy = 0; // first row
        tabConstraints.fill = GridBagConstraints.BOTH; // make the tabs panel stretches horizontally
        tabConstraints.weightx = 1.0; // expand horizontally
        tabConstraints.weighty = 1.0; // expand horizontally
        displayPanel.add(tabs, tabConstraints);

        GridBagConstraints controlConstraints = new GridBagConstraints();
        controlConstraints.gridx = 0; // first column
        controlConstraints.gridy = 1; // below the tabs panel (in the next row)
        controlConstraints.fill = GridBagConstraints.BOTH; // make controls panel fill horizontally
        controlConstraints.weightx = 0;
        controlConstraints.weighty = 0;

        displayPanel.add(controls, controlConstraints);  

        display.add(displayPanel);
        display.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
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

    private ArrayList<WordNode> dictionary(WordNode root) { 
        ArrayList<WordNode> traversal = new ArrayList<>();
        dictionaryHelper(root, traversal);
        return traversal;
    } 

    private void dictionaryHelper(WordNode curr, ArrayList<WordNode> list) { 
        if (curr == null) {
            return;
        }
        dictionaryHelper(curr.getLeft(), list);
        list.add(curr);
        dictionaryHelper(curr.getRight(), list);
    }

    private JScrollPane createDictionary() {
        JScrollPane wordsAndDefs = new JScrollPane();

        JPanel wordsPanel = new JPanel(); 
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.PAGE_AXIS));

        ArrayList<WordNode> inorder = dictionary(dictionary.getRoot());
        
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
            }
        });

        JButton deleteButton = new JButton("Delete Word");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (wordEntry.getText() == null || wordEntry.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a word to delete!",
                            "Word Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                } 
                dictionary.removeWord(wordEntry.getText());
                bstDisplay.refresh();
                updateDictionary(); 
            }
        });

        entryPanel.add(wordPanel);
        entryPanel.add(defPanel);

        controls.add(entryPanel);
        controls.add(addButton);
        controls.add(deleteButton);

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