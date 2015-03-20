package com.assembler.augment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matthew on 12/11/2014.
 */
class Utils {
    private static ArrayList<String> mList = new ArrayList<String>();
    private static int usageThreshold;
    private static int memoryThreshold;
    private static double availMemory;
    private static double usedMemory;
    private static double osMemoryUsage;
    private static MainActivity mainActivity;
    private static LayoutInflater layoutInflater;

    public static void getAllRunningApplicationMemoryUsage(LinearLayout linearLayout, Context context) {
        linearLayout.removeAllViews();
        ArrayList<String> al = new ArrayList<>();
        mList = new ArrayList<>();
        ActivityManager mgr = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();
        Log.e("DEBUG", "Running processes:");

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mgr.getMemoryInfo(memoryInfo);

        availMemory = memoryInfo.totalMem / 1024; // / 1024; //Return is in KB, easier to work with in MB

        for(Iterator i = processes.iterator(); i.hasNext();){
            ActivityManager.RunningAppProcessInfo p = (ActivityManager.RunningAppProcessInfo)i.next();
            int[] pids = new int[1];
            pids[0] = p.pid;
            android.os.Debug.MemoryInfo[] MI = mgr.getProcessMemoryInfo(pids);
            if(!p.processName.contains("com.assembler.daisy")) {

                LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.memory_layout, null);
                ((TextView)layout.findViewById(R.id.processName)).setText("Process name - " + p.processName);
                ((TextView)layout.findViewById(R.id.privateMemory)).setText("Total Private Memory: " + MI[0].getTotalPrivateDirty() / 1024 + "(Mb)");
                ((TextView)layout.findViewById(R.id.privateShared)).setText("Total Shared Memory: " + MI[0].getTotalSharedDirty() / 1024 + "(Mb)");
                ((TextView)layout.findViewById(R.id.setSize)).setText("Proportional Set Size: " + MI[0].getTotalPss() / 1024 + "(Mb)");
                ((TextView)layout.findViewById(R.id.totalMemoryUsagePercent)).setText("Total Usage Percent: " + new DecimalFormat("#.##").format(((availMemory / 1024) - ((availMemory / 1024) - (MI[0].getTotalPrivateDirty() / 1024))) / (availMemory / 1024) * 100) + "%");

                linearLayout.addView(layout);

                al.add(p.processName + " " + Double.toString(MI[0].getTotalPrivateDirty()) + " " + p.pid);
                usedMemory += MI[0].getTotalPrivateDirty();
            }
        }
        processMemoryUsageStats(context, al);
        Toast.makeText(context, "Updated Memory", Toast.LENGTH_SHORT).show();
    }

    public static void getAllRunningApplicationsCPUUsage(LinearLayout linearLayout, Context context) {
        linearLayout.removeAllViews();
        ArrayList<String> list = new ArrayList<String>();
        try {
            Process p = Runtime.getRuntime().exec("top -m 500 -d 1 -n 1"); //-m = number of entries. -d = delay. -n = num of iterations.
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                list.add(line);
                line = reader.readLine();
                i++;
            }

            mList = list;
            processCPUUsageStats(linearLayout, context);

            p.waitFor();
            Toast.makeText(context, "Updated Processes. Threshold: " + Integer.toString(usageThreshold) + "%", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Caught Exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void processCPUUsageStats(LinearLayout linearLayout, Context context){
        for(String string : mList){
            String[] splitString = string.split(" ");
            for(String secondString : splitString){
                if(secondString.contains("%") && splitString[splitString.length - 1].contains("com.")){
                    secondString = secondString.replace("%", "");
                    LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.cpu_layout, null);
                    ((TextView)layout.findViewById(R.id.cpuProcessName)).setText("Process name - " + splitString[splitString.length - 1]);
                    ((TextView)layout.findViewById(R.id.cpuProcessUsage)).setText("CPU Usage - " + secondString + "%");
                    linearLayout.addView(layout);
                    if(!secondString.contains("CPU") && Integer.parseInt(secondString) > usageThreshold){
                        android.os.Process.killProcess(android.os.Process.getUidForName(splitString[splitString.length - 1]));
                        Toast.makeText(context, "Process: " + splitString[splitString.length - 1] + " broke the " + Integer.toString(usageThreshold) + "% threshold. Application Terminated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public static void processMemoryUsageStats(Context context, ArrayList al){
        for(int i = 0; i < al.size(); i++){
            String[] strings = al.get(i).toString().split(" ");
            if((Double.parseDouble(strings[1]) / 1024)/*KB to MB*/ > ((availMemory / 1024) /*KB to MB*/* ((float)memoryThreshold / (float)100))){
                android.os.Process.killProcess(android.os.Process.getUidForName(strings[0]));
                Toast.makeText(context, "Process: " + strings[0] + " broke the " + Integer.toString(memoryThreshold) + "% threshold. Application Terminated.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void setMainActivity(MainActivity main){
        mainActivity = main;
        layoutInflater = (LayoutInflater)mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public static void setUsageThreshold(int threshold){
        usageThreshold = threshold;
    }

    public static void setMemoryThreshold(int threshold) {memoryThreshold = threshold;}

    public static String getHexForColor(String color){
        if(color.equalsIgnoreCase("Red"))
            return "#c0392b";

        if(color.equalsIgnoreCase("Purple"))
            return "#8e44ad";

        if(color.equalsIgnoreCase("Green"))
            return "#27ae60";

        if(color.equalsIgnoreCase("Grey"))
            return "#95a5a6";

        if(color.equalsIgnoreCase("Blue"))
            return "#2c3e50";

        return "#27ae60";
    }
}