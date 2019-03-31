package com.example.midijfugue;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Chord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.kshoji.javax.sound.midi.Sequence;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        MediaMidiSystem.OnDeviceConnectionChangedListener {


    private StyleSetup styleSetup;

    private ArrayAdapter<String> spinnerSynthAdapter;
    private ArrayAdapter<String> spinnerStylesAdapter;

    MediaMidiSystem mediaMidiSystem;
    ArrayList<Integer> button_ids = new ArrayList<>(
            Arrays.asList(R.id.chord1,R.id.chord2,R.id.chord3,R.id.chord4,R.id.chord5,
                    R.id.chord6,R.id.chord7,R.id.chord8));

    public SparseArray<Chord> chords = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        styleSetup = new StyleSetup();
        setSpinnerSynth();
        setStylesSpinner();

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
        Log.i("style name", styleSetup.getStyle());
        Log.i("tempo", "" +styleSetup.getTempo());
        Log.i("bars", ""+styleSetup.getNumberOfBars());
//        Sequence s = new MediaMidiPlayer().getSequence(pattern);

//        new MediaMidiPlayer().play(s);

    }

    public void setChord(View view){
        int  currChord = button_ids.indexOf(view.getId());
        showChordDialogue((Button) view, currChord);
    }

    public void decrementBar(View view){
//        Log.e("bars", ""+styleSetup.getNumberOfBars());

        if(!styleSetup.decrementBars()){
            Toast.makeText(this, "Can't remove any more bars", Toast.LENGTH_SHORT).show();
        }

        Button remButton = findViewById(button_ids.get(styleSetup.getNumberOfBars()));
        remButton.setVisibility(view.INVISIBLE);
        Log.e("id removed", "" + styleSetup.getNumberOfBars());
        remButton.setText("");
        TextView textBar = findViewById(R.id.numBars);
        textBar.setText(""+styleSetup.getNumberOfBars());
        Log.e("chords left: ", chords.toString());

    }

    public void incrementBar(View view){
        if(!styleSetup.incrementBars()){
            Toast.makeText(this, "Can't add any more bars", Toast.LENGTH_SHORT).show();

        }

        Log.e("number of bars", ""+ styleSetup.getNumberOfBars());
        Button addButton = findViewById(button_ids.get(styleSetup.getNumberOfBars() - 1));
        addButton.setVisibility(view.VISIBLE);
        TextView textBar = findViewById(R.id.numBars);
        textBar.setText(""+styleSetup.getNumberOfBars());
    }

    public void incrementTempo(View view){
        if(!styleSetup.incrementTempo()){
            Toast.makeText(this,"Your tempo can't be over the maximum of 200", Toast.LENGTH_SHORT).show();
        }
        styleSetup.incrementTempo();
        TextView tempoView = findViewById(R.id.tempoText);
        tempoView.setText(""+styleSetup.getTempo());
    }

    public void decrementTempo(View view){
        if(!styleSetup.decrementTempo()){
            Toast.makeText(this,"Your tempo can't be under the minimum of 60", Toast.LENGTH_SHORT).show();
        }
        styleSetup.decrementTempo();
        TextView tempoView = findViewById(R.id.tempoText);
        tempoView.setText(""+styleSetup.getTempo());
    }


    private void setSpinnerSynth(){
        Spinner spinnerSynth = findViewById(R.id.spinSynth);
        spinnerSynth.setOnItemSelectedListener(this);
        spinnerSynthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new ArrayList<String>());
        spinnerSynthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSynth.setAdapter(spinnerSynthAdapter);

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

    private void setStylesSpinner(){

        Spinner spinnerStyles = findViewById(R.id.stylesList);
        spinnerStyles.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styleSetup.STYLES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStyles.setAdapter(adapter);
    }

    private void showChordDialogue(final Button button, final int currChord){
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
                styleSetup.setChord(currChord, c);
                button.setText(chord);
                dialog.dismiss();

                Log.e("chord", chords.toString());
//                Log.e("chord", chords.get(0).toString());
            }
        });

    }
}
