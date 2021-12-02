package com.github.unldenis.helper.annotation.craftbukkit;

import lombok.NonNull;

public class CBClassNotFoundException extends Exception {

    public CBClassNotFoundException(@NonNull String field) {
        super("CraftBukkit Class " + field + " not found");
    }

}
