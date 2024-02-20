package com.example.ba_transaktionen.klassen;

import javafx.scene.layout.Pane;

public class Position {

    private double x;
    private double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Position(Pane welt){
        //Zufalsspunkt auf der Anzeigefläsche erzeugen
        // -2*radius, auf jeder Seite etwas Abstand
        // + radius, damit es nicht bei Null startet
        // radius ist public in Person und immer der gleiche
        this(Person.radius + Math.random()*(welt.getWidth()-2*Person.radius),
                Person.radius + Math.random()*(welt.getHeight()-2*Person.radius));
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    //Euclidean distance, d.h. Abstand zwischen zwei Punkten
    public double punktabstand(Position other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y,2));
    }

    public void bewegen(Punkt punkt, Pane welt, Position ursprung){
        x += punkt.getDx();
        y += punkt.getDy();
        //Pane wichtig, weil man sonst nicht die Ränder bestimmen kann
        //wenn der x-Wert die unterste oder oberste Kannte berührt, soll verändert werden
        //radius, damit der Rand vom Kreis auch nicht ausserhalb der welt ist und nicht nur der Mittelpunkt
        //bewegungsradius, damit die Punkte sich nur im Radius von dem Regler bestimmten Wert befinden können
        if (x<Person.radius || x>welt.getWidth()-Person.radius || punktabstand(ursprung)>Person.bewegungsradius){
            punkt.bewegeX();
            x += punkt.getDx();
        }
        // wenn y die rechte oder linke Kante berührt, soll weg
        if (y<Person.radius || y>welt.getHeight()-Person.radius || punktabstand(ursprung)>Person.bewegungsradius){
            punkt.bewegeY();
            y += punkt.getDy();
        }
    }

    public boolean kollidieren(Position andere){
        //wenn dieser Punkt sich im Umfang von 2 * dem Radius befindet, kollidiert er
        return punktabstand(andere) < 2*Person.radius;
    }

}

