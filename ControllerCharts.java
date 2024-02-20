package com.example.ba_transaktionen;

import com.example.ba_transaktionen.klassen.Person;
import com.example.ba_transaktionen.klassen.Simulation;
import com.example.ba_transaktionen.klassen.Zustand;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.EnumMap;


public class ControllerCharts {

    @FXML
    Pane coinsUmlauf;
    @FXML
    Pane anteilTeilnehmer;
    @FXML
    Pane coinsDurchschnitt;
    @FXML
    Label anteilTransaktionen;
    @FXML
    ScrollPane scrollPane;
    @FXML
    GridPane gridPane;
    @FXML
    VBox vboxAT;


    LineChart<Number, Number> lineChartUmlauf = new LineChart<>(new NumberAxis(), new NumberAxis());
    XYChart.Series<Number, Number> lineChartDatenUmlauf = new XYChart.Series<>();
    LineChart<Number, Number> lineChartKurs = new LineChart<>(new NumberAxis(), new NumberAxis());
    XYChart.Series<Number, Number> lineChartDatenKurs = new XYChart.Series<>();
    boolean wurdehinzugefügt = false;

    EnumMap<Zustand, Rectangle> hrects = new EnumMap<Zustand, Rectangle>(Zustand.class);


    //erste Methode die Aufgerufen wird, wenn sich das Fenster öffnet
    @FXML
    public void initialize() {
        //Test, um zu gucken ob gridPane funktioniert
        //gridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }


    public void reset(Simulation sim, int Teilnehmeranzahl, Controller.Movement clock){

        //vorher muss alles gelöscht werden, dann können Balken neu gezeichnet werden
        anteilTransaktionen.setText("0.0 %");
        anteilTeilnehmer.getChildren().clear();
        //reset funktioniert nur einmal, nachgucken!!!
        lineChartUmlauf.getData().clear();
       // lineChartDatenUmlauf.getData().clear();
        lineChartDatenUmlauf = new XYChart.Series<>();
        //lineChartDatenUmlauf.getNode().setStyle("-fx-stroke: blue; ");
        lineChartUmlauf.getData().add(lineChartDatenUmlauf);
        coinsUmlauf.getChildren().clear();
        //lineChartUmlauf.setStyle("-fx-stroke: blue;");
        //damit Punkte nicht angezeigt werden
        lineChartUmlauf.setCreateSymbols(false);
        lineChartUmlauf.setMaxHeight(250);
        //damit Überschrift nicht angezeigt wird
        lineChartUmlauf.setLegendVisible(false);
        coinsUmlauf.getChildren().add(lineChartUmlauf);
        //lineChartDatenKurs.getData().clear();
        //Muss neue Serie, weil es sonst zu Fehlermeldung kommt, dass Serie mehrfach hinzugefügt wird
        lineChartDatenKurs = new XYChart.Series<>();
        lineChartKurs.getData().clear();
            lineChartKurs.getData().add(lineChartDatenKurs);
            wurdehinzugefügt = true;
        coinsDurchschnitt.getChildren().clear();
        lineChartKurs.setCreateSymbols(false);
        lineChartKurs.setMaxHeight(250);
        lineChartKurs.setLegendVisible(false);
        coinsDurchschnitt.getChildren().add(lineChartKurs);

        int offset = 0;
        for(Zustand zustand : Zustand.values()){
            Rectangle balken = new Rectangle(60,0, zustand.getColor());
            balken.setStroke(Color.BLACK);
            //damit sie nebeneinander gemalt werden
            balken.setTranslateX(offset);
            offset +=65;
            hrects.put(zustand,balken);
            anteilTeilnehmer.getChildren().add(balken);
        }

        //drawCharts(sim, Teilnehmeranzahl, clock);
    }

    //wird bei jedem Ticken aufgerufen
    public void drawCharts(Simulation sim, int Teilnehmeranzahl, Controller.Movement clock) {

        //beim Runden mit 10000.0 multiplizieren, um Zahl auf 4 Nachkommastellen zu bringen und dann zu runden
        double gerundeteProzentzahl = Math.round((Person.summeTransaktionen/Teilnehmeranzahl)*100 * 10000.0) / 10000.0;
        anteilTransaktionen.setText(gerundeteProzentzahl+" %");
        //Überlegen ob überhaupt Sinn macht... ist auch nicht gut zu berechnen, weil die Punkte ja nur ganz kurz kollidieren

        EnumMap<Zustand, Integer> aktuelleTeilnehmer = new EnumMap<Zustand, Integer>(Zustand.class);
        for(Person person:sim.getPersonen()){
            //wenn man keinen aktuellen Stand hat, muss der initialisiert werden
            if(!aktuelleTeilnehmer.containsKey(person.getZustand())){
                aktuelleTeilnehmer.put(person.getZustand(),0);
            }
            aktuelleTeilnehmer.put(person.getZustand(), 1+ aktuelleTeilnehmer.get(person.getZustand()));
        }
        //int summeTransaktionen = 0;

        for(Zustand zustand : hrects.keySet()){
            if(aktuelleTeilnehmer.containsKey(zustand)){
                //bei 15 Leuten, soll Höhe 15 sein
                hrects.get(zustand).setHeight(aktuelleTeilnehmer.get(zustand));
                //bei Null starten, wäre das Histogramm falsch rum, deshalb:
                //100 wegen 100 Population
                hrects.get(zustand).setTranslateY(30+100-aktuelleTeilnehmer.get(zustand));

                /*Circle circle = new Circle(1, zustand.getColor());
                circle.setTranslateX(clock.getTicks()/5.0);
                circle.setTranslateY(130-aktuelleTeilnehmer.get(zustand));
                circle.setTranslateY(Person.summeTransaktionen*5);
                //in Scene Builder definieren
                //überarbeiten:
                //Idee: Random-Werte von 100-150 als Coinanzahl, sodass bei jeder Transaktion 100-150 Coins in Umlauf kommen
                // + Chart mit Durchschnittswerten
                coinsUmlauf.getChildren().add(circle);*/

                //nachgucken wie Style funktioniert
                //lineChart.lookup(".default-color0.chart-line-symbol").setStyle("-fx-background-color: transparent;");
                lineChartDatenUmlauf.getData().add(new XYChart.Data<>(clock.getTicks() / 5.0, Person.coinsUmlaufWert));
                //Farbe der Daten muss geändert werden, damit sich LineChart Linie ändert
                lineChartDatenUmlauf.getNode().setStyle("-fx-stroke: blue; ");
                //Idee: Kurs bestimmt sich durch die Transaktionen (also die Nachfrage), wobei sich eine Transaktion positiv oder negativ auswirken kann
                double randomAuswirkung = ((Math.random() * 0.2) - 0.1);
                lineChartDatenKurs.getData().add(new XYChart.Data<>(clock.getTicks()/5.0, Person.summeTransaktionen *  randomAuswirkung));
                lineChartDatenKurs.getNode().setStyle("-fx-stroke: #bb1212;");
            }
        }


    }

}
