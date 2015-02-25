package st.sql.query;

// 動的なHAVING句を生成するクラス
// ※ 全ての式はAND演算されます。
public class HavingQuery extends SequentialParametersQuery {

	public HavingQuery() {}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException
	{
		if (sql.length() == 0) {
			throw new SQLQueryException(50000, "HAVING句はシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		// パラメータリストが空でなければWHERE句を生成
		if ( !this.params.isEmpty() ) 
		{
			StringBuilder whereQuery = new StringBuilder(String.format("HAVING %s", this.params.get(0).getExpression()));
			
			int size = this.params.size();
			for (int i=1; i < size ; ++i)
			{
				whereQuery.append(String.format(" AND %s", this.params.get(i).getExpression()));
			}
			
			// sqlに追加
			sql.append(" " + whereQuery.toString());
		}
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}
	
	// IS NULL式を追加します。
	// 例: id IS NULL
	//     addIsNull("id");
	public void addIsNull(String columnName) throws SQLQueryException
	{
		this.addConstantParameter(String.format("%s IS NULL", columnName));
	}
	
	// IS NULL式を追加します。
	// 例: id IS NOT NULL
	//     addIsNotNull("id");
	public void addIsNotNull(String columnName) throws SQLQueryException
	{
		this.addConstantParameter(String.format("%s IS NOT NULL", columnName));
	}
	
}
