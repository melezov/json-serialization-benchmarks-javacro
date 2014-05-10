package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.javacro.dslplatform.model.Accounting.Profile;
import com.javacro.serialization.io.jvm.json.JsonReader;

public abstract class ProfileManualOptJsonSerialization {
    public static void serialize(final Writer writer, final Profile value) throws IOException {
        writer.write('{');
        final String _email = value.getEmail();
        final String _phoneNumber = value.getPhoneNumber();
        if(_email != null) {
            writer.write("\"email\":");
            StringConverter.serialize(_email, writer);
	        if(_phoneNumber != null) {
	            writer.write(",\"phoneNumber\":");
	            StringConverter.serialize(_phoneNumber, writer);
	        }
        }
        else {
	        if(_phoneNumber != null) {
	            writer.write("\"phoneNumber\":");
	            StringConverter.serialize(_phoneNumber, writer);
	        }        	
        }
        writer.write('}');
    }

    public static Profile deserialize(final Reader reader, final char[] buffer, final int[] tokens, int nextToken) throws IOException {
        String _email = null;
        String _phoneNumber = null;
        if (nextToken == '}') return new Profile(_email, _phoneNumber);
        int nameHash = ManualJson.fillName(reader, buffer, nextToken);
        nextToken = ManualJson.getNextToken(reader);
        if (nextToken == 'n') {
            if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l')
                throw new IOException("null value expected");
        }
        else {
            switch(nameHash) {
                case 755089893:
                    _email= StringConverter.deserialize(reader, buffer, nextToken);
                    nextToken = ManualJson.getNextToken(reader);
                    break;
                case 1775932896:
                    _phoneNumber = StringConverter.deserialize(reader, buffer, nextToken);
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
                    case 755089893:
                        _email= StringConverter.deserialize(reader, buffer, nextToken);
                        nextToken = ManualJson.getNextToken(reader);
                        break;
                    case 1775932896:
                        _phoneNumber = StringConverter.deserialize(reader, buffer, nextToken);
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
        return new Profile(_email, _phoneNumber);
    }

    public static Profile deserialize(final JsonReader reader) throws IOException {
        String _email = null;
        String _phoneNumber = null;
        if (reader.read() == '}') return new Profile(_email, _phoneNumber);
        int nameHash = reader.fillName();
        int nextToken = reader.moveToNextToken();
        if (nextToken == 'n') {
            if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l')
                throw new IOException("null value expected");
        }
        else {
            switch(nameHash) {
                case 755089893:
                    _email = reader.readString();
                    nextToken = reader.getNextToken();
                    break;
                case 1775932896:
                    _phoneNumber = reader.readString();
                    nextToken = reader.getNextToken();
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
                    case 755089893:
                        _email = reader.readString();
                        nextToken = reader.getNextToken();
                        break;
                    case 1775932896:
                        _phoneNumber = reader.readString();
                        nextToken = reader.getNextToken();
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
        return new Profile(_email, _phoneNumber);
    }
}
