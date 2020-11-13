package mad.citysimulator.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import java.util.List;

import mad.citysimulator.R;
import mad.citysimulator.database.DBManager;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.GameState;
import mad.citysimulator.models.ImageHolder;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    DBManager dbManager;
    GameState storedState;
    List<ImageHolder> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        Button mapBtn = findViewById(R.id.resumeBtn);
        Button settingsBtn = findViewById(R.id.settingBtn);
        mapBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);

        // Load DB Manager
        dbManager = new DBManager();
        dbManager.load(getApplicationContext());

        // Attempt to get stored game state and images
        GameData.get().setDbManager(dbManager);
        storedState = dbManager.getSavedState("DEFAULT");
        images = dbManager.getAllImages();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.resumeBtn:
                if(storedState != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                            R.style.AlertDialogCustom));
                    builder.setTitle("Resume!")
                            .setMessage("Would you like to resume or start a new game with default settings?")
                            .setPositiveButton("NEW GAME",  new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    Dialog dialog = (Dialog) dialogInterface;
                                    newGame();
                                    // Launch map activity
                                    Intent intent = new Intent(dialog.getContext(), MapActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNeutralButton("RESUME", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    Dialog dialog = (Dialog) dialogInterface;
                                    resumeGame();
                                    // Launch map activity
                                    Intent intent = new Intent(dialog.getContext(), MapActivity.class);
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    newGame();
                    intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.settingBtn:
                if(storedState != null && storedState.getGameTime() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                            R.style.AlertDialogCustom));
                    builder.setTitle("Woah! Chill out buddy...")
                            .setMessage("It seems you have already started a game. To edit settings " +
                                    "you must start a new game.")
                            .setPositiveButton("NEW GAME",  new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    Dialog dialog = (Dialog) dialogInterface;
                                    newGame();
                                    Intent intent = new Intent(dialog.getContext(), SettingsActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNeutralButton("NAH, I'M GOOD", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    resumeGame();
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void resumeGame() {
        // If no previous save, make new game
        if(storedState == null) {
            newGame();
        }
        // Else load the stored state
        else {
            // Set up game data with both state and images
            GameData.get().setGameState(storedState);
            GameData.get().setImages(images);
        }
    }

    private void newGame() {
        // Check if save actually exists before trying to delete
        if(storedState != null) {
            dbManager.removeGameState(storedState);
        }
        // Remove all images stored
        dbManager.removeAllImages();
        // Create a new game state and save it to the db
        GameState newState = new GameState();
        dbManager.addGameState(newState);
        GameData.get().setGameState(newState);
    }
}