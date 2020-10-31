package mad.citysimulator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mad.citysimulator.R;
import mad.citysimulator.interfaces.SettingClickListener;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String INITIAL_MONEY = "initialMoney";

    private int width;
    private int height;
    private int initialMoney;

    EditText widthInput;
    EditText heightInput;
    EditText initialMoneyInput;

    private SettingClickListener clickListener;

    // Empty constructor
    public SettingsFragment() {}


    public static SettingsFragment newInstance(int width, int height, int initialMoney) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(WIDTH, width);
        args.putInt(HEIGHT, height);
        args.putInt(INITIAL_MONEY, initialMoney);
        fragment.setArguments(args);
        return fragment;
    }

    public void setSettingClickListener(SettingClickListener listener) { this.clickListener = listener; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            width = getArguments().getInt(WIDTH);
            height = getArguments().getInt(HEIGHT);
            initialMoney = getArguments().getInt(INITIAL_MONEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Set values in view
        this.widthInput = view.findViewById(R.id.mapWidthInput);
        this.heightInput = view.findViewById(R.id.mapHeightInput);
        this.initialMoneyInput = view.findViewById(R.id.initialMoneyInput);
        this.widthInput.setText(Integer.toString(this.width));
        this.heightInput.setText(Integer.toString(this.height));
        this.initialMoneyInput.setText(Integer.toString(this.initialMoney));

        // Set onclick listener for save button
        Button saveBtn = getActivity().findViewById(R.id.saveSettingsBtn);
        saveBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        this.width = Integer.parseInt(this.widthInput.getText().toString());
        this.height = Integer.parseInt(this.heightInput.getText().toString());
        this.initialMoney = Integer.parseInt(this.initialMoneyInput.getText().toString());

        clickListener.onSaveSettingClick(this.width, this.height, this.initialMoney);
    }
}