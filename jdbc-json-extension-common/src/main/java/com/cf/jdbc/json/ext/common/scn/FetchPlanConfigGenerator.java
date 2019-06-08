package com.cf.jdbc.json.ext.common.scn;

import java.io.Serializable;
import java.util.Set;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;

public interface FetchPlanConfigGenerator<D extends Serializable> {

    FetchPlanConfig generate(D dataSourceKey, String rootTableName);

    FetchPlanConfig generate(D dataSourceKey, String rootTableName, Set<String> parameters);

}
