package com.gmail.miv.Adder;


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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main extends Application {

    private static final int ELEMENTS_COUNT = (int) 6e8;
    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Adder");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Threads");
        //creating the chart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Time, millisec");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My research :)");

        Random random = new Random();

        int[] array = new int[ELEMENTS_COUNT];

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(20);
        }

        // size of thread pool
        for (int i = 1; i <= THREADS_COUNT; i++) {

            ExecutorService pool = Executors.newFixedThreadPool(i);

            List<Callable<Long>> listCallable = new ArrayList<>();

            int len = (ELEMENTS_COUNT + i - 1) / i;
            for (int j = 0; j < i; j++) {
                int left = j * len;
                int right = Math.min(ELEMENTS_COUNT - 1, left + len - 1);
                listCallable.add(new Adder(array, left, right));
            }

            long start = System.nanoTime();
            List<Future<Long>> futures = pool.invokeAll(listCallable);
            long end = System.nanoTime();

            Long result = 0L;
            for (Future<Long> future : futures) {
                try {
                    result += future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            pool.shutdown();

            long elapsedTime = end - start;
            elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);

            System.out.println(" Number of threads = " + i + " result = " + result + " elapsedTime = " + elapsedTime);
            series.getData().add(new XYChart.Data(i, elapsedTime));


        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.setAnimated(false);
        lineChart.getData().add(series);

        primaryStage.setScene(scene);
        primaryStage.show();

        WritableImage wim = lineChart.snapshot(new SnapshotParameters(), null);

        scene.snapshot(wim);

        File file = new File("ChartImage_Adder.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }

    }
}
