package com.javacro.serialization.manual;

import java.io.IOException;
import java.io.StringWriter;

import org.joda.time.DateTime;

import com.javacro.dslplatform.model.Accounting.Transaction;
import com.javacro.serialization.io.jvm.json.JsonReader;
import com.javacro.serialization.io.jvm.json.JsonWriter;

public abstract class TransactionManualJsonSerialization {
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

    public static Transaction deserialize(final byte[] inputBytes) throws IOException {
        final JsonReader jsonReader = new JsonReader(inputBytes);
        return read(jsonReader);
    }

    public static void write(final JsonWriter jsonWriter, final Transaction value) throws IOException {
        jsonWriter.writeOpenObject();

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

            switch(property.charAt(0)){
                case 'i': //inflow
                    sb.setLength(0);
                    _inflow = Double.parseDouble(jsonReader.readRawNumber(sb).toString());
                    needsComma=true;
                    continue;
                case 'o': //outflow
                    sb.setLength(0);
                    _outflow = Double.parseDouble(jsonReader.readRawNumber(sb).toString());
                    needsComma=true;
                    continue;
                case 'd': //description
                    _description= jsonReader.readString();
                    needsComma=true;
                    continue;
                case 'p': //paymentOn
                    _paymentOn = DateTime.parse(jsonReader.readString());
                    needsComma=true;
                    continue;
            }

//            if (property.equals("inflow")){
//                _inflow = Double.parseDouble(jsonReader.readRawNumber(new StringBuilder()).toString());
//                needsComma=true;
//                continue;
//            }
//            else if (property.equals("outflow")) {
//                _outflow = Double.parseDouble(jsonReader.readRawNumber(new StringBuilder()).toString());
//                needsComma=true;
//                continue;
//            }
//            else if (property.equals("description")){
//                _description= jsonReader.readString();
//                needsComma=true;
//                continue;
//            }
//            else if (property.equals("paymentOn")) {
//                _paymentOn = DateTime.parse(jsonReader.readString());
//                needsComma=true;
//                continue;
//            }
        }

        return new Transaction(
                _inflow
                , _outflow
                , _description == null? "" : _description
                , _paymentOn == null? DateTime.now() : _paymentOn);
    }
}
