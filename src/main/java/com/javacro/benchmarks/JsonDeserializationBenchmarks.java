package com.javacro.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
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
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.serialization.afterburner.JacksonAfterburnerSerialization;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.jacksonstreaming.CustomerJacksonStreamingSerialization;
import com.javacro.serialization.manual_optimized.CustomerManualOptJsonSerialization;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JsonDeserializationBenchmarks {

    private class Stats{
        public String testName="Test";
        public double summaSummarum=0;
        public double testRate;
        public Stats(final String testName, final double sumaSumarum, final double d){this.testName=testName; this.summaSummarum=sumaSumarum; this.testRate=d;}
    }

    private final JavaType customerType = JsonSerialization.buildType(Customer.class);
    private final JsonFactory jsonFactory = new JsonFactory();

    private final String[] useCases = TestCases.getCustomerUseCasesArray();
    private final List<String> bigAssCustomerUseCases = TestCases.getBigAssCustomerUseCases();

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

        final JsonDeserializationBenchmarks benchmark = new JsonDeserializationBenchmarks();

        final List<Stats> stats = new ArrayList<Stats>();

        try {
            benchmark.buildUp();

            final int NUM_TESTS = 100;
            final int test = 0;
            
            for (int cnt = 1; cnt <= 2; cnt ++) {

            System.out.println();
            System.out.println("=====");
            System.out.println("Deserializing objects from Json: " + cnt);
            System.out.println("# Number of tests: " + NUM_TESTS);
            System.out.println("# Number of transactions: ?");
            System.out.println("=====");

            if (test == 0 || test == 1) {
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

            if (test == 0 || test == 2) {
                double sumaSumarum = 0;
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeJacksonStreaming();
                }
                final long endAt = System.currentTimeMillis();
                sumaSumarum += endAt - startAt;
                stats.add(benchmark.new Stats("JacksonStreaming", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("JacksonStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
  //              System.out.println("JacksonStreaming (testRate): " + sumaSumarum);
            }

            if (test == 0 || test == 3) {
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

//            {
//                double sumaSumarum = 0;
//                final long startAt = System.currentTimeMillis();
//                for (int i = 0; i < NUM_TESTS; i++) {
//                    benchmark.timeManualJsonStreaming();
//                }
//                final long endAt = System.currentTimeMillis();
//                sumaSumarum += endAt - startAt;
//                stats.add(benchmark.new Stats("ManualStreaming",sumaSumarum, sumaSumarum/NUM_TESTS));
//
//                System.out.printf("ManualStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
////                System.out.println("ManualJsonStreaming (testRate): " + sumaSumarum);
//            }

            if (test == 0 || test == 5) {
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualOptimizedJsonStreaming();
                    //System.out.println(i);
                }
                final long endAt = System.currentTimeMillis();
                final double sumaSumarum = endAt - startAt;
                stats.add(benchmark.new Stats("ManualOptStreaming", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("ManualOptimizedStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("ManualOptimizedJsonStreaming (testRate): " + sumaSumarum);
            }

            if (test == 0 || test == 6) {
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeManualOptimizedJsonStreamingNew();
                    //System.out.println(i);
                }
                final long endAt = System.currentTimeMillis();
                final double sumaSumarum = endAt - startAt;
                stats.add(benchmark.new Stats("ManualOptNewStreaming", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("ManualOptimizedNewStreaming (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
//                System.out.println("ManualOptimizedJsonStreaming (testRate): " + sumaSumarum);
            }

            System.out.println();
            System.out.println("=====");
            System.out.println("Totals: " + cnt);
            System.out.println("=====");
            for(final Stats s : stats){
                System.out.printf("Test:\t%s,\tTotal:\t%.2f\t%n", s.testName, s.summaSummarum);
            }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Setup
    public void buildUp() throws IOException, UnsupportedEncodingException {
        this.locator = Bootstrap.init(JsonDeserializationBenchmarks.class.getResourceAsStream("/dsl-project.ini"));
        this.jsonSerialization = locator.resolve(JsonSerialization.class);
        this.afterburnerSerialization = new JacksonAfterburnerSerialization(locator);

        //this.useCase = useCases[r];
        this.useCase = TestCases.getBigAssCustomerUseCase();
        //this.useCase = TestCases.getSmallCustomerUseCase();
        this.useCaseBytes = useCase.getBytes("UTF-8");
        this.useCaseInputStream = new ByteArrayInputStream(useCaseBytes);

        this.jsonManualReader = new JsonReader(useCaseBytes);
        this.stringReader = new StringReader(useCase);

//        System.out.println("Deserializing");
//        System.out.println(useCase);
//        System.exit(0);
    }

    @GenerateMicroBenchmark
    public void baseline() {
        // Against dead code elimination
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgaris() throws IOException {
        final Customer customer = jsonSerialization.deserialize(customerType, useCase);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurner() throws IOException {
        final Customer customer = afterburnerSerialization.deserialize(customerType, useCase);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreaming() throws IOException {
        final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, useCase);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

//    @GenerateMicroBenchmark
//    public void timeManualJsonStreaming() throws IOException {
//        final Customer customer = CustomerManualJsonStreaming.deserialize(useCaseBytes);
//        //System.out.println(getTransactionsNum(customer));
//    }

    @GenerateMicroBenchmark
    public void timeManualOptimizedJsonStreaming() throws IOException {
        final Customer customer = CustomerManualOptJsonSerialization.deserializeWith(useCaseBytes);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeManualOptimizedJsonStreamingNew() throws IOException {
        final Customer customer = CustomerManualOptJsonSerialization.deserializeOptimized(useCaseBytes);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

//    @GenerateMicroBenchmark
//    public void timeProtobufferJson() throws IOException {
//        for(int r=0; r<useCases.length ; r++){
//            final Message.Builder builder = CustomeringProtobuf.Customer.newBuilder();
//            JsonFormat.merge(useCases[r], builder);
//        }
//    }
    private static long getTransactionsNum(final Customer c){
        long sum=0;
        for(final Account a : c.getAccounts()){
            sum+=a.getTransactions().size();
        }

        return sum;
    }
}
