package com.javacro.serialization.jacksonstreaming;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Profile;

public abstract class ProfileJacksonStreamingSerialization {
//    @Override
    public static boolean isDefault(final Profile profile) {
        if (profile.getEmail() != null)
            return false;

        if (profile.getPhoneNumber() != null)
            return false;

        return true;
    }

    public static String serialize(final JsonFactory jsonFactory, final Profile value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonGenerator jsonGenerator = jsonFactory.createGenerator(sw);
        write(jsonGenerator, value);
        return sw.toString();
    }

    public static Profile deserialize(final JsonFactory jsonFactory, final byte [] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Profile profile = read(jsonParser);
        jsonParser.close();
        return profile;
    }

    public static void write(final JsonGenerator jsonGenerator, final Profile value) throws IOException {

        final String _email = value.getEmail();
        final String _phoneNumber = value.getPhoneNumber();

        jsonGenerator.writeStartObject();

        if (_email != null)
            jsonGenerator.writeStringField("email", _email);

        if (_phoneNumber != null)
            jsonGenerator.writeStringField("phoneNumber", _phoneNumber);

        jsonGenerator.writeEndObject();
    }

    public static Profile read(final JsonParser jsonParser) throws IOException {
        String _email = null;
        String _phoneNumber = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();
            if ("email".equals(property)) {
                jsonParser.nextToken();
                _email = jsonParser.getText();
            }

            else if ("phoneNumber".equals(property)) {
                jsonParser.nextToken();
                _phoneNumber = jsonParser.getText();
            }
        }

        return new Profile(
                _email,
                _phoneNumber);
    }
}
