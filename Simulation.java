package com.example.ba_transaktionen.klassen;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Simulation {

    private ArrayList<Person> personenListe;

    //alles muss der "Welt" hinzugefügt werden
    public Simulation(Pane welt, int personenanzahl){
        personenListe = new ArrayList<Person>();
        //alle am Anfang neutral
        for(int i=0; i<personenanzahl; i++){
            personenListe.add(new Person(Zustand.neutral, welt));
        }
        //malen wichtig, sonst sammeln sich alle Punkte an einer Stelle
        malen();
    }

    public ArrayList<Person> getPersonen(){
        return personenListe;
    }

    public void bewegen(){
        for(Person p:personenListe){
            p.bewegen();
        }
    }

    public void malen(){
        for(Person p:personenListe){
            p.punktAnzeigen();
        }
    }

    public void prüfeKollision(double WkeitTransaktion){
        for(Person person1:personenListe){
            for (Person person2:personenListe){
                //um zu verhindern, dass man mit sich selbst "kollidiert"
                if(person1!=person2){
                    person1.kollisionCheck(person2, WkeitTransaktion);
                }
            }
        }
    }

    public void transaktionAbschließen(){
        for(Person person1:personenListe){
            for (Person person2:personenListe){
                //um zu verhindern, dass man mit sich selbst abschließt
                if(person1!=person2){
                    person1.transaktionAbschließen(person2);
                }
            }
        }
    }

}
