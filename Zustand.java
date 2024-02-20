package com.example.ba_transaktionen.klassen;

import javafx.scene.paint.Color;

public enum Zustand {

    neutral{
        public Color getColor(){
            return Color.BEIGE;
        }
    },
    transaktion {
        public Color getColor(){
            return Color.BLUE;
        }
    };

    public abstract Color getColor();
}
