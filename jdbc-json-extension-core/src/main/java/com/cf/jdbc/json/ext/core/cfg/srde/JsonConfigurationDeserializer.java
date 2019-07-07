package com.cf.jdbc.json.ext.core.cfg.srde;

import java.io.IOException;
import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.srde.ConfigurationDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConfigurationDeserializer<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationDeserializer<JsonNode, K, C> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<C> typeReference;

    public JsonConfigurationDeserializer(TypeReference<C> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public C deserialize(JsonNode source) {
        try {
            return objectMapper.readValue(source.binaryValue(), typeReference);
        } catch (IOException ioException) {
            throw new IllegalConfigurationException(ioException);
        }
    }

}
