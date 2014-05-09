package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;

import org.joda.time.DateTime;

import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class TransactionManualJsonStreaming {
//    @Override
    public static boolean isDefault(final Transaction transaction) {
        if (transaction.getInflow() != 0)
            return false;

        if (transaction.getOutflow() != 0)
            return false;

        if (!transaction.getDescription().isEmpty())
            return false;

        if (!transaction.getPaymentOn().equals(DateTime.parse("1-1-1T00:22")))
                return false;

        return true;
    }

    public static String serialize(final Transaction value) throws IOException {
        final StringWriter sw = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(sw);
        write(jsonWriter, value);
        return sw.toString();
    }

    public static Transaction deserialize(final String inputString) throws IOException {
        return deserialize(inputString.getBytes("UTF-8"));
    }

    public static Transaction deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes);
        return read(jsonReader);
    }

    public static void write(final JsonWriter jsonWriter, final Transaction value ) throws IOException {

        boolean needsComma = false;

        jsonWriter.writeOpenObject();
        final Double _inflow = value.getInflow();
        final Double _outflow = value.getOutflow();
        final String _description = value.getDescription();
        final DateTime _paymentOn = value.getPaymentOn();

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

        if(!_paymentOn.equals(DateTime.parse("1-1-1T00:22"))){
            if (needsComma) jsonWriter.writeComma();
            jsonWriter.writeRaw("\"paymentOn\":");
            jsonWriter.writeString(_paymentOn.toString());
            needsComma = true;
        }

        jsonWriter.writeCloseObject();
    }

    public static Transaction read(final JsonReader jsonReader) throws IOException {
        boolean needsComma=false;
        jsonReader.assertRead('{');

        double _inflow = 0.0;
        double _outflow = 0.0;
        String _description = null;
        DateTime _paymentOn = null;

        final StringBuilder sb = new StringBuilder();

        while (jsonReader.read() != '}') {
            if(needsComma) jsonReader.assertLast(',');

            final String property = jsonReader.readString();
            jsonReader.assertNext(':');

            if (property.equals("inflow")){
                _inflow = Double.parseDouble(jsonReader.readRawNumber(new StringBuilder()).toString());
                needsComma=true;
                continue;
            }
            else if (property.equals("outflow")) {
                _outflow = Double.parseDouble(jsonReader.readRawNumber(new StringBuilder()).toString());
                needsComma=true;
                continue;
            }
            else if (property.equals("description")){
                _description= jsonReader.readString();
                needsComma=true;
                continue;
            }
            else if (property.equals("paymentOn")) {
                _paymentOn = DateTime.parse(jsonReader.readString());
                needsComma=true;
                continue;
            }
        }

        return new Transaction(
                _inflow
                , _outflow
                , _description == null? "" : _description
                , _paymentOn == null? DateTime.now() : _paymentOn);
    }
}
