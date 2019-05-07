package com.cf.jdbc.json.ext.common.ds;

import com.cf.jdbc.json.ext.common.dto.QueryRequest;
import com.cf.jdbc.json.ext.common.dto.Response;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;

public interface JsonDataSource {

    Response<ResultNode> query(QueryRequest queryRequest);

}
