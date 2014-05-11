package com.javacro.benchmarks;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.protobuf.model.accounting.AccountingProtobuf;

public class Util {

    public static long getTransactionsNum(final Customer c){
        long sum=0;
        for(final Account a : c.getAccounts()){
            sum+=a.getTransactions().size();
        }

        return sum;
    }
          
    public static long getTransactionsNum(final AccountingProtobuf.Customer c){
        long sum=0;
        for(final AccountingProtobuf.Account a : c.getAccountsList()){
            sum+=a.getTransactionsList().size();
        }

        return sum;
    }
    
    public static Profile toDsl(final AccountingProtobuf.Profile profile_protobuf){
    	Profile profile_Dsl = new Profile();
    	
    	profile_Dsl.setEmail(profile_protobuf.getEmail());
    	profile_Dsl.setPhoneNumber(profile_protobuf.getPhoneNumber());
    	
    	return profile_Dsl;
    }
    
    public static Transaction toDsl(final AccountingProtobuf.Transaction transaction_protobuf){
    	Transaction transaction_dsl = new Transaction();
    	    	
    	transaction_dsl.setDescription(transaction_protobuf.getDescription());
    	transaction_dsl.setInflow(transaction_protobuf.getInflow());
    	transaction_dsl.setOutflow(transaction_protobuf.getOutflow());
    	
    	// Bottleneck
    	if(transaction_protobuf.getPaymentOn().equals(""))
    		transaction_dsl.setPaymentOn(DateTime.parse("1-1-1T00:22"));
    	else
    		transaction_dsl.setPaymentOn(DateTime.parse(transaction_protobuf.getPaymentOn()));
    	    	
    	return transaction_dsl;
    }
    
    public static Account toDsl(final AccountingProtobuf.Account account_protobuf){
    	Account account_dsl = new Account();
    	
    	account_dsl.setIBAN(account_protobuf.getIBAN());
    	account_dsl.setCurrency(account_protobuf.getCurrency());
    	
    	if(account_protobuf.getTransactionsCount() > 0){
    		List<Transaction> transactions_dsl = new ArrayList<Transaction>(); 
    		for(AccountingProtobuf.Transaction transaction_protobuf : account_protobuf.getTransactionsList()){
    			transactions_dsl.add(toDsl(transaction_protobuf));
    		}
    		account_dsl.setTransactions(transactions_dsl);
    	}
    	
    	return account_dsl;
    }
    
    public static Customer toDsl(final AccountingProtobuf.Customer customer_protobuf){
    	Customer customer_dsl = new Customer();
    	
    	customer_dsl.setId(customer_protobuf.getId());
    	customer_dsl.setName(customer_protobuf.getName());
    	customer_dsl.setProfile(toDsl(customer_protobuf.getProfile()));
    	
    	if(customer_protobuf.getAccountsCount() > 0){
    		List<Account> accounts_dsl = new ArrayList<Account>();
    		for(AccountingProtobuf.Account account_protobuf : customer_protobuf.getAccountsList()){
    			accounts_dsl.add(toDsl(account_protobuf));
    		}
    		customer_dsl.setAccounts(accounts_dsl);    		
    	}
    	
    	return customer_dsl;
    }
    
    public static AccountingProtobuf.Profile toProtobuf(Profile profile_dsl){
    	AccountingProtobuf.Profile.Builder pb = AccountingProtobuf.Profile.newBuilder();        	
    	
    	if(profile_dsl != null){
    		if(profile_dsl.getEmail()!=null)
    			pb.setEmail(profile_dsl.getEmail());
    		if(profile_dsl.getPhoneNumber()!=null)
    		pb.setPhoneNumber(profile_dsl.getPhoneNumber());
    	}
    	
    	return pb.build();
    }
    
    public static AccountingProtobuf.Transaction toProtobuf(Transaction transaction_dsl){
    	AccountingProtobuf.Transaction.Builder tb = AccountingProtobuf.Transaction.newBuilder();
    	    	
    	tb.setDescription(transaction_dsl.getDescription());
    	tb.setInflow(transaction_dsl.getInflow());
    	tb.setOutflow(transaction_dsl.getOutflow());
    	tb.setPaymentOn(transaction_dsl.getPaymentOn().toString());//Bottleneck
    	
    	return tb.build();
    }
    
    public static AccountingProtobuf.Account toProtobuf(Account ad){
    	AccountingProtobuf.Account.Builder ab = AccountingProtobuf.Account.newBuilder();
    	    	    	
    	ab.setIBAN(ad.getIBAN());
    	ab.setCurrency(ad.getCurrency());    	
    	
    	if(ad.getTransactions()!=null && ad.getTransactions().size()>0){
    		List<AccountingProtobuf.Transaction> pts = new ArrayList<AccountingProtobuf.Transaction>();
    		for(Transaction t: ad.getTransactions()){
    			pts.add(toProtobuf(t));
    		}
    		ab.clearTransactions();
    		ab.addAllTransactions(pts);
    	}
    	
    	return ab.build();
    }
    
    public static AccountingProtobuf.Customer toProtobuf(Customer cd){
    	AccountingProtobuf.Customer.Builder cb = AccountingProtobuf.Customer.newBuilder();
    	    	    	
    	cb.setId(cd.getId());
    	cb.setName(cd.getName());
    	cb.setProfile(toProtobuf(cd.getProfile()));
    	
    	if(cd.getAccounts()!=null && cd.getAccounts().size()>0){
    		List<AccountingProtobuf.Account> pas = new ArrayList<AccountingProtobuf.Account>();
    		for(Account a: cd.getAccounts()){
    			pas.add(toProtobuf(a));
    		}
    		cb.clearAccounts();
    		cb.addAllAccounts(pas);
    	}
    	
    	return cb.build();
    }
        	
}
