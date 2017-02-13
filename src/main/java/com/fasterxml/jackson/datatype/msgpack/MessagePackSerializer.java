package com.fasterxml.jackson.datatype.msgpack;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

import org.msgpack.value.ArrayValue;
import org.msgpack.value.MapValue;
import org.msgpack.value.Value;

import static java.util.Locale.ENGLISH;

public class MessagePackSerializer extends StdSerializer<Value>
{
    public MessagePackSerializer()
    {
        super(Value.class);
    }

    @Override
    public void serialize(Value value, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException
    {
        writeValue(value, generator);
    }

    private void writeFieldName(Value value, JsonGenerator generator)
            throws IOException
    {
        generator.writeFieldName(value.asStringValue().toString()); // TODO
    }

    private void writeValue(Value value, JsonGenerator generator)
            throws IOException
    {
        switch (value.getValueType()) {
            case NIL:
                generator.writeNull();
                break;
            case BOOLEAN:
                generator.writeBoolean(value.asBooleanValue().getBoolean()); // TODO
                break;
            case INTEGER:
                generator.writeNumber(value.asIntegerValue().toLong()); // TODO
                break;
            case FLOAT:
                generator.writeNumber(value.asFloatValue().toDouble()); // TODO
                break;
            case STRING:
                generator.writeNumber(value.asStringValue().toString()); // TODO
                break;
            case BINARY:
                generator.writeBinary(value.asBinaryValue().asByteArray()); // TODO
                break;
            case ARRAY:
                writeArrayValue(value.asArrayValue(), generator);
                break;
            case MAP:
                writeMapValue(value.asMapValue(), generator);
                break;
            default:
                throwUnknownTypeException(value);
        }
    }

    private void writeArrayValue(ArrayValue value, JsonGenerator generator)
            throws IOException
    {
        generator.writeStartArray(value.size());
        for (Value v : value.list()) {
            writeValue(v, generator);
        }
        generator.writeEndArray();
    }

    private void writeMapValue(MapValue value, JsonGenerator generator)
            throws IOException
    {
        generator.writeStartObject();
        for (Map.Entry<Value, Value> kv : value.entrySet()) {
            writeFieldName(kv.getKey(), generator);
            writeValue(kv.getValue(), generator);
        }
        generator.writeEndObject();
    }

    private void throwUnknownTypeException(Value value)
            throws IOException
    {
        String message = String.format(ENGLISH, "Unknown MessagePack type: %s", value.getValueType().toString());
        throw new JsonMappingException(message);
    }
}
