package com.zhanglin.labelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhanglin.labelview.view.LabelTextView;

public class MainActivity extends AppCompatActivity {
    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                ((LabelTextView) v).showOrHideLabel(isShow);
            }
        });
    }
}
