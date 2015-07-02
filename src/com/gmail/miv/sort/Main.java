package com.gmail.miv.sort;


import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private static final int ELEMENTS_COUNT_MAX = (int) 8e5;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Adder");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Length");
        //creating the chart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Time, millisec");
        //defining a series
        XYChart.Series seriesArraysSort = new XYChart.Series();
        seriesArraysSort.setName("Arrays.sort");

        XYChart.Series seriesArraysParallelSort = new XYChart.Series();
        seriesArraysParallelSort.setName("Arrays.parallelSort");

        XYChart.Series seriesMyMergeSort = new XYChart.Series();
        seriesMyMergeSort.setName("My parallel merge sort");

        long elapsedTime;

        Random random = new Random();

        int arrayLength = 1;

        while (arrayLength < ELEMENTS_COUNT_MAX) {

            arrayLength = arrayLength << 1;

            System.out.println("arrayLength = " + arrayLength);

            int[] defaultArray = new int[arrayLength];

            for (int i = 0; i < defaultArray.length; i++) {
                defaultArray[i] = random.nextInt(20);
            }

            int[] array = new int[arrayLength];


            long start, end;

            array = Arrays.copyOf(defaultArray, defaultArray.length);

            start = System.nanoTime();
            Arrays.sort(array);
            end = System.nanoTime();

            elapsedTime = end - start;
            elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
            seriesArraysSort.getData().add(new XYChart.Data(arrayLength, elapsedTime));


            array = Arrays.copyOf(defaultArray, defaultArray.length);

            start = System.nanoTime();
            Arrays.parallelSort(array);
            end = System.nanoTime();
            elapsedTime = end - start;
            elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
            seriesArraysParallelSort.getData().add(new XYChart.Data(arrayLength, elapsedTime));

            array = Arrays.copyOf(defaultArray, defaultArray.length);

            start = System.nanoTime();
            new MergeSort(array);
            end = System.nanoTime();
            elapsedTime = end - start;
            elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
            seriesMyMergeSort.getData().add(new XYChart.Data(arrayLength, elapsedTime));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.setAnimated(false);
        lineChart.getData().add(seriesArraysSort);
        lineChart.getData().add(seriesArraysParallelSort);
        lineChart.getData().add(seriesMyMergeSort);

        primaryStage.setScene(scene);
        primaryStage.show();

        WritableImage wim = lineChart.snapshot(new SnapshotParameters(), null);

        scene.snapshot(wim);

        File file = new File("ChartImage_Sort.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }
    }
}
