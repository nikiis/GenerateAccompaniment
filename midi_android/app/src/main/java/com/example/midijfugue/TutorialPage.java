package com.example.midijfugue;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class TutorialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_page);
    }

    public void goBackHome(View view){
        Intent intent = new Intent(this, LandingPage.class);

        startActivity(intent);
    }

    public void viewMIDITutorial(View view){
        inflateMidiTut();
    }

    public void inflateMidiTut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TutorialPage.this);
        View view = getLayoutInflater().inflate(R.layout.midi_device_tutorial, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


//        Button button = findViewById(R.id.goBack);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
    }

    public void viewAddChordTutorial(View view){

    }

    public void viewChooseStyleTutorial(View view){

    }

    public void viewAdjustTempoTutorial(View view){

    }

    public void viewPlayMusicTutorial(View view){


    }
}
