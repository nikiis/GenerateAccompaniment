package com.example.midijfugue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
    }

    public void toMusicActivity(View view){
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    public void toTutorialActivity(View view){

    }

    public void toAboutActivity(View view){

    }
}
