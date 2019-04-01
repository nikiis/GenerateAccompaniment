package com.example.midijfugue;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midijfugue.midi.support.MediaMidiSystem;
import com.example.midijfugue.styles.BaseStyle;
import com.example.midijfugue.styles.StyleFactory;

import org.jfugue.player.Player;
import org.jfugue.theory.Chord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        MediaMidiSystem.OnDeviceConnectionChangedListener {

    private StyleSetup styleSetup;

    private ArrayAdapter<String> spinnerSynthAdapter;

    MediaMidiSystem mediaMidiSystem;
    ArrayList<Integer> buttonIds = new ArrayList<>(Arrays.asList(R.id.chord1, R.id.chord2,
            R.id.chord3, R.id.chord4, R.id.chord5, R.id.chord6, R.id.chord7, R.id.chord8,
            R.id.chord9, R.id.chord10, R.id.chord11, R.id.chord12));

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
        if (!styleSetup.isSetupCorrect()){
            Toast.makeText(this, "Still missing some chords", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Button", "Pressed");
        Log.i("style name", styleSetup.getStyle());
        Log.i("tempo", styleSetup.getTempo() + "");
        Log.i("bars", styleSetup.getNumberOfBars() + "");
        ImageButton playButton = findViewById(R.id.play_button);
        ImageButton stopButton = findViewById(R.id.stop_button);

        playButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        BaseStyle style = StyleFactory.createStyle(styleSetup);
        style.play(100);
    }

    public void onClickStop(View view){
        ImageButton playButton = findViewById(R.id.play_button);
        ImageButton stopButton = findViewById(R.id.stop_button);

        playButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.INVISIBLE);

        new Player().getManagedPlayer().finish();
    }

    public void goBackToHome(View view){
        Intent intent = new Intent(this, LandingPage.class);

        startActivity(intent);
    }

    public void setChord(View view){
        int currChord = buttonIds.indexOf(view.getId());
        showChordDialogue((Button) view, currChord);
    }

    public void decrementBar(View view){
        if(!styleSetup.decrementBars()){
            Toast.makeText(this, "Should have at least one bar", Toast.LENGTH_SHORT).show();
            return;
        }

        Button button = findViewById(buttonIds.get(styleSetup.getNumberOfBars()));
        button.setVisibility(View.INVISIBLE);
        Log.e("id removed", "" + styleSetup.getNumberOfBars());
        button.setText("+");
        TextView textBar = findViewById(R.id.bar_count);
        textBar.setText(String.valueOf(styleSetup.getNumberOfBars()));
        Log.e("chords left: ", styleSetup.getNumberOfBars() + "");
    }

    public void incrementBar(View view){
        if(!styleSetup.incrementBars()){
            Toast.makeText(this, "Up to 12 bars supported only", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("number of bars", ""+ styleSetup.getNumberOfBars());
        Button addButton = findViewById(buttonIds.get(styleSetup.getNumberOfBars() - 1));
        addButton.setVisibility(View.VISIBLE);
        TextView textBar = findViewById(R.id.bar_count);
        textBar.setText(String.valueOf(styleSetup.getNumberOfBars()));
    }

    public void incrementTempo(View view){
        if(!styleSetup.incrementTempo()){
            Toast.makeText(this,"Maximum tempo is 200", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tempoView = findViewById(R.id.tempo_text);
        tempoView.setText(String.valueOf(styleSetup.getTempo()));
    }

    public void decrementTempo(View view){
        if(!styleSetup.decrementTempo()){
            Toast.makeText(this,"Minimum tempo is 60", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tempoView = findViewById(R.id.tempo_text);
        tempoView.setText(String.valueOf(styleSetup.getTempo()));
    }


    private void setSpinnerSynth(){
        Spinner spinnerSynth = findViewById(R.id.synthesizer_spinner);
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
        switch (adapterView.getId()){
            case R.id.synthesizer_spinner:
                if (mediaMidiSystem != null){
                    mediaMidiSystem.openDevice(i);
                    Log.e("selected", ""+ i);
                }
                break;
            case R.id.styles_spinner:
                styleSetup.setStyle(StyleSetup.STYLES[i]);
                break;
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
        Spinner spinnerStyles = findViewById(R.id.styles_spinner);
        spinnerStyles.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StyleSetup.STYLES);
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

        final String[] inversions = {"root", "1st inversion", "2nd inversion"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
        View view = getLayoutInflater().inflate(R.layout.view_chords, null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

        final NumberPicker picker_chords = dialog.findViewById(R.id.numpick_chords);
        picker_chords.setMinValue(0);
        picker_chords.setMaxValue(chordNames.length - 1);
        picker_chords.setDisplayedValues(chordNames);

        final NumberPicker picker_type = dialog.findViewById(R.id.numpick_types);
        picker_type.setMinValue(0);
        picker_type.setMaxValue(chordTypes.length - 1);
        picker_type.setDisplayedValues(chordTypes);

        final NumberPicker picker_inversion = dialog.findViewById(R.id.numpick_inversion);
        picker_inversion.setMinValue(0);
        picker_inversion.setMaxValue(inversions.length - 1);
        picker_inversion.setDisplayedValues(inversions);

        Button enterChord = dialog.findViewById(R.id.button_chords);

        enterChord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inversionType = "";

                switch(inversions[picker_inversion.getValue()]){
                    case "root":
                        Log.e("inverstion type: ", inversionType);
                        break;
                    case "1st inversion":
                        inversionType = "^";
                        Log.e("inverstion type: ", inversionType);
                        break;
                    case "2nd inversion":
                        inversionType = "^^";
                        Log.e("inverstion type: ", inversionType);

                        break;
                }

                String chord = chordNames[picker_chords.getValue()] + chordTypes[picker_type.getValue()] + inversionType;
                Log.e("chord", chord);
                Chord c = new Chord(chord);
                styleSetup.setChord(currChord, c);
                button.setText(chord);
                dialog.dismiss();
            }
        });

    }
}
