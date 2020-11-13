package mad.citysimulator.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;

import mad.citysimulator.database.DBSchema.GameStateTable;
import mad.citysimulator.models.GameState;
import mad.citysimulator.models.ImageHolder;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Settings;
import mad.citysimulator.models.Structure;

public class DBCursor extends CursorWrapper {

    public DBCursor(Cursor cursor) { super(cursor); }

    public ImageHolder getImageHolder() {
        int row = getInt(getColumnIndex(DBSchema.BitmapImageTable.Cols.ROW_NUM));
        int col = getInt(getColumnIndex(DBSchema.BitmapImageTable.Cols.COL_NUM));
        byte[] image = getBlob(getColumnIndex(DBSchema.BitmapImageTable.Cols.IMAGE_BLOB));
        System.out.println(image);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        return new ImageHolder(row, col, imageBitmap);
    }

    public GameState getGameState() {
        String saveName = getString(getColumnIndex(GameStateTable.Cols.SAVE_NAME));
        String cityName = getString(getColumnIndex(GameStateTable.Cols.CITY_NAME));
        String mapJson = getString(getColumnIndex(GameStateTable.Cols.MAP));
        String roadJson = getString(getColumnIndex(GameStateTable.Cols.STRUCTURES));

        int mapWidth = getInt(getColumnIndex(GameStateTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(GameStateTable.Cols.MAP_HEIGHT));
        int money = getInt(getColumnIndex(GameStateTable.Cols.MONEY));
        int initialMoney = getInt(getColumnIndex(GameStateTable.Cols.INITIAL_MONEY));
        int gameTime = getInt(getColumnIndex(GameStateTable.Cols.GAME_TIME));

        Settings settings = new Settings(saveName, cityName, mapWidth, mapHeight, initialMoney);

        MapElement[][] map = convertJsonToMap(mapJson);
        ArrayList<Structure> structures = convertJsonToStructures(roadJson);


        return new GameState(settings, money, gameTime, map, structures);
    }

    private MapElement[][] convertJsonToMap(String mapJson) {
        Gson g = new Gson();
        MapElement[][] newMap = g.fromJson(mapJson, MapElement[][].class);
        return newMap;
    }

    private ArrayList<Structure> convertJsonToStructures(String structureJson) {
        ArrayList<Structure> structures = new ArrayList<>();
        Gson g = new Gson();
        Structure[] roadArray = g.fromJson(structureJson, Structure[].class);
        if(roadArray != null) {
            structures.addAll(Arrays.asList(roadArray));
        }
        return structures;
    }
}

/*         int familySize = getInt(getColumnIndex(GameStateTable.Cols.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(GameStateTable.Cols.SHOP_SIZE));
        int salary = getInt(getColumnIndex(GameStateTable.Cols.SALARY));
        double taxRate = getDouble(getColumnIndex(GameStateTable.Cols.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(GameStateTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.HOUSE_BUILDING_COST));
        int commBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.COMM_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.ROAD_BUILDING_COST));

 */