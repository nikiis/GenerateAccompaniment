package com.example.midijfugue.styles;

import com.example.midijfugue.StyleSetup;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.Chord;

public class RockStyle extends BaseStyle {
    private StyleSetup styleSetup;

    public RockStyle(StyleSetup styleSetup) {
        super(styleSetup);
        this.styleSetup = styleSetup;
    }

    public Pattern createChordPattern(){
        Pattern chordPattern = new Pattern();

        chordPattern.add("V0 I[ELECTRIC_CLEAN_GUITAR]");

        for (Chord c: this.styleSetup.getChords()){
            String chord = String.format("%sw", c);
            chordPattern.add(chord);
        }

        return chordPattern;
    }

    public Pattern createNotePattern(){
        Pattern notePattern = new Pattern();

        notePattern.add("V1 I[ELECTRIC_PIANO]");

        for (Chord c: styleSetup.getChords()){
            notePattern.add("rh");
            notePattern.add(c.getRoot());
            notePattern.add(c.getRoot());
        }

        return notePattern;
    }

    public Pattern createRhythmPattern() {
        Rhythm rhythm = new Rhythm();
        rhythm.addLayer("O..oO...O..oOO..")
                .addLayer("..S...S...S...S.")
                .addLayer("````````````````")
                .addLayer("...............+");
        return rhythm.getPattern();
    }
}
