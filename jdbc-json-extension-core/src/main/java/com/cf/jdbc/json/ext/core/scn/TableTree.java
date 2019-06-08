package com.cf.jdbc.json.ext.core.scn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;

import lombok.NonNull;

public class TableTree {

    private class TableTreeNode {
        private final TableMetaData table;
        private Map<Reference, TableTreeNode> references;
        private TableTreeNode parent;

        public TableTreeNode(@NonNull TableMetaData table) {
            this(table, new HashMap<>());
        }

        public TableTreeNode(@NonNull final TableMetaData table, @NonNull Map<Reference, TableTreeNode> references) {
            this.table = table;
            if (!references.isEmpty()) {
                references.entrySet().forEach(entry -> {
                    entry.getValue().parent = this;
                });
            }
            this.references = references;
        }

        public void addReferenceNode(@NonNull final Reference reference, @NonNull final TableMetaData tableMetaData) {
            TableTreeNode node = new TableTreeNode(tableMetaData);
            node.parent = this;
            this.references.put(reference, node);
        }

        // referred from
        public boolean isReferredFrom(@NonNull final TableTreeNode tableNode) {

            return false;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + Objects.hash(table);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof TableTreeNode)) {
                return false;
            }
            TableTreeNode other = (TableTreeNode) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
                return false;
            }
            return Objects.equals(table, other.table);
        }

        private TableTree getEnclosingInstance() {
            return TableTree.this;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TableTreeNode [");
            if (table != null) {
                builder.append("table=").append(table);
            }
            builder.append("]");
            return builder.toString();
        }

    }

    private final TableTreeNode rootNode;

    public TableTree(@NonNull TableMetaData rootTableMetaData) {
        rootNode = new TableTreeNode(rootTableMetaData);
    }



    public boolean isParentOf(TableMetaData parentTableMetaData, TableMetaData childTableMetaData) {
        if (null == parentTableMetaData) {
            throw new IllegalArgumentException("Provided parent table info is NULL");
        }
        if (null == childTableMetaData) {
            throw new IllegalArgumentException("Provided child table info is NULL");
        }
        if (parentTableMetaData.equals(childTableMetaData)) {
            throw new IllegalArgumentException("Provided child table [ " + childTableMetaData.getName()
                    + " is same as parent table: " + parentTableMetaData.getName());
        }
        if (rootNode.equals(parentTableMetaData)) {
            return true;
        }

        return false;
    }

}
