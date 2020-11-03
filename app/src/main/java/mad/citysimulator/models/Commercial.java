package mad.citysimulator.models;

public class Commercial extends Structure {

    public Commercial(int imageId) { this.imageId = imageId; }

    // Structure abstract method
    @Override
    public String getStructureName() {
        return "Commercial";
    }

    @Override
    public int getCost() { return 200; }
}
