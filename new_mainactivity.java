package com.salarycalc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SalaryCalc";
    private java.util.ArrayList<String> records = new java.util.ArrayList<String>();
    private TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate start");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(48, 48, 48, 48);

            TextView title = new TextView(this);
            title.setText("工资计算器");
            title.setTextSize(24);
            layout.addView(title);

            final EditText nameInput = new EditText(this);
            nameInput.setHint("姓名");
            layout.addView(nameInput);

            final EditText baseInput = new EditText(this);
            baseInput.setHint("基本工资");
            baseInput.setInputType(8192);
            layout.addView(baseInput);

            final EditText bonusInput = new EditText(this);
            bonusInput.setHint("奖金");
            bonusInput.setInputType(8192);
            layout.addView(bonusInput);

            Button addBtn = new Button(this);
            addBtn.setText("添加");
            addBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String name = nameInput.getText().toString();
                    records.add(name);
                    nameInput.setText("");
                    baseInput.setText("");
                    bonusInput.setText("");
                    Toast.makeText(MainActivity.this, "已添加: " + name, Toast.LENGTH_SHORT).show();
                }
            });
            layout.addView(addBtn);

            Button calcBtn = new Button(this);
            calcBtn.setText("计算");
            calcBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (records.isEmpty()) {
                        resultTv.setText("请先添加记录");
                    } else {
                        int count = records.size();
                        resultTv.setText("共 " + count + " 条记录");
                    }
                }
            });
            layout.addView(calcBtn);

            resultTv = new TextView(this);
            resultTv.setTextSize(16);
            resultTv.setText("点击添加开始记录工资");
            layout.addView(resultTv);

            setContentView(layout);
            Log.d(TAG, "onCreate done, UI set");
        } catch (Exception e) {
            Log.e(TAG, "Crash in onCreate", e);
            Toast.makeText(this, "启动错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
