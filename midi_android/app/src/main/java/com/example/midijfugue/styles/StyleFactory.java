package com.example.midijfugue.styles;

import com.example.midijfugue.StyleSetup;
import com.example.midijfugue.styles.BaseStyle;
import com.example.midijfugue.styles.RockStyle;
import com.example.midijfugue.styles.SimpleStyle;

public class StyleFactory {

    public static BaseStyle createStyle(StyleSetup setup){
        BaseStyle style = null;

        switch (setup.getStyle()){
            case "Simple":
                style = new SimpleStyle(setup);
                break;
            case "Rock":
                style = new RockStyle(setup);
                break;
        }

        return style;
    }
}
