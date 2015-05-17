package com.thumbsnackapps.augment.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.thumbsnackapps.augment.R;

/**
 * Created by Matthew on 12/14/2014.
 */
public class SettingsActivity extends Activity{

    EditText cpuThreshold;
    EditText memoryThreshold;
    Button applySettings;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle bundle){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);

        prefs                                   = getSharedPreferences("Prefs" ,MODE_PRIVATE);
        applySettings                           = (Button) findViewById(R.id.ApplySettings);
        cpuThreshold                            = (EditText) findViewById(R.id.CPUThresholdEditText);
        memoryThreshold                         = (EditText) findViewById(R.id.MemoryThresholdEditText);

        cpuThreshold.setText(prefs.getInt("Threshold", 20) + "%");
        memoryThreshold.setText(prefs.getInt("MemoryThreshold", 20) + "%");

        applySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cpuThreshold.getText().toString().equalsIgnoreCase(""))
                    prefs.edit().putInt("Threshold", Integer.parseInt(cpuThreshold.getText().toString().replace("%", ""))).apply();

                if (!memoryThreshold.getText().toString().equalsIgnoreCase(""))
                    prefs.edit().putInt("MemoryThreshold", Integer.parseInt(memoryThreshold.getText().toString().replace("%", ""))).apply();

                onBackPressed();
            }
        });
    }
}
