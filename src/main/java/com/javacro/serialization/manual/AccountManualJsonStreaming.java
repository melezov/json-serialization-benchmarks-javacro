package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class AccountManualJsonStreaming {
//    @Override
    public static boolean isDefault(final Account account) {

        if(account.getIBAN().equals(""))
            return false;

        if(account.getCurrency().equals(""))
            return false;

        for(final Transaction t : account.getTransactions()){
            if(!TransactionManualJsonStreaming.isDefault(t))
                return false;
        }


        return false;
    }

    public static String serialize(final Account value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Account deserialize(final String inputString) throws IOException {
        return deserialize(inputString.getBytes("UTF-8"));
    }

    public static Account deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes);
        return read(jsonReader);
    }

    public static void write(final JsonWriter jsonWriter, final Account value) throws IOException {

        final String _IBAN = value.getIBAN();
        final String _currency = value.getCurrency();
        final List<Transaction> transactions = value.getTransactions();

        final boolean [] transactionIsDefault = new boolean [transactions.size()];
        boolean allTransactionsAreDefault = true;
        for(int i=0; i<transactions.size(); i++){
            final Transaction t = transactions.get(i);
            if(!TransactionManualJsonStreaming.isDefault(t)){
                allTransactionsAreDefault = false;
                transactionIsDefault[i] = false;
            }
            else
                transactionIsDefault[i] = true;
        }

        boolean needsComma = false;
        jsonWriter.writeOpenObject();

        if(!_IBAN.equals("")){
            if(needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"IBAN\":");
            jsonWriter.writeString(_IBAN);
            needsComma = true;
        }

        if(!_currency.equals("")){
            if(needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"currency\":");
            jsonWriter.writeString(_currency);
            needsComma = true;
        }

        if(!allTransactionsAreDefault)
        {

            if(needsComma) jsonWriter.writeComma();

            jsonWriter.writeRaw("\"transactions\":");
            jsonWriter.writeOpenArray();

            needsComma = false;

            for(int i=0; i<transactions.size() ; i++){

                if(transactionIsDefault[i]){
                    writeEmptyObject(jsonWriter);
                    continue;
                }

                if(needsComma) jsonWriter.writeComma();

                TransactionManualJsonStreaming.write(jsonWriter, transactions.get(i));

                needsComma = true;
            }

            jsonWriter.writeCloseArray();

        } else{
            jsonWriter.writeRaw("\"transactions\":");
            jsonWriter.writeOpenArray();

            for(int i=1;i<transactions.size();i++){
                jsonWriter.writeComma();
                writeEmptyObject(jsonWriter);
            }

            jsonWriter.writeCloseArray();
        }


        jsonWriter.writeCloseObject();

    }

    public static Account read(final JsonReader jsonReader) throws IOException {

        boolean needsComma = false;

        jsonReader.assertRead('{');

        String _IBAN = null;
        String _currency = null;
        List<Transaction> _transactions=null;

        while (jsonReader.read() != '}') {
            if(needsComma)
                jsonReader.assertLast(',');

            final String property = jsonReader.readString();
            jsonReader.assertNext(':');

            if (property.equals("IBAN")){
                _IBAN = jsonReader.readString();
                needsComma=true;
                continue;
            }

            if (property.equals("currency")){
                _currency = jsonReader.readString();
                needsComma=true;
                continue;
            }

            if (property.equals("transactions")){
                _transactions = readTransactions(jsonReader);
                needsComma=true;
                continue;
            }
        }

        return new Account(
                    _IBAN == null ? "" : _IBAN
                    , _currency == null? "" : _currency
                    , _transactions == null ? new ArrayList<Transaction>(): _transactions);
    }

    private static List<Transaction> readTransactions(final JsonReader jsonReader) throws IOException{
        final List<Transaction> _transactions = new ArrayList<Transaction>();

        jsonReader.assertNext('[');

        boolean transactionNeedsComma=false;
        while(jsonReader.read() != ']')
        {
            if(transactionNeedsComma)
                jsonReader.assertLast(',');
            _transactions.add(TransactionManualJsonStreaming.read(jsonReader));
            jsonReader.invalidate();
            transactionNeedsComma=true;
        }

        jsonReader.assertLast(']');

        return _transactions;
    }

    private static void writeEmptyObject(final JsonWriter jsonWriter) throws IOException{
        jsonWriter.writeOpenObject();
        jsonWriter.writeCloseObject();
    }
}
