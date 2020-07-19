package com.eu.annoation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fuyangrong
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {

    String des() default "";

}
