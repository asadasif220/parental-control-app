package com.example.dell.androidhive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nephi.getoffyourphone.Main;

public class AfterChildClick extends AppCompatActivity {

    Button callLog, msgLog, history, lockApp, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_child_click);

        callLog = findViewById(R.id.B_callLog);
        lockApp = findViewById(R.id.B_appLock);
        history = findViewById(R.id.B_history);
        msgLog = findViewById(R.id.B_msg);
        back = findViewById(R.id.B_back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterChildClick.this, ViewChildActivity.class);
                startActivity(intent);
                finish();
            }
        });

        callLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterChildClick.this, CustomCallLog.class);
                startActivity(intent);
                finish();
            }
        });

        lockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterChildClick.this, Main.class);
                startActivity(intent);
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterChildClick.this, BrowserHistory.class);
                startActivity(intent);
                finish();
            }
        });

        msgLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterChildClick.this, GetMessage.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
