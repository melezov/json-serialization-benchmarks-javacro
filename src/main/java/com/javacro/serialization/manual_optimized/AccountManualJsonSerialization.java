package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;

public abstract class AccountManualJsonSerialization {
    public static void serialize(final Writer writer, final Account value) throws IOException {
        writer.write('{');
		writer.write("\"IBAN\":");
		StringConverter.serialize(value.getIBAN(), writer);
    	final String _currency = value.getCurrency(); 
    	if(_currency.length() > 0) {
    		writer.write(",\"currency\":");
    		StringConverter.serialize(_currency, writer);
    	}
    	final List<Transaction> _transactions = value.getTransactions(); 
    	if(!_transactions.isEmpty())  {
    		writer.write(",\"transactions\":[");
    		int _cnt = 0;
    		int _total = _transactions.size() - 1;
    		for (; _cnt < _total; _cnt++) {
    			TransactionManualJsonSerialization.serialize(writer, _transactions.get(_cnt));
    			writer.write(',');
    		}
			TransactionManualJsonSerialization.serialize(writer, _transactions.get(_cnt));
    		writer.write(']');
    	}
    	writer.write('}');
    }

    public static Account deserialize(final Reader reader, final char[] buffer, final int[] tokens, int nextToken) throws IOException {
        String _iban = "";
        String _currency = "";
        ArrayList<Transaction> _transactions = new ArrayList<Transaction>();
    	if (nextToken == '}') return new Account(_iban, _currency, _transactions);
    	int nameHash = ManualJson.fillName(reader, buffer, nextToken);
    	nextToken = ManualJson.getNextToken(reader);
    	if (nextToken == 'n') {
    		if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l') 
    			throw new IOException("null value expected");
    	}
    	else {
	    	switch(nameHash) {
				case 23481245:
					_iban = StringConverter.deserialize(reader, buffer, nextToken);
					nextToken = ManualJson.getNextToken(reader);
					break;
				case 1953622312:
					_currency = StringConverter.deserialize(reader, buffer, nextToken);
					nextToken = ManualJson.getNextToken(reader);
					break;
				case -499179796:
					if (nextToken != '[') throw new IOException("Expecting '['");
					nextToken = ManualJson.getNextToken(reader);
					if (nextToken != '{') throw new IOException("Expecting '{'");
					nextToken = ManualJson.getNextToken(reader);
					_transactions.add(TransactionManualJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
					while((nextToken = ManualJson.getNextToken(reader)) == ',') {
						nextToken = ManualJson.getNextToken(reader);
						if (nextToken != '{') throw new IOException("Expecting '{'");
						nextToken = ManualJson.getNextToken(reader);
						_transactions.add(TransactionManualJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
					}
					nextToken = ManualJson.getNextToken(reader);
					break;
				default:
					nextToken = ManualJson.skip(reader, buffer, tokens, nextToken);
					break;
	    	}	    			
    	}
    	while (nextToken == ',') {
    		nextToken = ManualJson.getNextToken(reader);
        	nameHash = ManualJson.fillName(reader, buffer, nextToken);
        	nextToken = ManualJson.getNextToken(reader);
        	if (nextToken == 'n') {
        		if (reader.read() == 'u' && reader.read() == 'l' && reader.read() == 'l') {
            		nextToken = ManualJson.getNextToken(reader);
        			continue;
        		}
        		throw new IOException("null value expected");
        	}
        	else {
    	    	switch(nameHash) {
    				case 23481245:
    					_iban = StringConverter.deserialize(reader, buffer, nextToken);
    					nextToken = ManualJson.getNextToken(reader);
    					break;
    				case 1953622312:
    					_currency = StringConverter.deserialize(reader, buffer, nextToken);
    					nextToken = ManualJson.getNextToken(reader);
    					break;
    				case -499179796:
    					if (nextToken != '[') throw new IOException("Expecting '['");
    					nextToken = ManualJson.getNextToken(reader);
    					if (nextToken != '{') throw new IOException("Expecting '{'");
    					nextToken = ManualJson.getNextToken(reader);
    					_transactions.add(TransactionManualJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
    					while((nextToken = ManualJson.getNextToken(reader)) == ',') {
    						nextToken = ManualJson.getNextToken(reader);
    						if (nextToken != '{') throw new IOException("Expecting '{'");
    						nextToken = ManualJson.getNextToken(reader);
    						_transactions.add(TransactionManualJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
    					}
    					nextToken = ManualJson.getNextToken(reader);
    					break;
    				default:
    					nextToken = ManualJson.skip(reader, buffer, tokens, nextToken);
    					break;
    	    	}	    			
        	}
    	}
		if (nextToken != '}') {
			if (nextToken == -1) throw new IOException("Unexpected end of json in object Transaction");
			else throw new IOException("Expecting '}' at position " + ManualJson.positionInStream(reader) + ". Found " + (char)nextToken);
		}
        return new Account(_iban, _currency, _transactions);
    }
    
    public static Account deserialize(final JsonReader reader) throws IOException {
        String _iban = "";
        String _currency = "";
        ArrayList<Transaction> _transactions = new ArrayList<Transaction>();
    	if (reader.read() == '}') return new Account(_iban, _currency, _transactions);
    	int nameHash = reader.fillName();
    	int nextToken = reader.moveToNextToken();
    	if (nextToken == 'n') {
    		if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l') 
    			throw new IOException("null value expected");
    	}
    	else {
	    	switch(nameHash) {
				case 23481245:
					_iban = reader.readString();
					nextToken = reader.getNextToken();
					break;
				case 1953622312:
					_currency = reader.readString();
					nextToken = reader.getNextToken();
					break;
				case -499179796:
					if (reader.read() != '[') throw new IOException("Expecting '['. Found: " + (char)reader.read());
					nextToken = reader.getNextToken();
					if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)reader.read());
					nextToken = reader.getNextToken();
					_transactions.add(TransactionManualJsonSerialization.deserialize(reader));
					while((nextToken = reader.getNextValidToken()) == ',') {
						reader.invalidate();
						if (reader.read() != '{') throw new IOException("Expecting '{'");
						nextToken = reader.getNextValidToken();
						_transactions.add(TransactionManualJsonSerialization.deserialize(reader));
					}
					nextToken = reader.getNextValidToken();
					break;
				default:
					nextToken = reader.skip();
					break;
	    	}	    			
    	}
    	while (nextToken == ',') {
    		nextToken = reader.moveToNextToken();
        	nameHash = reader.fillName();
        	nextToken = reader.moveToNextToken();
        	if (nextToken == 'n') {
        		if (reader.read() == 'u' && reader.read() == 'l' && reader.read() == 'l') {
            		nextToken = reader.getNextToken();
        			continue;
        		}
        		throw new IOException("null value expected");
        	}
        	else {
    	    	switch(nameHash) {
    				case 23481245:
    					_iban = reader.readString();
    					nextToken = reader.getNextToken();
    					break;
    				case 1953622312:
    					_currency = reader.readString();
    					nextToken = reader.getNextToken();
    					break;
    				case -499179796:
    					if (reader.read() != '[') throw new IOException("Expecting '['. Found: " + (char)reader.read());
    					nextToken = reader.getNextToken();
    					if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)reader.read());
    					nextToken = reader.getNextToken();
    					_transactions.add(TransactionManualJsonSerialization.deserialize(reader));
    					while((nextToken = reader.getNextValidToken()) == ',') {
    						reader.invalidate();
    						if (reader.read() != '{') throw new IOException("Expecting '{'");
    						nextToken = reader.getNextValidToken();
    						_transactions.add(TransactionManualJsonSerialization.deserialize(reader));
    					}
    					nextToken = reader.getNextValidToken();
    					break;
    				default:
    					nextToken = reader.skip();
    					break;
    	    	}	    			
        	}
    	}
		if (nextToken != '}') {
			if (nextToken == -1) throw new IOException("Unexpected end of json in object Transaction");
			else throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		}
        return new Account(_iban, _currency, _transactions);
    }
    
}
