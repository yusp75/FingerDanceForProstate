package com.yushealth.prostate.fingerdanceforprostate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.HashMap;

public class SettingLevelActivity extends AppCompatActivity {

    RadioButton rb_new;
    RadioButton rb_expert;
    EditText new_inhale;
    EditText new_hold;
    EditText new_exhale;
    EditText expert_inhale;
    EditText expert_hold;
    EditText expert_exhale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_level);

        // 初始化
        rb_new = (RadioButton) findViewById(R.id.rb_new);
        rb_expert = (RadioButton) findViewById(R.id.rb_expert);

        new_inhale = (EditText) findViewById(R.id.new_inhale);
        new_hold = (EditText) findViewById(R.id.new_hold);
        new_exhale = (EditText) findViewById(R.id.new_exhale);
        expert_inhale = (EditText) findViewById(R.id.expert_inhale);
        expert_hold = (EditText) findViewById(R.id.expert_hold);
        expert_exhale = (EditText) findViewById(R.id.expert_exhale);

        HashMap<String, Integer> breathData = (HashMap<String, Integer>) getIntent().getSerializableExtra("level");
        rb_new.setChecked(breathData.get("level").equals(0));
        rb_expert.setChecked(breathData.get("level").equals(1));

        new_inhale.setText(breathData.get("new_inhale").toString());
        new_hold.setText(breathData.get("new_hold").toString());
        new_exhale.setText(breathData.get("new_exhale").toString());
        expert_inhale.setText(breathData.get("expert_inhale").toString());
        expert_hold.setText(breathData.get("expert_hold").toString());
        expert_exhale.setText(breathData.get("expert_exhale").toString());

    }

    public void btnApplyClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("level", rb_expert.isChecked() ? 1 : 0);
        bundle.putInt("new_inhale", Integer.parseInt(new_inhale.getText().toString()));
        bundle.putInt("new_hold", Integer.parseInt(new_hold.getText().toString()));
        bundle.putInt("new_exhale", Integer.parseInt(new_exhale.getText().toString()));
        bundle.putInt("expert_inhale", Integer.parseInt(expert_inhale.getText().toString()));
        bundle.putInt("expert_hold", Integer.parseInt(expert_hold.getText().toString()));
        bundle.putInt("expert_exhale", Integer.parseInt(expert_exhale.getText().toString()));

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
