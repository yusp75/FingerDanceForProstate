package com.yushealth.prostate.fingerdanceforprostate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.HashMap;

import static com.yushealth.prostate.fingerdanceforprostate.R.id.tbClockEnable;

public class SettingClockActivity extends AppCompatActivity {

    Switch clockEnable;
    EditText etClock_1;
    EditText etClock_2;
    EditText etClock_3;

    String str_clock_is_enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_clock);

        // 组件
        clockEnable = (Switch) findViewById(tbClockEnable);
        etClock_1 = (EditText) findViewById(R.id.etClock_1);
        etClock_2 = (EditText) findViewById(R.id.etClock_2);
        etClock_3 = (EditText) findViewById(R.id.etClock_3);

        clockEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                str_clock_is_enable = b ? "true" : "false";
            }
        });

        // 填入数据
        HashMap<String, String> clockData = (HashMap<String, String>) getIntent().getSerializableExtra("clock");
        clockEnable.setChecked(clockData.get("clock_is_on").equals("true"));
        etClock_1.setText(clockData.get("clock_1"));
        etClock_2.setText(clockData.get("clock_2"));
        etClock_3.setText(clockData.get("clock_3"));

    }

    public void btnApply(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("clock_is_on", str_clock_is_enable);
        bundle.putString("clock_1", etClock_1.getText().toString());
        bundle.putString("clock_2", etClock_2.getText().toString());
        bundle.putString("clock_3", etClock_3.getText().toString());

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btnCancel(View view) {
        finish();
    }

    // end
}
