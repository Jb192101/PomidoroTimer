package org.jedi_bachelor.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jedi_bachelor.model.SoundPlayer;
import org.jedi_bachelor.viewmodel.ViewModel;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.Objects;

public class View extends Stage {
    private Text timeText;
    private Button startButton;
    private Button pauseButton;
    TextField workField;
    TextField relaxField;
    TextField bigRelaxField;
    Button implementButton;

    private Circle timerCircle;

    private ViewThread vt;

    private final ViewModel vm;

    private static Clip currentClip;

    public View(ViewModel _vm) {
        this.vm = _vm;
        this.vt = new ViewThread();

        setupUI();
    }

    private void setupUI() {
        timerCircle = new Circle();
        timerCircle.setRadius(80);
        timerCircle.setStrokeWidth(10);
        timerCircle.setFill(Color.DODGERBLUE);
        timerCircle.setStroke(Color.DODGERBLUE);

        Circle backgroundCircle = new Circle(80);
        backgroundCircle.setFill(Color.LIGHTGRAY);
        backgroundCircle.setStroke(Color.LIGHTGRAY);
        backgroundCircle.setStrokeWidth(10);

        StackPane circlePane = new StackPane(backgroundCircle, timerCircle);
        circlePane.setAlignment(Pos.CENTER);

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

        circlePane.setAlignment(Pos.CENTER);

        VBox root1 = new VBox(20, circlePane, timeText, startButton, pauseButton);
        root1.setAlignment(Pos.CENTER);
        root1.setPadding(new Insets(20));
        root1.setStyle("-fx-background-color: #f5f5f5;");

        // Настройки времени
        Label labelWork = new Label("Время работы (минуты):");
        workField = new TextField(String.valueOf(vm.getWorkTime()));
        Label labelRelax = new Label("Время отдыха (минуты):");
        relaxField = new TextField(String.valueOf(vm.getRelaxTime()));
        Label labelBigRelax = new Label("Время большого отдыха (минуты):");
        bigRelaxField = new TextField(String.valueOf(vm.getBigRelaxTime()));

        implementButton = new Button("Применить");
        implementButton.setOnAction(e -> {
            vm.setWorkTime(Integer.parseInt(workField.getText()));
            vm.setRelaxTime(Integer.parseInt(relaxField.getText()));
            vm.setBigRelaxTime(Integer.parseInt(relaxField.getText()));

            try {
                vm.saveSettings(workField.getText(), relaxField.getText(), bigRelaxField.getText());
            } catch(IOException ex) {
                ex.printStackTrace();
            }
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

        // Установка иконки
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ico.png")));
        getIcons().add(icon);

        // Действия при закрытии
        this.setOnCloseRequest(e -> {
            stopSound();

            new Thread(() -> {
                try {
                    stopCycle();
                    vm.stopWorkingThreads();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

            Platform.runLater(() -> this.close());
        });

        show();
    }

    public void startCycle() {
        vt.start();
    }

    public void stopCycle() {
        vt.interrupt();
    }

    class ViewThread extends Thread {
        private boolean soundPlayed = false;

        @Override
        public void run() {
            while(true) {
                if(vm.getTime() == 0) {
                    if (!soundPlayed) {
                        playSound();
                        soundPlayed = true;
                        pauseButton.setDisable(true);
                        startButton.setDisable(true);
                    }

                    try {
                        sleep(27000); // время на паузу после завершения
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    soundPlayed = false;

                    if (vm.getTime() <= 120) {
                        timerCircle.setStroke(Color.ORANGE);
                    } else {
                        timerCircle.setStroke(Color.DODGERBLUE);
                    }

                    updateTimeDisplay();
                }

                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateTimeDisplay() {
        int totalTime = 60 * vm.getWorkTime();
        int remainingTime = vm.getTime();
        double progress = (double) remainingTime / totalTime;

        timerCircle.setRadius(80 * progress);

        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        timeText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void stopSound() {
        SoundPlayer.stopSound();
    }

    public void playSound() {
        String soundFile = "/sound.wav";
        SoundPlayer.playSound(soundFile);
    }
}
