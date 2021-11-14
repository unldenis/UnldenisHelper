package com.github.unldenis.helper.annotation.nms;

import lombok.NonNull;

public class NMSClassNotFoundException extends Exception {

    public NMSClassNotFoundException(@NonNull String field) {
        super("NMS Class " + field + " not found");
    }

}
