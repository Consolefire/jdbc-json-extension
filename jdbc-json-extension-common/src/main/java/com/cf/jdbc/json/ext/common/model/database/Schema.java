
package com.cf.jdbc.json.ext.common.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schema extends BaseDbModel implements Serializable {

	private List<Table> tableList;
	
	public Schema() {
		tableList = new ArrayList<>();
	}
	

	public List<Table> getTableList() {
		return tableList;
	}

	public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}


}
