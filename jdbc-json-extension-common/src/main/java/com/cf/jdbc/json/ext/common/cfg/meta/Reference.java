package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reference {

    private String table;
    private String column;
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
        return reference;
    }

}
