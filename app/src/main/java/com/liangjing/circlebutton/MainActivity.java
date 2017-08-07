package com.liangjing.circlebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liangjing.circlebutton.views.CircleButtonView;

public class MainActivity extends AppCompatActivity {

    private CircleButtonView circleButtonView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleButtonView = (CircleButtonView) findViewById(R.id.cbv);
        circleButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
