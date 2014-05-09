package com.javacro.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.dslplatform.client.Bootstrap;
import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.serialization.afterburner.JacksonAfterburnerSerialization;
import com.javacro.serialization.io.jvm.json.JsonWriter;
import com.javacro.serialization.jacksonstreaming.CustomerJacksonStreamingSerialization;
import com.javacro.serialization.manual.CustomerManualJsonStreaming;
import com.javacro.serialization.manual_optimized.CustomerManualOptJsonSerialization;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonSerializationBenchmarks {

    private class Stats{
        public String testName="Test";
        public double summaSummarum=0;
        public double testRate;
        public Stats(final String testName, final double sumaSumarum, final double d){this.testName=testName; this.summaSummarum=sumaSumarum; this.testRate=d;}
    }

    private final JavaType customerType = JsonSerialization.buildType(Customer.class);
    private final JsonFactory jsonFactory = new JsonFactory();


    private final List<Customer> testCustomerStubs = TestCases.getCustomerStubs();

    private ServiceLocator locator;
    private JsonSerialization jsonSerialization;
    private JacksonAfterburnerSerialization afterburnerSerialization;

    private final int r = testCustomerStubs.size() - 1;

    private byte[] testCustomerStubBytes;
    private ByteArrayInputStream testCustomerStubInputStream;
    private Customer testCustomerStub;

    private JsonWriter jsonManualWriter;
    private StringReader stringReader;

    public static void main(final String[] args) {

        final JsonSerializationBenchmarks benchmark = new JsonSerializationBenchmarks();

        final List<Stats> stats = new ArrayList<Stats>();

        try {
            benchmark.buildUp();

            final int NUM_TESTS = 1000;

            System.out.println();
            System.out.println("=====");
            System.out.println("Number of tests: " + NUM_TESTS);
            System.out.println("=====");

            {
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonAfterBurner();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                stats.add(benchmark.new Stats("JacksonAfterBurner", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("JacksonAfterBurner (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("JacksonAfterBurner (testRate): " + sumaSumarum);
            }

            {
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonStreaming();;
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                stats.add(benchmark.new Stats("JacksonStreaming", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("JacksonStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("JacksonStreaming (testRate): " + sumaSumarum);
            }

            {
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonVulgaris();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                stats.add(benchmark.new Stats("JacksonVulgaris", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("JacksonVulgaris (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("JacksonVulgaris (testRate): " + sumaSumarum);
            }

            {
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualJsonStreaming();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                stats.add(benchmark.new Stats("ManualStreaming",sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("ManualStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("ManualJsonStreaming (testRate): " + sumaSumarum);
            }

            {
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualOptimizedJsonStreaming();
                }
                final long endAt = System.currentTimeMillis();
                final double sumaSumarum = endAt - startAt;
                stats.add(benchmark.new Stats("ManualOptStreaming", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("ManualOptStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("ManualOptimizedJsonStreaming (testRate): " + sumaSumarum);
            }

            System.out.println();
            System.out.println("=====");
            System.out.println("Totals:");
            System.out.println("=====");
            for(final Stats s : stats){
                System.out.printf("Test:\t%s,\tTotal:\t%.2f\t%n", s.testName, s.summaSummarum);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Setup
    public void buildUp() throws IOException, UnsupportedEncodingException {
        this.locator = Bootstrap.init(JsonSerializationBenchmarks.class.getResourceAsStream("/dsl-project.ini"));
        this.jsonSerialization = locator.resolve(JsonSerialization.class);
        this.afterburnerSerialization = new JacksonAfterburnerSerialization(locator);

        final StringWriter sw = new StringWriter();
        this.jsonManualWriter = new JsonWriter(sw);

        this.testCustomerStub = testCustomerStubs.get(r);

    }

    @GenerateMicroBenchmark
    public void baseline() {
        // Against dead code elimination
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgaris() throws IOException {
        final String customer_string = jsonSerialization.serialize(testCustomerStub);
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurner() throws IOException {
        final String customer_string = afterburnerSerialization.serialize(testCustomerStub);
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreaming() throws IOException {
        final String customer_string = CustomerJacksonStreamingSerialization.serialize(jsonFactory, testCustomerStub);
    }

    @GenerateMicroBenchmark
    public void timeManualJsonStreaming() throws IOException {
        final String customer_string = CustomerManualJsonStreaming.serialize(testCustomerStub);
    }

    @GenerateMicroBenchmark
    public void timeManualOptimizedJsonStreaming() throws IOException {
        final StringWriter sw = new StringWriter();
        CustomerManualOptJsonSerialization.serialize(new StringWriter(), testCustomerStub);
        final String customer_string = sw.toString();
    }

//    @GenerateMicroBenchmark
//    public void timeProtobufferJson() throws IOException {
//        for(int r=0; r<testCustomerStubs.length ; r++){
//            final Message.Builder builder = CustomeringProtobuf.Customer.newBuilder();
//            JsonFormat.merge(testCustomerStubs[r], builder);
//        }
//    }
}
