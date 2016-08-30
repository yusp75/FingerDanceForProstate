package com.yushealth.prostate.fingerdanceforprostate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

public class SettingMusicActivity extends AppCompatActivity {

    TextView music_file;
    Switch music_is_on;
    String str_music_is_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_music);
        //
        music_file = (TextView)findViewById(R.id.tvMusicfile);
        music_is_on = (Switch)findViewById(R.id.music_is_on);

        music_is_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                str_music_is_on = b? "true": "false";
            }
        });

        // 初始化显示
        HashMap<String, String> musicData = (HashMap<String, String>)getIntent().getSerializableExtra("music");
        music_is_on.setChecked(musicData.get("music_on").equals("true"));
        music_file.setText(musicData.get("music_file"));

    }

    public void selMusicClick(View view) {

        Intent intent = new Intent(SettingMusicActivity.this, MusicFileListActivity.class);
        startActivityForResult(intent, 0);  // 0

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String file_name = bundle.getString("file_selected");
            // 显示选择的文件
            music_file.setText(file_name);

        }
    }


    public void btnApplyClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("music_is_on", str_music_is_on);
        bundle.putString("music_file", music_file.getText().toString());
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btnCancelClick(View view) {
        finish();
    }

    // end
}
