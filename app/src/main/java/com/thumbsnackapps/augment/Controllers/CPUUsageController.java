package com.thumbsnackapps.augment.Controllers;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thumbsnackapps.augment.Activities.MainActivity;
import com.thumbsnackapps.augment.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Matthew on 3/22/2015.
 */
public class CPUUsageController {
    private static ArrayList<String> list = new ArrayList<>();
    private static Hashtable<Integer, String> hashtable = new Hashtable<>();
    private static Context c;
    private static LayoutInflater layoutInflater;

    public CPUUsageController(Context context, MainActivity main) {
        c = context;
        layoutInflater = (LayoutInflater)main.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<String> getAllDeviceProcessCPUUsage() {
        list = new ArrayList<>();

        try {
            Process p = Runtime.getRuntime().exec("top -m 500 -d 1 -n 5"); //-m = number of entries. -d = delay. -n = num of iterations.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }

            p.waitFor();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(c, "Caught Exception", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void parseCPUUsageArray(){
        for(String s : list){
            String[] ss = s.split(" ");
            for(String v : ss){
                if(v.contains("%") && ss[ss.length - 1].contains("com.")){
                    hashtable.put(Integer.parseInt(v.replace("%", "")), ss[ss.length - 1]);
                }
            }
        }
    }

    public void populateCPUUsageLayout(final LinearLayout ll) {
        ll.removeAllViews();
        for(Integer key : hashtable.keySet()) {
            LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.cpu_layout, null);
            ((TextView)layout.findViewById(R.id.cpuProcessName)).setText("Process name - " + hashtable.get(key));
            ((TextView)layout.findViewById(R.id.cpuProcessUsage)).setText("CPU Usage - " + key + "%");

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),((TextView)v.findViewById(R.id.cpuProcessName)).getText().toString().split("\\-")[1].trim() + " was terminated.", Toast.LENGTH_LONG).show();
                    ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(((TextView)v.findViewById(R.id.cpuProcessName)).getText().toString().split("\\-")[1].trim());
                    ll.removeView(v);
                }
            });

            ll.addView(layout);
        }
    }

    public void killProcessesGreaterThanThreshold(int threshold) {
        for(Integer key : hashtable.keySet()) {
            if(key > threshold) {
                ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(hashtable.get(key));
            }
        }
    }
}
