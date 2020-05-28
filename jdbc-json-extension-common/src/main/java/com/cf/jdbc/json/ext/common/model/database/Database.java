/**
 * 
 */
package com.cf.jdbc.json.ext.common.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sabuj.das
 *
 */
public class Database extends BaseDbModel implements Serializable {

	private List<Schema> schemaList;
	
	public Database() {
		schemaList = new ArrayList<>();
	}

	/**
	 * @return the schemaList
	 */
	public List<Schema> getSchemaList() {
		return schemaList;
	}

	/**
	 * @param schemaList the schemaList to set
	 */
	public void setSchemaList(List<Schema> schemaList) {
		this.schemaList = schemaList;
	}
	
	
}
