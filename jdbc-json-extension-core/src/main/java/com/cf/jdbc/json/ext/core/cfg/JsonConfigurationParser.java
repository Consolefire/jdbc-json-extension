package com.cf.jdbc.json.ext.core.cfg;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;

public class JsonConfigurationParser<T extends Configuration<?>, C extends Collection<T>>
        implements ConfigurationParser<String, T, C> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<C> typeReference;

    public JsonConfigurationParser(TypeReference<C> typeReference) {
        this.typeReference = typeReference;
    }

    /*
     * public JsonConfigurationParser() { Type type = getClass().getGenericSuperclass(); ParameterizedType
     * parameterizedType = (ParameterizedType) type; Class<T> type2 = (Class<T>)
     * parameterizedType.getActualTypeArguments()[1]; Class<C> type3 = (Class<C>)
     * parameterizedType.getActualTypeArguments()[2]; typeReference = new TypeReference<C>() {
     * 
     * @Override public Type getType() { return type3; } }; }
     */

    @Override
    public C parse(String source) {
        try {
            return objectMapper.readValue(source, typeReference);
        } catch (IOException e) {
            throw new IllegalConfigurationException(e);
        }
    }

}
