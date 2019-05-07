package com.cf.jdbc.json.ext.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultDataSet {

    private String tableName;
    private String alias;
    private final int columnCount;
    private final Map<String, Integer> columnIndexMap;
    private final List<Object[]> data;

    public ResultDataSet(int columnCount) {
        this.columnCount = columnCount;
        this.data = new ArrayList<>();
        this.columnIndexMap = new HashMap<>(columnCount);
    }

    public void addRow(Object[] row) {
        this.data.add(row);
    }

    public int getRowCount() {
        return data.size();
    }

    public List<String> getColumnNames() {
        return columnIndexMap.keySet().stream().sorted((x, y) -> {
            return x.compareTo(y);
        }).collect(Collectors.toList());
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Map<String, Integer> getColumnIndexMap() {
        return Collections.unmodifiableMap(columnIndexMap);
    }

    public List<Object[]> getData() {
        return Collections.unmodifiableList(data);
    }

    public void setColumnIndex(String columnName, Integer index) {
        columnIndexMap.put(columnName, index);
    }

    public Map<String, Object> getRow(int index) {
        if (index < 0 || index >= data.size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        Object[] rowData = data.get(index);
        Map<String, Object> rowMap = new HashMap<>();
        columnIndexMap.entrySet().forEach(entry -> {
            rowMap.put(entry.getKey(), rowData[entry.getValue()]);
        });
        return rowMap;
    }
}
