package com.proyecto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameController implements Initializable {

    @FXML private ProgressBar playerHpBar;
    @FXML private ProgressBar bossHpBar;
    @FXML private ImageView bossImage;
    @FXML private Label questionLabel;
    @FXML private VBox optionsContainer;
    @FXML private Label feedbackLabel;

    private int playerMaxHp = 3;
    private int playerHp = 3;
    private int bossMaxHp = 5;
    private int bossHp = 5;

    private List<Question> questions;
    private int currentQuestionIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadQuestions();
        Collections.shuffle(questions);
        
        try {
            Image img = new Image(getClass().getResourceAsStream("boss.png"));
            bossImage.setImage(img);
            startBossAnimation();
        } catch (Exception e) {
            System.err.println("Could not load boss image.");
        }

        updateHpBars();
        displayQuestion();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("¿Qué significa POO en programación?", 
                Arrays.asList("Programación Orientada a Objetos", "Proceso de Operaciones Ocultas", "Programación Operacional Optimizada", "Protocolo de Objetos Organizados"), 0));
        questions.add(new Question("¿Cuál de estos lenguajes se ejecuta principalmente en el navegador?", 
                Arrays.asList("Java", "C++", "JavaScript", "Python"), 2));
        questions.add(new Question("¿Qué estructura de datos usa LIFO (Last In, First Out)?", 
                Arrays.asList("Cola (Queue)", "Lista enlazada", "Árbol", "Pila (Stack)"), 3));
        questions.add(new Question("¿Qué palabra clave se usa para heredar una clase en Java?", 
                Arrays.asList("implement", "extends", "inherits", "super"), 1));
        questions.add(new Question("¿Qué es un bucle infinito?", 
                Arrays.asList("Un bucle con muchas iteraciones", "Un bucle que nunca termina su ejecución", "Una función recursiva", "Un error de compilación"), 1));
        questions.add(new Question("En bases de datos, ¿qué significa SQL?", 
                Arrays.asList("Structured Query Language", "System Query Logic", "Simple Question Language", "Standard Query Language"), 0));
        questions.add(new Question("¿Cuál de las siguientes es una excepción verificada (checked exception) en Java?", 
                Arrays.asList("NullPointerException", "ArithmeticException", "IOException", "IndexOutOfBoundsException"), 2));
    }

    private void startBossAnimation() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), bossImage);
        transition.setByY(-15f);
        transition.setAutoReverse(true);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            Collections.shuffle(questions);
            currentQuestionIndex = 0;
        }

        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText(q.getText());
        optionsContainer.getChildren().clear();

        List<String> options = q.getOptions();
        for (int i = 0; i < options.size(); i++) {
            Button btn = new Button(options.get(i));
            btn.getStyleClass().add("option-button");
            final int selectedIndex = i;
            btn.setOnAction(e -> handleAnswer(selectedIndex, q.getCorrectIndex()));
            optionsContainer.getChildren().add(btn);
        }
    }

    private void handleAnswer(int selectedIndex, int correctIndex) {
        optionsContainer.setDisable(true);

        if (selectedIndex == correctIndex) {
            bossHp--;
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setTextFill(Color.web("#2ecc71"));
            bossDamageAnimation();
        } else {
            playerHp--;
            feedbackLabel.setText("¡Incorrecto!");
            feedbackLabel.setTextFill(Color.web("#e74c3c"));
            playerDamageAnimation();
        }

        updateHpBars();

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            feedbackLabel.setText("");
            if (bossHp <= 0) {
                endGame("¡Has derrotado al Boss! ¡Ganaste!", Color.web("#f1c40f"));
            } else if (playerHp <= 0) {
                endGame("Has sido derrotado. ¡Fin del juego!", Color.web("#e74c3c"));
            } else {
                currentQuestionIndex++;
                optionsContainer.setDisable(false);
                displayQuestion();
            }
        });
        pause.play();
    }

    private void updateHpBars() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5),
                new KeyValue(playerHpBar.progressProperty(), (double) playerHp / playerMaxHp),
                new KeyValue(bossHpBar.progressProperty(), (double) bossHp / bossMaxHp)
            )
        );
        timeline.play();
    }

    private void bossDamageAnimation() {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), bossImage);
        shake.setByX(10f);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void playerDamageAnimation() {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), questionLabel.getParent());
        shake.setByX(-10f);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void endGame(String message, Color color) {
        questionLabel.setText(message);
        questionLabel.setTextFill(color);
        optionsContainer.getChildren().clear();
        if (bossHp <= 0) {
            bossImage.setVisible(false);
        }
    }
}
