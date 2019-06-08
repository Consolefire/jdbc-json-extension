package com.cf.jdbc.json.ext.common.utils;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.ScanMode;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableReferenceGraph {

    private final TableReference root;
    private final MutableGraph<TableReference> graph;
    private static final Comparator<TableReference> TABLE_NAME_COMPARATOR = (t1, t2) -> {
        if (null == t1) {
            return -1;
        }
        if (null == t2) {
            return 1;
        }
        return t1.compareTo(t2);
    };

    public TableReferenceGraph(@NonNull TableReference root) {
        this.root = root;
        GraphBuilder<TableReference> graphBuilder =
                GraphBuilder.directed().allowsSelfLoops(false).nodeOrder(ElementOrder.sorted(TABLE_NAME_COMPARATOR));
        this.graph = graphBuilder.build();
        this.graph.addNode(root);
    }

    public boolean addReference(@NonNull TableReference root, @NonNull TableReference reference) {
        this.graph.addNode(reference);
        this.graph.putEdge(root, reference);
        if (Graphs.hasCycle(this.graph)) {
            String message = "Failed to add references from [ " + root.getTableName() + " -> "
                    + reference.getTableName() + " due to cycle";
            log.warn(message);
            // this.graph.removeNode(reference);
            this.graph.removeEdge(root, reference);
            return false;
        }
        root.addReference(reference);
        return true;
    }

    public Set<String> getAllTableNames() {
        return this.graph.nodes().stream().map(TableReference::getTableName).collect(Collectors.toSet());
    }

    public DatabaseMetaData buildDatabaseMetaData(final DatabaseMetaData actualDatabaseMetaData) {
        DatabaseMetaData resultDatabaseMetaData = new DatabaseMetaData();
        resultDatabaseMetaData.setCaseSensitive(actualDatabaseMetaData.isCaseSensitive());
        resultDatabaseMetaData.setScanMode(ScanMode.NONE);
        resultDatabaseMetaData.setSchema(actualDatabaseMetaData.getSchema());

        Set<String> requiredTableNames = getAllTableNames();
        resultDatabaseMetaData.setTables(
                actualDatabaseMetaData.getTables().stream().filter(tm -> requiredTableNames.contains(tm.getName()))
                        .map(TableMetaData::copy).collect(Collectors.toSet()));

        final TableMetaData rootTableMetaData = resultDatabaseMetaData.getTableMetaData(this.root.getTableName());

        Traverser<TableReference> traverser = Traverser.forGraph(this.graph);

        doInTraverse(actualDatabaseMetaData, resultDatabaseMetaData, rootTableMetaData, traverser, root);

        return resultDatabaseMetaData;
    }

    private void doInTraverse(final DatabaseMetaData actualDatabaseMetaData,
            final DatabaseMetaData resultDatabaseMetaData, final TableMetaData rootTableMetaData,
            final Traverser<TableReference> traverser, final TableReference root) {
        Iterable<TableReference> nextLevel = traverser.breadthFirst(root);
        if (null != nextLevel) {
            for (TableReference tableReference : nextLevel) {
                if (tableReference.equals(root)) {
                    continue;
                }
                Reference reference = new Reference();
                reference.setTable(tableReference.getTableName());
                reference.setReferenceTo(tableReference.getReferenceColumn());
                reference.setColumn(tableReference.getColumnName());
                rootTableMetaData.addReference(tableReference.getTableName(), reference);

                final TableMetaData refTableMetaData =
                        resultDatabaseMetaData.getTableMetaData(tableReference.getTableName());
                doInTraverse(actualDatabaseMetaData, resultDatabaseMetaData, refTableMetaData, traverser,
                        tableReference);
            }
        }
    }

}
