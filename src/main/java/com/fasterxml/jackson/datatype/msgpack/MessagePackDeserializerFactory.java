package com.fasterxml.jackson.datatype.msgpack;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.msgpack.value.Value;

public class MessagePackDeserializerFactory
        extends Deserializers.Base
{
    public MessagePackDeserializerFactory()
    {
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
          throws JsonMappingException
    {
        if (Value.class.isAssignableFrom(type.getRawClass())) {
            return new MessagePackDeserializer();
        }
        else {
            return super.findBeanDeserializer(type, config, beanDesc);
        }
    }
}
