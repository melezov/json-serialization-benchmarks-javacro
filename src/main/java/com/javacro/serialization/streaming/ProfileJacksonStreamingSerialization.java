package com.javacro.serialization.streaming;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class ProfileJacksonStreamingSerialization {
//    @Override
    public static boolean isDefault(final Profile profile) {
        if (profile.getEmail() != null)
            return false;

        if (profile.getPhoneNumber() != null)
            return false;

        return true;
    }

    public static String serialize(final Profile value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Profile deserialize(final JsonFactory jsonFactory, final byte [] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Profile profile = read(jsonParser);
        jsonParser.close();
        return profile;
    }

    public static void write(final JsonWriter jsonWriter, final Profile value) throws IOException {
        jsonWriter.writeOpenObject();
        boolean needsComma = false;

        final String _email = value.getEmail();
        if (_email != null) {
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"email\":");
            jsonWriter.writeString(_email);
            needsComma = true;
        }

        final String _phoneNumber = value.getPhoneNumber();
        if (_phoneNumber != null) {
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"phoneNumber\":");
            jsonWriter.writeString(_phoneNumber);
            needsComma = true;
        }

        jsonWriter.writeCloseObject();
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
