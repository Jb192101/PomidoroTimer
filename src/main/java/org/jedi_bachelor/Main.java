package org.jedi_bachelor;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jedi_bachelor.viewmodel.ViewModel;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ViewModel vm = new ViewModel();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ico.png")));
        stage.getIcons().add(icon);

        vm.startApp();
    }
}