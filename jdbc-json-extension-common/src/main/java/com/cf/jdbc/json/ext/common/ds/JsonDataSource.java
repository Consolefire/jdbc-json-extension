package com.cf.jdbc.json.ext.common.ds;

import java.util.Map;

import com.cf.jdbc.json.ext.common.dto.QueryRequest;
import com.cf.jdbc.json.ext.common.dto.Response;

public interface JsonDataSource {

    Response<Map<String, Object>> query(QueryRequest queryRequest);

}
