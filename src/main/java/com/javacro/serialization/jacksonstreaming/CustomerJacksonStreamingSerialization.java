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

public abstract class CustomerJacksonStreamingSerialization {
//    @Override

    public static String serialize(final JsonFactory jsonFactory, final Customer value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonGenerator jsonGenerator = jsonFactory.createGenerator(sw);
        write(jsonGenerator, value);
        jsonGenerator.close();
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

        jsonGenerator.writeStartObject();

        if (id != 0) 
        	jsonGenerator.writeNumberField("id", id);
        if (!name.equals("")) 
        	jsonGenerator.writeStringField("name", name);

        jsonGenerator.writeFieldName("profile");
        ProfileJacksonStreamingSerialization.write(jsonGenerator, profile);

        if (accounts != null) {
            jsonGenerator.writeFieldName("accounts");
            jsonGenerator.writeStartArray();
            for (int i = 0; i < accounts.size(); i++) {
                AccountJacksonStreamingSerialization.write(jsonGenerator, accounts.get(i));
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }

    public static Customer read(final JsonParser jsonParser) throws IOException {
        long _id = 0;
        String _name = "";
        Profile _profile = new Profile();
        ArrayList<Account> _accounts = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();
            if ("id".equals(property)) {
                jsonParser.nextToken();
                _id = jsonParser.getLongValue();
            } else if ("name".equals(property)) {
                jsonParser.nextToken();
                _name = jsonParser.getText();
            } else if ("profile".equals(property)) {
                _profile = ProfileJacksonStreamingSerialization.read(jsonParser);
            } else if ("accounts".equals(property)) {
            	_accounts = new ArrayList<Account>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    _accounts.add(AccountJacksonStreamingSerialization.read(jsonParser));
                }
            }
        }

        return new Customer(_id, _name, _profile, _accounts);
    }
}
