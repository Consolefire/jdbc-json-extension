
package com.cf.jdbc.json.ext.common.model.database;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseDbModel implements Serializable {

	@EqualsAndHashCode.Include
	private String name;
	private String comments;
	private boolean deleted = false;
	private String schemaName;

}
