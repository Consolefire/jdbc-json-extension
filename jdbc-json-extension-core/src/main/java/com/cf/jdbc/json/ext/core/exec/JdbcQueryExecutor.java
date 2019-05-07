package com.cf.jdbc.json.ext.core.exec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.common.query.Query;
import com.cf.jdbc.json.ext.common.utils.StringUtils;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcQueryExecutor implements ActionNodeExecutor<QueryActionNode> {


    @Override
    public ResultDataSet execute(ExecutionContext executionContext, QueryActionNode actionNode) {
        final DataSource dataSource = executionContext.getDataSource();
        Query query = actionNode.getQuery();
        String parameterizedSql = query.toSql();
        log.info("Parameterized query: {}", parameterizedSql);
        String executableSql = query.toSql(executionContext.getSourceParameters());
        log.info("Executable query: {}", executableSql);
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(executableSql);
                ResultSet resultSet = statement.executeQuery();) {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int colCount = resultSetMetaData.getColumnCount();
            ResultDataSet resultDataSet = new ResultDataSet(colCount);
            for (int i = 1; i <= colCount; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                if (StringUtils.nullOrEmpty(columnName)) {
                    columnName = resultSetMetaData.getColumnLabel(i);
                }
                resultDataSet.setColumnIndex(columnName, i - 1);
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                resultDataSet.addRow(rowData);
            }
            return resultDataSet;
        } catch (SQLException exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }


}
