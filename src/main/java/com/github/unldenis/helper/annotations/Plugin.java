package com.github.unldenis.helper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Plugin {

    String name();

    String version();

    String description();

    String api_version() default "1.13";

    String author() default "unldenis";

    String[] depend() default "";

    String[] softdepend() default "";

    String[] loadbefore() default "";

    String[] commands() default "";

}