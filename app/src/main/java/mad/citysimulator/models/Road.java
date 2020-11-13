package mad.citysimulator.models;

public class Road extends Structure {

    public Road(int imageId) {
        this.imageId = imageId;
        this.name = "Road";
    }

    @Override
    public String getStructureType() {
        return "Road";
    }
}
