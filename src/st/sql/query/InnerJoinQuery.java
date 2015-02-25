package st.sql.query;

// 動的な INNER JOIN 句を生成する
public final class InnerJoinQuery extends SequentialParametersQuery {

	// 結合するテーブル
	private String tableName;
	
	public InnerJoinQuery(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException {
		if (sql.length() == 0) {
			throw new SQLQueryException(50000, "INNER JOIN句はシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		// パラメータリストが空でなければWHERE句を生成
		if ( !this.params.isEmpty() ) 
		{
			StringBuilder whereQuery = new StringBuilder(String.format("INNER JOIN %s ON %s", this.tableName, this.params.get(0).getExpression()));
			
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

}
