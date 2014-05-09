package model.Accounting.sanity.serialization;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.Main;

import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.core.JsonFactory;
import com.javacro.benchmarks.TestCases;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.jacksonstreaming.AccountJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.CustomerJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.ProfileJacksonStreamingSerialization;
import com.javacro.serialization.jacksonstreaming.TransactionJacksonStreamingSerialization;

public class JacksonStreamingSerializationSanityCheckTest {

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
        System.out.println("=================");
        System.out.println("Testing serialization for Transaction: ");
        for(final Transaction c : TestCases.getTransactionStubs()){
            System.out.println(c.toString());
            System.out.println("Serialization output: ");
            System.out.println(TransactionJacksonStreamingSerialization.serialize(jsonFactory, c));
        }
    }

    @Test
    public void ProfileTest() throws IOException {
        System.out.println("=================");
        System.out.println("Testing serialization for Profile: ");
        for(final Profile c : TestCases.getProfileStubs()){
            System.out.println(c.toString());
            System.out.println("Serialization output: ");
            System.out.println(ProfileJacksonStreamingSerialization.serialize(jsonFactory, c));
        }
    }

    @Test
    public void AccountTest() throws IOException {
        System.out.println("=================");
        System.out.println("Testing serialization for Account: ");
        for(final Account c : TestCases.getAccountStubs()){
            System.out.println(c.toString());

            final String out = AccountJacksonStreamingSerialization.serialize(jsonFactory, c);
            System.out.println("Serialization done, output: ");
            System.out.println(out);
        }
    }


    @Test
    public void CustomerTest() throws IOException {
        System.out.println("=================");
        System.out.println("Testing serialization for Customer: ");
        for(final Customer c : TestCases.getCustomerStubs()){
            System.out.println(c.toString());
            System.out.println("Serialization output: ");
            System.out.println(CustomerJacksonStreamingSerialization.serialize(jsonFactory, c));
        }
    }

}
