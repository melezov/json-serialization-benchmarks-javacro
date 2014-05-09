package com.javacro.serialization.manual_optimized;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.joda.time.DateTime;

import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;

public abstract class TransactionManualJsonSerialization {
    public static void serialize(final Writer writer, final Transaction value) throws IOException {
        writer.write('{');
		writer.write("\"paymentOn\":\"");
		writer.write(value.getPaymentOn().toString());
		writer.write("\"");
    	final String _description = value.getDescription(); 
    	if(_description.length() > 0) {
    		writer.write(",\"description\":");
    		StringConverter.serialize(_description, writer);
    	}
    	final double _inFlow = value.getInflow(); 
    	if(_inFlow != 0.0)  {
    		writer.write(",\"inFlow\":");
    		NumberConverter.serializeDouble(_inFlow, writer);
    	}
    	final double _outFlow = value.getOutflow(); 
    	if(_outFlow != 0.0)  {
    		writer.write(",\"outFlow\":");
    		NumberConverter.serializeDouble(_outFlow, writer);
    	}
    	writer.write('}');
    }

    public static Transaction deserialize(final Reader reader, final char[] buffer, final int[] tokens, int nextToken) throws IOException {
        double _inFlow = 0.0;
        double _outFlow = 0.0;
        String _description = "";
        DateTime _paymentOn = DateTimeConverter.minDate;
    	if (nextToken == '}') return new Transaction(_inFlow, _outFlow, _description, _paymentOn);
    	int nameHash = ManualJson.fillName(reader, buffer, nextToken);
    	nextToken = ManualJson.getNextToken(reader);
    	if (nextToken == 'n') {
    		if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l') 
    			throw new IOException("null value expected");
    	}
    	else {
	    	switch(nameHash) {
				case 2048542378:
					_inFlow = NumberConverter.deserializeDouble(reader, buffer, tokens, nextToken);
					nextToken = tokens[0];
					break;
				case 323510661:
					_outFlow = NumberConverter.deserializeDouble(reader, buffer, tokens, nextToken);
					nextToken = tokens[0];
					break;
				case 1244356485:
					_description = StringConverter.deserialize(reader, buffer, nextToken);
					nextToken = ManualJson.getNextToken(reader);
					break;
				case -311362482:
					_paymentOn = DateTimeConverter.deserialize(reader, buffer, nextToken);
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
    				case 2048542378:
    					_inFlow = NumberConverter.deserializeDouble(reader, buffer, tokens, nextToken);
    					nextToken = tokens[0];
    					break;
    				case 323510661:
    					_outFlow = NumberConverter.deserializeDouble(reader, buffer, tokens, nextToken);
    					nextToken = tokens[0];
    					break;
    				case 1244356485:
    					_description = StringConverter.deserialize(reader, buffer, nextToken);
    					nextToken = ManualJson.getNextToken(reader);
    					break;
    				case -311362482:
    					_paymentOn = DateTimeConverter.deserialize(reader, buffer, nextToken);
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
        return new Transaction(_inFlow, _outFlow, _description, _paymentOn);
    }
    
    public static Transaction deserialize(final JsonReader reader) throws IOException {
        double _inFlow = 0.0;
        double _outFlow = 0.0;
        String _description = "";
        DateTime _paymentOn = DateTimeConverter.minDate;
    	if (reader.read() == '}') return new Transaction(_inFlow, _outFlow, _description, _paymentOn);
    	int nameHash = reader.fillName();
    	int nextToken = reader.moveToNextToken();
    	if (nextToken == 'n') {
    		if (reader.read() != 'u' || reader.read() != 'l' || reader.read() != 'l') 
    			throw new IOException("null value expected");
    	}
    	else {
	    	switch(nameHash) {
				case 2048542378:
					_inFlow = Double.parseDouble(reader.readRawNumber());
					nextToken = reader.getNextToken();
					break;
				case 323510661:
					_outFlow = Double.parseDouble(reader.readRawNumber());
					nextToken = reader.getNextToken();
					break;
				case 1244356485:
					_description = reader.readString();
					nextToken = reader.getNextToken();
					break;
				case -311362482:
					_paymentOn = DateTime.parse(reader.readString());
					nextToken = reader.getNextToken();
					break;
				default:
					nextToken = reader.skip();
					break;
	    	}	    			
    	}
    	while (nextToken == ',') {
    		reader.read();
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
    				case 2048542378:
    					_inFlow = Double.parseDouble(reader.readRawNumber());
    					nextToken = reader.getNextToken();
    					break;
    				case 323510661:
    					_outFlow = Double.parseDouble(reader.readRawNumber());
    					nextToken = reader.getNextToken();
    					break;
    				case 1244356485:
    					_description = reader.readString();
    					nextToken = reader.getNextToken();
    					break;
    				case -311362482:
    					_paymentOn = DateTime.parse(reader.readString());
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
        return new Transaction(_inFlow, _outFlow, _description, _paymentOn);
    }    
}
