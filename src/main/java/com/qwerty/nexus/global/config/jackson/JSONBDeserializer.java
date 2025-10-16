package com.qwerty.nexus.global.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jooq.JSONB;

import java.io.IOException;

public class JSONBDeserializer extends JsonDeserializer<JSONB> {
    @Override
    public JSONB deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String json = p.readValueAsTree().toString();
        return JSONB.valueOf(json);
    }
}
