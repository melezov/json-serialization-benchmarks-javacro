package com.javacro.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.serialization.afterburner.JacksonAfterburnerSerialization;
import com.javacro.serialization.jacksonvulgaris.JsonSerialization;
import com.javacro.serialization.manual.CustomerManualJsonSerialization;
import com.javacro.serialization.streaming.CustomerJacksonStreamingSerialization;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonSerializationBenchmarks {

    private final JavaType customerType = JsonSerialization.buildType(Customer.class);
    private final JsonFactory jsonFactory = new JsonFactory();

    private final String[] useCases = TestCases.getCustomerUseCasesArray();

    private ServiceLocator locator;
    private JsonSerialization jsonSerialization;
    private JacksonAfterburnerSerialization afterburnerSerialization;

    private final int r = useCases.length - 1;

    private byte[] useCase_bytes;
    private ByteArrayInputStream useCaseInputStream;
    private String useCase_string;

    final static int NUM_TESTS = 3000;

    public static void main(final String[] args) {

        final JsonSerializationBenchmarks benchmark = new JsonSerializationBenchmarks();

        try {
            benchmark.buildUp();

            for (final int NUM_TESTS : Arrays.asList(new Integer[]{1, 30, 300, 3000, 15000})){
                timeBytesVersion(benchmark, NUM_TESTS);
                timeStringVersion(benchmark, NUM_TESTS);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done with work.");
        System.out.println();
    }

    private static void timeStringVersion(final JsonSerializationBenchmarks benchmark, final int NUM_TESTS) throws IOException{
        System.out.println();
        System.out.println("==========");
        System.out.println("Strings: ");
        System.out.println();
        System.out.println("Number of tests: " + NUM_TESTS);
        System.out.println();

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonAfterBurnerString();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonAfterBurner (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonStreamingString();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonStreaming (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonVulgarisString();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonVulgaris (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeManualJsonStreamingString();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("ManualJsonStreaming (ms/tests): \t %.2f%n", (sumaSumarum / NUM_TESTS));
        }
    }

    private static void timeBytesVersion(final JsonSerializationBenchmarks benchmark, final int NUM_TESTS) throws IOException{

        System.out.println();
        System.out.println("==========");
        System.out.println("Byte streams: ");
        System.out.println();
        System.out.println("Number of tests: " + NUM_TESTS);
        System.out.println();

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonAfterBurnerBytes();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonAfterBurner (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonStreamingBytes();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonStreaming (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeJacksonVulgarisBytes();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("JacksonVulgaris (ms/tests): \t\t %.2f%n", (sumaSumarum / NUM_TESTS));
        }

        {
            double sumaSumarum = 0;
            final long startAt = System.currentTimeMillis();
            for (int i = 0; i < NUM_TESTS; i++) {
                benchmark.timeManualJsonStreamingBytes();
            }
            final long endAt = System.currentTimeMillis();
            sumaSumarum += endAt - startAt;
            System.out.printf("ManualJsonStreaming (ms/tests): \t %.2f%n", (sumaSumarum / NUM_TESTS));
        }
    }

    @Setup
    public void buildUp() throws IOException, UnsupportedEncodingException {
        this.locator = Bootstrap.init(JsonSerializationBenchmarks.class.getResourceAsStream("/dsl-project.ini"));
        this.jsonSerialization = locator.resolve(JsonSerialization.class);
        this.afterburnerSerialization = new JacksonAfterburnerSerialization(locator);

        this.useCase_string = useCases[r];
        this.useCase_bytes = useCases[r].getBytes("UTF-8");
        this.useCaseInputStream = new ByteArrayInputStream(useCase_bytes);
    }

    @GenerateMicroBenchmark
    public void baseline() {
        // Against dead code elimination
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgarisBytes() throws IOException {
        final Customer customer = jsonSerialization.deserialize(customerType, useCase_bytes);
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgarisString() throws IOException {
        final Customer customer = jsonSerialization.deserialize(customerType, useCase_string);
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurnerBytes() throws IOException {
        final Customer customer = afterburnerSerialization.deserialize(customerType, useCase_bytes);
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurnerString() throws IOException {
        final Customer customer = afterburnerSerialization.deserialize(customerType, useCase_string);
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreamingBytes() throws IOException {
        final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, useCase_bytes);
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreamingString() throws IOException {
        final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, useCase_string);
    }

    @GenerateMicroBenchmark
    public void timeManualJsonStreamingBytes() throws IOException {
        final Customer customer = CustomerManualJsonSerialization.deserialize(useCase_bytes);
    }

    @GenerateMicroBenchmark
    public void timeManualJsonStreamingString() throws IOException {
        final Customer customer = CustomerManualJsonSerialization.deserialize(useCase_string);
    }

//    @GenerateMicroBenchmark
//    public void timeProtobufferJson() throws IOException {
//        for(int r=0; r<useCases.length ; r++){
//            final Message.Builder builder = CustomeringProtobuf.Customer.newBuilder();
//            JsonFormat.merge(useCases[r], builder);
//        }
//    }
}
