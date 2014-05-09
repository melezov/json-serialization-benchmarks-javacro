package com.javacro.benchmarks;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.javacro.protobuf.model.accounting.AccountingProtobuf;
import com.javacro.protobuf.model.accounting.AccountingProtobuf.Account;
import com.javacro.protobuf.model.accounting.AccountingProtobuf.Customer;
import com.javacro.protobuf.model.accounting.AccountingProtobuf.Profile;
import com.javacro.protobuf.model.accounting.AccountingProtobuf.Transaction;


// TODO: implement, not implemented at all

public class TestCasesProtobuf {

    //public static final long NUMBER_OF_RUNS = 100000;

    public static List<Profile> getProfileStubs() {

        final List<Profile> ps = new ArrayList<AccountingProtobuf.Profile>();

        final Profile.Builder profile = Profile.newBuilder();

        ps.add(profile.build());
        profile.setEmail("email@email.mail");
        ps.add(profile.build());
        profile.setPhoneNumber("+12345678");
        ps.add(profile.build());

        return ps;
    }

    public static List<Transaction> getBigAssTransactionStubs() {

        final List<Transaction> ts = new ArrayList<Transaction>();

        for (int i = 0; i < 1000; i++) {
            final Transaction.Builder b = Transaction.newBuilder();
            ts.add(b.build());
            b.setDescription("abcese");
            ts.add(b.build());
            b.setInflow(1);
            ts.add(b.build());
            b.setOutflow(2);
            ts.add(b.build());
            b.setPaymentOn(DateTime.now().toString());
            ts.add(b.build());
        }

        return ts;
    }

    public static List<Transaction> getTransactionStubs() {
        final List<Transaction> ts = new ArrayList<Transaction>();

            final Transaction.Builder b = Transaction.newBuilder();
            ts.add(b.build());
            b.setDescription("abcese");
            ts.add(b.build());
            b.setInflow(1);
            ts.add(b.build());
            b.setOutflow(2);
            ts.add(b.build());
        b.setPaymentOn(DateTime.now().toString());
        ts.add(b.build());

        return ts;

    }

    public static List<Account> getBigAssAccountStubs() {
        final List<Account> stubs = new ArrayList<Account>();

        final Account.Builder b = Account.newBuilder();

        stubs.add(b.build());
        b.setIBAN("HR123123124");
        final List<Transaction> bgts = getBigAssTransactionStubs();
        b.addAllTransactions(getBigAssTransactionStubs());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());

        return stubs;
    }

    public static List<Account> getAccountStubs() {
        final List<Account> stubs = new ArrayList<Account>();

        final Account.Builder b = Account.newBuilder();

        stubs.add(b.build());
        b.setIBAN("HR123123124");
        final List<Transaction> bgts = getTransactionStubs();
        b.addAllTransactions(getTransactionStubs());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());
        stubs.add(b.build());

        return stubs;
    }

    public static List<Customer> getCustomerStubs() {
        final List<Customer> stubs = new ArrayList<Customer>();

        final Customer.Builder b = Customer.newBuilder();
        stubs.add(b.build());
        b.setName("Mirko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getAccountStubs());
        b.setName("Slavko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getAccountStubs());
        b.setName("Marko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getAccountStubs());

        return stubs;
    }

    public static List<Customer> getBigAssCustomerStubs() {
        final List<Customer> stubs = new ArrayList<Customer>();

        final Customer.Builder b = Customer.newBuilder();
        stubs.add(b.build());
        b.setName("Mirko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getBigAssAccountStubs());
        b.setName("Slavko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getBigAssAccountStubs());
        b.setName("Marko").setProfile(getProfileStubs().get(0)).setId(5).addAllAccounts(getBigAssAccountStubs());

        return stubs;
    }

    public static void getSmallCustomerUseCase(){

    }

    public static void getBigAssCustomerUseCase(){

    }

    public static Customer getSmallCustomer() {
        return getCustomerStubs().get(3);
    }

    public static Customer getBigAssCustomer() {
        return getBigAssCustomerStubs().get(3);
    }
}
