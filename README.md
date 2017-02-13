## Overview

Jackson module that adds support for serializing and deserializing MessagePack's Values to and from JSON.

## Usage

### Registering module

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new MessagePackModule());
```

Sample code:
```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.msgpack.MessagePackModule;
import org.msgpack.value.Value;

import static org.msgpack.value.ValueFactory.newMapBuilder;
import static org.msgpack.value.ValueFactory.newString;

public class Sample
{
    public static void main(String[] args)
            throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MessagePackModule());

        Value v = newMapBuilder()
                .put(newString("k1"), newString("v1"))
                .put(newString("k2"), newString("v2"))
                .build();
        System.out.println("1: " + mapper.writeValueAsString(v));

        ObjectNode on = JsonNodeFactory.instance.objectNode();
        on.set("k3", JsonNodeFactory.instance.textNode("v3"));
        on.set("k4", JsonNodeFactory.instance.textNode("v4"));

        System.out.println("2: " + mapper.treeToValue(on, Value.class).toJson());
    }
}
```