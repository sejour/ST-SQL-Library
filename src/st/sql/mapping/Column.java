package st.sql.mapping;

import java.lang.reflect.Field;
import java.util.List;

// フィールドとカラムの対応付け
class Column {
	private Field field;
	private Class<?> fieldType;
	private String fieldTypeName;
	private String columnName;
	private String columnQuery;
	private List<Column> columns;
	
	// フィールドとカラムリストの対応付けを行う
	public Column(Field field, DBColumnName columnNameAnnotation, DBTableName tableNameAnnotation) throws ResultSetMappingException
	{
		if (field == null) throw new NullPointerException("field is null");
		
		// フィールド情報設定
		this.field = field;
		this.fieldType = field.getType();
		this.fieldTypeName = this.fieldType.getName();
		
		// テーブル名設定
		String tableName = null;
		if (tableNameAnnotation != null) {
			tableName = tableNameAnnotation.value();
			if (tableName.equals("")) throw new NullPointerException("tableName is empty");
		}
		
		// カラム名とクエリの設定
		if (columnNameAnnotation != null) {
			// カラム名を取得
			String columnName = columnNameAnnotation.value();
			if (columnName.equals("")) throw new NullPointerException("columnName is empty");
			
			// カラム式を取得
			String expression = columnNameAnnotation.expression();
			if ((!expression.equals("")) && tableName != null) throw new ResultSetMappingException(50000, "[expression AS tableName.columnName]の形式は使用できません。");
			
			// カラム名の設定
			this.columnName = tableName == null ? columnName : String.format("%s.%s", tableName, columnName);
			// カラム式があればSQLクエリとして設定
			this.columnQuery = expression.equals("") ? this.columnName : String.format("%s AS %s", expression, this.columnName);
		} else {
			// フィールド名をカラム名として引用
			this.columnName = tableName == null ? this.field.getName() : String.format("%s.%s", tableName, this.field.getName());
			this.columnQuery = this.columnName;
		}
		
		this.columns = null;
	}
	
	// フィールドとカラムリストの対応付けを行う
	public Column(Field field, List<Column> columns) {
		if (field == null || columns == null) throw new NullPointerException("field or columns is null");
		
		this.field = field;
		this.fieldType = field.getType();
		this.fieldTypeName = this.fieldType.getName();
		this.columnName = null;
		this.columnQuery = null;
		this.columns = columns;
	}
	
	public Field getField() { return this.field; }
	public Class<?> getFieldType() { return this.fieldType; }
	public String getFieldTypeName() { return this.fieldTypeName; }
	public String getColumnName() { return this.columnName; }
	public String getColumnQuery() { return this.columnQuery; }
	public List<Column> getColumns() { return this.columns; }
	
	public boolean hasColumns() { return this.columns != null; }
}
