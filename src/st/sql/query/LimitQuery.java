package st.sql.query;



// LIMIT句を生成します。
public final class LimitQuery extends SequentialQuery {

	public LimitQuery(int count) throws SQLQueryException
	{
		if ( count <= 0 ) {
			throw new SQLQueryException(40002, "取得件数が0以下です。");
		}
		
		this.params.add(new SQLIntParameter("?", count));
	}
	
	public LimitQuery(int count, int offset) throws SQLQueryException
	{
		if ( count <= 0 ) {
			throw new SQLQueryException(40002, "取得件数が0以下です。");
		}
		
		this.params.add(new SQLTwoIntParameter("?, ?", offset, count));
	}
	
	@Override
	protected void generateTemplate(StringBuilder sql) throws SQLQueryException
	{
		if (sql.length() == 0) {
			throw new SQLQueryException(50000, "LIMIT句はシーケンシャルクエリの先頭として使用することはできません。");
		}
		
		// sqlに追加
		sql.append(String.format(" LIMIT %s", this.params.get(0).getExpression()));
		
		// シーケンスのテンプレートを生成
		if( this.sequence != null ) {
			this.sequence.generateTemplate(sql);
		}
	}
}
