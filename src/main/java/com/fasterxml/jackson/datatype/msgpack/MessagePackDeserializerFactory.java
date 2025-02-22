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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import org.msgpack.value.Value;

/**
* MessagePackDeserializerFactory.
*/
public class MessagePackDeserializerFactory extends Deserializers.Base {
    public MessagePackDeserializerFactory() {
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException {
        if (Value.class.isAssignableFrom(type.getRawClass())) {
            return new MessagePackDeserializer();
        } else {
            return super.findBeanDeserializer(type, config, beanDesc);
        }
    }
}
