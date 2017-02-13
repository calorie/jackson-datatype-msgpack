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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePackDeserializer
        extends StdDeserializer<Value>
{
    public MessagePackDeserializer()
            throws JsonMappingException
    {
        super(Value.class);
    }

    @Override
    public Value deserialize(JsonParser parser, DeserializationContext context)
            throws IOException
    {
        return populate(parser);
    }

    private Value populate(JsonParser parser)
            throws IOException
    {
        JsonToken token = parser.getCurrentToken();
        if (token == null) {
            return null;
        }
        return jsonTokenToValue(parser, token);
    }

    private Value jsonTokenToValue(JsonParser parser, JsonToken token)
            throws IOException
    {
        switch(token) {
            case VALUE_NULL:
                return ValueFactory.newNil();
            case VALUE_TRUE:
                return ValueFactory.newBoolean(true);
            case VALUE_FALSE:
                return ValueFactory.newBoolean(false);
            case VALUE_NUMBER_FLOAT:
                return ValueFactory.newFloat(parser.getDoubleValue());
            case VALUE_NUMBER_INT:
                try {
                    return ValueFactory.newInteger(parser.getLongValue());
                }
                catch (JsonParseException ex) {
                    return ValueFactory.newInteger(parser.getBigIntegerValue());
                }
            case VALUE_STRING:
                return ValueFactory.newString(parser.getText());
            case START_ARRAY: {
                List<Value> list = new ArrayList<>();
                while (true) {
                    token = parser.nextToken();
                    if (token == JsonToken.END_ARRAY) {
                        return ValueFactory.newArray(list);
                    }
                    else if (token == null) {
                        throw new JsonParseException("Unexpected end of JSON", parser.getTokenLocation());
                    }
                    list.add(jsonTokenToValue(parser, token));
                }
            }
            case START_OBJECT:
                Map<Value, Value> map = new HashMap<>();
                while (true) {
                    token = parser.nextToken();
                    if (token == JsonToken.END_OBJECT) {
                        return ValueFactory.newMap(map);
                    }
                    else if (token == null) {
                        throw new JsonParseException("Unexpected end of JSON", parser.getTokenLocation());
                    }
                    String key = parser.getCurrentName();
                    if (key == null) {
                        throw new JsonParseException("Unexpected token " + token, parser.getTokenLocation());
                    }
                    token = parser.nextToken();
                    if (token == null) {
                        throw new JsonParseException("Unexpected end of JSON", parser.getTokenLocation());
                    }
                    Value value = jsonTokenToValue(parser, token);
                    map.put(ValueFactory.newString(key), value);
                }
            case VALUE_EMBEDDED_OBJECT:
            case FIELD_NAME:
            case END_ARRAY:
            case END_OBJECT:
            case NOT_AVAILABLE:
            default:
                throw new JsonParseException("Unexpected token " + token, parser.getTokenLocation());
        }
    }
}
