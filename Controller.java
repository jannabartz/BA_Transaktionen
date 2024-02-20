package com.example.ba_transaktionen;

import com.example.ba_transaktionen.klassen.Person;
import com.example.ba_transaktionen.klassen.Simulation;
import com.example.ba_transaktionen.klassen.Zustand;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumMap;

public class Controller {

    @FXML
    Button resetB;
    @FXML
    Button startB;
    @FXML
    Button stopB;
    @FXML
    Button stepB;
    @FXML
    Button chartB;
    @FXML
    Slider anzahlSlider;
    @FXML
    Slider transaktionSlider;
    @FXML
    Slider abstandSlider;
    @FXML
    TextField tickText;
    @FXML
    ToggleButton hackerOn;
    @FXML
    Pane world;
    Simulation sim;
    int Teilnehmeranzahl = 50;

    double WkeitTransaktion = 0.5;
    private Movement clock;
    ControllerCharts chartFenster;
    EnumMap<Zustand, Rectangle> hrects = new EnumMap<Zustand, Rectangle>(Zustand.class);

    public class Movement extends AnimationTimer{

        private long FRAMES_PER_SEC = 50L;
        //eine Null hinzugefügt, damit sich die Punkte langsamer bewegen
        private long INTERVAL = 10000000000L/FRAMES_PER_SEC;
        private long last = 0;
        private int ticks = 0;

        @Override
        //nach einer kurzen Zeit werden die Punkte immer wieder bewegt
        public void handle(long now) {
            if(now-last > INTERVAL){
                step();
                last = now;
                ticks++;
            }
        }
        public int getTicks(){
            return ticks;
        }
        public void resetTicks(){
            ticks = 0;
        }
        public void tick(){
            ticks++;
        }
    }

    //erste Methode die Aufgerufen wird, wenn sich das Fenster öffnet
    @FXML
    public void initialize(){
        //Slider soll sich automatisch aktualisieren, beim bewegen
        anzahlSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setAnzahl();
            }
        });
        transaktionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setWkeitTransaktion();
            }
        });
        abstandSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setDistance();
            }
        });
        clock = new Movement();
        world.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
    }

    @FXML
    public void reset(){
        //man soll simulation nicht stoppen können, da es ja noch nicht läuft
        //Charts sollen auch stoppen, muss weitergegeben werden
        stop();
        clock.resetTicks();
        tickText.setText(""+clock.getTicks());
        world.getChildren().clear();
        //muss bei Reset auch schon initialisiert werden, weil sonst läuft es da nicht mit
        sim = new Simulation(world, Teilnehmeranzahl);
        if(chartFenster!=null) {
            //wichtige Abfrage, da ansonsten chartfenster (vor drücken des button) noch nicht initialisiert wurde und es eine NullPointerException gibt
            chartFenster.reset(sim, Teilnehmeranzahl, clock);
        }
        //wichtig, damit die Slider richtig angezeigt werden
        setAnzahl();
        setWkeitTransaktion();
        setDistance();


    }

    public void setAnzahl(){
        //Person.radius = (int) anzahlSlider.getValue();
        Teilnehmeranzahl = (int) anzahlSlider.getValue();
        sim.malen();
    }

    public void setWkeitTransaktion(){
        //bei einer Kollision soll nicht immer eine Transaktion ausgeführt werden, sondern nur mit einer bestimmten Wahrscheinlichkeit
        WkeitTransaktion = transaktionSlider.getValue();
    }

    public void setDistance(){
        //sollen sich beim Runtersetzen des Reglers in einem geringeren Radius bewegen
        Person.bewegungsradius = (int) abstandSlider.getValue();
    }

    @FXML
    public void start(){
        clock.start();
        disableButton(true,false,true);
    }

    @FXML
    public void stop(){
        clock.stop();
        disableButton(false,true,false);
    }
    @FXML
    public void step(){
        sim.bewegen();
        sim.transaktionAbschließen();
        sim.prüfeKollision(WkeitTransaktion);
        sim.malen();
        clock.tick();
        tickText.setText(""+clock.getTicks());
        //System.out.println(anzahlSlider.getValue());
        //System.out.println(Person.summeTransaktionen+" ");
        //überlegen, wieso nicht angezeigt wird?
        //chartFenster.anteilTransaktionen.setText((Person.summeTransaktionen/anzahlSlider.getValue())*100 + " %");
        //System.out.println(chartFenster.anteilTransaktionen.getText());
        if(chartFenster!=null) {
            chartFenster.drawCharts(sim, Teilnehmeranzahl, clock);
        }
    }

    //neues Fenster mit Charts soll sich öffnen, um die Übersichtlichkeit zu verbessern
    @FXML
    void chartsAnzeigen(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ChartGui.fxml"));
            Scene scene2 = new Scene(fxmlLoader.load());
            chartFenster = fxmlLoader.getController();
            //falsch:
            //chartFenster = new ControllerCharts();
            //einbauen, dass wenn Fenster geschlossen wird, man wieder den Button drücken kann
            chartB.setDisable(true);
            //bei Öffnen soll ControllerChart initialisiert werden
            Stage stage2 = new Stage();
            stage2.setTitle("Diagramme zu der Simulation");
            stage2.setScene(scene2);
            stage2.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disableButton(boolean start, boolean stop, boolean step){
        startB.setDisable(start);
        stopB.setDisable(stop);
        stepB.setDisable(step);
    }


}