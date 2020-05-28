/**
 * 
 */
package com.cf.jdbc.json.ext.common.model.database;

import java.io.Serializable;
import java.util.List;

/**
 * @author sabuj.das
 *
 */
public class TableDependency implements Serializable{

	private Table currentTable;
	
	private List<ImportedTableRelation> importedRelations;
	private List<ExportedTableRelation> exportedRelations;
	
	public TableDependency() {
		// TODO Auto-generated constructor stub
	}

	public Table getCurrentTable() {
		return currentTable;
	}

	public void setCurrentTable(Table currentTable) {
		this.currentTable = currentTable;
	}

	public List<ImportedTableRelation> getImportedRelations() {
		return importedRelations;
	}

	public List<ExportedTableRelation> getExportedRelations() {
		return exportedRelations;
	}

	public void setImportedRelations(List<ImportedTableRelation> importedRelations) {
		this.importedRelations = importedRelations;
	}

	public void setExportedRelations(List<ExportedTableRelation> exportedRelations) {
		this.exportedRelations = exportedRelations;
	}


	
}
