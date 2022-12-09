package pl.malluk.algorithms;

import java.io.*;
import java.util.Scanner;

public class Garden {
    private String inputFilePath;
    private String outputFilePath;
    private int[][] apples;

    public Garden(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public Garden(int[][] apples) {
        this.apples = apples;
    }

    public int findPathWithMaxApples() {
        if (apples == null) {
            apples = readInputFile();
        }

        int[][] sumOfApples = createSumOfApplesTable();

        for (int i = 0; i < apples.length; i++) {
            for (int j = 0; j < apples[0].length; j++) {
                int leftElementSum = isEdgePosition(i) ? 0 : sumOfApples[i - 1][j];
                int topElementSum = isEdgePosition(j) ? 0 : sumOfApples[i][j - 1];
                sumOfApples[i][j] = Math.max(leftElementSum, topElementSum) + apples[i][j];
            }
        }
        return sumOfApples[apples.length - 1][apples[0].length - 1];
    }

    private int[][] readInputFile() {
        try (Scanner scanner = new Scanner(new FileInputStream(inputFilePath))) {
            int numberOfRows = scanner.nextInt();
            int numberOfColumns = scanner.nextInt();

            int[][] result = new int[numberOfRows][];
            for (int i = 0; i < result.length; i++) {
                result[i] = new int[numberOfColumns];
                for (int j = 0; j < result[i].length; j++) {
                    result[i][j] = scanner.nextInt();
                }
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("An input file does not exist: " + inputFilePath);
        }
    }

    private int[][] createSumOfApplesTable() {
        int[][] sumOfApples = new int[apples.length][];

        for (int i = 0; i < apples.length; i++) {
            sumOfApples[i] = new int[apples[0].length];
        }
        return sumOfApples;
    }

    private boolean isEdgePosition(int position) {
        return position == 0;
    }

    public void saveResultToFile() {
        try (Writer writer = new FileWriter(outputFilePath)) {
            writer.write(Integer.toString(findPathWithMaxApples()));
        } catch (IOException e) {
            throw new IllegalArgumentException("An output file cannot be created: " + outputFilePath);
        }
    }
}
