package mad.citysimulator.database;

public class SettingsSchema {
    // Settings table
    public static class SettingsTable {
        // Table Name
        public static final String NAME = "settings";
        // Column Names
        public static class Cols {
            public static final String SAVE_NAME = "session_id";
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String INITIAL_MONEY = "initial_money";
            public static final String MONEY = "money";
            public static final String FAMILY_SIZE = "family_size";
            public static final String SHOP_SIZE = "shop_size";
            public static final String SALARY = "salary";
            public static final String TAX_RATE = "tax_rate";
            public static final String SERVICE_COST = "service_cost";
            public static final String HOUSE_BUILDING_COST = "house_building_cost";
            public static final String COMM_BUILDING_COST = "comm_building_cost";
            public static final String ROAD_BUILDING_COST = "road_building_cost";
            public static final String GAME_TIME = "game_time";
        }
    }
}
