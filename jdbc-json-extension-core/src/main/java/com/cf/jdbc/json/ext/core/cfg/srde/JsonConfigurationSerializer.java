package com.cf.jdbc.json.ext.core.cfg.srde;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.srde.ConfigurationSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConfigurationSerializer<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationSerializer<JsonNode, K, C> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<C> typeReference;

    public JsonConfigurationSerializer(TypeReference<C> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public JsonNode serialize(C configuration) {
        return objectMapper.convertValue(configuration, typeReference);
    }

}
