package de.hensel.stream;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author henself
 * @since 15.06.2021
 */
class ExampleTest {

    @Test
    void happyPath() {
        List<InputData> input = List.of(new InputData("Karl", 1));

        var result = Example.conversion(input);

        assertThat(result)
                .containsExactlyInAnyOrder(
                        new OutputData("Karl", 1)
                );
    }

    @Test
    void multiElements() {
        List<InputData> input = List.of(
                new InputData("Karl", 1),
                new InputData("Fred", 5),
                new InputData("Horst", 7)
        );

        var result = Example.conversion(input);

        assertThat(result)
                .containsExactlyInAnyOrder(
                        new OutputData("Karl", 1),
                        new OutputData("Fred", 5),
                        new OutputData("Horst", 7)
                );
    }

    @Test
    void zusammenfassung() {
        List<InputData> input = List.of(
                new InputData("Karl", 1),
                new InputData("Karl", 5),
                new InputData("Horst", 7),
                new InputData("Horst", 8)
        );

        var result = Example.conversion(input);

        assertThat(result)
                .containsExactlyInAnyOrder(
                        new OutputData("Karl", 3),
                        new OutputData("Horst", 7.5)
                );
    }
}
