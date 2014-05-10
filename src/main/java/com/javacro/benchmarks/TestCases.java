package com.javacro.benchmarks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.protobuf.model.accounting.AccountingProtobuf;

public class TestCases {

    //public static final long NUMBER_OF_RUNS = 100000;

    public static String[] getProfileUseCasesArray() {
        return new String[] { "{}", "{\"email\":\"markož@element.hr\"}", "{\"phoneNumber\":\"+385 1 234 5678\"}",
                "{\"email\":\"markož@element.hr\",\"phoneNumber\":\"+385 1 234 5678\"}" };

    }

    public static List<String> getProfileUseCases() {
        return new ArrayList<String>(Arrays.asList(getProfileUseCasesArray()));
    }

    public static String[] getBigAssTransactionUseCasesArray() {
        final String[] useCases =
                new String[] { "{}", "{\"inflow\":1}", "{\"outflow\":2}", "{\"description\":\"A description\"}",
                        "{\"paymentOn\":\"01-01-01T02:02\"}"

                        , "{\"inflow\":1,\"outflow\":2}", "{\"inflow\":1,\"description\":\"A description\"}",
                        "{\"inflow\":1,\"paymentOn\":\"01-01-01T02:02\"}"

                        , "{\"outflow\":1,\"description\":\"A description\"}",
                        "{\"outflow\":1,\"paymentOn\":\"01-01-01T02:02\"}"

                        , "{\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}"

                        , "{\"inflow\":1,\"outflow\":2,\"description\":\"A description\"}",
                        "{\"inflow\":1,\"outflow\":2,\"paymentOn\":\"01-01-01T02:02\"}"

                        , "{\"inflow\":1,\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                        "{\"outflow\":1,\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}" };

        final String[] transactions = new String[50000];
        for (int i = 0; i < transactions.length; i++) {
            transactions[i] = useCases[i % useCases.length];
        }

        return transactions;
    }

    public static String[] getTransactionUseCasesArray() {
        return new String[] {
                "{}",
                "{\"inflow\":1}",
                "{\"outflow\":2}",
                "{\"description\":\"A description\"}",
                "{\"paymentOn\":\"01-01-01T02:02\"}"

                ,
                "{\"inflow\":1,\"outflow\":2}",
                "{\"inflow\":1,\"description\":\"A description\"}",
                "{\"inflow\":1,\"paymentOn\":\"01-01-01T02:02\"}"

                ,
                "{\"outflow\":1,\"description\":\"A description\"}",
                "{\"outflow\":1,\"paymentOn\":\"01-01-01T02:02\"}"

                ,
                "{\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}"

                ,
                "{\"inflow\":1,\"outflow\":2,\"description\":\"A description\"}",
                "{\"inflow\":1,\"outflow\":2,\"paymentOn\":\"01-01-01T02:02\"}"

                ,
                "{\"inflow\":1,\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"outflow\":1,\"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":1, \"description\":\"Another description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":1, \"description\":\"A description blabla\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":2, \"description\":\"something something\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":3, \"description\":\"dtjk\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":4, \"outflow\":1, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":5, \"outflow\":1, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":6, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":7, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":1, \"outflow\":8, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}",
                "{\"inflow\":9, \"outflow\":1, \"description\":\"A description\",\"paymentOn\":\"01-01-01T02:02\"}" };

    }

    public static List<String> getBigAssTransactionUseCases() {
        return Arrays.asList(getBigAssTransactionUseCasesArray());
    }

    public static List<String> getTransactionUseCases() {
        return Arrays.asList(getTransactionUseCasesArray());
    }

    public static String[] getBigAssAccountUseCasesArray() {
        return new String[] {
                "{\"IBAN\":\"12312123\",\"transactions\":" + getBigAssTransactionUseCases().toString().replace(", ", ",")
                        + "}",
                "{\"currency\":\"HRK\",\"transactions\":" + getBigAssTransactionUseCases().toString().replace(", ", ",")
                        + "}",
                "{\"IBAN\":\"12312123\",\"currency\":\"HRK\",\"transactions\":"
                        + getBigAssTransactionUseCases().toString().replace(", ", ",") + "}" };
    }

    public static String[] getAccountUseCasesArray() {
        return new String[] {
                "{}",
                "{\"IBAN\":\"12312123\"}",
                "{\"currency\":\"HRK\"}",
                "{\"IBAN\":\"12312123\",\"transactions\":" + getTransactionUseCases().toString().replace(", ", ",")
                        + "}",
                "{\"currency\":\"HRK\",\"transactions\":" + getTransactionUseCases().toString().replace(", ", ",")
                        + "}",
                "{\"IBAN\":\"12312123\",\"currency\":\"HRK\",\"transactions\":"
                        + getTransactionUseCases().toString().replace(", ", ",") + "}" };
    }

    public static List<String> getBigAssAccountUseCases() {
        return Arrays.asList(getBigAssAccountUseCasesArray());
    }

    public static List<String> getAccountUseCases() {
        return new ArrayList<String>(Arrays.asList(getAccountUseCasesArray()));
    }

    public static String[] getCustomerUseCasesArray() {
        return getCustomerUseCases().toArray(new String[getCustomerUseCases().size()]);
    }
    
    public static List<String> getBigAssCustomerUseCases() {
        final List<String> custs =
                new ArrayList<String>(
                        Arrays.asList(new String[] { "{}", "{\"id\":12312123}", "{\"name\":\"Jadranko\"}" }));

        custs.add("{\"accounts\":" + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"id\":12312123,\"accounts\":" + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"name\":\"Jadranko\",\"accounts\":" + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"id\":12312123,\"name\":\"Jadranko\",\"accounts\":"
                + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");

        for (final String profileUseCase : getProfileUseCases()) {
            custs.add("{\"profile\":" + profileUseCase + "}");
            custs.add("{\"id\":12312123,\"profile\":" + profileUseCase + "}");
            custs.add("{\"name\":\"Jadranko\",\"profile\":" + profileUseCase + "}");
            custs.add("{\"id\":12312123,\"name\":\"Jadranko\",\"profile\":" + profileUseCase + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"accounts\":"
                    + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"id\":12312123,\"accounts\":"
                    + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"name\":\"Jadranko\",\"accounts\":"
                    + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"id\":12312123,\"name\":\"Jadranko\",\"accounts\":"
                    + getBigAssAccountUseCases().toString().replace(", ", ",") + "}");
        }

        return custs;
    }

    public static List<String> getCustomerUseCases() {

        final List<String> custs =
                new ArrayList<String>(
                        Arrays.asList(new String[] { "{}", "{\"id\":12312123}", "{\"name\":\"Jadranko\"}" }));

        custs.add("{\"accounts\":" + getAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"id\":12312123,\"accounts\":" + getAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"name\":\"Jadranko\",\"accounts\":" + getAccountUseCases().toString().replace(", ", ",") + "}");
        custs.add("{\"id\":12312123,\"name\":\"Jadranko\",\"accounts\":"
                + getAccountUseCases().toString().replace(", ", ",") + "}");

        for (final String profileUseCase : getProfileUseCases()) {
            custs.add("{\"profile\":" + profileUseCase + "}");
            custs.add("{\"id\":12312123,\"profile\":" + profileUseCase + "}");
            custs.add("{\"name\":\"Jadranko\",\"profile\":" + profileUseCase + "}");
            custs.add("{\"id\":12312123,\"name\":\"Jadranko\",\"profile\":" + profileUseCase + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"accounts\":"
                    + getAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"id\":12312123,\"accounts\":"
                    + getAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"name\":\"Jadranko\",\"accounts\":"
                    + getAccountUseCases().toString().replace(", ", ",") + "}");
            custs.add("{\"profile\":" + profileUseCase + ",\"id\":12312123,\"name\":\"Jadranko\",\"accounts\":"
                    + getAccountUseCases().toString().replace(", ", ",") + "}");
        }

        return custs;
    }

    public static List<Profile> getProfileStubs() {
        return new ArrayList<Profile>(Arrays.asList(new Profile[] { new Profile(),
                new Profile("email@email.mail", null), new Profile(null, "+12345678"),
                new Profile("email@email.mail", "+12345678") }));

    }

    public static List<Transaction> getBigAssTransactionStubs() {

        final List<Transaction> ts = new ArrayList<Transaction>();

        for(int i=0;i<1000;i++){
        	ts.addAll(getTransactionStubs());
        }

        return ts;
    }

    public static List<Transaction> getTransactionStubs() {
        return Arrays.asList(new Transaction[] { 
        		new Transaction(),
                new Transaction(1, 0, "", DateTime.now()), 
                new Transaction(1, 1, "abcde", DateTime.now()),
                new Transaction(0, 1, "abcde", DateTime.now()), 
                new Transaction(0, 0, "ABCDE", DateTime.now()), });
    }

    public static List<Account> getBigAssAccountStubs() {
        final List<Account> stubs = new ArrayList<Account>();

        stubs.add(new Account());
        stubs.add(new Account("HR123123124", "", getBigAssTransactionStubs().subList(0, 1)));
        stubs.add(new Account("", "HRK", getBigAssTransactionStubs().subList(0, 1)));
        stubs.add(new Account("", "tumegledaj", new ArrayList<Transaction>()));
        stubs.add(new Account("", "", getBigAssTransactionStubs()));
        stubs.add(new Account("HR123123124", "HRK", getBigAssTransactionStubs().subList(0, 1)));
        stubs.add(new Account("HR123123124", "HRK", getBigAssTransactionStubs()));

        return stubs;
    }

    public static List<Account> getAccountStubs() {
        final List<Account> stubs = new ArrayList<Account>();

        stubs.add(new Account());
        stubs.add(new Account("HR123123124", "", getTransactionStubs().subList(0, 1)));
        stubs.add(new Account("", "HRK", getTransactionStubs().subList(0, 1)));
        stubs.add(new Account("", "tumegledaj", new ArrayList<Transaction>()));
        stubs.add(new Account("", "", getTransactionStubs()));
        stubs.add(new Account("HR123123124", "HRK", getTransactionStubs().subList(0, 1)));
        stubs.add(new Account("HR123123124", "HRK", getTransactionStubs()));

        return stubs;
    }

    public static List<Customer> getCustomerStubs() {
        final List<Customer> stubs = new ArrayList<Customer>();

        stubs.add(new Customer());
        stubs.add(new Customer(5, "Mirko", getProfileStubs().get(0), getAccountStubs().subList(0, 1)));
        stubs.add(new Customer(5, "Slavko", getProfileStubs().get(1), getAccountStubs()));
        stubs.add(new Customer(5, "Marko", getProfileStubs().get(2), getAccountStubs()));

        return stubs;
    }

    public static List<Customer> getBigAssCustomerStubs() {
        final List<Customer> stubs = new ArrayList<Customer>();

        stubs.add(new Customer());
        stubs.add(new Customer(5, "Mirko", getProfileStubs().get(0), getBigAssAccountStubs().subList(0, 1)));
        stubs.add(new Customer(5, "Slavko", getProfileStubs().get(1), getBigAssAccountStubs()));
        stubs.add(new Customer(5, "Marko", getProfileStubs().get(2), getBigAssAccountStubs()));

        return stubs;
    }

    public static Customer getSmallCustomer() {    	    	
        Customer c = new Customer();
        Account a = new Account();        
        
        a.setCurrency("HRK");
        a.setIBAN("HR1234567");
        
        List<Transaction> ts = new ArrayList<Transaction>();
        for(int i = 0 ; i < 50 ; i ++){
        	ts.add(new Transaction(1,1,"Transaction description",DateTime.now()));
        }
        
        a.getTransactions().addAll(ts);        
        
        List<Account> as = new ArrayList<Account>();
        as.add(a);
        c.setAccounts(as);
        
        return c;        
    }

    public static Customer getBigAssCustomer() {
    	Customer c = new Customer();
        Account a = new Account();
        
        a.setCurrency("HRK");
        a.setIBAN("HR1234567");
        
        List<Transaction> ts = new ArrayList<Transaction>();
        for(int i = 0 ; i < 10000 ; i ++){
        	ts.add(new Transaction(1,1,"Transaction description",DateTime.now()));
        }
        
        a.getTransactions().addAll(ts);        
        
        List<Account> as = new ArrayList<Account>();
        as.add(a);
        c.setAccounts(as);
                
        return c;                
    }
    
    public static String getSmallCustomerUseCase() {
        Customer c = getSmallCustomer();
        c.setId(1);
        c.setName("TestniMaliCustomer");
        
        Profile p = c.getProfile();        		                      
        
        List<Account> as = c.getAccounts();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
//        sb.append("\"uri\":\""+c.getURI()+"\"");              
        sb.append("\"id\":"+c.getId());        
        sb.append(",\"name\":"+"\""+c.getName()+"\"");
        sb.append(",\"profile\":");
        sb.append("{");
        sb.append("\"email\":"+"\""+p.getEmail()+"\"");
        sb.append(",\"phoneNumber\":"+"\""+p.getPhoneNumber()+"\"");
        sb.append("}");
        sb.append(",\"accounts\":");
        sb.append("[");        
        boolean aCm=false;
        for(Account a : as){
        	if(aCm) sb.append(",");
    		aCm = true;
        	sb.append("{");
        	sb.append("\"IBAN\":\""+a.getIBAN()+"\"");
        	sb.append(",\"currency\":\""+a.getCurrency()+"\"");
        	sb.append(",\"transactions\":");
        	sb.append("[");
        	boolean tCm=false;
        	for(Transaction t : a.getTransactions()){
        		if(tCm) sb.append(",");
        		tCm = true;
        		sb.append("{");
        		sb.append("\"description\":\""+t.getDescription()+"\"");
        		sb.append(",\"inflow\":"+t.getInflow());
        		sb.append(",\"outflow\":"+t.getOutflow());
        		sb.append(",\"paymentOn\":\""+t.getPaymentOn().toString()+"\"");
        		sb.append("}");        		
        	}
        	sb.append("]");    
        	sb.append("}");        	
        }
        sb.append("]");        
        sb.append("}");
        
        return sb.toString();		
    }
    
    public static String getBigAssCustomerUseCase() {
        Customer c = getBigAssCustomer();
        
        c.setId(1);
        c.setName("TestniVelkiCustomer");        
        
        Profile p = c.getProfile();        		       
        
        List<Account> as = c.getAccounts();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
//        sb.append("\"URI\":\""+c.getURI()+"\"");              
        sb.append("\"id\":"+c.getId());        
        sb.append(",\"name\":"+"\""+c.getName()+"\"");
        sb.append(",\"profile\":");
        sb.append("{");
        sb.append("\"email\":"+"\""+p.getEmail()+"\"");
        sb.append(",\"phoneNumber\":"+"\""+p.getPhoneNumber()+"\"");
        sb.append("}");
        sb.append(",\"accounts\":");
        sb.append("[");
        boolean aCm = false;
        for(Account a : as){
        	if(aCm) sb.append(",");
    		aCm = true;
        	sb.append("{");
        	sb.append("\"IBAN\":\""+a.getIBAN()+"\"");
        	sb.append(",\"currency\":\""+a.getCurrency()+"\"");
        	sb.append(",\"transactions\":");
        	sb.append("[");
        	boolean tCm = false;
        	for(Transaction t : a.getTransactions()){
        		if(tCm) sb.append(",");
        		tCm = true;
        		sb.append("{");
        		sb.append("\"description\":\""+t.getDescription()+"\"");
        		sb.append(",\"inflow\":"+t.getInflow());
        		sb.append(",\"outflow\":"+t.getOutflow());
        		sb.append(",\"paymentOn\":\""+t.getPaymentOn().toString()+"\"");
        		sb.append("}");        		
        	}
        	sb.append("]");    
        	sb.append("}");        	
        }
        sb.append("]");        
        sb.append("}");
        
        return sb.toString();
    }
}
