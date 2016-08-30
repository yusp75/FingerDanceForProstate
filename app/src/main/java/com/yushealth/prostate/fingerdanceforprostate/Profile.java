package com.yushealth.prostate.fingerdanceforprostate;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Profile {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Profile(Context context) {

        sharedPreferences = context.getSharedPreferences("MyProfile", Context.MODE_PRIVATE);
        // 是否有初始化数据
        if (!sharedPreferences.contains("initialized")) {
            editor = sharedPreferences.edit();
            editor.putBoolean("initialized", true);
            // 时钟
            editor.putString("clock_is_on", "true");
            editor.putString("clock_1", "6:30");
            editor.putString("clock_2", "11:30");
            editor.putString("clock_3", "19:30");

            // 呼吸间隔
            editor.putInt("level", 0);  // 0: 新手

            editor.putInt("new_inhale", 3);
            editor.putInt("new_hold", 2);
            editor.putInt("new_exhale", 3);
            editor.putInt("expert_inhale", 5);
            editor.putInt("expert_hold", 3);
            editor.putInt("expert_exhale", 5);

            // 背景音乐
            editor.putString("music_on", "1");
            editor.putString("music_file", "");  // 音乐文件


            editor.apply();
        }

    }


    // 闹钟提醒
    public HashMap<String, String> readClockData() {

        HashMap<String, String> ClockData = new HashMap<>();

        ClockData.put("clock_is_on", sharedPreferences.getString("clock_is_on", "true"));
        ClockData.put("clock_1", sharedPreferences.getString("clock_1", "6:30"));
        ClockData.put("clock_2", sharedPreferences.getString("clock_2", "11:30"));
        ClockData.put("clock_3", sharedPreferences.getString("clock_3", "19:30"));

        return ClockData;
    }

    public void writeClockData(String[] data) {
        if (data.length < 4) return;

        editor = sharedPreferences.edit();

        editor.putString("clock_is_on", data[0]);
        editor.putString("clock_1", data[1]);
        editor.putString("clock_2", data[2]);
        editor.putString("clock_3", data[3]);

        editor.apply();
    }


    // 呼吸间隔
    public HashMap<String, Integer> readBreathData() {

        HashMap<String, Integer> BreathData = new HashMap<>();

        BreathData.put("level", sharedPreferences.getInt("level", 0));

        BreathData.put("new_inhale", sharedPreferences.getInt("new_inhale", 3));
        BreathData.put("new_hold", sharedPreferences.getInt("new_hold", 2));
        BreathData.put("new_exhale", sharedPreferences.getInt("new_exhale", 3));

        BreathData.put("expert_inhale", sharedPreferences.getInt("expert_inhale", 5));
        BreathData.put("expert_hold", sharedPreferences.getInt("expert_hold", 3));
        BreathData.put("expert_exhale", sharedPreferences.getInt("expert_exhale", 5));

        return BreathData;
    }

    public void writeBreathData(int[] data) {

        if (data.length < 7) return;

        editor = sharedPreferences.edit();

        editor.putInt("level", data[0]);

        editor.putInt("new_inhale", data[1]);
        editor.putInt("new_hold", data[2]);
        editor.putInt("new_exhale", data[3]);

        editor.putInt("expert_inhale", data[4]);
        editor.putInt("expert_hold", data[5]);
        editor.putInt("expert_exhale", data[6]);

        editor.apply();

    }

    // 背景音乐
    public HashMap<String, String> readBackgroundMusicData() {
        HashMap<String, String> MusicData = new HashMap<>();
        MusicData.put("music_on", sharedPreferences.getString("music_on", "true"));
        MusicData.put("music_file", sharedPreferences.getString("music_file", ""));

        return MusicData;
    }

    public void writeBackgroundMusicData(String[] data) {

        if (data.length < 2) return;

        editor = sharedPreferences.edit();

        editor.putString("music_on", data[0]);
        editor.putString("music_file", data[1]);

        editor.apply();
    }

    // end
}
