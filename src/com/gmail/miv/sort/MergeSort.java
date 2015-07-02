package com.gmail.miv.sort;


import java.util.concurrent.*;

public class MergeSort implements Callable<Boolean> {

    static private int numberOfLevels;
    private int[] array;
    private int leftBorder;
    private int rightBorder;

    public MergeSort(int[] array, int leftBorder, int rightBorder) {
        this.array = array;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        numberOfLevels--;
    }

    public MergeSort(int[] array) {
        this.array = array;
        this.leftBorder = 0;
        this.rightBorder = array.length - 1;
        sort(leftBorder, rightBorder);
        numberOfLevels = 3; // the maximum recursive depth at which uses Executor
    }

    @Override
    public Boolean call() throws Exception {

        sort(leftBorder, rightBorder);
        return true;
    }

    public void sort(int left, int right) {

        if (left < right) {
            int mid = left + (right - left) / 2;

            if (numberOfLevels <= 0) {
                sort(left, mid);
                sort(mid + 1, right);
                merge(left, mid, mid + 1, right);
            } else {
                try {
                    ExecutorService pool = Executors.newFixedThreadPool(2);

                    Future<Boolean> isSortedLeft = pool.submit(new MergeSort(array, left, mid));
                    Future<Boolean> isSortedRight = pool.submit(new MergeSort(array, mid + 1, right));
                    if (isSortedLeft.get() && isSortedRight.get()) {
                        merge(left, mid, mid + 1, right);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void merge(int leftFirst, int rightFirst, int leftSecond, int rightSecond) {

        int begin = leftFirst;
        int[] buff = new int[rightSecond - leftFirst + 1];

        int index = 0;
        while ((leftFirst <= rightFirst) && (leftSecond <= rightSecond)) {
            if (array[leftFirst] <= array[leftSecond]) {
                buff[index] = array[leftFirst];
                index++;
                leftFirst++;
            } else {
                buff[index] = array[leftSecond];
                index++;
                leftSecond++;
            }
        }

        while (leftFirst <= rightFirst) {
            buff[index] = array[leftFirst];
            index++;
            leftFirst++;
        }

        while (leftSecond <= rightSecond) {
            buff[index] = array[leftSecond];
            index++;
            leftSecond++;
        }

        System.arraycopy(buff, 0, array, begin, buff.length);
    }

    public void swap(int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }
}
