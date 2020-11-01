package mad.citysimulator.models;

// Abstract Structure class to be extended by Residential, Road, Commercial classes
public abstract class Structure {
    // Image ID field
    public int imageId;

    public int getImageId() { return this.imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    // Abstract methods to extend
    public abstract String getStructureName();
}
