package mad.citysimulator.models;

// Abstract Structure class to be extended by Residential, Road, Commercial classes
public abstract class Structure {
    // Image ID field
    public int imageId;
    public int row;
    public int col;

    // Getters
    public int getImageId() { return this.imageId; }
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }

    // Setters
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }

    // Abstract methods to extend
    public abstract String getStructureName();
    public abstract int getCost();
}
