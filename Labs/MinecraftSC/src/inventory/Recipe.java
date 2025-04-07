package inventory;
/**
 * Stores a crafting recipe.
 * Consists of a 3x3 string array of item names and
 * a resulting count (how many items this recipe can generate). 
 * 
 * @author Kal Pandit
 */
public class Recipe {
    private String[][] items;
    private int resultingCount;

    public Recipe(String[][] items, int resultingCount) {
        this.items = items;
        this.resultingCount = resultingCount;
    }

    public Recipe() {
        items = new String[3][3];
    }

    public String[][] getItems() {
        return this.items;
    }

    public void setItems(String[][] items) {
        this.items = items;
    }

    public int getResultingCount() {
        return this.resultingCount;
    }

    public void setResultingCount(int resultingCount) {
        this.resultingCount = resultingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) o;
        if (this.items.length != other.getItems().length) {
            return false;
        } else if (this.items.length > 0 && this.items[0].length != other.getItems()[0].length) {
            return false;
        } else if (this.items.length > 0 && this.items[0].length > 0) {
            for (int i = 0; i < this.items.length; i++) {
                for (int j = 0; j < this.items[i].length; j++) {
                    if (this.items[i][j] != null && !this.items[i][j].equals(other.getItems()[i][j])) {
                        return false;
                    }
                }
            } 
        } 
        return true;
    }
}
