package st.sql.query;

// コンスタントなSQLクエリを生成します。
public final class ConstantQuery extends SequentialQuery {

	private String statement;
	
	public ConstantQuery(String statement) throws SQLQueryException
	{
		if (statement == null || statement.equals("")) {
			throw new SQLQueryException(50000, "ステートメントがnullまたは空です。");
		}
		
		this.statement = statement;
	}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException 
	{
		if (sql.length() == 0) {
			throw new SQLQueryException(50000, "ConstantQueryはシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		// sqlに追加
		sql.append(" " + statement);
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}

}
