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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

/**
* Test MessagePackModule.
*/
public class TestMessagePackModule {
    @Test
    public void testSerialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MessagePackModule());

        byte[] b = { 0x61 };

        Value v = ValueFactory.newMapBuilder()
                .put(ValueFactory.newString("k1"), ValueFactory.newNil())
                .put(ValueFactory.newString("k2"), ValueFactory.newBoolean(true))
                .put(ValueFactory.newString("k3"), ValueFactory.newInteger(1))
                .put(ValueFactory.newString("k4"), ValueFactory.newFloat(1F))
                .put(ValueFactory.newString("k5"), ValueFactory.newString("value"))
                .put(ValueFactory.newString("k6"), ValueFactory.newBinary(b))
                .put(ValueFactory.newString("k7"), ValueFactory.newArray(Arrays.asList(ValueFactory.newString("a"))))
                .put(ValueFactory.newString("k8"), ValueFactory.newMap(ValueFactory.newString("k"), ValueFactory.newString("v")))
                .build();
        ObjectNode node = mapper.valueToTree(v);

        assertThat(node.get("k1"), is(NullNode.getInstance()));
        assertThat(node.get("k2").asBoolean(), is(true));
        assertThat(node.get("k3").asInt(), is(1));
        assertThat(node.get("k4").asDouble(), is(1.0));
        assertThat(node.get("k5").asText(), is("value"));
        byte[] b2 = {};
        try {
            b2 = node.get("k6").binaryValue();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertThat(b2, is(b));
        assertThat(node.get("k7").get(0).asText(), is("a"));
        assertThat(node.get("k8").get("k").asText(), is("v"));
    }

    @Test
    public void testDeserialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MessagePackModule());

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.set("k1", factory.nullNode());
        node.set("k2", factory.booleanNode(true));
        node.set("k3", factory.numberNode(1F));
        node.set("k4", factory.numberNode(1));
        node.set("k5", factory.textNode("value"));
        node.set("k6", factory.arrayNode().add("a"));
        node.set("k7", factory.objectNode().put("k", "v"));

        Map<Value, Value> v = new HashMap<Value, Value>();
        try {
            v = mapper.treeToValue(node, Value.class).asMapValue().map();
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        assertThat(v.get(ValueFactory.newString("k1")).asNilValue(), is(ValueFactory.newNil()));
        assertThat(v.get(ValueFactory.newString("k2")).asBooleanValue().getBoolean(), is(true));
        assertThat(v.get(ValueFactory.newString("k3")).asFloatValue().toFloat(), is(1F));
        assertThat(v.get(ValueFactory.newString("k4")).asIntegerValue().toInt(), is(1));
        assertThat(v.get(ValueFactory.newString("k5")).asStringValue().toString(), is("value"));
        assertThat(v.get(ValueFactory.newString("k6")).asArrayValue().get(0), is(ValueFactory.newString("a")));
        assertThat(v.get(ValueFactory.newString("k7")).asMapValue().map().get(ValueFactory.newString("k")), is(ValueFactory.newString("v")));
    }
}
