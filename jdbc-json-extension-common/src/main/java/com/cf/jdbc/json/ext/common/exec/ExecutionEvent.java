package com.cf.jdbc.json.ext.common.exec;

import com.cf.jdbc.json.ext.common.enums.ExecutionState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionEvent {

    private final ExecutionState state;
    private final Object source;

    public ExecutionEvent(ExecutionState state, Object source) {
        this.state = state;
        this.source = source;
    }


}
