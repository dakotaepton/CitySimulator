package mad.citysimulator.models;

public class Road extends Structure {

    public Road(int imageId) { this.imageId = imageId; }

    // Structure abstract method
    @Override
    public String getStructureName() {
        return "Road";
    }
}
