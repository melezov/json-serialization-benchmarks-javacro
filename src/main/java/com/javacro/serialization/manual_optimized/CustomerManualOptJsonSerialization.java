package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.dslplatform.patterns.ServiceLocator;
import com.javacro.dslplatform.model.Accounting.Account;
import com.javacro.dslplatform.model.Accounting.Customer;
import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.io.jvm.json.JsonReader;

public abstract class CustomerManualOptJsonSerialization {
    public static void serialize(final Writer writer, final Customer value) throws IOException {
        writer.write("{\"URI\":");
        StringConverter.serialize(value.getURI(), writer);

        final String _name = value.getName();
        if(_name.length() > 0) {
            writer.write(",\"name\":");
            StringConverter.serialize(_name, writer);
        }
        writer.write(",\"profile\":");
        ProfileManualOptJsonSerialization.serialize(writer, value.getProfile());
        final List<Account> _accounts = value.getAccounts();
        if (_accounts != null && !_accounts.isEmpty()) {
            writer.write(",\"accounts\":[");
            int _cnt = 0;
            final int _total = _accounts.size() - 1;
            for (;_cnt < _total; _cnt++) {
                AccountManualOptJsonSerialization.serialize(writer, _accounts.get(_cnt));
                writer.write(',');
            }
            AccountManualOptJsonSerialization.serialize(writer, _accounts.get(_cnt));
            writer.write(']');
        }
        else if (_accounts != null) writer.write(",\"accounts\":[]");
        writer.write('}');
    }

    public static Customer deserialize(final byte[] inputBytes) throws IOException {
        final Reader reader = new StringReader(new String(inputBytes, "UTF-8"));
        int nextToken = ManualJson.getNextToken(reader);
        if(nextToken != '{') throw new IOException("Expecting '{'");
        nextToken = ManualJson.getNextToken(reader);
        return deserialize(reader, new char[38], new int[1], nextToken, null);
    }

    public static Customer deserializeWith(final byte[] inputBytes) throws IOException {
        final JsonReader reader = new JsonReader(inputBytes);
        if(reader.getNextToken() != '{') throw new IOException("Expecting '{'");
        return deserialize(reader, null);
    }

    public static Customer deserialize(final String input) throws IOException {
        final Reader reader = new StringReader(input);
        int nextToken = ManualJson.getNextToken(reader);
        if(nextToken != '{') throw new IOException("Expecting '{'");
        nextToken = ManualJson.getNextToken(reader);
        return deserialize(reader, new char[38], new int[1], nextToken, null);
    }

    public static Customer deserialize(final Reader reader, final char[] buffer, final int[] tokens, int nextToken, final ServiceLocator locator) throws IOException {
        String _uri = "";
        long _id = 0L;
        String _name = "";
        Profile _profile = new Profile();
        ArrayList<Account> _accounts = null;
        if (nextToken == '}') return new Customer(_uri, _id, _name, _profile, _accounts, locator);
        int nameHash = ManualJson.fillName(reader, buffer, nextToken);
        nextToken = ManualJson.getNextToken(reader);
        if (nextToken == 'n') {
            if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l')
                throw new IOException("null value expected");
        }
        else {
            switch(nameHash) {
                case 1:
                    _uri = StringConverter.deserialize(reader, buffer, nextToken);
                    nextToken = ManualJson.getNextToken(reader);
                    break;
                case 25458:
                    _id = NumberConverter.deserializeInt(reader, buffer, tokens, nextToken);
                    nextToken = ManualJson.moveToNextToken(reader, tokens[0]);
                    break;
                case 24614690:
                    _name = StringConverter.deserialize(reader, buffer, nextToken);
                    nextToken = ManualJson.getNextToken(reader);
                    break;
                case 1120506290:
                    if (nextToken != '{') throw new IOException("Expecting '{'");
                    nextToken = ManualJson.getNextToken(reader);
                    _profile = ProfileManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken);
                    nextToken = ManualJson.getNextToken(reader);
                    break;
                case -758926083:
                    if (nextToken != '[') throw new IOException("Expecting '['");
                    nextToken = ManualJson.getNextToken(reader);
                    if (nextToken != '{') throw new IOException("Expecting '{'");
                    nextToken = ManualJson.getNextToken(reader);
                    _accounts = new ArrayList<Account>();
                    _accounts.add(AccountManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
                    while((nextToken = ManualJson.getNextToken(reader)) == ',') {
                        nextToken = ManualJson.getNextToken(reader);
                        if (nextToken != '{') throw new IOException("Expecting '{'");
                        nextToken = ManualJson.getNextToken(reader);
                        _accounts.add(AccountManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
                    }
                    nextToken = ManualJson.getNextToken(reader);
                    break;
                default:
                    nextToken = ManualJson.skip(reader, buffer, tokens, nextToken);
                    break;
            }
        }
        while (nextToken == ',') {
            nextToken = ManualJson.getNextToken(reader);
            nameHash = ManualJson.fillName(reader, buffer, nextToken);
            nextToken = ManualJson.getNextToken(reader);
            if (nextToken == 'n') {
                if (reader.read() == 'u' && reader.read() == 'l' && reader.read() == 'l') {
                    nextToken = ManualJson.getNextToken(reader);
                    continue;
                }
                throw new IOException("null value expected");
            }
            else {
                switch(nameHash) {
                    case 1:
                        _uri = StringConverter.deserialize(reader, buffer, nextToken);
                        nextToken = ManualJson.getNextToken(reader);
                        break;
                    case 25458:
                        _id = NumberConverter.deserializeInt(reader, buffer, tokens, nextToken);
                        nextToken = ManualJson.moveToNextToken(reader, tokens[0]);
                        break;
                    case 24614690:
                        _name = StringConverter.deserialize(reader, buffer, nextToken);
                        nextToken = ManualJson.getNextToken(reader);
                        break;
                    case 1120506290:
                        _profile = ProfileManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken);
                        nextToken = ManualJson.getNextToken(reader);
                        break;
                    case -758926083:
                        if (nextToken != '[') throw new IOException("Expecting '['");
                        nextToken = ManualJson.getNextToken(reader);
                        if (nextToken != '{') throw new IOException("Expecting '{'");
                        nextToken = ManualJson.getNextToken(reader);
                        _accounts = new ArrayList<Account>();
                        _accounts.add(AccountManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
                        while((nextToken = ManualJson.getNextToken(reader)) == ',') {
                            nextToken = ManualJson.getNextToken(reader);
                            if (nextToken != '{') throw new IOException("Expecting '{'");
                            nextToken = ManualJson.getNextToken(reader);
                            _accounts.add(AccountManualOptJsonSerialization.deserialize(reader, buffer, tokens, nextToken));
                        }
                        nextToken = ManualJson.getNextToken(reader);
                        break;
                    default:
                        nextToken = ManualJson.skip(reader, buffer, tokens, nextToken);
                        break;
                }
            }
        }
        if (nextToken != '}') {
            if (nextToken == -1) throw new IOException("Unexpected end of json in object Transaction");
            else throw new IOException("Expecting '}' at position " + ManualJson.positionInStream(reader) + ". Found " + (char)nextToken);
        }
        return new Customer(_uri, _id, _name, _profile, _accounts, locator);
    }

    public static Customer deserialize(final JsonReader reader, final ServiceLocator locator) throws IOException {
        String _uri = "";
        long _id = 0L;
        String _name = "";
        Profile _profile = new Profile();
        ArrayList<Account> _accounts = null;
        if (reader.read() == '}') return new Customer(_uri, _id, _name, _profile, _accounts, locator);
        int nameHash = reader.fillName();
        int nextToken = reader.moveToNextToken();
        if (nextToken == 'n') {
            if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l')
                throw new IOException("null value expected");
        }
        else {
            switch(nameHash) {
                case 1:
                    _uri = reader.readString();
                    nextToken = reader.getNextToken();
                    break;
                case 25458:
                    _id = Long.parseLong(reader.readRawNumber());
                    nextToken = reader.getNextToken();
                    break;
                case 24614690:
                    _name = reader.readString();
                    nextToken = reader.getNextToken();
                    break;
                case 1120506290:
                    if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)reader.read());
                    nextToken = reader.getNextToken();
                    _profile = ProfileManualOptJsonSerialization.deserialize(reader);
                    nextToken = reader.getNextToken();
                    break;
                case -758926083:
                    if (reader.read() != '[') throw new IOException("Expecting '['. Found: " + (char)reader.read());
                    nextToken = reader.getNextToken();
                    if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)reader.read());
                    nextToken = reader.getNextToken();
                    _accounts = new ArrayList<Account>();
                    _accounts.add(AccountManualOptJsonSerialization.deserialize(reader));
                    while((nextToken = reader.getNextToken()) == ',') {
                        nextToken = reader.getNextToken();
                        if (nextToken != '{') throw new IOException("Expecting '{'");
                        nextToken = reader.getNextToken();
                        _accounts.add(AccountManualOptJsonSerialization.deserialize(reader));
                    }
                    nextToken = reader.getNextValidToken();
                    break;
                default:
                    nextToken = reader.skip();
                    break;
            }
        }
        while (nextToken == ',') {
            nextToken = reader.moveToNextToken();
            nameHash = reader.fillName();
            nextToken = reader.moveToNextToken();
            if (nextToken == 'n') {
                if (reader.read() == 'u' && reader.read() == 'l' && reader.read() == 'l') {
                    nextToken = reader.getNextToken();
                    continue;
                }
                throw new IOException("null value expected");
            }
            else {
                switch(nameHash) {
                    case 1:
                        _uri = reader.readString();
                        nextToken = reader.getNextToken();
                        break;
                    case 25458:
                        _id = Long.parseLong(reader.readRawNumber());
                        nextToken = reader.getNextToken();
                        break;
                    case 24614690:
                        _name = reader.readString();
                        nextToken = reader.getNextToken();
                        break;
                    case 1120506290:
                        if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)nextToken);
                        nextToken = reader.getNextToken();
                        _profile = ProfileManualOptJsonSerialization.deserialize(reader);
                        nextToken = reader.getNextToken();
                        break;
                    case -758926083:
                        if (reader.read() != '[') throw new IOException("Expecting '['. Found: " + (char)reader.read());
                        nextToken = reader.getNextToken();
                        if (reader.read() != '{') throw new IOException("Expecting '{'. Found: " + (char)reader.read());
                        nextToken = reader.getNextToken();
                        _accounts = new ArrayList<Account>();
                        _accounts.add(AccountManualOptJsonSerialization.deserialize(reader));
                        while((nextToken = reader.getNextValidToken()) == ',') {
                            reader.invalidate();
                            if (reader.read() != '{') throw new IOException("Expecting '{'");
                            nextToken = reader.getNextValidToken();
                            _accounts.add(AccountManualOptJsonSerialization.deserialize(reader));
                        }
                        nextToken = reader.getNextValidToken();
                        break;
                    default:
                        nextToken = reader.skip();
                        break;
                }
            }
        }
        if (nextToken != '}') {
            if (nextToken == -1) throw new IOException("Unexpected end of json in object Transaction");
            else throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
        }
        return new Customer(_uri, _id, _name, _profile, _accounts, locator);
    }

}
