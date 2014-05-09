package com.javacro.serialization.jacksonstreaming;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.manual.TransactionManualJsonStreaming;

public abstract class AccountJacksonStreamingSerialization {

    private static boolean needsComma = false;

    public static boolean isDefault(final Account account) {

        if (account.getIBAN().equals("")) return false;

        if (account.getCurrency().equals("")) return false;

        for (final Transaction t : account.getTransactions()) {
            if (!TransactionManualJsonStreaming.isDefault(t)) return false;
        }

        return false;
    }

    public static String serialize(final JsonFactory jsonFactory, final Account value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonGenerator jsonGenerator = jsonFactory.createGenerator(sw);
        write(jsonGenerator, value);
        return sw.toString();
    }

    public static Account deserialize(final JsonFactory jsonFactory, final byte[] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Account account = read(jsonParser);
        jsonParser.close();
        return account;
    }

    public static void write(final JsonGenerator jsonGenerator, final Account account) throws IOException {

        final String IBAN = account.getIBAN();
        final String currency = account.getCurrency();
        final List<Transaction> transactions = account.getTransactions();

        final boolean[] transactionIsDefault = new boolean[transactions.size()];
        boolean allTransactionsAreDefault = true;
        for (int i = 0; i < transactions.size(); i++) {
            final Transaction t = transactions.get(i);
            if (!TransactionManualJsonStreaming.isDefault(t)) {
                allTransactionsAreDefault = false;
                transactionIsDefault[i] = false;
            } else transactionIsDefault[i] = true;
        }

        jsonGenerator.writeStartObject();

        if (!IBAN.equals("")) jsonGenerator.writeStringField("IBAN", IBAN);

        if (!currency.equals("")) jsonGenerator.writeStringField("currency", currency);

        if (transactions.size() > 0) {
            jsonGenerator.writeStartArray();

            if (!allTransactionsAreDefault) {
                for (int i = 0; i < transactions.size(); i++) {
                    if (transactionIsDefault[i]) {
                        writeEmptyObject(jsonGenerator);
                    } else {
                        TransactionJacksonStreamingSerialization.write(jsonGenerator, transactions.get(i));
                    }
                }
            } else {
                writeEmptyObjects(transactions.size(), jsonGenerator);
            }

            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }

    public static Account read(final JsonParser jsonParser) throws IOException {
        String _IBAN = null;
        String _currency = null;
        List<Transaction> _transactions = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();
            if ("IBAN".equals(property)) {
                jsonParser.nextToken();
                _IBAN = jsonParser.getText();
            } else if ("currency".equals(property)) {
                jsonParser.nextToken();
                _currency = jsonParser.getText();
            } else if ("transactions".equals(property)) {
                _transactions = new ArrayList<Transaction>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    _transactions.add(TransactionJacksonStreamingSerialization.read(jsonParser));
                }
            }
        }

        if (_IBAN == null) _IBAN = "";
        if (_currency == null) _currency = "";
        if (_transactions == null) _transactions = new ArrayList<Transaction>();

        return new Account(_IBAN, _currency, _transactions);
    }

    private static void writeEmptyObjects(final int howMuch, final JsonGenerator jsonGenerator) throws IOException {
        for (int i = 0; i < howMuch; i++) {
            writeEmptyObject(jsonGenerator);
        }
    }

    private static void writeEmptyObject(final JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
    }

}
