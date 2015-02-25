package st.sql.mapping;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import st.sql.STSQLException;

/** ResultSetからクラスへのマッピングを行うクラス */
public class ResultSetClassMapper {
	// 型名一覧
	public static final String TYPENAME_STRING = "java.lang.String";
	public static final String TYPENAME_BOOL = "boolean";
	public static final String TYPENAME_BYTE = "byte";
	public static final String TYPENAME_SHORT = "short";
	public static final String TYPENAME_INT = "int";
	public static final String TYPENAME_LONG = "long";
	public static final String TYPENAME_FLOAT = "float";
	public static final String TYPENAME_DOUBLE = "double";
	
	// カラムディクショナリ(カラムとフィールドの対応付けリスト)
	private Map<String, List<Column>> columnDictionaries;
	
	public ResultSetClassMapper()
	{
		this.columnDictionaries = new HashMap<String, List<Column>>();
	}
	
	// バインド済みのカラムリストを取得する
	List<Column> getColumns(Class<?> type) throws ResultSetMappingException 
	{
		List<Column> columns = this.columnDictionaries.get(type.getName());
		if (columns == null) throw new ResultSetMappingException(50000, String.format("Class<%s>はbindされていません。", type.getName()));
		return columns;
	}
	
	
	// BINDER **********************************************************************************************************
	/** フィールドとカラムの対応付けを行う */
	public void bind(Class<?> type) throws ResultSetMappingException
	{
		if (type == null) throw new NullPointerException("type is null.");
		if (type.getAnnotation(DBMapping.class) == null) throw new ResultSetMappingException(50000, "The class is unsupported mapping from result set."); 
		
		try {
			// カラムリストを生成して登録
			this.columnDictionaries.put(type.getName(), this.generateColumnList(type, null));
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
	}

	/** フィールドとカラムの対応付けを行う (クラスに付加したDBTableNameAnnotationをカラムのテーブル名修飾子として使用するかどうかを指定する) */
	public void bind(Class<?> type, boolean useTableNameAsColumnQualifier) throws ResultSetMappingException
	{
		if (type == null) throw new NullPointerException("type is null.");
		if (type.getAnnotation(DBMapping.class) == null) throw new ResultSetMappingException(50000, "The class is unsupported mapping from result set."); 
		
		try {
			// カラムリストを生成して登録
			this.columnDictionaries.put(type.getName(), this.generateColumnList(type, useTableNameAsColumnQualifier ? type.getAnnotation(DBTableName.class) : null));
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
	}
	
	// カラムリストを生成する
	private List<Column> generateColumnList(Class<?> type, DBTableName parentTableNameAnnotation) throws ResultSetMappingException
	{
		List<Column> columns = new ArrayList<Column>();
	
		for (Field field : type.getFields())
		{
			// フィールドが有効か確認
			if (field.getAnnotation(DBUnsupportedColumn.class) != null) continue;
			
			// カラムの修飾テーブル名を取得
			DBTableName tableNameAnnotation = parentTableNameAnnotation == null ? field.getAnnotation(DBTableName.class) : parentTableNameAnnotation;
			
			// さらにマッピングできるか確認
			if (field.getType().getAnnotation(DBMapping.class) != null) {
				// カラムリストの生成を再帰して追加
				columns.add(new Column(field, this.generateColumnList(field.getType(), tableNameAnnotation)));
				continue;
			}
		
			// カラムアノテーション取得
			DBColumnName columnNameAnnotation = field.getAnnotation(DBColumnName.class);
			
			// カラム追加
			columns.add(new Column(field, columnNameAnnotation, tableNameAnnotation));
		}
		
		return columns;
	}
	// *****************************************************************************************************************
	
	// MAPPER [instance] ***********************************************************************************************
	/** 先頭のレコードのみマッピングを行う */
	public Object mappingFirst(Class<?> type, ResultSet resultSet) throws SQLException, ResultSetMappingException
	{
		if (type == null || resultSet == null) throw new NullPointerException("type or resultSet is null.");
		
		// カーソルを先頭に
		if (!resultSet.first()) throw new ResultSetMappingException();
		
		// トップカラムリストを取得
		List<Column> columns = this.getColumns(type);
		
		try {
			// マッピング
			return this.mapping(type, columns, resultSet);
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
	}
	
	/** 全てのレコードをマッピングする */
	public List<Object> mappingAll(Class<?> type, ResultSet resultSet) throws ResultSetMappingException
	{
		if (type == null || resultSet == null) throw new NullPointerException("type or resultSet is null.");

		// トップカラムリストを取得
		List<Column> columns = this.getColumns(type);

		// マッピングされたオブジェクトのリストを生成
		List<Object> objects = new ArrayList<Object>();

		try {
			while (resultSet.next())
			{
				// 一行をマッピング
				objects.add(this.mapping(type, columns, resultSet));
			}
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
		
		return objects;
	}
	
	// マッピングする
	private Object mapping(Class<?> type, List<Column> columns, ResultSet resultSet) throws SQLException, ResultSetMappingException, InstantiationException, IllegalAccessException
	{
		Object obj = type.newInstance();
		
		for (Column column : columns) 
		{
			Field field = column.getField();
			
			// さらにマッピングできれば再帰
			List<Column> children = column.getColumns(); 
			if (children != null) {
				field.set(obj, this.mapping(column.getFieldType(), children, resultSet));
				continue;
			}
			
			// マッピング
			map(obj, field, column.getFieldTypeName(), column.getColumnName(), resultSet);
		}		
		
		return obj;
	}
	// *****************************************************************************************************************
	
	// MAPPER [class] **************************************************************************************************
	/** 先頭のレコードのみマッピングを行う */
	public static Object sMappingFirst(Class<?> type, ResultSet resultSet) throws ResultSetMappingException
	{
		return sMappingFirst(type, resultSet, false);
	}

	/** 先頭のレコードのみマッピングを行う (クラスに付加したDBTableNameAnnotationをカラムのテーブル名修飾子として使用するかどうかを指定する) */
	public static Object sMappingFirst(Class<?> type, ResultSet resultSet, boolean useTableNameAsColumnQualifier) throws ResultSetMappingException
	{
		if (type == null || resultSet == null) throw new NullPointerException("type or resultSet is null.");
		if (type.getAnnotation(DBMapping.class) == null)  throw new ResultSetMappingException(50000, "The class is unsupported mapping from result set.");
		
		try {
			// カーソルを先頭に
			if (!resultSet.first()) throw new ResultSetMappingException();
			// マッピング
			return sMapping(type, resultSet, useTableNameAsColumnQualifier ? type.getAnnotation(DBTableName.class) : null);
			
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
	}
	
	/** 全てのレコードをマッピングする (クラスに付加したDBTableNameAnnotationをカラムのテーブル名修飾子として使用するかどうかを指定する) */
	public static List<Object> sMappingAll(Class<?> type, ResultSet resultSet) throws ResultSetMappingException
	{
		return sMappingAll(type, resultSet, false);
	}

	/** 全てのレコードをマッピングする */
	public static List<Object> sMappingAll(Class<?> type, ResultSet resultSet, boolean useTableNameAsColumnQualifier) throws ResultSetMappingException
	{
		if (type == null || resultSet == null) throw new NullPointerException("type or resultSet is null.");
		if (type.getAnnotation(DBMapping.class) == null)  throw new ResultSetMappingException(50000, "The class is unsupported mapping from result set.");
		
		List<Object> objects = new ArrayList<Object>();
		
		try {
			while (resultSet.next()) {
				// マッピング
			 	objects.add(sMapping(type, resultSet, useTableNameAsColumnQualifier ? type.getAnnotation(DBTableName.class) : null));
			}
		} catch (Throwable e) {
			throw new ResultSetMappingException(e);
		}
		
		return objects;
	}
	
	private static Object sMapping(Class<?> type, ResultSet resultSet, DBTableName parentTableNameAnnotation) throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException, STSQLException
	{
		Object obj = type.newInstance();
		
		for (Field field : type.getFields())
		{
			// フィールドが有効か確認
			if (field.getAnnotation(DBUnsupportedColumn.class) != null) continue;
			
			// カラムの修飾テーブル名を取得
			DBTableName tableNameAnnotation = parentTableNameAnnotation == null ? field.getAnnotation(DBTableName.class) : parentTableNameAnnotation;
			
			// フィールドの型を取得
			Class<?> fieldType = field.getType();
			
			// さらにマッピング可能か確認
			if (fieldType.getAnnotation(DBMapping.class) != null) {
				field.set(obj, sMapping(fieldType, resultSet, tableNameAnnotation));
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
			
			String columnName;
			if (columnNameAnnotation != null) {
				// カラム名を取得
				columnName = columnNameAnnotation.value();
				if (columnName.equals("")) throw new NullPointerException("columnName is empty");
				
				// カラム名の設定
				columnName = tableName == null ? columnName : String.format("%s.%s", tableName, columnName);
			} else {
				// フィールド名をカラム名として引用
				columnName = tableName == null ? field.getName() : String.format("%s.%s", tableName, field.getName());
			}
			// マッピング
			map(obj, field, columnName, resultSet);
		}
		
		return obj;
	}
	// *****************************************************************************************************************
	
	// MAP KERNEL ******************************************************************************************************
	// マップする
	private static void map(Object obj, Field field, String columnName, ResultSet resultSet) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		map(obj, field, field.getType().getName(), columnName, resultSet);
	}
	
	// マップする
	private static void map(Object obj, Field field, String fieldTypeName, String columnName, ResultSet resultSet) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		switch (fieldTypeName) {
		case TYPENAME_STRING:
			field.set(obj, resultSet.getString(columnName));
			break;
		case TYPENAME_BOOL:
			field.setBoolean(obj, resultSet.getBoolean(columnName));
			break;
		case TYPENAME_BYTE:
			field.setByte(obj, resultSet.getByte(columnName));
			break;
		case TYPENAME_SHORT:
			field.setShort(obj, resultSet.getShort(columnName));
			break;
		case TYPENAME_INT:
			field.setInt(obj, resultSet.getInt(columnName));
			break;
		case TYPENAME_LONG:
			field.setLong(obj, resultSet.getLong(columnName));
			break;
		case TYPENAME_FLOAT:
			field.setFloat(obj, resultSet.getFloat(columnName));
			break;
		case TYPENAME_DOUBLE:
			field.setDouble(obj, resultSet.getDouble(columnName));
			break;
		default:
			break;
		}
	}
	// *****************************************************************************************************************
}
