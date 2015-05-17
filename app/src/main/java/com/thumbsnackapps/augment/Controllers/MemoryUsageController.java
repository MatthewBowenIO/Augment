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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matthew on 3/22/2015.
 */
public class MemoryUsageController {
    private static ArrayList<String> list = new ArrayList<>();
    private static Hashtable<Integer, String> hashtable = new Hashtable<>();
    private static Context c;
    private static LayoutInflater layoutInflater;

    public MemoryUsageController(Context context, MainActivity main) {
        c = context;
        layoutInflater = (LayoutInflater)main.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<String> getAllDeviceProcessMemoryUsage() {
        ActivityManager mgr = (ActivityManager)c.getSystemService(c.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mgr.getMemoryInfo(memoryInfo);

        float availMemory = memoryInfo.totalMem / 1024;

        for(Iterator i = processes.iterator(); i.hasNext();){
            ActivityManager.RunningAppProcessInfo p = (ActivityManager.RunningAppProcessInfo)i.next();
            int[] pids = new int[1];
            pids[0] = p.pid;
            android.os.Debug.MemoryInfo[] MI = mgr.getProcessMemoryInfo(pids);
            if(!p.processName.contains("com.thumbsnackapps.augment")) {
                list.add("Process name - " + p.processName + "|" + "Total Private Memory - " + MI[0].getTotalPrivateDirty() / 1024 + "(Mb)" + "|" + "Total Shared Memory: " + MI[0].getTotalSharedDirty() / 1024 + "(Mb)" + "|" + "Proportional Set Size: " + MI[0].getTotalPss() / 1024 + "(Mb)" + "|" + "Total Usage Percent: " + new DecimalFormat("#.##").format(((availMemory / 1024) - ((availMemory / 1024) - (MI[0].getTotalPrivateDirty() / 1024))) / (availMemory / 1024) * 100) + "%" + "|" + "ProcessID :" + p.pid);
            }
        }

        return list;
    }

    public void parseMemoryUsageArray() {
        for(String s : list) {
            String[] ss = s.split("\\|");
            hashtable.put(Integer.parseInt(ss[5].split(":")[1].trim()), ss[0].split("\\-")[1].trim());
        }
    }

    public void populateMemoryUsageLayout(final LinearLayout ll) {
        ll.removeAllViews();

        for(String s : list) {
            String[] ss = s.split("\\|");
            LinearLayout layout = (LinearLayout)layoutInflater.inflate(R.layout.memory_layout, null);
            ((TextView)layout.findViewById(R.id.processName)).setText(ss[0]);
            ((TextView)layout.findViewById(R.id.privateMemory)).setText(ss[1]);
            ((TextView)layout.findViewById(R.id.privateShared)).setText(ss[2]);
            ((TextView)layout.findViewById(R.id.setSize)).setText(ss[3]);
            ((TextView)layout.findViewById(R.id.totalMemoryUsagePercent)).setText(ss[4]);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),((TextView) v.findViewById(R.id.processName)).getText().toString().split("\\-")[1].trim() + " was terminated.", Toast.LENGTH_LONG).show();
                    ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(((TextView) v.findViewById(R.id.processName)).getText().toString().split("\\-")[1].trim());
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
