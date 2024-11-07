package view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OutputViewTest {
    OutputView outputView = new OutputView();
    ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoresStreams() {
        System.setOut(System.out);
    }

    @Test
    @DisplayName("출력할 메시지를 넘기면 이를 제대로 출력 하는지 확인하는 테스트입니다.")
    public void 일반_메시지_출력_테스트() {
        String testOutputString = "hello everyone :)";
        outputView.printMessage(testOutputString);

        assertTrue(outContent.toString().contains(testOutputString));
    }
}
