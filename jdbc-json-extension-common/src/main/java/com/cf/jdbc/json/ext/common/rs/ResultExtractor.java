package com.cf.jdbc.json.ext.common.rs;

import com.cf.jdbc.json.ext.common.model.ResultDataSet;

public interface ResultExtractor<R> {

    R extract(ResultDataSet resultDataSet);

}
