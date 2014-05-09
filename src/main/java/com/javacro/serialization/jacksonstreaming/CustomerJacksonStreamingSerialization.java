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
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.manual.AccountManualJsonStreaming;

public abstract class CustomerJacksonStreamingSerialization {
//    @Override

    private static boolean needsComma = false;

    public static boolean isDefault(final Customer customer) {

        if(customer.getId()!=0)
            return false;

        if(!customer.getName().isEmpty())
            return false;

        if(ProfileJacksonStreamingSerialization.isDefault(customer.getProfile()))
            return false;

        for(final Account account : customer.getAccounts()){
            if(!AccountJacksonStreamingSerialization.isDefault(account))
                    return false;
        }

        return true;
    }

    public static String serialize(final JsonFactory jsonFactory, final Customer value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonGenerator jsonGenerator = jsonFactory.createGenerator(sw);
        write(jsonGenerator, value);
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

    public static void write(final JsonGenerator jsonGenerator, final Customer customer) throws IOException {
        final long id = customer.getId();
        final String name = customer.getName();
        final Profile profile = customer.getProfile();
        final List<Account> accounts = customer.getAccounts();

        final boolean[] accountIsDefault = new boolean[accounts.size()];
        boolean allAccountsAreDefault = true;
        for (int i = 0; i < accounts.size(); i++) {
            final Account t = accounts.get(i);
            if (!AccountManualJsonStreaming.isDefault(t)) {
                allAccountsAreDefault = false;
                accountIsDefault[i] = false;
            } else accountIsDefault[i] = true;
        }

        jsonGenerator.writeStartObject();

        if(id != 0) jsonGenerator.writeNumberField("id", id);
        if(!name.equals("")) jsonGenerator.writeStringField("name", name);

        if(!ProfileJacksonStreamingSerialization.isDefault(profile))
            ProfileJacksonStreamingSerialization.write(jsonGenerator, profile);

        if(!allAccountsAreDefault){
            for (int i = 0; i < accounts.size(); i++) {
                if (accountIsDefault[i]) {
                    writeEmptyObject(jsonGenerator);
                } else {
                    AccountJacksonStreamingSerialization.write(jsonGenerator, accounts.get(i));
                }
            }
        }else{
            writeEmptyObjects(accounts.size(), jsonGenerator);
        }

        jsonGenerator.writeEndObject();
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
