package com.example.midijfugue;

import android.util.SparseArray;

import org.jfugue.theory.Chord;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StyleSetup {

    public final static String[] KEYS = {
            "C", "C#", "Cb",
            "G", "G#", "Gb",
            "A", "A#", "Ab",
            "D", "D#", "Db",
            "E", "E#", "Eb",
            "F", "F#", "Fb",
            "B", "B#", "Bb"};

    public final static String[] STYLES = {"Simple", "Rock"};

    private final int MAX_TEMPO = 200;
    private final int MIN_TEMPO = 60;
    private final int MAX_BARS = 12;
    private final int MIN_BARS = 1;

    private final int DEFAULT_TEMPO = 120;
    private final int DEFAULT_BARS = 12;
    private final String DEFAULT_KEY = KEYS[0];
    private final String DEFAULT_STYLE = STYLES[0];

    private int numberOfBars;
    private String style;
    private int tempo;
    private String key;
    private SparseArray<Chord> chords;

    public StyleSetup(){
        this.numberOfBars = DEFAULT_BARS;
        this.style = DEFAULT_STYLE;
        this.tempo = DEFAULT_TEMPO;
        this.key = DEFAULT_KEY;
        this.chords = new SparseArray<>();
    }

    public boolean isSetupCorrect(){
        for (int i = 0; i < this.numberOfBars; i++){
            if (chords.get(i) == null) return false;
        }

        return true;
    }

    public boolean incrementTempo(){
        if (tempo < MAX_TEMPO){
            tempo++;
            return true;
        }
        return false;
    }

    public boolean decrementTempo(){
        if (tempo > MIN_TEMPO){
            tempo--;
            return true;
        }
        return false;
    }

    public boolean incrementBars(){
        if (numberOfBars < MAX_BARS){
            numberOfBars++;
            return true;
        }
        return false;
    }

    public boolean decrementBars(){
        if (numberOfBars > MIN_BARS){
            chords.remove(numberOfBars - 1);
            numberOfBars--;
            return true;
        }
        return false;

    }

    public int getNumberOfBars() {
        return numberOfBars;
    }

    public int getTempo() {
        return tempo;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Chord> getChords() {
        if (this.chords == null) return null;

        List<Chord> chordsList = new ArrayList<>(numberOfBars);
        for (int i = 0; i < this.chords.size(); i++)
            chordsList.add(this.chords.valueAt(i));
        return chordsList;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setChord(int index, Chord chord){
        chords.put(index, chord);
    }

    @Override
    public String toString() {
        return String.format(Locale.UK,
                "bars=%d, style=%s, tempo=%d, key=%s, chords=%s",
                numberOfBars, style, tempo, key, chords.toString());
    }

}
