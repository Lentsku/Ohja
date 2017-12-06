package matopeli;

import matopeli.domain.Matopeli;
import matopeli.domain.Omena;
import matopeli.domain.Suunta;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MatopeliSovellus extends Application {

    @Override
    public void start(Stage ikkuna) throws Exception {
        int ruudunkoko = 20;
        int ruutuja = 30;

        Canvas piirtoalusta = new Canvas(ruutuja * ruudunkoko, ruutuja * ruudunkoko);
        GraphicsContext piirturi = piirtoalusta.getGraphicsContext2D();

        Matopeli matopeli = new Matopeli(ruutuja, ruutuja);
        
//         pelin piirtäminen tapahtuu 30 kertaa sekunnissa
        new AnimationTimer() {
            private long edellinen;

            @Override
            public void handle(long nykyhetki) {
                if (nykyhetki - edellinen < 1_000_000_000 / 30) {
                    return;
                }
                edellinen = nykyhetki;

                piirturi.setFill(Color.BLACK);
                piirturi.fillRect(0, 0, ruutuja * ruudunkoko, ruutuja * ruudunkoko);

                piirturi.setFill(Color.WHITE);

                if (matopeli.loppu()) {
                    piirturi.setFill(Color.LIGHTGRAY);
                }

                matopeli.getMato().getPalat().stream().forEach(pala -> {
                    piirturi.fillRect(pala.getX() * ruudunkoko, pala.getY() * ruudunkoko, ruudunkoko, ruudunkoko);
                });

                piirturi.setFill(Color.RED);
                Omena omena = matopeli.getOmena();
                piirturi.fillRect(omena.getX() * ruudunkoko, omena.getY() * ruudunkoko, ruudunkoko, ruudunkoko);

            }
        }.start();

//         pelin päivittäminen (madon liikkuminen ym) tapahtuu 5 kertaa sekunnissa
        new AnimationTimer() {
            private long edellinen;

            @Override
            public void handle(long nykyhetki) {
                if (nykyhetki - edellinen < 1_000_000_000 / 7) {
                    return;
                }
                edellinen = nykyhetki;

                matopeli.paivita();

                if (matopeli.loppu()) {
                    stop();
                }
            }
        }.start();

        BorderPane asettelu = new BorderPane();
        asettelu.setCenter(piirtoalusta);

        Scene nakyma = new Scene(asettelu);

//         tapahtuman käsittely (näppäimistön kuuntelu)
        nakyma.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.UP)) {
                if (matopeli.getMato().getSuunta().ordinal() != Suunta.ALAS.ordinal()) {
                    matopeli.getMato().setSuunta(Suunta.YLOS);
                }
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                if (matopeli.getMato().getSuunta().ordinal() != Suunta.YLOS.ordinal()) {
                    matopeli.getMato().setSuunta(Suunta.ALAS);
                }
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                if (matopeli.getMato().getSuunta().ordinal() != Suunta.VASEN.ordinal()) {
                    matopeli.getMato().setSuunta(Suunta.OIKEA);
                }
            } else if (event.getCode().equals(KeyCode.LEFT)) {
                if (matopeli.getMato().getSuunta().ordinal() != Suunta.OIKEA.ordinal()) {
                    matopeli.getMato().setSuunta(Suunta.VASEN);
                }
            }
        });

        ikkuna.setScene(nakyma);
        ikkuna.show();

    }

    public static void main(String[] args) {
        launch(MatopeliSovellus.class);
    }

}
