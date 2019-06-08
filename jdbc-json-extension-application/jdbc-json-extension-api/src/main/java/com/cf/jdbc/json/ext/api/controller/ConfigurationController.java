package com.cf.jdbc.json.ext.api.controller;

import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cfg")
public class ConfigurationController {

    @Autowired
    @Qualifier("dataSourceConfigurationContext")
    private ConfigurationContext<String, DataSourceConfig> dataSourceConfigurationContext;
    @Autowired
    @Qualifier("fetchPlanConfigurationContext")
    private ConfigurationContext<String, FetchPlanConfig> fetchPlanConfigurationContext;

    @GetMapping("/datasource")
    public ResponseEntity<Collection<DataSourceConfig>> fetchDataSourceConfigs() {
        return new ResponseEntity<>(dataSourceConfigurationContext.getConfigurations(), HttpStatus.OK);
    }

    @GetMapping("/datasource/{name}")
    public ResponseEntity<DataSourceConfig> fetchDataSourceConfig(@PathVariable String name) {
        return new ResponseEntity<>(dataSourceConfigurationContext.getConfiguration(name), HttpStatus.OK);
    }

    @GetMapping("/fetchplan")
    public ResponseEntity<Collection<FetchPlanConfig>> fetchPlanConfigs() {
        return new ResponseEntity<>(fetchPlanConfigurationContext.getConfigurations(), HttpStatus.OK);
    }

    @GetMapping("/fetchplan/{name}")
    public ResponseEntity<FetchPlanConfig> fetchPlanConfig(@PathVariable String name) {
        return new ResponseEntity<>(fetchPlanConfigurationContext.getConfiguration(name), HttpStatus.OK);
    }

}
