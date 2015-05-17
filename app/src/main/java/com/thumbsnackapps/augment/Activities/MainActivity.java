package com.thumbsnackapps.augment.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thumbsnackapps.augment.Controllers.CPUUsageController;
import com.thumbsnackapps.augment.Controllers.MemoryUsageController;
import com.thumbsnackapps.augment.R;

public class MainActivity extends ActionBarActivity {
    Button augmentCpuButton;
    Button augmentMemoryButton;
    ImageView settingsButton;
    LinearLayout containerLayout;

    private static SharedPreferences prefs;
    private static CPUUsageController cpuController;
    private static MemoryUsageController memoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs                                       = getSharedPreferences("Prefs" ,MODE_PRIVATE);
        cpuController                               = new CPUUsageController(getApplicationContext(), MainActivity.this);
        memoryController                            = new MemoryUsageController(getApplicationContext(), MainActivity.this);

        augmentCpuButton                            = (Button) findViewById(R.id.optimizeCPU);
        augmentMemoryButton                         = (Button) findViewById(R.id.optimizeMemory);
        settingsButton                              = (ImageView) findViewById(R.id.settingsButton);
        containerLayout                             = (LinearLayout) findViewById(R.id.cpuUtilizationContainer);

        augmentCpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpuController.getAllDeviceProcessCPUUsage();
                cpuController.parseCPUUsageArray();
                cpuController.populateCPUUsageLayout(containerLayout);
                cpuController.killProcessesGreaterThanThreshold(prefs.getInt("Threshold", 20));
            }
        });

        augmentMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryController.getAllDeviceProcessMemoryUsage();
                memoryController.parseMemoryUsageArray();
                memoryController.populateMemoryUsageLayout(containerLayout);
                memoryController.killProcessesGreaterThanThreshold(prefs.getInt("MemoryThreshold", 10));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 0);
            }
        });
    }
}
