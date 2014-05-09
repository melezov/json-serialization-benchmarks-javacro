package com.javacro.serialization.jacksonstreaming;

import java.io.IOException;
import java.io.StringWriter;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.javacro.dslplatform.model.Accounting.Transaction;

public abstract class TransactionJacksonStreamingSerialization {

    public static String serialize(final JsonFactory jsonFactory, final Transaction value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonGenerator jsonGenerator = jsonFactory.createGenerator(sw);
        write(jsonGenerator, value);
        jsonGenerator.close();
        return sw.toString();
    }

    public static Transaction deserialize(final JsonFactory jsonFactory, final byte [] ib) throws IOException {
        final JsonParser jsonParser = jsonFactory.createParser(ib);
        final Transaction transaction = read(jsonParser);
        jsonParser.close();
        return transaction;
    }
    
    //private static DateTime minDate = DateTime.parse("1-1-1T00:22");

    public static void write(final JsonGenerator jsonGenerator, final Transaction value) throws IOException {

        final double inflow = value.getInflow();
        final double outflow = value.getOutflow();
        final String description = value.getDescription();
        final DateTime paymentOn = value.getPaymentOn();

        jsonGenerator.writeStartObject();

        if (inflow != 0)
            jsonGenerator.writeNumberField("inflow", inflow);
        if (outflow != 0)
            jsonGenerator.writeNumberField("outflow", outflow);
        if (!description.equals(""))
            jsonGenerator.writeStringField("description", description);
        //if (!paymentOn.equals(minDate))
            jsonGenerator.writeStringField("paymentOn", paymentOn.toString());

        jsonGenerator.writeEndObject();
    }
    private static DateTime minDate = DateTime.parse("1-1-1T12:34");

    public static Transaction read(final JsonParser jsonParser) throws IOException {
        double _inflow = 0.0;
        double _outflow = 0.0;
        String _description = "";
        DateTime _paymentOn = minDate;

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

        return new Transaction(
                _inflow,
                _outflow,
                _description,
                _paymentOn);
    }
}
