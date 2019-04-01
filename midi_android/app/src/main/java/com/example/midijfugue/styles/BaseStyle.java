package com.example.midijfugue.styles;

import com.example.midijfugue.StyleSetup;
import com.example.midijfugue.midi.support.MediaMidiPlayer;

import org.jfugue.pattern.Pattern;

public abstract class BaseStyle {
    private StyleSetup styleSetup;

    public BaseStyle(StyleSetup styleSetup){
        this.styleSetup = styleSetup;
    }


    public Pattern buildPlayable(int repeatCount) {
        Pattern playable = new Pattern();
        playable.add(
                createNotePattern(),
                createChordPattern()).repeat(repeatCount);

        playable.add(createRhythmPattern().repeat(repeatCount / 2 * styleSetup.getNumberOfBars()));

        playable.setTempo(styleSetup.getTempo());

        return playable;
    }

    public void play(int repeatCount){
        MediaMidiPlayer player = new MediaMidiPlayer();
        player.play(buildPlayable(repeatCount));
    }

    public void play(Pattern playable){
        MediaMidiPlayer player = new MediaMidiPlayer();
        player.play(playable);
    }

    public abstract Pattern createChordPattern();
    public abstract Pattern createRhythmPattern();
    public abstract Pattern createNotePattern();
}
