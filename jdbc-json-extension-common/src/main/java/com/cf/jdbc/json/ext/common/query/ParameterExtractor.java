package com.cf.jdbc.json.ext.common.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;

@FunctionalInterface
public interface ParameterExtractor {

    ParameterExtractor CONTEXT_PARAMETER_EXTRACTOR = (parameters, result, context) -> {
        if (null == context || null == context.getSourceParameters()) {
            return new HashMap<>();
        }
        return context.getSourceParameters();
    };

    ParameterExtractor PARENT_RESULT_PARAMETER_EXTRACTOR = (parameters, result, context) -> {
        if (null == parameters || parameters.isEmpty() || null == result || null == result.getProperties()
                || result.getProperties().isEmpty()) {
            return new HashMap<>();
        }
        return result.getProperties().entrySet().stream().filter(entry -> parameters.contains(entry.getKey()))
                .filter(entry -> null != entry && null != entry.getValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    };

    Map<String, Object> extract(Set<String> parameters, ResultNode resultNode, ExecutionContext executionContext);

}
