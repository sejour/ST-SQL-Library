package st.sql.query;



// 変数を持つSQLクエリを生成します。
public class ValiableQuery extends SequentialParametersQuery {

	private String statement;
	
	public ValiableQuery(String statement) throws SQLQueryException
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
			throw new SQLQueryException(50000, "ValiableQueryはシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		if (this.params.isEmpty()) {
			throw new SQLQueryException(40002, "ValiableQueryにパラメータが存在しません。");
		}
		
		// sqlに追加
		sql.append(" " + statement);
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}

}
