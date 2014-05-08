package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;

import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class ProfileManualJsonSerialization {
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

    public static Profile deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes, 0);
        return read(jsonReader);
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

    public static Profile read(final JsonReader jr) throws IOException {
        boolean needComma=false;
        jr.assertRead('{');

        String _email = null;
        String _phoneNumber = null;

        while (jr.read() != '}') {
            if(needComma) jr.assertLast(',');

            final String property = jr.readString();
            jr.assertNext(':');

            switch(property.charAt(0)){
                case 'e': //email
                    _email = jr.readString();
                    needComma=true;
                    continue;
                case 'p': //phone number
                    _phoneNumber = jr.readString();
                    needComma=true;
                    continue;
            }

//            if (property.equals("email")) {
//                _email = jr.readString();
//                needComma=true;
//                continue;
//            }
//
//            if (property.equals("phoneNumber")) {
//                _phoneNumber = jr.readString();
//                needComma=true;
//                continue;
//            }
        }

        return new Profile(
                _email,
                _phoneNumber);
    }
}
