package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 * 
 * @author Seth Kelley
 * @author Kal Pandit
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {

        /* PROVIDED, DO NOT EDIT */

        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() {
        // WRITE YOUR CODE HERE
        char c = StdIn.readChar();
        String encoding = StdIn.readString();
        StdIn.readLine();

        return new Symbol(c, encoding);
    }

    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     * 
     First, use .getEncoding() on the symbol in parameters to get the encoding you’ll need. You’ll insert its partial encodings as well as the leaf (character node) itself. Here’s how:

ONLY leaf nodes have Symbol objects with characters. Initialize intermediate nodes with Character.MIN_VALUE characters and the partial encodings — the constructors below will handle the expected logic.
Create intermediate nodes along the encoding path if nodes do not exist; do not put any characters in them. USE the (String encoding) constructor for symbols: new Symbol(partialEncoding). You should keep track of a partial encoding – the root has an empty string, and its left or right children would have either “L” or “R” as encodings.
The last digit of the encoding represents the position (left or right) of the character with respect to its parent. Characters are at level 6 on the tree (starting from level 0, the empty root). Use the new Symbol(character, encoding) constructor for leaves.
     */
    public void addCharacter(Symbol newSymbol) {
        // WRITE YOUR CODE HERE
        String encoding = newSymbol.getEncoding();
        if(treeRoot == null){
            treeRoot = new TreeNode(new Symbol(""), null, null);
        }
        TreeNode currentNode = treeRoot;
        String partialEncoding = "";
        for (int i = 0; i < encoding.length(); i++) {
            char direction = encoding.charAt(i);
            if (currentNode == null) {
                currentNode = new TreeNode(new Symbol(partialEncoding), null, null);
            }
            if (direction == 'L') {
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(new TreeNode(new Symbol(partialEncoding + "L"), null, null));
                }
                currentNode = currentNode.getLeft();
            } else if (direction == 'R') {
                if (currentNode.getRight() == null) {
                    currentNode.setRight(new TreeNode(new Symbol(partialEncoding + "R"), null, null));
                }
                currentNode = currentNode.getRight();
            }
            partialEncoding += direction;
        }
        currentNode.setSymbol(newSymbol);
 
    }

    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if there is no path
     */
    public TreeNode getSymbolNode(String encoding) {
        // WRITE YOUR CODE HERE
        TreeNode currentNode = treeRoot;
        for (int i = 0; i < encoding.length(); i++) {
            char direction = encoding.charAt(i);
            if (direction == 'L') {
                currentNode = currentNode.getLeft();
            } else if (direction == 'R') {
                currentNode = currentNode.getRight();
            }
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;
    }

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */
    public String findBrailleEncoding(char character) {
        // WRITE YOUR CODE HERE
        if(treeRoot == null)
            return null;
        else
            return findBrailleEncodingHelper(treeRoot, character);
             


    }

    private String findBrailleEncodingHelper(TreeNode node, char character) {
        if(node == null)
            return null;
        if(node.getSymbol().getCharacter() == character)
            return node.getSymbol().getEncoding();
        String left = findBrailleEncodingHelper(node.getLeft(), character);
        String right = findBrailleEncodingHelper(node.getRight(), character);
        if(left != null)
            return left;
        if(right != null)
            return right;
        return null;
    }

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */
    public ArrayList<Symbol> encodingsStartWith(String start) {
        // WRITE YOUR CODE HERE

        return null; // Replace this line, it is provided so your code compiles
    }

    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) {
        // WRITE YOUR CODE HERE

        return null; // Replace this line, it is provided so your code compiles
    }


    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    public void deleteSymbol(char symbol) {
        // WRITE YOUR CODE HERE
 
    }

    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        }
        else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }

}
