package com.github.unldenis.helper.annotation.nms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NMSMethod {

    String clazz();

    String name();

    String[] parametersTypes() default {};

}
