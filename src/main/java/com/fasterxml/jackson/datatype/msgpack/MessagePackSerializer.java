//
// jackson-datatype-msgpack
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

package com.fasterxml.jackson.datatype.msgpack;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.MapValue;
import org.msgpack.value.Value;

/**
* MessagePackSerializer.
*/
public class MessagePackSerializer extends StdSerializer<Value> {
    public MessagePackSerializer() {
        super(Value.class);
    }

    @Override
    public void serialize(Value value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        writeValue(value, generator);
    }

    private void writeFieldName(Value value, JsonGenerator generator) throws IOException {
        generator.writeFieldName(value.asStringValue().toString());
    }

    private void writeValue(Value value, JsonGenerator generator) throws IOException {
        // TODO this conversion should be customized by users.
        switch (value.getValueType()) {
            case NIL:
                generator.writeNull();
                break;
            case BOOLEAN:
                generator.writeBoolean(value.asBooleanValue().getBoolean());
                break;
            case INTEGER:
                generator.writeNumber(value.asIntegerValue().toLong());
                break;
            case FLOAT:
                generator.writeNumber(value.asFloatValue().toDouble());
                break;
            case STRING:
                generator.writeString(value.asStringValue().toString());
                break;
            case BINARY:
                generator.writeBinary(value.asBinaryValue().asByteArray());
                break;
            case ARRAY:
                writeArrayValue(value.asArrayValue(), generator);
                break;
            case MAP:
                writeMapValue(value.asMapValue(), generator);
                break;
            default:
                throwUnknownTypeException(value, generator);
        }
    }

    private void writeArrayValue(ArrayValue value, JsonGenerator generator) throws IOException {
        generator.writeStartArray();
        for (Value v : value.list()) {
            writeValue(v, generator);
        }
        generator.writeEndArray();
    }

    private void writeMapValue(MapValue value, JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        for (Map.Entry<Value, Value> kv : value.entrySet()) {
            writeFieldName(kv.getKey(), generator);
            writeValue(kv.getValue(), generator);
        }
        generator.writeEndObject();
    }

    private void throwUnknownTypeException(Value value, JsonGenerator generator) throws IOException {
        String message = String.format(Locale.ENGLISH, "Unknown MessagePack type: %s", value.getValueType().toString());
        throw new JsonMappingException(generator, message);
    }
}
