package com.javacro.serialization.streaming;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class CustomerJacksonStreamingSerialization {
//    @Override

    private static boolean needsComma = false;

    public static boolean isDefault(final Customer customer) {

        if(customer.getId()!=0)
            return false;

        if(!customer.getName().isEmpty())
            return false;

        for(final Account account : customer.getAccounts()){
            if(!AccountJacksonStreamingSerialization.isDefault(account))
                    return false;
        }

        if(ProfileJacksonStreamingSerialization.isDefault(customer.getProfile()))
            return false;



        return true;
    }

    public static String serialize(final Customer value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Customer deserialize(final JsonFactory jsonFactory, final byte[] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Customer customer = read(jsonParser);
        jsonParser.close();
        return customer;
    }

    public static Customer deserialize(final JsonFactory jsonFactory, final String ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Customer customer = read(jsonParser);
        jsonParser.close();
        return customer;
    }

    public static void write(final JsonWriter jsonWriter, final Customer value) throws IOException {

        final List<Account> accounts = value.getAccounts();
        final boolean [] accountsDefaultness = new boolean [accounts.size()];
        boolean allAccountsAreDefault = true;
        for(int i=0; i<accounts.size(); i++){
            final Account a = accounts.get(i);
            if(!AccountJacksonStreamingSerialization.isDefault(a)){
                allAccountsAreDefault = false;
                accountsDefaultness[i] = false;
            }
            else
                accountsDefaultness[i] = true;
        }

        jsonWriter.writeOpenObject();

        final Long _id = value.getId();
        if (needsComma) jsonWriter.writeComma();
        jsonWriter.writeRaw("\"id\":");
        jsonWriter.writeString(_id.toString());
        needsComma = true;

        final String _name = value.getName();
        if (needsComma) jsonWriter.writeComma();
        jsonWriter.writeRaw("\"name\":");
        jsonWriter.writeString(_name);
        needsComma = true;

        {
            final Profile _profile = value.getProfile();
            if(!ProfileJacksonStreamingSerialization.isDefault(_profile)){
                if(needsComma) jsonWriter.writeComma();
                jsonWriter.writeRaw("\"profile\":");
                ProfileJacksonStreamingSerialization.write(jsonWriter, _profile);
                needsComma=true;
            }
        }

        if(!allAccountsAreDefault){
            if(needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"accounts\":");
            jsonWriter.writeOpenArray();

            needsComma = false;
            for(int i=0; i<accounts.size(); i++){

                if(accountsDefaultness[i]) continue;

                final Account _a = accounts.get(i);
                if(needsComma) jsonWriter.writeComma();
                writeAccount(_a, jsonWriter);
                needsComma = true;
            }

            jsonWriter.writeCloseArray();
        }

        jsonWriter.writeCloseObject();
    }

    private static void writeAccount(final Account _account, final JsonWriter jsonWriter) throws IOException {

        if (needsComma) jsonWriter.writeComma();
        jsonWriter.writeOpenObject();

        final String _IBAN = _account.getIBAN();
        jsonWriter.writeRaw("\"IBAN\":");
        jsonWriter.writeString(_IBAN);

        jsonWriter.writeComma();

        final String _currency = _account.getCurrency();
        jsonWriter.writeRaw("\"currency\":");
        jsonWriter.writeString(_currency);

        jsonWriter.writeComma();

        {
            jsonWriter.writeRaw("\"transactions\":");
            jsonWriter.writeOpenArray();

            needsComma = false;
            for (final Transaction t : _account.getTransactions()) {
                TransactionJacksonStreamingSerialization.write(jsonWriter, t);
                writeTransaction(t, jsonWriter);
            }
            jsonWriter.writeCloseArray();
        }

        jsonWriter.writeCloseObject();

        needsComma = true;

    }

    private static void writeTransaction(final Transaction _transaction, final JsonWriter jsonWriter)
            throws IOException {

        if (needsComma) jsonWriter.writeComma();
        jsonWriter.writeOpenObject();

        final Double _inflow = _transaction.getInflow();
        jsonWriter.writeRaw("\"inflow\":");
        jsonWriter.writeString(_inflow.toString());

        jsonWriter.writeComma();

        final Double _outflow = _transaction.getOutflow();
        jsonWriter.writeRaw("\"outflow\":");
        jsonWriter.writeString(_outflow.toString());

        jsonWriter.writeComma();

        final String _description = _transaction.getDescription();
        jsonWriter.writeRaw("\"description\":");
        jsonWriter.writeString(_description.toString());

        jsonWriter.writeComma();

        final DateTime _paymentOn = _transaction.getPaymentOn();
        jsonWriter.writeRaw("\"paymentOn\":");
        jsonWriter.writeString(_paymentOn.toString());

        jsonWriter.writeCloseObject();
        needsComma = true;
    }

    public static Customer read(final JsonParser jsonParser) throws IOException {
        long _id = 0;
        String _name = null;
        Profile _profile = null;
        List<Account> _accounts = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();
            if ("id".equals(property)) {
                jsonParser.nextToken();
                _id = jsonParser.getLongValue();
            }
            else if ("name".equals(property)) {
                jsonParser.nextToken();
                _name = jsonParser.getText();
            }
            else if ("profile".equals(property)) {
                _profile = ProfileJacksonStreamingSerialization.read(jsonParser);
            }
            else if ("accounts".equals(property)) {
                _accounts = new ArrayList<Account>();
                while(jsonParser.nextToken() != JsonToken.END_ARRAY){
                    _accounts.add(AccountJacksonStreamingSerialization.read(jsonParser));
                }
            }
        }

        if(_name == null) _name = "";
        if(_profile == null) _profile = new Profile();
        if(_accounts == null) _accounts = new ArrayList<Account>();

        return new Customer(_id, _name, _profile, _accounts);
    }
}
