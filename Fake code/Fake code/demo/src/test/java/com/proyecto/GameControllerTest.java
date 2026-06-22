package com.proyecto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class GameControllerTest {

    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testInitialState(FxRobot robot) {
        // Verificar que las barras de HP se inicializan al 100% (1.0)
        ProgressBar playerHpBar = robot.lookup("#playerHpBar").queryAs(ProgressBar.class);
        ProgressBar bossHpBar = robot.lookup("#bossHpBar").queryAs(ProgressBar.class);
        
        assertNotNull(playerHpBar);
        assertNotNull(bossHpBar);
        assertEquals(1.0, playerHpBar.getProgress(), 0.01);
        assertEquals(1.0, bossHpBar.getProgress(), 0.01);

        // Verificar que la pregunta ha cambiado de su estado de carga
        Label questionLabel = robot.lookup("#questionLabel").queryAs(Label.class);
        assertNotNull(questionLabel);
        assertNotEquals("Loading Question...", questionLabel.getText());
        
        // Verificar que se hayan cargado opciones (botones) para la pregunta
        VBox optionsContainer = robot.lookup("#optionsContainer").queryAs(VBox.class);
        assertNotNull(optionsContainer);
        assertFalse(optionsContainer.getChildren().isEmpty(), "Debería haber opciones de respuesta cargadas");
    }
    
    @Test
    public void testClickingAnswerDisablesOptions(FxRobot robot) {
        VBox optionsContainer = robot.lookup("#optionsContainer").queryAs(VBox.class);
        
        // Asegurarnos de que hay opciones para hacer clic
        assertFalse(optionsContainer.getChildren().isEmpty());
        Button firstOption = (Button) optionsContainer.getChildren().get(0);
        
        // Al inicio las opciones no deben estar deshabilitadas
        assertFalse(optionsContainer.isDisable());
        
        // Simular un clic en la primera opción de respuesta
        robot.clickOn(firstOption);
        
        // Inmediatamente tras responder, el contenedor de opciones debe deshabilitarse para evitar dobles clics
        assertTrue(optionsContainer.isDisable());
        
        // Verificar que se le muestra retroalimentación al jugador
        Label feedbackLabel = robot.lookup("#feedbackLabel").queryAs(Label.class);
        String feedbackText = feedbackLabel.getText();
        assertTrue(feedbackText.equals("¡Correcto!") || feedbackText.equals("¡Incorrecto!"));
    }
}
