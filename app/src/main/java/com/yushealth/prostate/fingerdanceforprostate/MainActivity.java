package com.yushealth.prostate.fingerdanceforprostate;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    // 请求码
    private final int REQUEST_CLOCK = 0;
    private final int REQUEST_LEVEL = 1;
    private final int REQUEST_MUSIC = 2;
    // 进度条长度
    private final int PROGRESSBAR_LENGTH = 100;
    // 手指切换时间
    private final int INTERVAL_FINGER = 30;  // 换手指间隔： 60秒
    // 循环次数
    private final int CYCLE_COUNT = 3;  // 两个手指操作循环： 11次
    // 闹钟
    private ArrayList<PendingIntent> intentArray;
    // 按返回键当前时间
    private long currentBackPressedTime = 0;
    // 数据定义
    private HashMap<String, String> ClockData;
    private HashMap<String, Integer> BreathData;
    private HashMap<String, String> MusicData;

    Resources resources;

    // 标志
    private Boolean running = false;
    //组件
    ImageButton btnPlay;
    ImageView fingerImage;
    TextView txtCycleCount;

    // section: 呼吸
    TextView txtBreathStage;
    TextView txtBreathCount;

    // main data
    private SeekBar seekBar;
    private int pos;

    // 吸气，呼气间隔
    private int inhale;
    private int hold;
    private int exhale;
    private int inhale_count;
    private int hold_count;
    private int exhale_count;

    private int breath_count;

    private CountDownTimer timer;
    private int division; // 进度条分度值

    private enum Stage {INHALE, HOLD, EXHALE}

    ;
    private Stage stage = Stage.INHALE;
    private int cycle_count;

    // 手指阶段
    private ArrayList<FingerAction> fingers;  // 0, 1
    private int fingerIndex;
    private FingerAction finger_cur;

    // 动作计数 进度条
    private RoundCornerProgressBar barAction;
    private SnappingSeekBar barAction_2;

    // 背景音乐
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // my code
        readData();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(PROGRESSBAR_LENGTH);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        txtCycleCount = (TextView) findViewById(R.id.txtFingerAction);

        txtBreathStage = (TextView) findViewById(R.id.txtBreathStage);
        txtBreathCount = (TextView) findViewById(R.id.txtBreathCount);

        fingerImage = (ImageView) findViewById(R.id.imgFinger);
        // 取得资源
        resources = getResources();

        // 动作： 手指
        fingers = new ArrayList<>();
        fingers.add(new FingerAction(9, R.drawable.stage_0));
        fingers.add(new FingerAction(3, R.drawable.stage_1));

        // 动作次数计数
        barAction = (ProgessBar) findViewById(R.id.barAction);
        barAction_2 = (ProgessBar) findViewById(R.id.bar_action_2);
        // main
        timer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {
                // 呼吸
                switch (stage) {
                    case INHALE:
                        inhale_count -= 100;
                        pos++;
                        txtBreathCount.setText(String.valueOf(inhale_count / 1000.0));
                        txtBreathStage.setText("inhale");

                        if (inhale_count == 0) {
                            inhale_count = inhale * 1000;
                            stage = Stage.HOLD;
                        }
                        //
                        break;
                    case HOLD:
                        hold_count -= 100;
                        txtBreathCount.setText(String.valueOf(hold_count / 1000.0));
                        txtBreathStage.setText("hold");

                        if (hold_count == 0) {
                            hold_count = hold * 1000;
                            stage = Stage.EXHALE;
                        }
                        break;
                    case EXHALE:
                        exhale_count -= 100;
                        pos--;
                        txtBreathCount.setText(String.valueOf(exhale_count / 1000.0));
                        txtBreathStage.setText("exhale");

                        if (exhale_count == 0) {
                            exhale_count = exhale * 1000;
                            stage = Stage.INHALE;
                        }
                        break;
                }
                // 更新进度条
                seekBar.setProgress(pos * division);
            }

            @Override
            public void onFinish() {
                // 重复定时器
                timer.start();
                // 手指动作
                finger_cur = fingers.get(fingerIndex);
                finger_cur.run(1);
                barAction.setProgress(finger_cur.getCycle_count());
                barAction_2.setProgressToIndex(finger_cur.getCycle_count());

                if (finger_cur.isFinished()) {
                    fingerIndex++;

                    if (fingerIndex > 1) {
                        // 结束
                        btnPlay.callOnClick();
                        // 播放鼓励动画
                    }
                }

            }
        };

        //
        Scon = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                mServ = ((MusicService.ServiceBinder) binder).getService();
            }

            public void onServiceDisconnected(ComponentName name) {
                mServ = null;
            }
        };

        doBindService();
    }


    void doBindService() {
        bindService(new Intent(this, MusicService.class), Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // 菜单相应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // 运行时，不响应菜单

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_exercise:
                intent = new Intent(MainActivity.this, BreatheActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_train:
                break;
            case R.id.menu_profile:
                break;
            case R.id.menu_setting_clock:
                intent = new Intent(MainActivity.this, SettingClockActivity.class);
                intent.putExtra("clock", ClockData);
                startActivityForResult(intent, REQUEST_CLOCK);
                break;
            case R.id.menu_setting_level:
                intent = new Intent(MainActivity.this, SettingLevelActivity.class);
                intent.putExtra("level", BreathData);
                startActivityForResult(intent, REQUEST_LEVEL);
                break;
            case R.id.menu_setting_music:
                intent = new Intent(MainActivity.this, SettingMusicActivity.class);
                intent.putExtra("music", MusicData);
                startActivityForResult(intent, REQUEST_MUSIC);
        }
        return true;
    }

    // 闹钟
    // 重启闹钟
    private void ClockAlarm() {

        intentArray = new ArrayList<>();
        Context context = getApplication();
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 添加到闹钟
        Iterator iterator = ClockData.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            i++;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            HashMap.Entry entry = (HashMap.Entry) iterator.next();
            String s = entry.getValue().toString();
            String[] time = s.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 60 * 24, alarmIntent);
            intentArray.add(alarmIntent);
        }
    }


    // 读取数据
    private void readData() {
        // 数据读取
        // 闹钟
        ClockData = new HashMap<>();
        Profile profile = new Profile(getApplication());
        ClockData = profile.readClockData();

        // 呼吸
        BreathData = profile.readBreathData();

        // 音乐数据
        MusicData = profile.readBackgroundMusicData();
    }


    // 后退按键
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - currentBackPressedTime > 3000) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplication(), "Press again, exit.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    // Activity 返回

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CLOCK:
                if (resultCode == RESULT_OK) {
                    // 更新数据
                    ClockData.put("clock_is_on", data.getStringExtra("clock_is_on"));
                    ClockData.put("clock_1", data.getStringExtra("clock_1"));
                    ClockData.put("clock_2", data.getStringExtra("clock_2"));
                    ClockData.put("clock_3", data.getStringExtra("clock_3"));

                    // 保存
                    Profile profile = new Profile(getApplication());
                    profile.writeClockData(new String[]{
                            data.getStringExtra("clock_is_on"),
                            data.getStringExtra("clock_1"),
                            data.getStringExtra("clock_2"),
                            data.getStringExtra("clock_3")
                    });
                }
                break;
            case REQUEST_LEVEL:
                if (resultCode == RESULT_OK) {
                    // 更新数据
                    BreathData.put("level", data.getIntExtra("level", 0));

                    BreathData.put("new_inhale", data.getIntExtra("new_inhale", 3));
                    BreathData.put("new_hold", data.getIntExtra("new_hold", 2));
                    BreathData.put("new_exhale", data.getIntExtra("new_exhale", 3));

                    BreathData.put("expert_inhale", data.getIntExtra("expert_inhale", 5));
                    BreathData.put("expert_hold", data.getIntExtra("expert_hold", 3));
                    BreathData.put("expert_exhale", data.getIntExtra("expert_exhale", 5));

                    // 保存
                    Profile profile = new Profile(getApplication());
                    profile.writeBreathData(new int[]{
                            data.getIntExtra("level", 0),
                            data.getIntExtra("new_inhale", 3),
                            data.getIntExtra("new_hold", 2),
                            data.getIntExtra("new_exhale", 3),
                            data.getIntExtra("expert_inhale", 5),
                            data.getIntExtra("expert_hold", 3),
                            data.getIntExtra("expert_exhale", 5),

                    });

                }
                break;
            case REQUEST_MUSIC:
                if (resultCode == RESULT_OK) {

                    // 更新
                    MusicData.put("music_on", data.getStringExtra("music_is_on"));
                    MusicData.put("music_file", data.getStringExtra("music_file"));
                    // 保存音乐开关、文件
                    Profile profile = new Profile(getApplication());
                    profile.writeBackgroundMusicData(new String[]{
                            data.getStringExtra("music_is_on"),
                            data.getStringExtra("music_file")
                    });
                }

        }  // switch
    }

    // 活动销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        // stop
        //mServ.stopMusic();
    }

    // Play 按钮
    public void btnPlayClick(View view) {
        String btn_image;

        // 停止
        if (!running) {
            init();
            // 定时器
            if (timer != null) {
                timer.start();
            }
            running = true;
            btn_image = "@android:drawable/ic_media_stop";
            // 启动音乐
            startMusic();

        } else {
            btn_image = "@android:drawable/ic_media_play";
            running = false;
            // 取消定时器
            timer.cancel();
            // 复位进度条
            seekBar.setProgress(0);

            barAction.setMax(90);
            barAction.setProgress(0);
            // 停止音乐
            stopMusic();
        }
        // update
        btnPlay.setImageResource(resources.getIdentifier(btn_image, null, null));
        //
    }

    private void init() {
        if (timer != null) {
            timer.cancel();
        }
        // 取呼吸间隔数据
        switch (BreathData.get("level")) {
            case 0:
                inhale = BreathData.get("new_inhale");
                hold = BreathData.get("new_hold");
                exhale = BreathData.get("new_exhale");
                break;
            case 1:
                inhale = BreathData.get("expert_inhale");
                hold = BreathData.get("expert_hold");
                exhale = BreathData.get("expert_exhale");
        }

        inhale_count = inhale * 1000;
        hold_count = hold * 1000;
        exhale_count = exhale * 1000;

        division = PROGRESSBAR_LENGTH / (inhale * 10);

        pos = 0;
        stage = Stage.INHALE;
        fingerIndex = 0;
        cycle_count = 0;

        fingerImage.setImageResource(R.drawable.stage_0);
    }

    // 背景音乐 停止
    private void startMusic() {

        Intent music = new Intent();
        // 取设置的音乐
        String f = MusicData.get("music_file");
        int id_music = getApplication().getResources().getIdentifier(f, "raw", getPackageName());
        // >0，选择音乐
        if (id_music > 0) {
            music.putExtra("music_item", id_music);
        }

        music.setClass(this, MusicService.class);
        startService(music);

    }

    // 背景音乐 停止
    private void stopMusic() {
        mServ.pauseMusic();
    }

    // 背景音乐服务是否已经启动
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    // end
}
