package com.javacro.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.javacro.protobuf.model.accounting.AccountingProtobuf;
import com.javacro.serialization.afterburner.JacksonAfterburnerSerialization;
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

    private ServiceLocator locator;
    private JsonSerialization jsonSerialization;
    private JacksonAfterburnerSerialization afterburnerSerialization;    

    private byte[] customerJsonSource_bytes;
    private ByteArrayInputStream customerJsonSource_byteArrayInputStream;
    private String customerJsonSource_string;
    
    private byte[] testCustomerStubBytes;
	private byte[] customerProtobufSource_byteArray;		
        

    public static void main(final String[] args) {

        final JsonDeserializationBenchmarks benchmark = new JsonDeserializationBenchmarks();

        final List<Stats> stats = new ArrayList<Stats>();

        try {
            benchmark.buildUp();

            final int NUM_TESTS = 100;
            final int test = 0;
            
            for (int cnt = 1; cnt <= 1; cnt ++) {

            System.out.println();
            System.out.println("=====");
            System.out.println("Deserializing objects from Json: " + cnt);
            System.out.println("# Number of tests: " + NUM_TESTS);            
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
            
            if (test == 0 || test == 7) {
                final long startAt = System.currentTimeMillis();
                for (int i = 0; i < NUM_TESTS; i++) {
                    benchmark.timeProtobufferJson();
                    //System.out.println(i);
                }
                final long endAt = System.currentTimeMillis();
                final double sumaSumarum = endAt - startAt;
                stats.add(benchmark.new Stats("Protobuf", sumaSumarum, sumaSumarum/NUM_TESTS));

                System.out.printf("Protobuf (ms/tests):\t\t%.2f %n",  sumaSumarum / NUM_TESTS);
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

        this.customerJsonSource_string = TestCases.getBigAssCustomerUseCase();                
		this.customerProtobufSource_byteArray = TestCasesProtobuf.getBigAssCustomerUseCase();
		System.out.println("DSL object transactions: " + getTransactionsNum(TestCases.getBigAssCustomer()));
		System.out.println("Protobuf object transactions: " + getTransactionsNum(TestCasesProtobuf.getBigAssCustomer()));
        
        this.customerJsonSource_bytes = customerJsonSource_string.getBytes("UTF-8");
        this.customerJsonSource_byteArrayInputStream = new ByteArrayInputStream(customerJsonSource_bytes);        
        
    }

    @GenerateMicroBenchmark
    public void baseline() {
        // Against dead code elimination
    }

    @GenerateMicroBenchmark
    public void timeJacksonVulgaris() throws IOException {
        final Customer customer = jsonSerialization.deserialize(customerType, customerJsonSource_string);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeJacksonAfterBurner() throws IOException {
        final Customer customer = afterburnerSerialization.deserialize(customerType, customerJsonSource_string);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeJacksonStreaming() throws IOException {
        final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, customerJsonSource_string);
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
        final Customer customer = CustomerManualOptJsonSerialization.deserializeWith(customerJsonSource_bytes);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeManualOptimizedJsonStreamingNew() throws IOException {
        final Customer customer = CustomerManualOptJsonSerialization.deserializeOptimized(customerJsonSource_bytes);
        //System.out.println(customer);
        //System.out.println(getTransactionsNum(customer));
    }

    @GenerateMicroBenchmark
    public void timeProtobufferJson() throws IOException {
    	final com.javacro.protobuf.model.accounting.AccountingProtobuf.
		Customer customer_protobuf =
		com.javacro.protobuf.model.accounting.AccountingProtobuf.
		Customer.parseFrom(customerProtobufSource_byteArray);
    }
    
    private static long getTransactionsNum(final Customer c){
        long sum=0;
        for(final Account a : c.getAccounts()){
            sum+=a.getTransactions().size();
        }

        return sum;
    }
    
    private static long getTransactionsNum(final AccountingProtobuf.Customer c){
        long sum=0;
        for(final AccountingProtobuf.Account a : c.getAccountsList()){
            sum+=a.getTransactionsList().size();
        }

        return sum;
    }
}
