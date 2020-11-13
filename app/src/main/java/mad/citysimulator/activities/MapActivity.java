package mad.citysimulator.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import mad.citysimulator.R;
import mad.citysimulator.fragments.MapFragment;
import mad.citysimulator.fragments.SelectorFragment;
import mad.citysimulator.fragments.StatusFragment;
import mad.citysimulator.interfaces.DetailsClickListener;
import mad.citysimulator.interfaces.MapClickListener;
import mad.citysimulator.models.GameData;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, MapClickListener,
        DetailsClickListener
{

    StatusFragment statusFrag;
    Double currentTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        currentTemp = 0.0;
        // Set activity as onclick listener for run day btn
        Button incTimeBtn = findViewById(R.id.incTimeBtn);
        incTimeBtn.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        initStatusFragment(fm);

        MapFragment mapFrag = initMapFragment(fm);
        mapFrag.addMapClickListener(this);
        mapFrag.addDetailsClickListener(this);

        SelectorFragment selectorFrag = initSelectorFragment(fm);

        mapFrag.setBuilderSelector(selectorFrag);
        selectorFrag.setMapFragment(mapFrag);

        // Make API Weather call
        //  reloads temperature on status fragment every 30 seconds
        Timer timer = new Timer();
        TimerTask getTemp = new TimerTask() {
            @Override
            public void run() {
                new GetCurrentWeather(getParent()).execute(GameData.get().getCityName());
            }
        };
        timer.scheduleAtFixedRate(getTemp, 0l, (30 * 1000));
    }

    private void initStatusFragment(FragmentManager fm) {
        statusFrag = (StatusFragment) fm.findFragmentById(R.id.statusFragment);
        if(statusFrag == null) {
            int gameTime = GameData.get().getGameTime();
            int money = GameData.get().getMoney();
            int income = GameData.get().getRecentIncome();
            int population = GameData.get().getPopulation();
            double employmentRate = GameData.get().getEmploymentRate();
            String cityName = GameData.get().getCityName();

            statusFrag = StatusFragment.newInstance(gameTime, money, income, population,
                    employmentRate, cityName, currentTemp);
            fm.beginTransaction().add(R.id.statusFragmentPane, statusFrag).commit();
        }
    }

    private MapFragment initMapFragment(FragmentManager fm) {
        MapFragment mapFrag = (MapFragment) fm.findFragmentById(R.id.mapRecyclerView);
        if(mapFrag == null) {
            mapFrag = new MapFragment();
            fm.beginTransaction().add(R.id.mapFragmentPane, mapFrag).commit();
        }
        return mapFrag;
    }

    private SelectorFragment initSelectorFragment(FragmentManager fm) {
        SelectorFragment selectorFrag = (SelectorFragment) fm.findFragmentById(R.id.selectorRecyclerView);
        if(selectorFrag == null) {
            selectorFrag = new SelectorFragment();
            fm.beginTransaction().add(R.id.selectorFragmentPane, selectorFrag).commit();
        }
        return selectorFrag;
    }

    @Override
    public void onClick(View view) {
        GameData.get().incGameTime();
        FragmentManager fm = getSupportFragmentManager();
        initStatusFragment(fm);
    }

    @Override
    public void onBuild() {
        FragmentManager fm = getSupportFragmentManager();
        initStatusFragment(fm);
    }

    @Override
    public void onDetailsClick(int row, int col) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("row", row);
        intent.putExtra("col", col);
        startActivity(intent);
    }

    private class GetCurrentWeather extends AsyncTask<String,Void,Double> {

        // Context for error modal
        private Context context;
        private String error;

        // API Call Constants
        private static final String API_KEY = "48803a2307760152462669e50e408dc8";
        private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
        private static final String MEASUREMENT_UNITS = "metric";

        public GetCurrentWeather(Context context) {
            this.context = context;
            this.error = "";
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Double doInBackground(String... cities) {
            double temperature = 0;
            for(String cityName : cities) {
                String fullUrl = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter("q", cityName)
                        .appendQueryParameter("appid", API_KEY)
                        .appendQueryParameter("units", MEASUREMENT_UNITS)
                        .build().toString();

                URL url = null;
                try {
                    url = new URL(fullUrl);
                } catch (MalformedURLException e) {
                    error = "MALFORMED URL EXCEPTION:\n" + e;
                }

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new Exception("Something went wrong trying to get the current weather info for city: " + cityName);
                    }
                    else {
                        String responseData = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
                        JsonObject fullJsonObj = new JsonParser().parse(responseData).getAsJsonObject();
                        JsonObject mainData = new JsonParser().parse(fullJsonObj.get("main").toString()).getAsJsonObject();
                        temperature = Double.parseDouble(mainData.get("temp").toString());
                    }
                } catch (IOException e) {
                   error = "UNABLE TO OPEN WEATHER API CONNECTION:\n" + e;
                } catch (Exception e) {
                    error = e.getMessage();
                } finally {
                    if(conn != null) { conn.disconnect(); }
                }
            }
            return temperature;
        }

        @Override
        protected void onPostExecute(Double temperature) {
            if(error != "") {
                apiErrorModal(error);
            }
            else if(statusFrag != null) {
                statusFrag.setTemperature(temperature);
                currentTemp = temperature;
            }
        }

        public void apiErrorModal(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                    R.style.AlertDialogCustom));
            builder.setTitle("API Weather Error!")
                    .setMessage(message)
                    .setPositiveButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}