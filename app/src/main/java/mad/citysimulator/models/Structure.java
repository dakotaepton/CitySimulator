package mad.citysimulator.models;

// Structure class to be extended by Residential, Road, Commercial classes
public class Structure {

    // Image ID field
    public int imageId;
    public int row;
    public int col;
    public String name;

    public Structure() {
        this.imageId = 0;
        this.row = 0;
        this.col = 0;
    }

    // Getters
    public int getImageId() { return this.imageId; }
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public String getName() { return this.name; }

    // Setters
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void setName(String name) { this.name = name; }

    public String getStructureType() { return "Structure"; }
}
