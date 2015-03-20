package com.assembler.augment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Matthew on 12/14/2014.
 */
public class SettingsActivity extends Activity{

    EditText threshold;
    EditText memoryThreshold;
    Spinner colorSelector;
    TextView settingsTitle;
    Button applySettings;
    LinearLayout colorVisualizer;
    String[] strings = new String[] {"Green", "Purple", "Blue", "Red", "Grey"};
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);

        threshold = (EditText) findViewById(R.id.CPUThresholdEditText);
        memoryThreshold = (EditText) findViewById(R.id.MemoryThresholdEditText);
        colorSelector = (Spinner) findViewById(R.id.ColorSpinner);
        settingsTitle = (TextView) findViewById(R.id.SettingsText);
        applySettings = (Button) findViewById(R.id.ApplySettings);
        colorVisualizer = (LinearLayout) findViewById(R.id.ColorVisualizer);
        prefs = getSharedPreferences("Prefs" ,MODE_PRIVATE);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strings);
        colorSelector.setAdapter(arrayAdapter);

        threshold.setText(prefs.getString("Threshold", "20") + "%");
        memoryThreshold.setText(prefs.getString("MemoryThreshold", "20") + "%");
        settingsTitle.setTextColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        colorVisualizer.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));

        sortColorList();

        colorSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString("SelectedColor", colorSelector.getSelectedItem().toString()).commit();
                settingsTitle.setTextColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
                colorVisualizer.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        applySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!threshold.getText().toString().equalsIgnoreCase(""))
                    prefs.edit().putString("Threshold", threshold.getText().toString().replace("%", "")).commit();

                if(!memoryThreshold.getText().toString().equalsIgnoreCase(""))
                    prefs.edit().putString("MemoryThreshold", memoryThreshold.getText().toString().replace("%", ""));

                prefs.edit().putString("SelectedColor", colorSelector.getSelectedItem().toString()).commit();
                onBackPressed();
            }
        });

        threshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!threshold.getText().toString().contains("%"))
                    threshold.setText(threshold.getText() + "%");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sortColorList(){
        if(strings[0] != prefs.getString("SelectedColor", "Green")){
            int i = 0;
            for(String string : strings){
                if(string.equalsIgnoreCase(prefs.getString("SelectedColor", "Green"))){
                    strings[i] = strings[0];
                    strings[0] = string;
                }
                i++;
            }
        }
    }
}
