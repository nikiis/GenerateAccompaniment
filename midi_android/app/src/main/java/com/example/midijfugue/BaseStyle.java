package com.example.midijfugue;

import org.jfugue.pattern.Pattern;

public abstract class BaseStyle {
    private StyleSetup styleSetup;

    public BaseStyle(StyleSetup styleSetup){
        this.styleSetup = styleSetup;
    }

    public abstract Pattern createChordPattern();
    public abstract Pattern createNotePattern();

    private Pattern buildPlayable() {
        Pattern playable = new Pattern();
        playable.add(
                createNotePattern(),
                createChordPattern(),
                createRhythmPattern().repeat(styleSetup.getNumberOfBars() % 2 + 2));

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
