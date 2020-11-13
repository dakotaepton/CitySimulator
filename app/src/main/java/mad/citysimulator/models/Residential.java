package mad.citysimulator.models;

public class Residential extends Structure{

    public Residential(int imageId) {
        this.imageId = imageId;
        this.name = "Residential";
    }

    @Override
    public String getStructureType() {
        return "Residential";
    }
}
