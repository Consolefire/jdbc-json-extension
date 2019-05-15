package com.cf.jdbc.json.ext.common.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;
import com.cf.jdbc.json.ext.common.utils.StringUtils;

import lombok.Getter;
import lombok.NonNull;

public final class ResponseBuilder {

    private final DatabaseMetaData databaseMetaData;
    private final String rootTableName;

    @Getter
    private final ResultNode rootResultNode;
    private final List<String> errors = new ArrayList<>();

    public ResponseBuilder(@NonNull String rootTableName, @NonNull DatabaseMetaData databaseMetaData) {
        this.rootTableName = rootTableName;
        this.databaseMetaData = databaseMetaData;
        this.rootResultNode = new ResultNode(rootTableName);
    }

    public final ResponseBuilder setRootData(final ResultDataSet dataSet) {
        if (null != dataSet && !dataSet.isEmpty()) {
            if (dataSet.getRowCount() > 1) {
                throw new UnsupportedOperationException("Root with many records not supported!!!");
            }
            this.rootResultNode.setResultDataSet(dataSet);
        }
        return this;
    }

    public final ResponseBuilder setResult(@NonNull ResultNode result) {
        return setResult(this.rootResultNode, result);
    }

    public final ResponseBuilder setResult(@NonNull final ResultNode parent, @NonNull ResultNode result) {
        parent.addChild(result);
        result.setParent(parent);
        return this;
    }

    public final ResponseBuilder withError(String error) {
        if (StringUtils.hasText(error)) {
            this.errors.add(error);
        }
        return this;
    }

    public Response<Map<String, Object>> build() {
        if (!this.errors.isEmpty()) {
            return Response.error(this.errors);
        }
        Map<String, Object> root = new HashMap<>();
        if (!rootResultNode.hasData()) {
            root.put(this.rootTableName, null);
            return Response.empty();
        }
        final Map<String, Object> rootTableData = this.rootResultNode.getResultDataSet().getRow(0);
        TableMetaData rootTableMetaData = databaseMetaData.getTableMetaData(rootTableName);
        root.put(this.rootTableName, rootTableData);
        ResultNode rootResult = this.rootResultNode;
        extractChildProperties(rootTableData, null, rootTableMetaData, rootResult);
        return Response.success(root);
    }

    private void extractChildProperties(final Map<String, Object> rootTableData, TableMetaData rootTableMetaData,
            TableMetaData tableMetaData, ResultNode rootResult) {
        if (tableMetaData.hasReferences() && rootResult.hasChildren()) {
            tableMetaData.getReferences().forEach((name, reference) -> {
                TableMetaData refTableMetaData = databaseMetaData.getTableMetaData(reference.getTable());
                if (reference.isCollection()) {
                    List<Map<String, Object>> childProps = new ArrayList<>();
                    List<ResultNode> childCollection = rootResult.getChildrenByName(name);
                    if (null != childCollection && !childCollection.isEmpty()) {
                        childCollection.forEach(node -> {
                            if (node.hasData()) {
                                Map<String, Object> row = node.getResultDataSet().getRow(0);
                                extractChildProperties(row, tableMetaData, refTableMetaData, node);
                                childProps.add(row);
                            }

                        });
                    }
                    rootTableData.put(name, childProps);
                } else {
                    ResultNode result = rootResult.getChildByName(name);
                    if (null != result && result.hasData()) {
                        Map<String, Object> row = result.getResultDataSet().getRow(0);
                        extractChildProperties(row, tableMetaData, refTableMetaData, result);
                        rootTableData.put(name, row);
                    }
                }
            });
        }
    }


}
