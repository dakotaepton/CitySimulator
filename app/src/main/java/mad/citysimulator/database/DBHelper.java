package mad.citysimulator.database;
import mad.citysimulator.database.DBSchema.BitmapImageTable;
import mad.citysimulator.database.DBSchema.GameStateTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "game_state.db";

    public DBHelper(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Game State table
        String createStateQuery =
                "CREATE TABLE " + GameStateTable.NAME + "(" +
                GameStateTable.Cols.SAVE_NAME + " TEXT, " +
                GameStateTable.Cols.CITY_NAME + " TEXT, " +
                GameStateTable.Cols.MAP + " TEXT, " +
                GameStateTable.Cols.STRUCTURES + " TEXT, " +
                GameStateTable.Cols.MAP_HEIGHT + " INTEGER, " +
                GameStateTable.Cols.MAP_WIDTH + " INTEGER, " +
                GameStateTable.Cols.MONEY + " INTEGER, " +
                GameStateTable.Cols.INITIAL_MONEY + " INTEGER, " +
                GameStateTable.Cols.GAME_TIME + " INTEGER)";
        db.execSQL(createStateQuery);

        // Create Stored Image Table
        String createImageQuery =
                "CREATE TABLE " + BitmapImageTable.NAME + "(" +
                        BitmapImageTable.Cols.ROW_NUM + " INTEGER, " +
                        BitmapImageTable.Cols.COL_NUM + " INTEGER, " +
                        BitmapImageTable.Cols.IMAGE_BLOB + " BLOB)";
        db.execSQL(createImageQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {}
}