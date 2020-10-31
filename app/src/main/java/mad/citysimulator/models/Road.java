package mad.citysimulator.models;

public class Road extends Structure {

    public Road(int imageId) {
        this.imageId = imageId;
    }

    // Structure abstract method
    @Override
    String getStructureName() {
        return "Road";
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
