package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class AccountManualJsonSerialization {
//    @Override
    public static boolean isDefault(final Account account) {

        final boolean accountsAreDefault = true;
        for(final Transaction t : account.getTransactions()){
            if(!TransactionManualJsonSerialization.isDefault(t))
                return false;
        }

        if(account.getIBAN().isEmpty()
                && account.getCurrency().isEmpty()
                && accountsAreDefault)
            return true;


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
        jsonWriter.writeOpenObject();

        // TODO:
//        boolean needsComma = false;
//
//        final String _email = value.getEmail();
//        if (_email != null) {
//            if (needsComma) jsonWriter.writeComma();
//            jsonWriter.writeRaw("\"email\":");
//            jsonWriter.writeString(_email);
//            needsComma = true;
//        }
//
//        final String _phoneNumber = value.getPhoneNumber();
//        if (_phoneNumber != null) {
//            if (needsComma) jsonWriter.writeComma();
//            jsonWriter.writeRaw("\"phoneNumber\":");
//            jsonWriter.writeString(_phoneNumber);
//            needsComma = true;
//        }

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
            _transactions.add(TransactionManualJsonSerialization.read(jsonReader));
            jsonReader.invalidate();
            transactionNeedsComma=true;
        }

        jsonReader.assertLast(']');

        return _transactions;
    }

}
