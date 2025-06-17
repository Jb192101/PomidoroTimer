package org.jedi_bachelor.view;

import javafx.scene.media.MediaPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jedi_bachelor.viewmodel.ViewModel;

public class View extends Stage {
    private Text timeText;
    private Button startButton;
    private Button pauseButton;
    TextField workField;
    TextField relaxField;
    TextField bigRelaxField;
    Button implementButton;

    private Arc arc;

    ViewThread vt;

    private ViewModel vm;

    // Для вывода звука
    private MediaPlayer mediaPlayer;

    public View(ViewModel _vm) {
        this.vm = _vm;
        this.vt = new ViewThread();

        setupUI();
    }

    private void setupUI() {
        // Бэк-круг (не трогаем его)
        Arc backgroundArc = new Arc();
        backgroundArc.setCenterX(200/2);
        backgroundArc.setCenterY(200/2);
        backgroundArc.setRadiusX(200/2 - 20);
        backgroundArc.setRadiusY(200/2 - 20);
        backgroundArc.setStartAngle(90);
        backgroundArc.setLength(360);
        backgroundArc.setType(ArcType.OPEN);
        backgroundArc.setStroke(Color.LIGHTGRAY);
        backgroundArc.setStrokeWidth(10);
        backgroundArc.setFill(null);

        // Основной круг
        arc = new Arc();
        arc.setCenterX(200/2);
        arc.setCenterY(200/2);
        arc.setRadiusX(200/2 - 20);
        arc.setRadiusY(200/2 - 20);
        arc.setStrokeWidth(10);
        arc.setStroke(Color.DODGERBLUE);
        arc.setStartAngle(90);
        arc.setFill(null);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStrokeWidth(10);

        timeText = new Text();
        timeText.setFont(Font.font(24));
        updateTimeDisplay();

        startButton = new Button("Запуск");
        startButton.setMinWidth(100);

        pauseButton = new Button("Пауза");
        pauseButton.setMinWidth(100);
        pauseButton.setDisable(true);

        startButton.setOnAction(e -> {
            vm.startTimer();
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            workField.setDisable(true);
            relaxField.setDisable(true);
            bigRelaxField.setDisable(true);
            implementButton.setDisable(true);
        });

        pauseButton.setOnAction(e -> {
            vm.pauseTimer();
            startButton.setDisable(false);
            pauseButton.setDisable(true);
        });

        StackPane arcs = new StackPane(backgroundArc, arc);

        VBox root1 = new VBox(20, arcs, timeText, startButton, pauseButton);
        root1.setAlignment(Pos.CENTER);
        root1.setPadding(new Insets(20));
        root1.setStyle("-fx-background-color: #f5f5f5;");

        // Настройки времени
        Label labelWork = new Label("Время работы (минуты):");
        workField = new TextField("25");
        Label labelRelax = new Label("Время отдыха (минуты):");
        relaxField = new TextField("5");
        Label labelBigRelax = new Label("Время большого отдыха (минуты):");
        bigRelaxField = new TextField("30");

        implementButton = new Button("Применить");
        implementButton.setOnAction(e -> {
            vm.setWorkTime(Integer.parseInt(workField.getText()));
            vm.setRelaxTime(Integer.parseInt(relaxField.getText()));
            vm.setBigRelaxTime(Integer.parseInt(relaxField.getText()));
        });

        VBox root2 = new VBox(20, labelWork, workField, labelRelax,
                relaxField, labelBigRelax, bigRelaxField, implementButton);
        root2.setAlignment(Pos.CENTER);
        root2.setPadding(new Insets(20));
        root2.setStyle("-fx-background-color: #f5f5f5;");

        HBox root = new HBox(20, root1, root2);

        Scene scene = new Scene(root, 460, 400);
        setTitle("Метод Помидора");
        setScene(scene);
        setResizable(false);
        show();
    }

    public void startCycle() {
        vt.start();
    }

    class ViewThread extends Thread {
        @Override
        public void run() {
            while(true) {
                if(vm.getTime() == 0) {
                    try {
                        mediaPlayer.play();
                        sleep(3000); // время условно, потом можно поменять
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (vm.getTime() <= 120) {
                    arc.setStroke(Color.ORANGE);
                } else {
                    arc.setStroke(Color.DODGERBLUE);
                }

                updateTimeDisplay();

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateTimeDisplay() {
        int minutes = vm.getTime() / 60;
        int seconds = vm.getTime() % 60;
        timeText.setText(String.format("%02d:%02d", minutes, seconds));
        arc.setLength(-360 * ((double) vm.getTime() / (60*60)));
    }
}
