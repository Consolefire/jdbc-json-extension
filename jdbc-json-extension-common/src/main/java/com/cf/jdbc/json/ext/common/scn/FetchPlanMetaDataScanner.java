package com.cf.jdbc.json.ext.common.scn;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;

import lombok.NonNull;

public interface FetchPlanMetaDataScanner<DSK extends Serializable, FPK extends Serializable> {

    FetchPlanConfig scan(@NonNull DSK dataSourceKey, @NonNull FPK fetchPlanKey);

}
