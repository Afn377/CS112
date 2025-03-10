package dictionary; 

/**
 * This is a BST which represents Dictionary, containing WordNodes with a word 
 * and a defintion
 * 
 * You will implement the recursive helper method for removeWord(), to complete 
 * the removal functionality of the dictionary.
 */
public class BSTDictionary {

    // The root node of this BST
    private WordNode root;

    /**
     * This method is provided to you. 
     * 
     * This calls your removeWordHelper() method to remove the given Word from the dictionary, using Hibbard deletion for BSTs
     * 
     * @param word
     * @return
     */
    public void removeWord(String word) {
        // DO NOT EDIT
        root = removeWordHelper(root, word);
    }

    /**
     * This is a recursive helper method to delete a node from a BST.
     *  
     * There are 4 main cases to handle:
     *      1) curr is null
     *      2) word is to the left of curr.getWord() 
     *      3) word is to the right of curr.getWord() 
     *      4) word is equal to curr.getWord() 
     * 
     * The fourth case above has 4 cases of its own:
     *      1) The target has no children (return null)
     *      2) The target only has a left child (return left)
     *      3) The target only has a right child (return right)
     *      4) The target has both children (replace with inorder successor)
     * 
     * View the assignment description for more information on filling out the above cases.
     * 
     * @param word the word to delete
     * @return Recursively return WordNodes, to modify the structure of the tree as you traverse
     */
    private WordNode removeWordHelper(WordNode curr, String word) {
        // WRITE YOUR CODE HERE
        if(curr == null) {
            return null;
        } else if (word.compareTo(curr.getWord()) < 0) {
            curr.setLeft(removeWordHelper(curr.getLeft(), word));
        } else if (word.compareTo(curr.getWord()) > 0) {
            curr.setRight(removeWordHelper(curr.getRight(), word));
        } else {
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            } else if (curr.getLeft() == null) {
                return curr.getRight();
            } else if (curr.getRight() == null) {
                return curr.getLeft();
            } else {
                WordNode min = findMin(curr.getRight());
                WordNode temp = new WordNode(min.getWord(), min.getDefinition());
                curr.setRight(removeWordHelper(curr.getRight(), temp.getWord()));
                temp.setLeft(curr.getLeft());
                temp.setRight(curr.getRight());
                return temp;
            }
        }
        return curr;
    }

    /**
     * This method is provided for you.
     * Helper method to find the minimum node of a tree
     * 
     * While curr's left child is not null, move the curr pointer left.
     * After, return curr.
     * 
     * @param node
     * @return
     */
    private WordNode findMin(WordNode curr) {
        // DO NOT EDIT
        while (curr != null && curr.getLeft() != null) {
            curr = curr.getLeft();
        }
        return curr;
    }
    

    /**
     * This method is provided for you, do not edit it.
     * 
     * This inserts a new WordNode in this BST, containing the given
     * word and the given definition
     * 
     * @param word       The word to add
     * @param definition The definition of the word
     */
    public void addWord(String word, String definition) {
        // DO NOT EDIT
        if (root == null) {
            root = new WordNode(word, definition);
            return;
        }
        WordNode ptr = root;
        while (ptr != null) {
            if (word.compareTo(ptr.getWord()) < 0) {
                if (ptr.getLeft() == null) {
                    ptr.setLeft(new WordNode(word, definition));
                    return;
                }
                ptr = ptr.getLeft();
            } else if (word.compareTo(ptr.getWord()) > 0) {
                if (ptr.getRight() == null) {
                    ptr.setRight(new WordNode(word, definition));
                    return;
                }
                ptr = ptr.getRight();
            } else {
                return;
            }
        }
    }

    /**
     * This method is provided for you, do not edit it.
     * 
     * @return the root node of this BST
     */
    public WordNode getRoot() {
        // DO NOT EDIT
        return this.root;
    }
}
