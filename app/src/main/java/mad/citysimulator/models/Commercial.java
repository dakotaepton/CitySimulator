package mad.citysimulator.models;

public class Commercial extends Structure {

    public Commercial(int imageId) {
        this.imageId = imageId;
        this.name = "Commercial";
    }

    @Override
    public String getStructureType() {
        return "Commercial";
    }
}
