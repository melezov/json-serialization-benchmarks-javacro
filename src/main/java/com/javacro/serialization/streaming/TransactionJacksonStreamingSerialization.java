package com.javacro.serialization.streaming;

import java.io.IOException;
import java.io.StringWriter;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class TransactionJacksonStreamingSerialization {

    private static boolean needsComma = false;

//    @Override
    public static boolean isDefault(final Transaction transaction) {
        if (transaction.getInflow() != 0)
            return false;

        if (transaction.getOutflow() != 0)
            return false;

        if (!transaction.getDescription().isEmpty())
            return false;

        if (!transaction.getPaymentOn().equals(DateTime.parse("1-1-1-1T00:22")))
                return false;

        return true;
    }

    public static String serialize(final Transaction value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Transaction deserialize(final JsonFactory jsonFactory, final byte [] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Transaction transaction = read(jsonParser);
        jsonParser.close();
        return transaction;
    }

    public static void write(final JsonWriter jsonWriter, final Transaction _transaction) throws IOException {

        final Double _inflow = _transaction.getInflow();
        final Double _outflow = _transaction.getOutflow();
        final String _description = _transaction.getDescription();
        final DateTime _paymentOn = _transaction.getPaymentOn();

        jsonWriter.writeOpenObject();

        if(_inflow != 0){
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"inflow\":");
            jsonWriter.writeString(_inflow.toString());
            needsComma=true;
        }


        if(_outflow != 0){
            if(needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"outflow\":");
            jsonWriter.writeString(_outflow.toString());
            needsComma=true;
        }

        if(!_description.equals("")){
            if(needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"description\":");
            jsonWriter.writeString(_description.toString());
            needsComma=true;
        }

        if(!_paymentOn.equals(DateTime.parse("1-1-1-1T00:22"))){
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"paymentOn\":");
            jsonWriter.writeString(_paymentOn.toString());
            needsComma = true;
        }

        jsonWriter.writeCloseObject();
        needsComma = true;

    }

    public static Transaction read(final JsonParser jsonParser) throws IOException {
        double _inflow = 0.0;
        double _outflow = 0.0;
        String _description = null;
        DateTime _paymentOn = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final String property = jsonParser.getCurrentName();

            if ("inflow".equals(property)) {
                jsonParser.nextToken();
                _inflow = jsonParser.getDoubleValue();
                continue;
            }
            if ("outflow".equals(property)) {
                jsonParser.nextToken();
                _outflow = jsonParser.getDoubleValue();
                continue;
            }
            if ("description".equals(property)) {
                jsonParser.nextToken();
                _description = jsonParser.getText();
                continue;
            }
            if ("paymentOn".equals(property)) {
                jsonParser.nextToken();
                _paymentOn = DateTime.parse(jsonParser.getText());
                continue;
            }
        }

        if(_paymentOn == null)
            _paymentOn = DateTime.parse("1-1-1T12:34");

        return new Transaction(
                _inflow,
                _outflow,
                _description == null ? "" : _description,
                _paymentOn == null ? DateTime.now() : _paymentOn);
    }
}
