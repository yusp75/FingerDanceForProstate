package com.yushealth.prostate.fingerdanceforprostate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MusicFileListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_file_list);

        // 列出音乐文件
        ArrayList<MusicFileData> list = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for(Field f: fields) {
            String field_name = f.getName();
            if (field_name.startsWith("$") || field_name.equals("SerialVersionUID")) {
                continue;
            }
            MusicFileData data = new MusicFileData(f.getName());
            list.add(data);
        }

        listView = (ListView) findViewById(R.id.lv_music);
        MusicListAdapter adapter = new MusicListAdapter(this, list);
        listView.setAdapter(adapter);

        // 返回文件名
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView f = (TextView)view.getTag(R.id.txtMusicfile);
                Bundle bundle = new Bundle();
                bundle.putString("file_selected", f.getText().toString());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // end
}
