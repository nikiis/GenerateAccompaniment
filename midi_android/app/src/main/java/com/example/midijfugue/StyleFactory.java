package com.example.midijfugue;

public class StyleFactory {

    public static BaseStyle createStyle(StyleSetup setup){
        BaseStyle style = null;

        switch (setup.getStyle()){
            case SIMPLE:
                style = new SimpleStyle(setup);
                break;
            case ROCK:
                style = new RockStyle(setup);
                break;
        }

        return style;
    }
}
