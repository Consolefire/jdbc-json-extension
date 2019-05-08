package com.cf.jdbc.json.ext.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cf.jdbc.json.ext.common.ds.JsonDataSource;
import com.cf.jdbc.json.ext.common.dto.QueryRequest;
import com.cf.jdbc.json.ext.common.dto.Response;

@RestController
@RequestMapping(value = "/api/data")
public class JsonDataController {

    @Autowired
    private JsonDataSource jsonDataSource;

    @GetMapping
    public ResponseEntity<Response<Map<String, Object>>> fetchData(
            @RequestParam(name = "ds", required = true) String dataSourceName,
            @RequestParam(name = "fp", required = true) String fetchPlanName,
            @RequestParam(name = "param", required = true) String paramName,
            @RequestParam(name = "value", required = true) Object value) {
        QueryRequest queryRequest = new QueryRequest(dataSourceName, fetchPlanName);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(paramName, value);
        queryRequest.setParameters(parameters);
        Response<Map<String, Object>> response = jsonDataSource.query(queryRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
