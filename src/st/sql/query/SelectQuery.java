package st.sql.query;

// シーケンシャルクエリから成るSELECT句を表します。
public final class SelectQuery extends SequentialQuery {

	// ステートメント
	private String statement;
	
	// SelectQueryのインスタンスを生成します。
	public SelectQuery(String statement) throws SQLQueryException 
	{
		if (statement == null || statement.equals("")) {
			throw new SQLQueryException(50000, "ステートメントがnullまたは空です。");
		}
		
		this.statement = statement;
	}
	
	// SelectQueryのインスタンスを生成します。
	// appendQueryにはテーブルのエイリアスや結合句を指定します。
	public SelectQuery(String selectQuery, String appendQuery) throws SQLQueryException 
	{
		if (selectQuery == null || selectQuery.equals("")) {
			throw new SQLQueryException(50000, "SELECT句がnullまたは空です。");
		}
		
		if (appendQuery == null || appendQuery.equals("")) {
			throw new SQLQueryException(50000, "JOIN句がnullまたは空です。");
		}
		
		this.statement = String.format("%s %s", selectQuery, appendQuery);
	}

	// SelectQueryのインスタンスを生成します。
	public SelectQuery(String selectQuery, String[] joinQuerys) throws SQLQueryException 
	{
		if (selectQuery == null || selectQuery.equals("")) {
			throw new SQLQueryException(50000, "SELECT句がnullまたは空です。");
		}
		
		StringBuilder builder = new StringBuilder(selectQuery);
		for (String line : joinQuerys) {
			if (!line.equals("")) builder.append(String.format(" %s", line));
		}
		
		this.statement = builder.toString();
	}
	
	public void appendConstantQuery(String query)
	{
		this.statement = String.format("%s %s", this.statement, query);
	}
	
	// ステートメントを取得します。
	public String getStatement()
	{
		return this.statement;
	}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException
	{
		if (sql.length() != 0) {
			throw new SQLQueryException(50000, "SELECT句はシーケンスの先頭に配置する必要があります。");
		}
		
		// sqlに追加
		sql.append(this.statement);
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}

}
