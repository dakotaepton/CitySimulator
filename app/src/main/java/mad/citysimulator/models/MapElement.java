package mad.citysimulator.models;


import android.graphics.Bitmap;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 */
public class MapElement
{
    // Private fields
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;
    private String ownerName;
    private Bitmap image;

    // Constructor
    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.image = null;
        this.ownerName = null;
    }

    // Accessors
    public boolean isBuildable()
    {
        return buildable;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    // Returns null if no image set
    public Bitmap getImage() { return image; }

    // If no owner name set, return the structures default name
    public String getOwnerName() {
        String name = "";
        if(ownerName == null) { name = structure.getStructureName(); }
        else { name = ownerName; }
        return name;
    }

    public Structure getStructure()  { return structure; }

    // Mutators
    public void setStructure(Structure structure)  { this.structure = structure; }
    public void setOwnerName(String name) { this.ownerName = name; }
    public void setImage(Bitmap image) { this.image = image; }
}
