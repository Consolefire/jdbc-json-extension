/**
 * 
 */
package com.cf.jdbc.json.ext.common.model.database;

public enum TableMetaDataEnum {

	TABLE_CAT("TABLE_CAT"),
	TABLE_SCHEM("TABLE_SCHEM"),
	TABLE_NAME("TABLE_NAME"),
	TABLE_TYPE("TABLE_TYPE"),
	REMARKS("REMARKS"),
	TYPE_CAT("TYPE_CAT"),
	TYPE_SCHEM("TYPE_SCHEM"),
	TYPE_NAME("TYPE_NAME"),
	SELF_REFERENCING_COL_NAME("SELF_REFERENCING_COL_NAME"),
	REF_GENERATION("REF_GENERATION");
	
	private String code;

	private TableMetaDataEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	
}
