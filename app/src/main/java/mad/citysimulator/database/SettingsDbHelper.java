package mad.citysimulator.database;
import mad.citysimulator.database.SettingsSchema.SettingsTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "settings.db";

    public SettingsDbHelper(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery =
                "CREATE TABLE " + SettingsTable.NAME + "(" +
                SettingsTable.Cols.SAVE_NAME + " TEXT, " +
                SettingsTable.Cols.MAP_HEIGHT + " INTEGER, " +
                SettingsTable.Cols.MAP_WIDTH + " INTEGER, " +
                SettingsTable.Cols.MONEY + " INTEGER, " +
                SettingsTable.Cols.INITIAL_MONEY + " INTEGER, " +
                SettingsTable.Cols.FAMILY_SIZE + " INTEGER, " +
                SettingsTable.Cols.SHOP_SIZE + " INTEGER, " +
                SettingsTable.Cols.SALARY + " INTEGER, " +
                SettingsTable.Cols.TAX_RATE + " REAL, " +
                SettingsTable.Cols.SERVICE_COST + " INTEGER, " +
                SettingsTable.Cols.HOUSE_BUILDING_COST + " INTEGER, " +
                SettingsTable.Cols.COMM_BUILDING_COST + " INTEGER, " +
                SettingsTable.Cols.ROAD_BUILDING_COST + " INTEGER, " +
                SettingsTable.Cols.GAME_TIME + " INTEGER)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {}
}
