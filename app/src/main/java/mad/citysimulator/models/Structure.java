package mad.citysimulator.models;

// Abstract Structure class to be extended by Residential, Road, Commercial classes
abstract class Structure {
    // Image ID field
    public int imageId;

    // Abstract methods to extend
    abstract String getStructureName();
    abstract int getImageId();
    abstract void setImageId(int imageId);
}
