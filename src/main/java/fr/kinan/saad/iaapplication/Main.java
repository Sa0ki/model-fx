package fr.kinan.saad.iaapplication;

import fr.kinan.saad.iaapplication.services.ModelLoading;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private Thread modelThread;
    private Thread timerThread;
    private Timeline timeline;
    private long startTime;
    private Label timeLabel;
    private TextArea logs;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Model - App");

        timeLabel = new Label();
        timeLabel.setVisible(true);
        timeLabel.setText("Elapsed time: 0s");

        logs = new TextArea();
        logs.setEditable(false);
        logs.setPrefRowCount(30);

        Button button = new Button("Use the model");
        button.setOnAction(actionEvent -> {
            button.setDisable(true);

            Task<Void> timerTask = new Task<>() {
                @Override
                protected Void call() {
                    startTime = System.currentTimeMillis();
                    startTimer();
                    return null;
                }
            };

            Task<Void> modelTask = new Task<>() {
                @Override
                protected Void call() {
                    ModelLoading.run(logs);
                    return null;
                }
            };
            modelTask.setOnSucceeded(e -> {
                stopTimer();
                button.setDisable(false);
            });

            modelThread = new Thread(modelTask);
            timerThread = new Thread(timerTask);

            modelThread.start();
            timerThread.start();
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(button, timeLabel, logs);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 400, 400);
        stage.setScene(scene);

        stage.show();
    }

    private void startTimer(){
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            timeLabel.setText("Elapsed time: " + elapsedTime + "s");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void stopTimer(){
        if(timeline != null)
            timeline.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}