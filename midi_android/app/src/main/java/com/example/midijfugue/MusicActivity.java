package com.example.midijfugue;

import android.app.AlertDialog;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.kshoji.javax.sound.midi.Sequence;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        MediaMidiSystem.OnDeviceConnectionChangedListener {

    public static final int MIN_NUM_BARS = 0;
    public static final int MAX_NUM_BARS = 7;
    public static final int DEFAULT_TEMPO = 120;
    public static final int MAX_TEMPO = 200;
    public static final int MIN_TEMPO = 60;

    private ArrayAdapter<String> spinnerSynthAdapter;
    MediaMidiSystem mediaMidiSystem;
    ArrayList<Integer> button_ids = new ArrayList<Integer>(
            Arrays.asList(R.id.chord1,R.id.chord2,R.id.chord3,R.id.chord4,R.id.chord5,
                    R.id.chord6,R.id.chord7,R.id.chord8));
    public int numOfBars = 7; //7 because we start from 0
    public int currentTempo = DEFAULT_TEMPO;

    public ArrayList<Chord> chords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setSpinnerSynth();

        setDefaultChords();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaMidiSystem = new MediaMidiSystem(this);
        mediaMidiSystem.setOnDeviceConnectionChanged(this);
        mediaMidiSystem.initialize();
        setSpinnerSynthAdapterValues(mediaMidiSystem.getDevices());
    }

    public void onPlay (View view) {
        Log.e("Button", "Pressed");

        Pattern pattern = new Pattern();
        pattern.add("I[flute] Gq");

        Sequence s = new MediaMidiPlayer().getSequence(pattern);

//        Sequence s = new MediaMidiPlayer().getSequence(new Pattern(
//                new ChordProgression("I IV vi V").eachChordAs("$_i $_i $_i $_i"),
//                new Rhythm().addLayer("..XOOOX...X...XO")));

        new MediaMidiPlayer().play(s);

    }

    public void setChord(View view){
        int  currChord = button_ids.indexOf(view.getId());
        showChordDialogue((Button) view, currChord);
    }

    public void decrementBar(View view){
        if(numOfBars != MIN_NUM_BARS){
            int remID = button_ids.get(numOfBars);
            Button remButton = findViewById(remID);
            remButton.setVisibility(view.INVISIBLE);
            Log.e("id removed", "" + remID);
//            Log.e("arraylist", ""+button_ids.get(numOfBars));
            chords.remove(numOfBars);
            numOfBars--;
            TextView textBar = findViewById(R.id.numBars);
            textBar.setText(""+(numOfBars+1));
            Log.e("chords left: ", chords.toString());

        }
        else{
            Toast.makeText(this, "Can't remove any more bars", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDefaultChords(){
        //TODO
        Chord defaultChord = new Chord("CMaj");
        for(int i = 0; i<=numOfBars; i++){
            chords.add(defaultChord);
            Button b = findViewById(button_ids.get(i));
            b.setText("CMaj");
        }

        Log.e("defaultChords", chords.toString());
    }

    public void incrementBar(View view){
        if(numOfBars < MAX_NUM_BARS){
            Log.e("number of bars", ""+ numOfBars);
            int addID = button_ids.get(numOfBars + 1);
            Button addButton = findViewById(addID);
            addButton.setVisibility(view.VISIBLE);
            numOfBars++;
            TextView textBar = findViewById(R.id.numBars);
            textBar.setText(""+(numOfBars+1));
        }
        else{
            Toast.makeText(this, "Can't add any more bars", Toast.LENGTH_SHORT).show();
        }
    }

    public void incrementTempo(View view){
        //TODO
        if(currentTempo < MAX_TEMPO){
            currentTempo++;
            TextView tempoView = findViewById(R.id.tempoText);
            tempoView.setText(""+currentTempo);
        }
        else{
            Toast.makeText(this,"Your tempo can't be over the maximum of 200", Toast.LENGTH_SHORT).show();
        }

    }

    public void decrementTempo(View view){
        //TODO
        if(currentTempo > MIN_TEMPO){
            currentTempo--;
            TextView tempoView = findViewById(R.id.tempoText);
            tempoView.setText(""+currentTempo);
        }
        else{
            Toast.makeText(this,"Your tempo can't be under the minimum of 60", Toast.LENGTH_SHORT).show();
        }
    }




    private void setSpinnerSynth(){
        Spinner spinnerSynth = findViewById(R.id.spinSynth);
        spinnerSynth.setOnItemSelectedListener(this);
        spinnerSynthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new ArrayList<String>());
        spinnerSynthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSynth.setAdapter(spinnerSynthAdapter);

    }

    private void setStylesList(){
        //TODO
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaMidiSystem != null){
            mediaMidiSystem.terminate();
            mediaMidiSystem = null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mediaMidiSystem != null){
            mediaMidiSystem.openDevice(i);
            Log.e("selected", ""+ i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //user doesnt choose a synth
        Toast.makeText(this, "Please Choose a Synthesizer or install one. Read 'Instructions' to" +
                        "learn more",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDevicesChanged(List<MidiDeviceInfo> devices) {
        setSpinnerSynthAdapterValues(devices);
    }

    private void setSpinnerSynthAdapterValues(List<MidiDeviceInfo> devices){
        if (devices == null || devices.isEmpty()){
            return;
        }

        final List<String> list = new ArrayList<>();

        for (MidiDeviceInfo d: devices){
            list.add(d.getProperties().getString(MidiDeviceInfo.PROPERTY_PRODUCT));
            Log.i("deviceInfo", d.toString());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerSynthAdapter.clear();
                spinnerSynthAdapter.addAll(list);

            }
        });
    }

    private void showChordDialogue(final Button button, int currChord){
        final String[] chordNames= {"C","C#", "Cb",
                "D", "D#", "Db",
                "E", "E#", "Eb",
                "F", "F#", "Fb",
                "G", "G#", "Gb",
                "A", "A#", "Ab",
                "B", "B#", "Bb"};
        final String[] chordTypes = {"Maj", "Min", "Maj7", "Min7", "Dom7",
                "Dim", "Aug", "sus2", "sus4"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
        View view = getLayoutInflater().inflate(R.layout.view_chords, null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

        final NumberPicker picker_chords = dialog.findViewById(R.id.numpick_chords);
        picker_chords.setMinValue(0);
        picker_chords.setMaxValue(20);
        picker_chords.setDisplayedValues(chordNames);

        final NumberPicker picker_type = dialog.findViewById(R.id.numpick_types);
        picker_type.setMinValue(0);
        picker_type.setMaxValue(8);
        picker_type.setDisplayedValues(chordTypes);

        Button enterChord = dialog.findViewById(R.id.button_chords);

        enterChord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chord = chordNames[picker_chords.getValue()] + chordTypes[picker_type.getValue()];
                Chord c = new Chord(chord);
                //TODO: NOT MEANT TO ADD WE NEED TO REPLACE THE PREVIOUS ONE IF THAT MAKES SENSE?
                chords.add(c); //TODO THIS LINE IS WRONG PLZ FIX IT
                button.setText(chord);
                dialog.dismiss();

                Log.e("chord", chords.toString());
            }
        });

    }
}
