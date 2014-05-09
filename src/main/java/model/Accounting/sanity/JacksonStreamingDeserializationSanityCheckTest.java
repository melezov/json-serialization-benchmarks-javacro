package model.Accounting.sanity;
import static com.javacro.benchmarks.TestCases.getAccountUseCases;
import static com.javacro.benchmarks.TestCases.getCustomerUseCases;
import static com.javacro.benchmarks.TestCases.getProfileUseCases;
import static com.javacro.benchmarks.TestCases.getTransactionUseCases;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.Main;

import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.core.JsonFactory;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.jacksonstreaming.AccountJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.CustomerJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.ProfileJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.TransactionJacksonStreamingSerialization;

public class JacksonStreamingDeserializationSanityCheckTest {

    private static ServiceLocator locator;
    private static final JsonFactory jsonFactory = new JsonFactory();

    /**
     * Call to initialize an instance of ServiceLocator with a dsl-project.ini
     */
    @BeforeClass
    public static void init() throws IOException {
        locator = Bootstrap.init(Main.class.getResourceAsStream("/dsl-project.ini"));
    }

    @Test
    public void TransactionTest() throws IOException {

        for (final String useCase : getTransactionUseCases()) {
            System.out.println("Transaction; Testing for test case: " + useCase);

            final byte[] useCaseBytes = useCase.getBytes("UTF-8");
            final long startedAt = System.nanoTime();
            final Transaction transaction = TransactionJacksonStreamingSerialization.deserialize(jsonFactory, useCaseBytes);

            System.out.println(transaction.toString().replace(", ", ","));


        }
    }

    @Test
    public void ProfileTest() throws IOException {

        for (final String useCase : getProfileUseCases()) {
            System.out.println("Profile; Testing for test case: " + useCase);

            final byte[] useCaseBytes = useCase.getBytes("UTF-8");
            final long startedAt = System.nanoTime();
            final Profile profile = ProfileJacksonStreamingSerialization.deserialize(jsonFactory, useCaseBytes);

            System.out.println(profile.toString().replace(", ", ","));
        }
    }

    @Test
    public void AccountTest() throws IOException {

        for (final String useCase : getAccountUseCases()) {
            System.out.println("Account; Testing for test case: " + useCase);

            final byte[] useCaseBytes = useCase.getBytes("UTF-8");
            final long startedAt = System.nanoTime();
            final Account account = AccountJacksonStreamingSerialization.deserialize(jsonFactory, useCaseBytes);

            System.out.println(account.toString().replace(", ", ","));
        }
    }

    @Test
    public void CustomerTest() throws IOException {

        for (final String useCase : getCustomerUseCases()) {
            System.out.println("Customer; Testing for test case: " + useCase);

            final byte[] useCaseBytes = useCase.getBytes("UTF-8");
            final long startedAt = System.nanoTime();
            final Customer customer = CustomerJacksonStreamingSerialization.deserialize(jsonFactory, useCaseBytes);

            System.out.println(customer.toString().replace(", ", ","));
        }
    }

}
