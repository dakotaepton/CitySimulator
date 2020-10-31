package mad.citysimulator.models;

public class Residential extends Structure{

    public Residential(int imageId) {
        this.imageId = imageId;
    }

    // Structure abstract method
    @Override
    String getStructureName() {
        return "Residential";
    }

    @Override
    int getImageId() {
        return imageId;
    }

    @Override
    void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
