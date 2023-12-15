package com.brixo.productcatalogue.dtos;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SettingsValueDtoDeSerializer extends StdDeserializer<SettingsValueDto> {
    public SettingsValueDtoDeSerializer() {
        super(SettingsValueDto.class);
    }

    @Override
    public SettingsValueDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();
        // Implement custom deserialization logic here
        // For example, you can read values from the JSON node and construct a SettingsValueDto object
        return new SettingsValueDto(/* values from the JSON node */);
    }
}
