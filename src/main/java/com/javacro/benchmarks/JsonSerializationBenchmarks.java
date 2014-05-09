package com.javacro.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.manual.CustomerManualJsonSerialization;
import com.javacro.serialization.manual_optimized.CustomerManualOptimizedJsonSerialization;
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

    private byte[] useCaseBytes;
    private ByteArrayInputStream useCaseInputStream;
    private String useCase;

    private JsonReader jsonManualReader;
    private StringReader stringReader;

    public static void main(final String[] args) {

        final JsonSerializationBenchmarks benchmark = new JsonSerializationBenchmarks();

        try {
            benchmark.buildUp();

            final int NUM_TESTS = 10000;

            System.out.println("Number of tests: " + NUM_TESTS);
            int test = 5;

            if(test == 1){
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonAfterBurner();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                System.out.println("JacksonAfterBurner (ms/tests): " + sumaSumarum / NUM_TESTS);
                System.out.println("JacksonAfterBurner (total): " + sumaSumarum);
            }

            if(test == 2){
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonStreaming();;
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                System.out.println("JacksonStreaming (ms/tests): " + sumaSumarum / NUM_TESTS);
                System.out.println("JacksonStreaming (total): " + sumaSumarum);
            }

            if(test == 3){
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonVulgaris();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                System.out.println("JacksonVulgaris (ms/tests): " + sumaSumarum / NUM_TESTS);
                System.out.println("JacksonVulgaris (total): " + sumaSumarum);
            }

            if(test == 4){
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualJsonStreaming();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                System.out.println("ManualJsonStreaming (ms/tests): " + sumaSumarum / NUM_TESTS);
                System.out.println("ManualJsonStreaming (total): " + sumaSumarum);
            }

            if(test == 5){
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualOptimizedJsonStreaming();
                }
                final long endAt = System.currentTimeMillis();
                double sumaSumarum = endAt - startAt; 
                System.out.println("ManualOptimizedJsonStreaming (ms/tests): " + sumaSumarum / NUM_TESTS);
                System.out.println("ManualOptimizedJsonStreaming (total): " + sumaSumarum);
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

        this.useCase = useCases[r];
        this.useCaseBytes = useCases[r].getBytes("UTF-8");
        this.useCaseInputStream = new ByteArrayInputStream(useCaseBytes);

        this.jsonManualReader = new JsonReader(useCaseBytes);
        this.stringReader = new StringReader(useCase);
    }

    @GenerateMicroBenchmark
    public void baseline() {
        // Against dead code elimination
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgaris() throws IOException {
        final Customer customer = jsonSerialization.deserialize(customerType, useCase);
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurner() throws IOException {
        final Customer customer = afterburnerSerialization.deserialize(customerType, useCase);
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreaming() throws IOException {
        final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, useCase);
    }

    @GenerateMicroBenchmark
    public void timeManualJsonStreaming() throws IOException {
        final Customer customer = CustomerManualJsonSerialization.deserialize(useCaseBytes);
        //final Customer customer = CustomerManualJsonSerialization.read(jsonManualReader);
    }

    @GenerateMicroBenchmark
    public void timeManualOptimizedJsonStreaming() throws IOException {
        final Customer customer = CustomerManualOptimizedJsonSerialization.deserializeWith(useCaseBytes);
        //final Customer customer = CustomerManualJsonSerialization.read(jsonManualReader);
    }

//    @GenerateMicroBenchmark
//    public void timeProtobufferJson() throws IOException {
//        for(int r=0; r<useCases.length ; r++){
//            final Message.Builder builder = CustomeringProtobuf.Customer.newBuilder();
//            JsonFormat.merge(useCases[r], builder);
//        }
//    }
}
