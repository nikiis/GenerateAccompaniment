package com.example.midijfugue.styles;

import com.example.midijfugue.StyleSetup;
import com.example.midijfugue.midi.support.MediaMidiPlayer;

import org.jfugue.pattern.Pattern;

public abstract class BaseStyle {
    private StyleSetup styleSetup;

    public BaseStyle(StyleSetup styleSetup){
        this.styleSetup = styleSetup;
    }

    public abstract Pattern createChordPattern();
    public abstract Pattern createNotePattern();

    public Pattern buildPlayable() {
        Pattern playable = new Pattern();
        playable.add(
                createNotePattern(),
                createChordPattern(),
                createRhythmPattern());

        playable.setTempo(styleSetup.getTempo());
        return playable;
    }

    public void play(){
        MediaMidiPlayer player = new MediaMidiPlayer();
        player.play(buildPlayable());
    }


    /**
     *  This needs to be reimplemented with every style.
     * @return
     */
    public abstract Pattern createRhythmPattern();
}
