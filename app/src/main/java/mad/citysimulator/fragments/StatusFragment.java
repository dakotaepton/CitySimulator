package mad.citysimulator.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Text;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import mad.citysimulator.R;

public class StatusFragment extends Fragment {

    private static final String GAME_TIME = "gameTime";
    private static final String MONEY = "money";
    private static final String RECENT_INCOME = "recentIncome";
    private static final String POPULATION = "population";
    private static final String CITY_NAME = "cityName";
    private static final String EMPLOYMENT = "employment";
    private static final String TEMPERATURE = "temperature";

    private int gameTime;
    private int money;
    private int recentIncome;
    private int population;
    private String cityName;
    private double employment;
    private double temperature;

    TextView temperatureValue;

    // Required empty public constructor
    public StatusFragment() {}

    public static StatusFragment newInstance(int gameTime, int money, int recentIncome, int population,
                                             double employment, String cityName, double temperature) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_TIME, gameTime);
        args.putInt(MONEY, money);
        args.putInt(RECENT_INCOME, recentIncome);
        args.putInt(POPULATION, population);
        args.putString(CITY_NAME, cityName);
        args.putDouble(EMPLOYMENT, employment);
        args.putDouble(TEMPERATURE, temperature);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameTime = getArguments().getInt(GAME_TIME);
            money = getArguments().getInt(MONEY);
            recentIncome = getArguments().getInt(RECENT_INCOME);
            population = getArguments().getInt(POPULATION);
            cityName = getArguments().getString(CITY_NAME);
            employment = getArguments().getDouble(EMPLOYMENT);
            temperature = getArguments().getDouble(TEMPERATURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status, container, false);

        // Get view elements
        TextView timeValue = v.findViewById(R.id.timeValue);
        TextView moneyValue = v.findViewById(R.id.moneyValue);
        TextView incomeValue = v.findViewById(R.id.incomeValue);
        TextView populationValue = v.findViewById(R.id.populationValue);
        TextView cityNameValue = v.findViewById(R.id.cityNameValue);
        TextView employmentValue = v.findViewById(R.id.employmentValue);
        temperatureValue = v.findViewById(R.id.temperatureValue);

        // Fill view elements
        employmentValue.setText(Double.toString(employment) + "%");
        timeValue.setText(Integer.toString(gameTime));
        moneyValue.setText("$" + Integer.toString(money));
        incomeValue.setText("$" + Integer.toString(recentIncome));
        populationValue.setText(Integer.toString(population));
        cityNameValue.setText(cityName);
        setTemperature(temperature);

        return v;
    }

    public void setTemperature(double temperature) {
        if(temperatureValue != null) {
            temperatureValue.setText(Double.toString(temperature) + "\u2103");
        }
    }

}