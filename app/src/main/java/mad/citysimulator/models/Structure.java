package mad.citysimulator.models;

// Structure class to be extended by Residential, Road, Commercial classes
public class Structure {

    // Image ID field
    public int imageId;
    public int row;
    public int col;
    public String name;
    public StructureType type;

    public Structure(int imageId, StructureType type) {
        this.imageId = imageId;
        this.type = type;
        this.row = 0;
        this.col = 0;
        switch (type) {
            case ROAD:
                this.name = "Road";
                break;
            case COMMERCIAL:
                this.name = "Commercial";
                break;
            case RESIDENTIAL:
                this.name = "Residential";
                break;
        }
    }

    // Getters
    public int getImageId() { return this.imageId; }
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public String getName() { return this.name; }
    public StructureType getStructureType() { return type; }

    // Setters
    public void setImageId(int imageId) { this.imageId = imageId; }
    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void setName(String name) { this.name = name; }
    public void setType(StructureType type) { this.type = type; }
}
