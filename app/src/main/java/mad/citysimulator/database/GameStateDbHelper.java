package mad.citysimulator.database;
import mad.citysimulator.database.GameStateSchema.GameStateTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameStateDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "game_state.db";

    public GameStateDbHelper(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery =
                "CREATE TABLE " + GameStateTable.NAME + "(" +
                GameStateTable.Cols.SAVE_NAME + " TEXT, " +
                GameStateTable.Cols.CITY_NAME + " TEXT, " +
                GameStateTable.Cols.MAP + " TEXT, " +
                GameStateTable.Cols.ROADS + " TEXT, " +
                GameStateTable.Cols.COMMERCIALS + " TEXT, " +
                GameStateTable.Cols.RESIDENTIALS + " TEXT, " +
                GameStateTable.Cols.MAP_HEIGHT + " INTEGER, " +
                GameStateTable.Cols.MAP_WIDTH + " INTEGER, " +
                GameStateTable.Cols.MONEY + " INTEGER, " +
                GameStateTable.Cols.INITIAL_MONEY + " INTEGER, " +
                GameStateTable.Cols.GAME_TIME + " INTEGER)";
                db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {}
}

/*              FULL SETTINGS
                GameStateTable.Cols.FAMILY_SIZE + " INTEGER, " +
                GameStateTable.Cols.SHOP_SIZE + " INTEGER, " +
                GameStateTable.Cols.SALARY + " INTEGER, " +
                GameStateTable.Cols.TAX_RATE + " REAL, " +
                GameStateTable.Cols.SERVICE_COST + " INTEGER, " +
                GameStateTable.Cols.HOUSE_BUILDING_COST + " INTEGER, " +
                GameStateTable.Cols.COMM_BUILDING_COST + " INTEGER, " +
                GameStateTable.Cols.ROAD_BUILDING_COST + " INTEGER, " +
 */