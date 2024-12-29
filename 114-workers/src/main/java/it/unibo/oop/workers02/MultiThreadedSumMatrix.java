package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthreads;

    public MultiThreadedSumMatrix(int nthreads) {
        this.nthreads = nthreads;
    }

    @Override
    public double sum(double[][] matrix) {
        final int size = matrix.length % this.nthreads + matrix.length / this.nthreads;
        final List<Worker> workers = new ArrayList<>(nthreads);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for(final Worker w : workers) {
            w.start();
        }
        long sum = 0;
        for(final Worker w : workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private double res;

        Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working in startpos " + this.startpos);
            for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
                for(final double d : this.matrix[i]) {
                    this.res += d;
                }
            }
        }

        public double getResult() {
            return this.res;
        }
    } 
    
}
