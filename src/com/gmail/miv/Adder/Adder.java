package com.gmail.miv.Adder;


import java.util.concurrent.Callable;

public class Adder implements Callable<Long> {

    private int[] array;
    private int leftBorder;
    private int rightBorder;

    public Adder(int[] array, int leftBorder, int rightBorder) {

        this.array = array;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;

    }

    @Override
    public Long call() throws Exception {
        if (leftBorder < 0 || rightBorder < 0) {
            System.out.println(rightBorder + " " + leftBorder);
            return 0L;
        } else {

            long result = 0;

            for (int i = leftBorder; i <= rightBorder; i++) {
                result += array[i];
            }

            return result;
        }

    }
}
