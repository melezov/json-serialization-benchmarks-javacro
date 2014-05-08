package com.javacro.serialization.streaming;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class AccountJacksonStreamingSerialization {

    private static boolean needsComma = false;

//    @Override
    public static boolean isDefault(final Account account) {

        final boolean transactionsAreDefault = true;
        for(final Transaction t : account.getTransactions()){
            if(!TransactionJacksonStreamingSerialization.isDefault(t))
                return false;
        }

        if(account.getIBAN().isEmpty()
                && account.getCurrency().isEmpty()
                && transactionsAreDefault)
            return true;


        return false;
    }

    public static String serialize(final Account value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Account deserialize(final JsonFactory jsonFactory, final byte[] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Account account = read(jsonParser);
        jsonParser.close();
        return account;
    }

    public static void write(final JsonWriter jsonWriter, final Account _account) throws IOException {

        final List<Transaction> transactions = _account.getTransactions();
        final boolean [] transactionsDefaultness = new boolean [transactions.size()];
        boolean allTransactionsAreDefault = true;
        for(int i=0; i<transactions.size(); i++){
            final Transaction t = transactions.get(i);
            if(!TransactionJacksonStreamingSerialization.isDefault(t)){
                allTransactionsAreDefault = false;
                transactionsDefaultness[i] = false;
            }
            else
                transactionsDefaultness[i] = true;
        }

        needsComma = false;
        jsonWriter.writeOpenObject();

        final String _IBAN = _account.getIBAN();
        jsonWriter.writeRaw("\"IBAN\":");
        jsonWriter.writeString(_IBAN);

        jsonWriter.writeComma();

        final String _currency = _account.getCurrency();
        jsonWriter.writeRaw("\"currency\":");
        jsonWriter.writeString(_currency);

        jsonWriter.writeComma();


        if(allTransactionsAreDefault)
        {
            jsonWriter.writeRaw("\"transactions\":");
            jsonWriter.writeOpenArray();

            needsComma = false;

            for(int i=0; i<transactions.size() ; i++){

                if(transactionsDefaultness[i]) continue;

                final Transaction t = transactions.get(i);
                if(needsComma) jsonWriter.writeComma();
                TransactionJacksonStreamingSerialization.write(jsonWriter, t);
                needsComma = true;
            }

            jsonWriter.writeCloseArray();
        }

        jsonWriter.writeCloseObject();

        needsComma = true;
    }

    public static Account read(final JsonParser jsonParser) throws IOException {
        String _IBAN = null;
        String _currency = null;
        List<Transaction> _transactions=null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();
            if ("IBAN".equals(property)) {
                jsonParser.nextToken();
                _IBAN = jsonParser.getText();
            }
            else if ("currency".equals(property)) {
                jsonParser.nextToken();
                _currency = jsonParser.getText();
            }
            else if ("transactions".equals(property)) {
                _transactions = new ArrayList<Transaction>();
                while(jsonParser.nextToken() !=  JsonToken.END_ARRAY){
                    _transactions.add(TransactionJacksonStreamingSerialization.read(jsonParser));
                }
            }
        }

        if(_IBAN == null) _IBAN = "";
        if(_currency == null) _currency = "";
        if(_transactions == null) _transactions = new ArrayList<Transaction>();

        return new Account(_IBAN, _currency, _transactions);
    }
}
