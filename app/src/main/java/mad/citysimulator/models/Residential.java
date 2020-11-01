package mad.citysimulator.models;

public class Residential extends Structure{

    public Residential(int imageId) {
        this.imageId = imageId;
    }

    // Structure abstract method
    @Override
    public String getStructureName() {
        return "Residential";
    }
}
