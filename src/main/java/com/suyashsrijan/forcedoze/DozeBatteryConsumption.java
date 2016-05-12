package com.suyashsrijan.forcedoze;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class DozeBatteryConsumption extends AppCompatActivity {
    ArrayList<BatteryConsumptionItem> batteryConsumptionItems;
    Set<String> dozeUsageStats;
    ListView listView;
    BatteryConsumptionAdapter batteryConsumptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doze_battery_consumption);
        listView = (ListView) findViewById(R.id.listView);
        batteryConsumptionItems = new ArrayList<>();
        dozeUsageStats = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet("dozeUsageData", new LinkedHashSet<String>());
        if (dozeUsageStats.size() > 150) {
            if (Utils.isMyServiceRunning(ForceDozeService.class, this)) {
                stopService(new Intent(this, ForceDozeService.class));
            }
            dozeUsageStats.clear();
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putStringSet("dozeUsageData", new LinkedHashSet<String>()).apply();
            if (!Utils.isMyServiceRunning(ForceDozeService.class, this)) {
                startService(new Intent(this, ForceDozeService.class));
            }
        }
        if (!dozeUsageStats.isEmpty()) {
            ArrayList<String> sortedList = new ArrayList(new TreeSet(dozeUsageStats));
            for (int i = 0; i < dozeUsageStats.size(); i++) {
                BatteryConsumptionItem item = new BatteryConsumptionItem();
                item.setTimestampPercCombo(sortedList.get(i));
                batteryConsumptionItems.add(item);
            }
        }
        batteryConsumptionAdapter = new BatteryConsumptionAdapter(this, batteryConsumptionItems);
        listView.setAdapter(batteryConsumptionAdapter);
    }
}
