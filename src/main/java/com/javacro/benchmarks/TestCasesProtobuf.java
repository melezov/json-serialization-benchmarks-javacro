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

	// public static final long NUMBER_OF_RUNS = 100000;

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
			ts.addAll(getTransactionStubs());
		}

		return ts;
	}

	public static List<Transaction> getTransactionStubs() {
		final List<Transaction> ts = new ArrayList<Transaction>();

		final Transaction.Builder b = Transaction.newBuilder();
//		ts.add(b.build());
		ts.add(b.setInflow(1).build());
		ts.add(b.setInflow(1).setOutflow(1).setDescription("abcde").build());
		ts.add(b.setInflow(0).setOutflow(1).setDescription("abcde").build());
		ts.add(b.setInflow(0).setOutflow(0).setDescription("ABCDE").build());

		return ts;

	}

	public static List<Account> getBigAssAccountStubs() {
		final List<Account> stubs = new ArrayList<Account>();

		final Account.Builder b = Account.newBuilder();

//		stubs.add(b.build());
		stubs.add(b.setIBAN("HR123123124").addAllTransactions(getBigAssTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setCurrency("HRK").addAllTransactions(getBigAssTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setCurrency("tumegledaj").build());
		stubs.add(b.addAllTransactions(getBigAssTransactionStubs()).build());
		stubs.add(b.setIBAN("HR123123124").setCurrency("HRK").addAllTransactions(getBigAssTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setIBAN("HR123123124").setCurrency("HRK").addAllTransactions(getBigAssTransactionStubs()).build());

		return stubs;
	}

	public static List<Account> getAccountStubs() {
		final List<Account> stubs = new ArrayList<Account>();

		final Account.Builder b = Account.newBuilder();

//		stubs.add(b.build());
		stubs.add(b.setIBAN("HR123123124").addAllTransactions(getTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setCurrency("HRK").addAllTransactions(getTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setCurrency("tumegledaj").build());
		stubs.add(b.addAllTransactions(getTransactionStubs()).build());
		stubs.add(b.setIBAN("HR123123124").setCurrency("HRK").addAllTransactions(getTransactionStubs().subList(0, 1)).build());
		stubs.add(b.setIBAN("HR123123124").setCurrency("HRK").addAllTransactions(getTransactionStubs()).build());

		return stubs;
	}

	public static List<Customer> getCustomerStubs() {
		final List<Customer> stubs = new ArrayList<Customer>();

		final Customer.Builder b = Customer.newBuilder();
//		stubs.add(b.build());
		b.setName("Mirko").setProfile(getProfileStubs().get(0)).setId(5)
				.addAllAccounts(getAccountStubs().subList(0, 1));
		stubs.add(b.build());
		b.setName("Slavko").setProfile(getProfileStubs().get(1)).setId(5)
				.addAllAccounts(getAccountStubs());
		stubs.add(b.build());
		b.setName("Marko").setProfile(getProfileStubs().get(2)).setId(5)
				.addAllAccounts(getAccountStubs());
		stubs.add(b.build());

		return stubs;
	}

	public static List<Customer> getBigAssCustomerStubs() {
		final List<Customer> stubs = new ArrayList<Customer>();

		final Customer.Builder b = Customer.newBuilder();
		stubs.add(b.build());
		b.setName("Mirko").setProfile(getProfileStubs().get(0)).setId(5)
				.addAllAccounts(getBigAssAccountStubs().subList(0, 1));
		stubs.add(b.build());
		b.setName("Slavko").setProfile(getProfileStubs().get(1)).setId(5)
				.addAllAccounts(getBigAssAccountStubs());
		stubs.add(b.build());
		b.setName("Marko").setProfile(getProfileStubs().get(2)).setId(5)
				.addAllAccounts(getBigAssAccountStubs());
		stubs.add(b.build());

		return stubs;
	}

	public static byte[] getSmallCustomerUseCase() {
		return getSmallCustomer().toByteArray();
	}

	public static byte[] getBigAssCustomerUseCase() {
		return getBigAssCustomer().toByteArray();
	}

	public static Customer getSmallCustomer() {		
		Customer.Builder cb = Customer.newBuilder();
		Account.Builder ab = Account.newBuilder();
		Transaction.Builder tb = Transaction.newBuilder();
		
		ab.setCurrency("HRK");
        ab.setIBAN("HR1234567");
		
		tb.setDescription("transaction description").setInflow(1).setOutflow(1);
		
		List<Transaction> ts = new ArrayList<AccountingProtobuf.Transaction>();
		
		for(int i = 0 ; i < 50 ; i ++){
			ts.add(tb.build());
		}		
		
		ab.addAllTransactions(ts);
		
		cb.addAccounts(ab.build());
		
		return cb.build();
	}

	public static Customer getBigAssCustomer() {
		Customer.Builder cb = Customer.newBuilder();
		Account.Builder ab = Account.newBuilder();
		Transaction.Builder tb = Transaction.newBuilder();

		ab.setCurrency("HRK");
        ab.setIBAN("HR1234567");
		
		tb.setDescription("transaction description").setInflow(1).setOutflow(1);
		
		List<Transaction> ts = new ArrayList<AccountingProtobuf.Transaction>();
		
		for(int i = 0 ; i < 10000 ; i ++){
			ts.add(tb.build());
		}		
		
		ab.addAllTransactions(ts);
		
		cb.addAccounts(ab.build());
		
		return cb.build();
	}
}
