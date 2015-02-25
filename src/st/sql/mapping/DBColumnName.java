package st.sql.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** フィールドに対してマッピングソースとなるデータベースのカラム名を指定する */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DBColumnName {
	// カラム名
	String value();
	// 式 (expression AS value)
	String expression() default "";
}
