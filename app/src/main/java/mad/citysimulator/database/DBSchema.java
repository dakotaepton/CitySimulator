package mad.citysimulator.database;

public class DBSchema {
    // Settings table
    public static class GameStateTable {
        // Table Name
        public static final String NAME = "game_state";
        // Column Names
        public static class Cols {
            public static final String SAVE_NAME = "save_name";
            public static final String CITY_NAME = "city_name";
            public static final String MAP = "map";
            public static final String STRUCTURES = "structures";
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String INITIAL_MONEY = "initial_money";
            public static final String MONEY = "money";
            public static final String GAME_TIME = "game_time";
        }
    }

    // Images table
    public static class BitmapImageTable {
        // Table Name
        public static final String NAME = "images";
        // Column Names
        public static class Cols {
            public static final String ROW_NUM = "row_number";
            public static final String COL_NUM = "col_number";
            public static final String IMAGE_BLOB = "image_blob";
        }
    }
}