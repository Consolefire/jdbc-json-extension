package com.cf.jdbc.json.ext.api.controller;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.service.SimpleFetchPlanConfigGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetchplan")
public class FetchPlanConfigController {

    @Autowired
    private SimpleFetchPlanConfigGenerator fetchPlanConfigGenerator;


    @GetMapping("/generate")
    public ResponseEntity<FetchPlanConfig> generateFetchPlanConfig(
            @RequestParam(name = "ds", required = true) String dataSourceName,
            @RequestParam(name = "table", required = true) String rootTableName,
            @RequestParam(name = "column", required = true) String queryColumnName) {

        FetchPlanConfig fetchPlanConfig = fetchPlanConfigGenerator.generate(dataSourceName, rootTableName,
                Stream.of(queryColumnName).collect(Collectors.toSet()));
        return new ResponseEntity<FetchPlanConfig>(fetchPlanConfig, HttpStatus.OK);
    }

}
