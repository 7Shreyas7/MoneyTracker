package com.hmproductions.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButtonClickListener();
    }

    public void ButtonClickListener()
    {
        button = (Button)findViewById(R.id.color_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InfoActivity.this,"Update coming soon, as Always.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
