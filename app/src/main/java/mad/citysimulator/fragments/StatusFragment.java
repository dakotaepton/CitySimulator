package mad.citysimulator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mad.citysimulator.R;

public class StatusFragment extends Fragment {


    private static final String GAME_TIME = "gameTime";
    private static final String MONEY = "money";
    private static final String RECENT_INCOME = "recentIncome";
    private static final String POPULATION = "population";
    private static final String CITY_NAME = "cityName";
    private static final String TEMPERATURE = "temperature";
    private static final String EMPLOYMENT = "employment";

    private int gameTime;
    private int money;
    private int recentIncome;
    private int population;
    private String cityName;
    private double temperature;
    private double employment;

    // Required empty public constructor
    public StatusFragment() {}

    public static StatusFragment newInstance(int gameTime, int money, int recentIncome, int population,
                                             double employment, double temperature, String cityName) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_TIME, gameTime);
        args.putInt(MONEY, money);
        args.putInt(RECENT_INCOME, recentIncome);
        args.putInt(POPULATION, population);
        args.putString(CITY_NAME, cityName);
        args.putDouble(TEMPERATURE, temperature);
        args.putDouble(EMPLOYMENT, employment);
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
            temperature = getArguments().getDouble(TEMPERATURE);
            employment = getArguments().getDouble(EMPLOYMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        TextView timeValue = v.findViewById(R.id.timeValue);
        timeValue.setText(Integer.toString(gameTime));
        TextView moneyValue = v.findViewById(R.id.moneyValue);
        moneyValue.setText(Integer.toString(money));
        TextView incomeValue = v.findViewById(R.id.incomeValue);
        incomeValue.setText(Integer.toString(recentIncome));
        TextView populationValue = v.findViewById(R.id.populationValue);
        populationValue.setText(Integer.toString(population));
        TextView cityNameValue = v.findViewById(R.id.cityNameValue);
        cityNameValue.setText(cityName);
        TextView temperatureValue = v.findViewById(R.id.temperatureValue);
        temperatureValue.setText(Double.toString(temperature));
        TextView employmentValue = v.findViewById(R.id.employmentValue);
        employmentValue.setText(Double.toString(employment));

        return v;
    }
}