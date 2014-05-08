package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class CustomerManualJsonSerialization {
//    @Override
    public static boolean isDefault(final Customer customer) {

        if(customer.getId()!=0)
            return false;

        if(!customer.getName().isEmpty())
            return false;

        for(final Account account : customer.getAccounts()){
            if(!AccountManualJsonSerialization.isDefault(account))
                    return false;
        }

        if(ProfileManualJsonSerialization.isDefault(customer.getProfile()))
            return false;

        return true;
    }

    public static String serialize(final Customer value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Customer deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes);
        return read(jsonReader);
    }

    public static void write(final JsonWriter jsonWriter, final Customer value) throws IOException {
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

    public static Customer read(final JsonReader jsonReader) throws IOException {
        boolean needsComma = false;
        jsonReader.assertRead('{');

        final StringBuilder sb= new StringBuilder();

        long _id = 0L;
        String _name = null;
        Profile _profile = null;
        List<Account> _accounts = null;

        while (jsonReader.read() != '}') {
            if(needsComma)
                jsonReader.assertLast(',');

            final String property = jsonReader.readString();
            jsonReader.assertNext(':');

            switch(property.charAt(0)){
                case 'i': // id
                    sb.setLength(0);
                    _id = Long.parseLong(jsonReader.readRawNumber(sb).toString());
                    needsComma=true;
                    continue;
                case 'n': // name
                    _name = jsonReader.readString();
                    needsComma=true;
                    continue;
                case 'p': // profile
                    _profile = ProfileManualJsonSerialization.read(jsonReader);
                    jsonReader.invalidate();
                    needsComma=true;
                    continue;
                case 'a': // accounts
                    _accounts = readAccounts(jsonReader);
                    needsComma=true;
                    continue;

            }

//            if (property.equals("id")){
//                sb.setLength(0);
//                _id = Long.parseLong(jsonReader.readRawNumber(sb).toString());
//                needsComma=true;
//                continue;
//            }
//
//            if (property.equals("name")){
//                _name = jsonReader.readString();
//                needsComma=true;
//                continue;
//            }
//
//            if (property.equals("profile")){
//                _profile = ProfileManualJsonSerialization.read(jsonReader);
//                jsonReader.invalidate();
//                needsComma=true;
//                continue;
//            }
//
//            if (property.equals("accounts")){
//                _accounts = readAccounts(jsonReader);
//                needsComma=true;
//                continue;
//            }
        }

        if (_name==null)     _name    = "";
        if (_profile==null)  _profile = new Profile();
        if (_accounts==null) _accounts = new ArrayList<Account>();

        return new Customer(_id, _name, _profile, _accounts);
    }

    private static List<Account> readAccounts (final JsonReader jsonReader) throws IOException{

        final List<Account> _accounts = new ArrayList<Account>();

        jsonReader.assertNext('[');

        boolean accountsNeedsComma = false;
        while(jsonReader.read() != ']'){
            if(accountsNeedsComma)
                jsonReader.assertLast(',');
            _accounts.add(AccountManualJsonSerialization.read(jsonReader));
            jsonReader.invalidate();
            accountsNeedsComma = true;
        }

        jsonReader.assertRead(']');

        return _accounts;
    }
}
