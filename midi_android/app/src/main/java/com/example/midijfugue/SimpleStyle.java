package com.example.midijfugue;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Chord;

public class SimpleStyle extends BaseStyle{
    private StyleSetup styleSetup;

    public SimpleStyle(StyleSetup styleSetup) {
        super(styleSetup);
        this.styleSetup = styleSetup;
    }

    public Pattern createChordPattern(){
        Pattern chordPattern = new Pattern();

        chordPattern.add("V0");

        for (Chord c: this.styleSetup.getChords()){
            String chord = String.format("%sw", c);
            chordPattern.add(chord);
        }

        return chordPattern;
    }

    public Pattern createNotePattern(){
        Pattern notePattern = new Pattern();

        notePattern.add("V1");

        for (Chord c: styleSetup.getChords()){
            notePattern.add("rq");
            notePattern.add(c.getNotes());
        }

        return notePattern;
    }

    public Pattern createRhythmPattern() {
        Rhythm rhythm = new Rhythm();
        rhythm.addLayer("O..oO...O..oOO..")
                .addLayer("````````````````");
        return rhythm.getPattern();
    }
}

