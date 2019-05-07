package com.cf.jdbc.json.ext.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response<R> {

    private R body;

    public Response(R body) {
        this.body = body;
    }


}
