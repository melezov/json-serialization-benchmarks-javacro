package com.javacro.benchmarks;

import java.util.Random;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

/**
 * An example of a benchmark that slows down after JVM warm-up
 */
public class UnrollingExample {

    private static final int[] buffer;
    private static final long checksum;

    private static final int ITERATIONS = 1000;

    private static final int SAMPLES = 20;

    static {
        final Random r = new Random();
        long sum = 0;
        buffer = new int[999999];
        for (int i = 0; i < buffer.length; i++) {
            sum += (buffer[i] = r.nextInt())&0x12345678;
        }
        checksum = sum;
    }

    public static void main(final String[] args) {
        final UnrollingExample b = new UnrollingExample();

        for (int i = 0; i < SAMPLES; i++) {
            {
                final long start = System.currentTimeMillis();
                b.microBenchmarkWithoutUnroll();
                final long end = System.currentTimeMillis();
                System.out.printf("Without: %.2f %n", (double) (end - start));
            }

            {
                final long start = System.currentTimeMillis();
                b.microBenchmarkWithUnroll();
                final long end = System.currentTimeMillis();
                System.out.printf("With: %.2f %n", (double) (end - start));
            }
        }
    }

    @GenerateMicroBenchmark
    public void microBenchmarkWithoutUnroll() {
        for (int i = 0; i < ITERATIONS; i++) {
            long sum = 0;
            for (int r = 0; r < buffer.length; r++) {
                sum += buffer[r]&0x12345678;
            }

            if (sum != checksum) { throw new RuntimeException("Invalid checksum: " + sum); }
        }
    }

    @GenerateMicroBenchmark
    public void microBenchmarkWithUnroll() {
        for (int i = 0; i < ITERATIONS; i++) {
            long sum = 0;
            final int split = buffer.length & ~3;

            if (buffer.length > 3) {
                for (int r = 0; r < split; r+=4) {
                    sum += buffer[r] &0x12345678;
                    sum += buffer[r+1]&0x12345678;
                    sum += buffer[r+2]&0x12345678;
                    sum += buffer[r+3]&0x12345678;
                }
            }
            for (int r = split; r < buffer.length; r++) {
                sum += buffer[r]&0x12345678;
            }
            if (sum != checksum) { throw new RuntimeException("Invalid checksum: " + sum); }
        }
    }

}
