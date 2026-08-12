package com.mari.flora.audit;

import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    AuditAction action();

    AuditEntityType entityType();

    /**
     * SpEL template. If left blank, AuditAspect auto-generates a generic message.
     */
    String message() default "";

    /**
     * SpEL expression to resolve the entity id when it can't be read off #result
     * (e.g. DELETE methods that return void). Example: "#id"
     */
    String entityIdExpression() default "";

    String condition() default "";
}
