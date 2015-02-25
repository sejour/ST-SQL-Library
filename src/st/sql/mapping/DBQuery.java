package st.sql.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Selectクエリジェネレータで自動生成されるクエリに追加する定数クエリを指定する */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DBQuery {
	// SQLクエリ
	String[] value();
}
