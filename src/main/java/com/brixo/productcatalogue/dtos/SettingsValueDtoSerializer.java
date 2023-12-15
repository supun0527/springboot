package com.brixo.productcatalogue.dtos;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SettingsValueDtoSerializer extends JsonSerializer<SettingsValueDto> {
    @Override
    public void serialize(SettingsValueDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Implement custom serialization logic here
        // For example, you can serialize the object as a JSON string
        gen.writeString(value.toString());
    }
}
