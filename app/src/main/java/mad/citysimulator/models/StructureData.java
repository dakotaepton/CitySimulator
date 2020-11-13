package mad.citysimulator.models;


import java.util.Arrays;
import java.util.List;

import mad.citysimulator.R;

/**
 * Stores the list of possible structures. This has a static get() method for retrieving an
 * instance, rather than calling the constructor directly.
 *
 * The remaining methods -- get(int), size(), add(Structure) and remove(int) -- provide
 * minimalistic list functionality.
 *
 * There is a static int array called DRAWABLES, which stores all the drawable integer references,
 * some of which are not actually used (yet) in a Structure object.
 */
public class StructureData
{
    public static final int[] DRAWABLES = {
        0, // No structure
        R.drawable.ic_building1, R.drawable.ic_building2, R.drawable.ic_building3,
        R.drawable.ic_building4, R.drawable.ic_building5, R.drawable.ic_building6,
        R.drawable.ic_building7, R.drawable.ic_building8,
        R.drawable.ic_road_ns, R.drawable.ic_road_ew, R.drawable.ic_road_nsew,
        R.drawable.ic_road_ne, R.drawable.ic_road_nw, R.drawable.ic_road_se, R.drawable.ic_road_sw,
        R.drawable.ic_road_n, R.drawable.ic_road_e, R.drawable.ic_road_s, R.drawable.ic_road_w,
        R.drawable.ic_road_nse, R.drawable.ic_road_nsw, R.drawable.ic_road_new, R.drawable.ic_road_sew,
        R.drawable.ic_tree1, R.drawable.ic_tree2, R.drawable.ic_tree3, R.drawable.ic_tree4};

    private final List<Structure> structureList = Arrays.asList(
            new Structure(R.drawable.ic_building1, StructureType.RESIDENTIAL),
            new Structure(R.drawable.ic_building2, StructureType.RESIDENTIAL),
            new Structure(R.drawable.ic_building3, StructureType.RESIDENTIAL),
            new Structure(R.drawable.ic_building4, StructureType.RESIDENTIAL),
            new Structure(R.drawable.ic_building5, StructureType.COMMERCIAL),
            new Structure(R.drawable.ic_building6, StructureType.COMMERCIAL),
            new Structure(R.drawable.ic_building7, StructureType.COMMERCIAL),
            new Structure(R.drawable.ic_building8, StructureType.COMMERCIAL),
            new Structure(R.drawable.ic_road_ns, StructureType.ROAD),
            new Structure(R.drawable.ic_road_ew, StructureType.ROAD),
            new Structure(R.drawable.ic_road_nsew, StructureType.ROAD),
            new Structure(R.drawable.ic_road_ne, StructureType.ROAD),
            new Structure(R.drawable.ic_road_nw, StructureType.ROAD),
            new Structure(R.drawable.ic_road_se, StructureType.ROAD),
            new Structure(R.drawable.ic_road_sw, StructureType.ROAD),
            new Structure(R.drawable.ic_road_n, StructureType.ROAD),
            new Structure(R.drawable.ic_road_e, StructureType.ROAD),
            new Structure(R.drawable.ic_road_s, StructureType.ROAD),
            new Structure(R.drawable.ic_road_w, StructureType.ROAD),
            new Structure(R.drawable.ic_road_nse, StructureType.ROAD),
            new Structure(R.drawable.ic_road_nsw, StructureType.ROAD),
            new Structure(R.drawable.ic_road_new, StructureType.ROAD),
            new Structure(R.drawable.ic_road_sew, StructureType.ROAD)
    );

    private static StructureData instance = null;

    public static StructureData get()
    {
        if(instance == null)
        {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public Structure getStructure(int i) { return structureList.get(i); }
    public int getNumStructures() {  return structureList.size(); }
    public void add(Structure s) { structureList.add(0, s); }
    public void remove(int i) { structureList.remove(i); }

}
