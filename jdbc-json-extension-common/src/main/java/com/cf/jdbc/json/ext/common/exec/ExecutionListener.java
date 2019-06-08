package com.cf.jdbc.json.ext.common.exec;

public interface ExecutionListener {

    default void onEvent(ExecutionEvent event) {
        if (null != event) {
            switch (event.getState()) {
              case FAILED:
                  onError(event);
                  break;
              case SUCCESS:
                  onSuccess(event);
                  break;
              case IN_PROGRESS:
              default:
                  tap(event);
                  break;
            }
        }
    }

    void tap(ExecutionEvent event);

    void onSuccess(ExecutionEvent event);

    void onError(ExecutionEvent event);

}
