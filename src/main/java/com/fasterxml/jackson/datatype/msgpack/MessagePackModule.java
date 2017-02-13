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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

public class MessagePackModule
        extends Module
{
    public static final String NAME = "MessagePackModule";
    public static final VersionUtil VERSION = new VersionUtil()
    {
    };

    @Override
    public String getModuleName()
    {
        return NAME;
    }

    @Override
    public Version version()
    {
        return VERSION.version();
    }

    @Override
    public void setupModule(SetupContext context)
    {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new MessagePackSerializer());

        context.addSerializers(serializers);
        context.addDeserializers(new MessagePackDeserializerFactory());
    }
}
