package st.sql.query;

// GROUP BY句を生成するクラス
public final class GroupByQuery extends SequentialQuery {

	public GroupByQuery(String key) throws SQLQueryException
	{
		if (key == null || key.equals("")) {
			throw new SQLQueryException(50000, "ソートの基準となるキーがnullまたは空です。");
		}
		
		this.params.add(new SQLConstantParameter(key));
	}
	
	public GroupByQuery(String key, SQLSortOrder order) throws SQLQueryException
	{
		if (key == null || key.equals("")) {
			throw new SQLQueryException(50000, "ソートの基準となるキーがnullまたは空です。");
		}
		
		this.params.add(new SQLConstantParameter(String.format("%s %s", key, (order == SQLSortOrder.DESC ? "DESC" : "ASC"))));
	}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException 
	{
		if (sql.length() == 0) {
			throw new SQLQueryException(50000, "GROUP BY句はシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		StringBuilder orderByQuery = new StringBuilder(String.format("GROUP BY %s", this.params.get(0).getExpression()));
		
		int size = this.params.size();
		for (int i=1 ; i < size ; ++i)
		{
			orderByQuery.append(String.format(", %s", this.params.get(i).getExpression()));
		}
		
		// sqlに追加
		sql.append(" " + orderByQuery.toString());
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}
	
	// ソートの基準となるキーを追加します。
	public void addKey(String key) throws SQLQueryException
	{
		this.params.add(new SQLConstantParameter(key));
	}
	
	// ソートの基準となるキーを追加します。
	public void addKey(String key, SQLSortOrder order) throws SQLQueryException
	{
		this.params.add(new SQLConstantParameter(String.format("%s %s", key, (order == SQLSortOrder.DESC ? "DESC" : "ASC"))));
	}

}
