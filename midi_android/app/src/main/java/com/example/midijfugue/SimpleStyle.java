package com.example.midijfugue;

import android.util.SparseArray;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Chord;

public class SimpleStyle {
    private SparseArray enteredChords;
    private int tempo;

    private Pattern finalPattern = new Pattern();
    private Pattern chordPattern = new Pattern();
    private Pattern notePattern = new Pattern();
    private Rhythm simpleRhythm = new Rhythm();
    private int numOfBars = enteredChords.size();

    public SimpleStyle(SparseArray enteredChords, int tempo) {
        this.enteredChords = enteredChords;
        this.tempo = tempo;
    }

    public void setPatterns(){
        chordPattern.add("V0 ");
        notePattern.add("V1 ");

        for(int i = 0; i < numOfBars; i++){
            Chord c = (Chord) enteredChords.get(i);
            String chord = c.toString() + 'w';
            chordPattern.add(chord);

            notePattern.add("rq");
            notePattern.add(c.getNotes());
        }
    }

    public void setFinalPattern() {

        setPatterns();
        finalPattern.add(notePattern,chordPattern,simpleRhythm.getPattern().repeat(numOfBars%2+2));
        finalPattern.setTempo(tempo);
    }

    public void setSimpleRhythm() {
        simpleRhythm
                .addLayer("O..oO...O..oOO..")
//                .addLayer("..S...S...S...S.")
                .addLayer("````````````````");
//                .addLayer("...............+");
    }

    public Pattern getFinalPattern() {
        return finalPattern;
    }


}

