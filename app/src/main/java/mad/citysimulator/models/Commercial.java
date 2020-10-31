package mad.citysimulator.models;

public class Commercial extends Structure {

    public Commercial(int imageId) {
        this.imageId = imageId;
    }

    // Structure abstract method
    @Override
    String getStructureName() {
        return "Commercial";
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
