package dictionary;

public class WordNode {

    private String word;
    private String definition;

    private WordNode left;
    private WordNode right;

    public WordNode(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return this.word;
    }

    public String getDefinition() {
        return this.definition;
    }

    public WordNode getLeft() {
        return this.left;
    }

    public WordNode getRight() {
        return this.right;
    }

    public void setLeft(WordNode left) {
        this.left = left;
    }

    public void setRight(WordNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return this.word;
    }

}
