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

public abstract class CustomerManualJsonStreaming {
//    @Override
    public static boolean isDefault(final Customer customer) {

        if (customer.getId() != 0) return false;

        if (!customer.getName().isEmpty()) return false;

        if (ProfileManualJsonStreaming.isDefault(customer.getProfile())) return false;

        for (final Account account : customer.getAccounts()) {
            if (!AccountManualJsonStreaming.isDefault(account)) return false;
        }

        return true;
    }

    public static String serialize(final Customer value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Customer deserialize(final String inputString) throws IOException {
        return deserialize(inputString.getBytes("UTF-8"));
    }

    public static Customer deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes);
        return read(jsonReader);
    }

    public static void write(final JsonWriter jsonWriter, final Customer value) throws IOException {

        final long _id = value.getId();
        final String _name = value.getName();
        final List<Account> accounts = value.getAccounts();

        boolean needsComma = false;

        final boolean[] accountIsDefaultOnPosition = new boolean[accounts.size()];
        boolean allAccountsAreDefault = true;
        for (int i = 0; i < accounts.size(); i++) {
            if (!AccountManualJsonStreaming.isDefault(accounts.get(i))) {
                allAccountsAreDefault = false;
                accountIsDefaultOnPosition[i] = false;
            } else accountIsDefaultOnPosition[i] = true;
        }

        jsonWriter.writeOpenObject();

        if (_id != 0) {
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"id\":");
            jsonWriter.writeString(Long.toString(_id));
            needsComma = true;
        }

        if (!_name.equals("")) {
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"name\":");
            jsonWriter.writeString(_name);
            needsComma = true;
        }

        {
            final Profile _profile = value.getProfile();
            if (!ProfileManualJsonStreaming.isDefault(_profile)) {
                if (needsComma) jsonWriter.writeComma();
                jsonWriter.writeRaw("\"profile\":");
                ProfileManualJsonStreaming.write(jsonWriter, _profile);
                needsComma = true;
            } else // The default objects are serialized as empty Json objects
            writeEmptyObject(jsonWriter);
        }

        if(!allAccountsAreDefault)
        {
            if (needsComma) jsonWriter.writeComma();

            jsonWriter.writeRaw("\"accounts\":");
            jsonWriter.writeOpenArray();

            needsComma = false;
            for (int i = 0; i < accounts.size(); i++) {
                if (needsComma) jsonWriter.writeComma();

                needsComma = true;

                if (accountIsDefaultOnPosition[i]) {
                    writeEmptyObject(jsonWriter);
                } else {
                    AccountManualJsonStreaming.write(jsonWriter, accounts.get(i));
                }
            }

            jsonWriter.writeCloseArray();
        }else if(accounts.size()>0){
            /* If all accounts are default, just write an array of empty objects */
            jsonWriter.writeRaw("\"accounts\":");
            jsonWriter.writeOpenArray();

            writeEmptyObject(jsonWriter);

            for(int i=1;i<accounts.size();i++){
                jsonWriter.writeComma();
                writeEmptyObject(jsonWriter);
            }

            jsonWriter.writeCloseArray();
        }

        jsonWriter.writeCloseObject();
    }

    public static final Customer read(final JsonReader jsonReader) throws IOException {
        boolean needsComma = false;
        jsonReader.assertRead('{');

        final StringBuilder sb = new StringBuilder();

        long _id = 0L;
        String _name = null;
        Profile _profile = null;
        List<Account> _accounts = null;

        while (jsonReader.read() != '}') {
            if (needsComma) jsonReader.assertLast(',');

            final String property = jsonReader.readString();
            jsonReader.assertNext(':');

            if (property.equals("id")) {
                sb.setLength(0);
                _id = Long.parseLong(jsonReader.readRawNumber(sb).toString());
                needsComma = true;
                continue;
            }

            if (property.equals("name")) {
                _name = jsonReader.readString();
                needsComma = true;
                continue;
            }

            if (property.equals("profile")) {
                _profile = ProfileManualJsonStreaming.read(jsonReader);
                jsonReader.invalidate();
                needsComma = true;
                continue;
            }

            if (property.equals("accounts")) {
                _accounts = readAccounts(jsonReader);
                needsComma = true;
                continue;
            }
        }

        if (_name == null) _name = "";
        if (_profile == null) _profile = new Profile();
        if (_accounts == null) _accounts = new ArrayList<Account>();

        return new Customer(_id, _name, _profile, _accounts);
    }

    private static List<Account> readAccounts(final JsonReader jsonReader) throws IOException {

        final List<Account> _accounts = new ArrayList<Account>();

        jsonReader.assertNext('[');

        boolean accountsNeedsComma = false;
        while (jsonReader.read() != ']') {
            if (accountsNeedsComma) jsonReader.assertLast(',');
            _accounts.add(AccountManualJsonStreaming.read(jsonReader));
            jsonReader.invalidate();
            accountsNeedsComma = true;
        }

        jsonReader.assertRead(']');

        return _accounts;
    }

    private static void writeEmptyObject(final JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeOpenObject();
        jsonWriter.writeCloseObject();
    }
}
