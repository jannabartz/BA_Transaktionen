package com.example.ba_transaktionen.klassen;

public class Punkt {

    public static final double SPEED = 3;
    private double dx;
    private double dy;

    public Punkt(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public Punkt(){
        //bei Java nicht 360 Grad, sondern Radien von Null zu 2 Pi
        double dir = Math.random() *2 * Math.PI; //Richtung in Radien als Winkel und zufällige Bewegung
        dx = Math.sin(dir);
        dy = Math.cos(dir); //x- und y-Werte auf Karte
    }

    public double getDx(){
        return dx*SPEED;
    }

    public double getDy(){
        return dy*SPEED;
    }

    public void bewegeX(){
        dx*=-1;
    }

    public void bewegeY(){
        dy*=-1;
    }
}
