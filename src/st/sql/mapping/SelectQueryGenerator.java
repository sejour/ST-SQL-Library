package st.sql.mapping;

import java.lang.reflect.Field;
import java.util.List;

import st.sql.query.SQLQueryException;
import st.sql.query.SelectQuery;

/** SelectQueryを生成するクラス */
public class SelectQueryGenerator {
	
	// マッパー
	private ResultSetClassMapper mapper;
	
	public SelectQueryGenerator(ResultSetClassMapper mapper)
	{
		if (mapper == null) throw new NullPointerException("mapper is null.");
		
		this.mapper = mapper;
	}

	// GENERATE SELECT [instance] **************************************************************************************
	/** マッピングクラスからSelect句を生成する */
	public String generateSelectAsString(Class<?> type) throws SQLQueryException, ResultSetMappingException
	{
		if (type == null) throw new NullPointerException("type is null.");
		
		// トップカラムリストを取得
		List<Column> columns = mapper.getColumns(type);
		
		StringBuilder select = new StringBuilder();
		try {
			this.enumerateColumn(type, columns, select);
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
		
		if (select.length() == 0) throw new SQLQueryException(50000, "カラムが1つも存在しないSELECT句は生成できません。");

		// テーブル名取得
		DBTableName tableName = type.getAnnotation(DBTableName.class);
		if (tableName == null) throw new SQLQueryException(50000, "テーブル名を指定されていません。");
		select.insert(0, "SELECT ");
		select.append(String.format(" FROM %s", tableName.value()));
		
		// 追加のクエリを取得
		DBQuery appendQuery = type.getAnnotation(DBQuery.class);
		if (appendQuery != null) {
			for (String line : appendQuery.value()) {
				select.append(String.format(" %s", line));
			}
		}
		
		return select.toString();
	}
	
	/** マッピングクラスからSelect句を生成する */
	public SelectQuery generateSelectQuery(Class<?> type) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(this.generateSelectAsString(type));
	}

	/** マッピングクラスからSelect句を生成する */
	public SelectQuery generateSelectQuery(Class<?> type, String joinQuery) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(this.generateSelectAsString(type), joinQuery);
	}

	/** マッピングクラスからSelect句を生成する */
	public SelectQuery generateSelectQuery(Class<?> type, String[] joinQuerys) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(this.generateSelectAsString(type), joinQuerys);
	}
	
	// StringBuilderにカラムを列挙する
	private void enumerateColumn(Class<?> type, List<Column> columns, StringBuilder select)
	{
		for (Column column : columns) 
		{
			// さらにマッピングできれば再帰
			List<Column> children = column.getColumns(); 
			if (children != null) {
				this.enumerateColumn(column.getFieldType(), children, select);
				continue;
			}
			
			// selectにカラムを追加
			select.append(String.format("%s%s", (select.length() == 0 ? "" : ", "), column.getColumnQuery()));
		}
	}
	// *****************************************************************************************************************

	// GENERATE SELECT [class] *****************************************************************************************
	/** マッピングクラスからSelect句を生成する */
	public static String sGenerateSelectAsString(Class<?> type) throws SQLQueryException, ResultSetMappingException
	{
		if (type == null) throw new NullPointerException("type is null.");
		if (type.getAnnotation(DBMapping.class) == null) throw new ResultSetMappingException(50000, "The class is unsupported mapping from result set.");
		
		StringBuilder select = new StringBuilder();
		try {
			sEnumerateColumn(type, select, null);
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
		
		if (select.length() == 0) throw new SQLQueryException(50000, "カラムが1つも存在しないSELECT句は生成できません。");
		
		// テーブル名取得
		DBTableName tableName = type.getAnnotation(DBTableName.class);
		if (tableName == null) throw new SQLQueryException(50000, "テーブル名が指定されていません。");		
		select.insert(0, "SELECT ");
		select.append(String.format(" FROM %s", tableName.value()));
		
		// 追加のクエリを取得
		DBQuery appendQuery = type.getAnnotation(DBQuery.class);
		if (appendQuery != null) {
			for (String line : appendQuery.value()) {
				select.append(String.format(" %s", line));
			}
		}
		
		return select.toString();
	}
	
	/** マッピングクラスからSelect句を生成する */
	public static SelectQuery sGenerateSelectQuery(Class<?> type) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(sGenerateSelectAsString(type));
	}
	
	/** マッピングクラスからSelect句を生成する */
	public static SelectQuery sGenerateSelectQuery(Class<?> type, String joinQuery) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(sGenerateSelectAsString(type), joinQuery);
	}

	/** マッピングクラスからSelect句を生成する */
	public static SelectQuery sGenerateSelectQuery(Class<?> type, String[] joinQuerys) throws SQLQueryException, ResultSetMappingException
	{
		return new SelectQuery(sGenerateSelectAsString(type), joinQuerys);
	}
	
	// StringBuilderにカラムを列挙する
	private static void sEnumerateColumn(Class<?> type, StringBuilder select, DBTableName parentTableNameAnnotation) throws ResultSetMappingException
	{
		for (Field field : type.getFields()) 
		{
			// フィールドが有効か確認
			if (field.getAnnotation(DBUnsupportedColumn.class) != null) continue;
			
			// カラムの修飾テーブル名を取得
			DBTableName tableNameAnnotation = parentTableNameAnnotation == null ? field.getAnnotation(DBTableName.class) : parentTableNameAnnotation;
			
			Class<?> fieldType = field.getType();
			
			// さらにマッピングできれば再帰
			if (fieldType.getAnnotation(DBMapping.class) != null) {
				sEnumerateColumn(fieldType, select, tableNameAnnotation);
				continue;
			}
			
			// テーブル名設定
			String tableName = null;
			if (tableNameAnnotation != null) {
				tableName = tableNameAnnotation.value();
				if (tableName.equals("")) throw new NullPointerException("tableName is empty");
			}
			
			// カラムアノテーション取得
			DBColumnName columnNameAnnotation = field.getAnnotation(DBColumnName.class);

			// カラム名とクエリの設定
			String columnQuery;
			if (columnNameAnnotation != null) {
				// カラム名を取得
				String columnName = columnNameAnnotation.value();
				if (columnName.equals("")) throw new NullPointerException("columnName is empty");
				
				// カラム式を取得
				String expression = columnNameAnnotation.expression();
				if ((!expression.equals("")) && tableName != null) throw new ResultSetMappingException(50000, "[expression AS tableName.columnName]の形式は使用できません。");
				
				// カラム名の設定
				columnName = tableName == null ? columnName : String.format("%s.%s", tableName, columnName);
				// カラム式があればSQLクエリとして設定
				columnQuery = expression.equals("") ? columnName : String.format("%s AS %s", expression, columnName);
			} else {
				// フィールド名をカラム名として引用
				columnQuery = tableName == null ? field.getName() : String.format("%s.%s", tableName, field.getName());
			}
			
			// selectにカラムを追加
			select.append(String.format("%s%s", (select.length() == 0 ? "" : ", "), columnQuery));
		}
	}
	// *****************************************************************************************************************
}
