package com.cf.jdbc.json.ext.core.scn;

import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;
import com.cf.jdbc.json.ext.common.model.database.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericJdbcMetaDataScanner extends AbstractMetaDataScanner {

    @Override
    protected Database doInScan(@NonNull final String schemaName, @NonNull DataSource dataSource,
                                @NonNull DatabaseInformation information) {
        try (Connection connection = dataSource.getConnection();) {
            return grabDatabase(connection, schemaName, ReadDepthEnum.DEEP);
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
            throw new IllegalDataSourceConfiguration(sqlException.getMessage(), sqlException);
        }
    }

    public Database grabDatabase(Connection connection, String selectedSchemaName, ReadDepthEnum readDepth) throws SQLException{
        if(connection == null){
            return null;
        }
        Database db = new Database();
        db.setName(selectedSchemaName);
        List<Schema> schemaList = new ArrayList<Schema>();
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        if(databaseMetaData != null){
            ResultSet rs = databaseMetaData.getSchemas();
            while(rs.next()){
                String cat = rs.getString("TABLE_SCHEM");
                if(null != selectedSchemaName && !"".equals(selectedSchemaName))
                    if(!selectedSchemaName.equalsIgnoreCase(cat)){
                        continue;
                    }
                Schema s = new Schema();
                s.setName(cat);
                ResultSet ret = databaseMetaData.getTables("", s.getName(), "%", new String[] {"TABLE"});
                while(ret.next()){
                    String tn = ret.getString(TableMetaDataEnum.TABLE_NAME.getCode());

                    Table t = grabTable(connection, s.getName(), tn, readDepth);
                    if(tn.startsWith("BIN$"))
                        t.setDeleted(true);
                    s.getTableList().add(t);
                }
                if(ret != null){
                    ret.close();
                }

                schemaList.add(s);

            }
            if(rs != null){
                rs.close();
            }

        }
        databaseMetaData = null;
        db.setSchemaList(schemaList);
        return db;
    }

    public Schema grabSchema(Connection connection, String schemaName) throws SQLException{
        if(connection == null)
            return null;
        Schema schema = new Schema();

        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        if(databaseMetaData != null){
            ResultSet rs = databaseMetaData.getCatalogs();
            while(rs.next()){
                String cat = rs.getString("TABLE_CAT");
                if(cat.equalsIgnoreCase(schemaName)){
                    schema.setName(schemaName);
                    break;
                }
            }
        }
        return schema;
    }

    public ResultSet grabColumnDetails(String schemaName, String tableName, Connection connection) throws SQLException{
        java.sql.DatabaseMetaData metaData = connection.getMetaData();
        return metaData.getColumns("", schemaName, tableName, "%");
    }

    public int grabColumnCount(String schemaName, String tableName, Connection connection) throws SQLException{
        java.sql.DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns("", schemaName, tableName, "%");
        int count = 0;
        while(rs.next()){
            count ++;
        }
        return count;
    }

    /**
     *
     * @param connection
     * @param schemaName
     * @param tableName
     * @param readDepth
     * @return
     */
    public Table grabTable(Connection connection, String schemaName, String tableName, ReadDepthEnum readDepth){
        enrichConnection(connection);
        Table table = new Table();
        table.setName(tableName);
        try{
            java.sql.DatabaseMetaData meta = connection.getMetaData();
            ResultSet ret = meta.getTables("", schemaName, tableName, new String[] {"TABLE"});
            while(ret.next()){
                String tn = ret.getString(TableMetaDataEnum.TABLE_NAME.getCode());
                table.setName(tn);
                table.setSchemaName(schemaName);
                if(ReadDepthEnum.DEEP.equals(readDepth) || ReadDepthEnum.MEDIUM.equals(readDepth)){
                    table.setPrimaryKeys(grabPrimaryKeys(connection, schemaName, tableName, readDepth));
                    table.setImportedKeys(grabImportedKeys(connection, schemaName, tableName, readDepth));
                    table.setExportedKeys(grabExportedKeys(connection, schemaName, tableName, readDepth));
                    try{
                        table.setColumnlist(getColumnList(table, connection, readDepth));
                    }catch(Exception e){
                        System.err.println("Table : " + table.getName() );
                        e.printStackTrace();
                    }
                    if(ReadDepthEnum.DEEP.equals(readDepth)){
                        table.setComments(ret.getString(TableMetaDataEnum.REMARKS.getCode()));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return table;
    }


    public List<Column> getColumnList(Table table, Connection connection, ReadDepthEnum readDepth) throws SQLException{
        enrichConnection(connection);
        List<Column> list = new ArrayList<Column>();
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        List<PrimaryKey> pkList = table.getPrimaryKeys();
        Set<String> pkColSet = new HashSet<String>();
        for (PrimaryKey pk : pkList) {
            pkColSet.add(pk.getColumnName());
        }

        Set<String> fkColSet = new HashSet<String>();
        List<ForeignKey> importedKeys = table.getImportedKeys();
        for (ForeignKey fk : importedKeys) {
            fkColSet.add(fk.getFkColumnName());
        }
        ResultSet colRs = databaseMetaData.getColumns("", table.getSchemaName(), table.getName(), "%");
        ResultSetMetaData rsm = colRs.getMetaData();
        int cc = rsm.getColumnCount();
        while(colRs.next()){
            Column c = new Column();
            // set the schema name
            c.setSchemaName(table.getSchemaName());
            //set table name
            c.setTableName(table.getName());
            // set column name
            c.setName(colRs.getString(ColumnMetaDataEnum.COLUMN_NAME.getCode()));
            // set PK
            if(pkColSet.contains(c.getName())){
                c.setPrimaryKey(true);
            }
            // set FK
            if(fkColSet.contains(c.getName())){
                c.setForeignKey(true);
            }
            // set type name
            c.setTypeName(colRs.getString(ColumnMetaDataEnum.TYPE_NAME.getCode()));
            // set nullable
            String nulAble = colRs.getString(ColumnMetaDataEnum.IS_NULLABLE.getCode());
            if(ColumnMetaDataEnum.IS_NULLABLE_YES.getCode().equalsIgnoreCase(nulAble)){
                c.setNullable(true);
            }else{
                c.setNullable(false);
            }


            // set size
            c.setSize(colRs.getInt(ColumnMetaDataEnum.COLUMN_SIZE.getCode()));


            if(ReadDepthEnum.DEEP.equals(readDepth)){
                // set sql type
                c.setDataType(colRs.getInt(ColumnMetaDataEnum.SQL_DATA_TYPE.getCode()));
                // set column id
                c.setColumnID(colRs.getInt(ColumnMetaDataEnum.ORDINAL_POSITION.getCode()));
                // Precision
                c.setPrecision(colRs.getInt(ColumnMetaDataEnum.DECIMAL_DIGITS.getCode()));
                // set default value
                //c.setDefaultValue(colRs.getString(ColumnMetaDataEnum.COLUMN_DEF.getCode()));
                // comment
                c.setComments(colRs.getString(ColumnMetaDataEnum.REMARKS.getCode()));
            }

            list.add(c);
        }
        if(colRs != null){
            colRs.close();
        }
        return list;
    }

    public List<Column> getColumnList(String schemaName, String tableName, Connection connection, ReadDepthEnum readDepth) throws SQLException{
        enrichConnection(connection);
        List<Column> list = new ArrayList<Column>();
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();

        List<PrimaryKey> pkList = grabPrimaryKeys(connection, schemaName, tableName, readDepth);
        Set<String> pkColSet = new HashSet<String>();
        for (PrimaryKey pk : pkList) {
            pkColSet.add(pk.getColumnName());
        }

        Set<String> fkColSet = new HashSet<String>();
        List<ForeignKey> importedKeys = grabImportedKeys(connection, schemaName, tableName, readDepth);
        for (ForeignKey fk : importedKeys) {
            fkColSet.add(fk.getFkColumnName());
        }

        ResultSet colRs = databaseMetaData.getColumns("", schemaName, tableName, "%");
        ResultSetMetaData rsm = colRs.getMetaData();
        int cc = rsm.getColumnCount();
        while(colRs.next()){
            Column c = new Column();
            //set schema name
            c.setSchemaName(schemaName);
            //set table name
            c.setTableName(tableName);
            // set column name
            c.setName(colRs.getString(ColumnMetaDataEnum.COLUMN_NAME.getCode()));
            // set PK
            if(pkColSet.contains(c.getName())){
                c.setPrimaryKey(true);
            }
            // set FK
            if(fkColSet.contains(c.getName())){
                c.setForeignKey(true);
            }
            // set type name
            c.setTypeName(colRs.getString(ColumnMetaDataEnum.TYPE_NAME.getCode()));
            // set nullable
            String nulAble = colRs.getString(ColumnMetaDataEnum.IS_NULLABLE.getCode());
            if(ColumnMetaDataEnum.IS_NULLABLE_YES.getCode().equalsIgnoreCase(nulAble)){
                c.setNullable(true);
            }else{
                c.setNullable(false);
            }
            // set size
            c.setSize(colRs.getInt(ColumnMetaDataEnum.COLUMN_SIZE.getCode()));
            if(ReadDepthEnum.DEEP.equals(readDepth)){
                // set sql type
                c.setDataType(colRs.getInt(ColumnMetaDataEnum.SQL_DATA_TYPE.getCode()));
                // set column id
                c.setColumnID(colRs.getInt(ColumnMetaDataEnum.ORDINAL_POSITION.getCode()));
                // Precision
                c.setPrecision(colRs.getInt(ColumnMetaDataEnum.DECIMAL_DIGITS.getCode()));
                // set default value
                //c.setDefaultValue(colRs.getString(ColumnMetaDataEnum.COLUMN_DEF.getCode()));
                // comment
                c.setComments(colRs.getString(ColumnMetaDataEnum.REMARKS.getCode()));
            }
            list.add(c);
        }
        if(colRs != null){
            colRs.close();
        }
        return list;
    }

    /**
     *
     * @param connection
     * @param schemaName
     * @param tableName
     * @param readDepth
     * @return
     * @throws SQLException
     */
    public List<PrimaryKey> grabPrimaryKeys(Connection connection, String schemaName,
                                            String tableName, ReadDepthEnum readDepth) throws SQLException{
        List<PrimaryKey> pkList = new ArrayList<PrimaryKey>();
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet pkRs = databaseMetaData.getPrimaryKeys("", schemaName, tableName);
        while(pkRs.next()){
            PrimaryKey pk = new PrimaryKey();
            pk.setColumnName(pkRs.getString(PKMetaDataEnum.COLUMN_NAME.getCode()));

            if(ReadDepthEnum.DEEP.equals(readDepth)){
                pk.setTableCat(pkRs.getString(PKMetaDataEnum.TABLE_CAT.getCode()));
                pk.setTableSchem(pkRs.getString(PKMetaDataEnum.TABLE_SCHEM.getCode()));
                pk.setTableName(tableName);
                pk.setName(pkRs.getString(PKMetaDataEnum.PK_NAME.getCode()));
                //pk.setDeleted(pkRs.getBoolean(PKMetaDataEnum.))
                pk.setKeySeq(pkRs.getShort(PKMetaDataEnum.KEY_SEQ.getCode()));
                //pk.setComments(pkRs.getString(PKMetaDataEnum.comments))
            }

            pkList.add(pk);
        }
        if(pkRs != null){
            pkRs.close();
        }
        return pkList;
    }

    public List<ForeignKey> grabImportedKeys(Connection connection, String schemaName, String tableName, ReadDepthEnum readDepth) throws SQLException{
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet fkRs = databaseMetaData.getImportedKeys("", schemaName, tableName);
        return readFksFromRS(fkRs, true, readDepth);
    }

    public List<ForeignKey> grabExportedKeys(Connection connection, String schemaName, String tableName, ReadDepthEnum readDepth) throws SQLException{
        java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet fkRs = databaseMetaData.getExportedKeys("", schemaName, tableName);
        return readFksFromRS(fkRs, false, readDepth);
    }

    private List<ForeignKey> readFksFromRS(ResultSet fkRs, Boolean imported, ReadDepthEnum readDepth) throws SQLException{
        List<ForeignKey> fks = new ArrayList<ForeignKey>();

        while(fkRs.next()){
            ForeignKey fk = new ForeignKey();
            fk.setPkColumnName(fkRs.getString(ForeignKeyMetaDataEnum.PKCOLUMN_NAME.getCode()));
            fk.setFkColumnName(fkRs.getString(ForeignKeyMetaDataEnum.FKCOLUMN_NAME.getCode()));
            if(ReadDepthEnum.DEEP.equals(readDepth)){
                fk.setPkTableCat(fkRs.getString(ForeignKeyMetaDataEnum.PKTABLE_CAT.getCode()));
                fk.setPkTableSchem(fkRs.getString(ForeignKeyMetaDataEnum.PKTABLE_SCHEM.getCode()));
                fk.setPkTableName(fkRs.getString(ForeignKeyMetaDataEnum.PKTABLE_NAME.getCode()));
                fk.setFkTableCat(fkRs.getString(ForeignKeyMetaDataEnum.FKTABLE_CAT.getCode()));
                fk.setFkTableSchem(fkRs.getString(ForeignKeyMetaDataEnum.FKTABLE_SCHEM.getCode()));
                fk.setFkTableName(fkRs.getString(ForeignKeyMetaDataEnum.FKTABLE_NAME.getCode()));
                fk.setKeySeq(fkRs.getShort(ForeignKeyMetaDataEnum.KEY_SEQ.getCode()));
                fk.setUpdateRule(fkRs.getShort(ForeignKeyMetaDataEnum.UPDATE_RULE.getCode()));
                fk.setDeleteRule(fkRs.getShort(ForeignKeyMetaDataEnum.DELETE_RULE.getCode()));
                fk.setPkName(fkRs.getString(ForeignKeyMetaDataEnum.PK_NAME.getCode()));
                fk.setFkName(fkRs.getString(ForeignKeyMetaDataEnum.FK_NAME.getCode()));
                fk.setDeferrability(fkRs.getShort(ForeignKeyMetaDataEnum.DEFERRABILITY.getCode()));
            }
            fk.setImportedKey(imported);
            fks.add(fk);
        }
        if(fkRs != null){
            fkRs.close();
        }
        return fks;
    }

    public Set<String> getAvailableSchemaNames(
            Connection connection) throws SQLException {
        Set<String> schemaNames = new HashSet<String>();
        if(null == connection)
            return schemaNames;

        DatabaseMetaData metaData = connection.getMetaData();
        if(metaData != null){
            ResultSet rs = metaData.getSchemas();
            while(rs.next()){
                String cat = rs.getString("TABLE_SCHEM");
                schemaNames.add(cat);
            }
            if(rs != null){
                rs.close();
            }
        }

        return schemaNames;
    }

    private void enrichConnection(Connection connection) {
//		if(connection instanceof OracleConnection)
//			((OracleConnection)connection).setRemarksReporting(true);
    }


}
