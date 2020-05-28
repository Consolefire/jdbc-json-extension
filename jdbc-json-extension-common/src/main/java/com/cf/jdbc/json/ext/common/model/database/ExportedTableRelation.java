/**
 * 
 */
package com.cf.jdbc.json.ext.common.model.database;

import java.io.Serializable;

/**
 * @author sabuj.das
 *
 */
public class ExportedTableRelation extends TableRelation implements
		Serializable {

	private ForeignKey exportedKey;
	private Table exportedTable;
	
	public ExportedTableRelation() {
		// TODO Auto-generated constructor stub
	}

	public ForeignKey getExportedKey() {
		return exportedKey;
	}

	public Table getExportedTable() {
		return exportedTable;
	}

	public void setExportedKey(ForeignKey exportedKey) {
		this.exportedKey = exportedKey;
	}

	public void setExportedTable(Table exportedTable) {
		this.exportedTable = exportedTable;
	}
	
	
}
