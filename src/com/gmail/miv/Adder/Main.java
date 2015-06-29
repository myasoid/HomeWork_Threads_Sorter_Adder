package com.gmail.miv.Adder;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main extends Application {
    private static final int ELEMENTS_COUNT = (int) 6e8;

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Threads");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Nanotime");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My research :)");

        Random random = new Random();

        int[] array = new int[ELEMENTS_COUNT];

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(20);
        }

        int leftBorder, rightBorder;
        // size of thread pool
        for (int i = 1; i <= 20; i++) {


            long nanoTimeBegin = System.nanoTime();

            ExecutorService pool = Executors.newFixedThreadPool(i);
            List<Future<Long>> futures = new ArrayList<Future<Long>>(i);

            int len = (ELEMENTS_COUNT + i - 1) / i;
            for (int j = 0; j < i; j++) {
                int left = j * len;
                int right = Math.min(ELEMENTS_COUNT - 1, left + len - 1);
                futures.add(pool.submit(new Adder(array, left, right)));
            }

            Long result = 0L;
            for (Future<Long> future : futures) {
                try {
                    result += future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //Compute the result
            }

            pool.shutdown();

            long nanoTimeEnd = System.nanoTime();

            System.out.println(" i = " + i + " result = " + result + " time = " + (nanoTimeEnd - nanoTimeBegin));
            series.getData().add(new XYChart.Data(i, nanoTimeEnd - nanoTimeBegin));

            // Thread.sleep(2000);

        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
