package com.github.sebyplays.jwebserver.api.annotations;

import com.github.sebyplays.jwebserver.utils.enums.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessController {
    String index() default "{nameOfClass}";
    Priority priority() default Priority.DEFAULT;
}
