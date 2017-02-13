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
