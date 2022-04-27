package zyc.work.databasework.annotation.toekn;

import zyc.work.databasework.enums.token.TokenType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TokenCheck {
    boolean required() default true;

    TokenType TYPE() default TokenType.User;
}

