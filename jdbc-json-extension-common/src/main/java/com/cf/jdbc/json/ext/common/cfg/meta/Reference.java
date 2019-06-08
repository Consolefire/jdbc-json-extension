package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reference {

    /**
     * The table from where it is referred.
     */
    private String table;

    /**
     * The column name on current table.
     */
    private String column;

    /**
     * The column in table from where it is referred.
     */
    private String referenceTo;
    private boolean collection;
    private boolean inverse;
    private Set<ColumnMetaData> uniqueOn;

    @Override
    public int hashCode() {
        return Objects.hash(referenceTo, table);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Reference)) {
            return false;
        }
        Reference other = (Reference) obj;
        return Objects.equals(referenceTo, other.referenceTo) && Objects.equals(table, other.table);
    }

    public final Reference copy() {
        Reference reference = new Reference();
        reference.table = this.table;
        reference.column = this.column;
        reference.referenceTo = this.referenceTo;
        reference.collection = this.collection;
        reference.inverse = this.inverse;
        if (null != uniqueOn) {
            reference.uniqueOn = this.uniqueOn.parallelStream().map(ColumnMetaData::copy).collect(Collectors.toSet());
        }
        return reference;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Reference [");
        if (table != null) {
            builder.append("table=").append(table).append(", ");
        }
        if (column != null) {
            builder.append("column=").append(column).append(", ");
        }
        if (referenceTo != null) {
            builder.append("referenceTo=").append(referenceTo).append(", ");
        }
        builder.append("inverse=").append(inverse).append("]");
        return builder.toString();
    }



}
