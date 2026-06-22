package com.proyecto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    public void testQuestionInitialization() {
        String text = "¿Qué significa POO en programación?";
        List<String> options = Arrays.asList(
                "Programación Orientada a Objetos", 
                "Proceso de Operaciones Ocultas", 
                "Programación Operacional Optimizada", 
                "Protocolo de Objetos Organizados"
        );
        int correctIndex = 0;

        Question question = new Question(text, options, correctIndex);

        assertEquals(text, question.getText());
        assertEquals(options, question.getOptions());
        assertEquals(correctIndex, question.getCorrectIndex());
        assertEquals(4, question.getOptions().size());
    }

    @Test
    public void testGetCorrectOption() {
        List<String> options = Arrays.asList("A", "B", "C");
        Question question = new Question("Test", options, 1);
        
        assertEquals(1, question.getCorrectIndex());
        assertEquals("B", question.getOptions().get(question.getCorrectIndex()));
    }
}
