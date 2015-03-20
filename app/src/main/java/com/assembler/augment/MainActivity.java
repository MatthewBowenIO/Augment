package com.assembler.augment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Button optimizeCPU;
    Button optimizeMemory;
    TextView mainActivityTitle;
    LinearLayout cpuUsageListView;
    ImageView settingsButton;

    Utils mUtils = new Utils();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUtils.setMainActivity(MainActivity.this);

        prefs = getSharedPreferences("Prefs" ,MODE_PRIVATE);

        optimizeCPU = (Button) findViewById(R.id.optimizeCPU);
        optimizeMemory = (Button) findViewById(R.id.optimizeMemory);
        cpuUsageListView = (LinearLayout) findViewById(R.id.cpuUtilizationContainer);
        settingsButton = (ImageView) findViewById(R.id.settingsButton);
        mainActivityTitle = (TextView) findViewById(R.id.MainActivityText);

        if(prefs.getString("Threshold", "20").contains("%"))
            prefs.edit().putString("Threshold", prefs.getString("Threshold", "20").replace("%", "")).commit();

        if(prefs.getString("MemoryThreshold", "10").contains("%"))
            prefs.edit().putString("MemoryThreshold", prefs.getString("MemoryThreshold", "10").replace("%", ""));

        mUtils.setUsageThreshold(Integer.parseInt(prefs.getString("Threshold", "20"))); //Initializing default value.
        mUtils.setMemoryThreshold(Integer.parseInt(prefs.getString("MemoryThreshold", "10")));

        mainActivityTitle.setTextColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        optimizeCPU.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        optimizeMemory.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        cpuUsageListView.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));

        optimizeCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.getAllRunningApplicationsCPUUsage(cpuUsageListView, getApplicationContext());
            }
        });

        optimizeMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtils.getAllRunningApplicationMemoryUsage(cpuUsageListView, getApplicationContext());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent){
        mUtils.getAllRunningApplicationsCPUUsage(cpuUsageListView, getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();

        mUtils.setUsageThreshold(Integer.parseInt(prefs.getString("Threshold", "20")));
        mUtils.setMemoryThreshold(Integer.parseInt(prefs.getString("MemoryThreshold", "10")));
        mainActivityTitle.setTextColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        optimizeCPU.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        optimizeMemory.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
        cpuUsageListView.setBackgroundColor(Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
    }
}
