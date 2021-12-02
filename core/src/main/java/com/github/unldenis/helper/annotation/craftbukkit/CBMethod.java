package com.github.unldenis.helper.annotation.craftbukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CBMethod {

    String clazz();

    String name();

    String[] parametersTypes() default {};

}
