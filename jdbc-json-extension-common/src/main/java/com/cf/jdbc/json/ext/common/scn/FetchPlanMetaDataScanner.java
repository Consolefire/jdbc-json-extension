package com.cf.jdbc.json.ext.common.scn;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;

import lombok.NonNull;

public interface FetchPlanMetaDataScanner<D extends Serializable, F extends Serializable> {

    FetchPlanConfig scan(@NonNull D dataSourceKey, @NonNull F fetchPlanKey);

}
