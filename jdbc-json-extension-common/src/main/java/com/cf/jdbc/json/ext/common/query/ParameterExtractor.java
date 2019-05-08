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
        return context.getSourceParameters().entrySet().stream().filter(entry -> parameters.contains(entry.getKey()))
                .filter(entry -> null != entry && null != entry.getValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    };


    ParameterExtractor PARENT_RESULT_DATA_SET_PARAMETER_EXTRACTOR = (parameters, result, context) -> {
        if (null == parameters || parameters.isEmpty() || null == result || !result.hasData()) {
            return new HashMap<>();
        }

        Map<String, Object> resolvedParams = new HashMap<>();
        parameters.forEach(name -> {
            resolvedParams.put(name, result.getResultDataSet().getColumnValue(name));
        });
        return resolvedParams;
    };

    Map<String, Object> extract(Set<String> parameters, ResultNode resultNode, ExecutionContext executionContext);

}
