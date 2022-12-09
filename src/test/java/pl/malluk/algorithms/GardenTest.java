package pl.malluk.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GardenTest {

    @ParameterizedTest()
    @MethodSource("findPathWithMaxApplesProvider")
    void shouldFindPathWithMaxApples(int[][] garden, int expectedResult) {
        assertEquals(expectedResult, new Garden(garden).findPathWithMaxApples());
    }

    static Stream<Arguments> findPathWithMaxApplesProvider() {
        return Stream.of(
                Arguments.of(new int[][]{
                        new int[]{0}
                }, 0),
                Arguments.of(new int[][]{
                        new int[]{1, 1, 1},
                        new int[]{1, 1, 1},
                        new int[]{1, 1, 1}
                }, 5),
                Arguments.of(new int[][]{
                        new int[]{1, 1, 1000},
                        new int[]{1, 1, 1},
                        new int[]{1, 1, 1}
                }, 1004),
                Arguments.of(new int[][]{
                        new int[]{1, 1, 1},
                        new int[]{1, 1, 1},
                        new int[]{1000, 1, 1}
                }, 1004)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "input1.txt, output1.txt",
            "input2.txt, output2.txt",
    })
    void shouldSaveResultToFile(String inputFileName, String outputFileName, @TempDir Path path) throws IOException {
        //GIVEN
        String inputFilePath = GardenTest.class.getClassLoader().getResource(inputFileName).getFile();
        String outputFilePath = GardenTest.class.getClassLoader().getResource(outputFileName).getFile();
        String resultFilePath = path.resolve("output.txt").toAbsolutePath().toString();

        //WHEN
        new Garden(inputFilePath, resultFilePath).saveResultToFile();

        //THEN
        assertArrayEquals(Files.readAllBytes(new File(outputFilePath).toPath()),
                Files.readAllBytes(new File(resultFilePath).toPath()));
    }

    @Test
    void shouldThrowExceptionWhenInputFileIsNull(@TempDir Path path) {
        assertThrows(NullPointerException.class, () -> {
            String outputFilePath = path.resolve("output.txt").toAbsolutePath().toString();

            new Garden(null, outputFilePath).saveResultToFile();
        });
    }

    @Test
    void shouldThrowExceptionWhenInputFileDoesNotExist(@TempDir Path path) {
        String outputFilePath = path.resolve("output.txt").toAbsolutePath().toString();

        assertThrows(IllegalArgumentException.class, () -> {
            new Garden("Z://test.test.test", outputFilePath).saveResultToFile();
        });
    }

    @Test
    void shouldThrowExceptionWhenOutputFileIsNull() {
        assertThrows(NullPointerException.class, () -> {
            String inputFilePath = GardenTest.class.getClassLoader().getResource("input1.txt").getFile();

            new Garden(inputFilePath, null).saveResultToFile();
        });
    }

    @Test
    void shouldThrowExceptionWhenOutputFileDoesNotExist() {
        String inputFilePath = GardenTest.class.getClassLoader().getResource("input1.txt").getFile();

        assertThrows(IllegalArgumentException.class, () -> {
            new Garden(inputFilePath, "Z://test.test.test").saveResultToFile();
        });
    }
}