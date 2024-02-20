package com.example.ba_transaktionen.klassen;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Person {

    public static int radius = 5;
    //3 Sekunden um Transaktion durchzuführen, aber wir zählen 50-Mal eine Sekunde, deshalb *50
    public static int transaktionszeit = 3*50;
    //in welchem Radius (vom Regler manipulierbar) bewegen sich die Punkte
    public static int bewegungsradius = 200;
    //Anzahl der Transaktionen die grade stattfinden (also blau leuchten), für Anteildiagramm
    public static double summeTransaktionen;
    //Anzahl der Coins die insgesamt im Umlauf sind, für Coins-Graph, werden aufsummiert, da immer das angezeigt wird
    //was insgesamt an Coins herausgegeben wurde
    public static int coinsUmlaufWert;

    //dürfen nicht static sein, da für jede Instanz andere Werte gelten
    private Zustand zustand;
    private Position position;
    private Position ursprung;
    private Punkt punkt;
    private Circle kreis;
    private Pane welt;
    //Zähler um zu gucken wie lange eine Transaktion dauert
    private int transaktionszähler = 0;

    public Person (Zustand zustand, Pane welt){
        this.zustand = zustand;
        this.punkt = new Punkt();
        this.position = new Position(welt);
        //man kann den ursprung nicht gleich position setzen, da sich diese ändert
        this.ursprung = new Position(position.getX(), position.getY());
        this.kreis = new Circle(radius, zustand.getColor());
        this.welt = welt;
        kreis.setStroke(Color.BLACK);
        welt.getChildren().add(kreis);
    }

    public Zustand getZustand(){
        return zustand;
    }

    public void setZustand(Zustand zustand){
        this.zustand = zustand;
        kreis.setFill(zustand.getColor());
    }

    public void bewegen(){
        position.bewegen(punkt, welt, ursprung);
    }

    public void punktAnzeigen(){
        kreis.setRadius(radius);
        kreis.setTranslateX(position.getX());
        kreis.setTranslateY(position.getY());
    }

    public void kollisionCheck(Person other, double WkeitTransaktion){
        if(position.kollidieren(other.position)){
            //Math.random Zahl von 0 bis kleiner 1, bestimmt Wkeit von Stattfinden der Transaktion
            double zufallszahl = Math.random();
            //wenn zwei Punkte kollidieren, führen sie eine Transaktion mit Wkeit von Slider Transaktion aus
            //können nur Transaktion ausführen, wenn sie grade nicht in einer sind
            if(other.getZustand()== Zustand.neutral && zustand == Zustand.neutral && zufallszahl<WkeitTransaktion){
                setZustand(Zustand.transaktion);
                other.setZustand(Zustand.transaktion);
                summeTransaktionen++;
                //bei jeder Transaktion geraten 1-5 Coins in Umlauf, diese werden addiert, sodass mit zunehmenden
                //Transaktionen auch zunehmende Coins in Umlauf geraten
                coinsUmlaufWert += (int) Math.random()*5 +1;
            }
        }
    }

    public void transaktionAbschließen(Person other){
        if(zustand == Zustand.transaktion && other.getZustand()== Zustand.transaktion){
            transaktionszähler++;
            //wenn Zeit abgelaufen ist, sollen wieder die Transaktionsteilnehmer neutral sein
            if(transaktionszähler>transaktionszeit){
                setZustand(Zustand.neutral);
                other.setZustand(Zustand.neutral);
                summeTransaktionen--;
            }
        }
    }
}
