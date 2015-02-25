package st.sql.query;



// 動的パラメータを有するシーケンシャルなSQLクエリを表します。
public abstract class SequentialParametersQuery extends SequentialQuery {

	public SequentialParametersQuery() {}

	public void addParameter(SQLParameter param)
	{
		if (param == null) throw new NullPointerException("param is null."); 
		
		this.params.add(param);
	}
	
	public void addConstantParameter(String expression) throws SQLQueryException
	{
		this.params.add(new SQLConstantParameter(expression));
	}
	
	public void addStringParameter(String expression, String value) throws SQLQueryException
	{
		this.params.add(new SQLStringParameter(expression, value));
	}

	public void addStringParameter(String expression, String value, boolean isRequire) throws SQLQueryException
	{
		// オプションであればそのまま返す
		if ((!isRequire) && value == null) return;
		
		this.params.add(new SQLStringParameter(expression, value));
	}
	
	public void addIntParameter(String expression, String value) throws SQLQueryException
	{
		this.params.add(new SQLIntParameter(expression, value));
	}
	
	public void addIntParameter(String expression, String value, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && value == null) return;
		
		this.params.add(new SQLIntParameter(expression, value));
	}
	
	public void addDoubleParameter(String expression, String value) throws SQLQueryException
	{
		this.params.add(new SQLDoubleParameter(expression, value));
	}
	
	public void addDoubleParameter(String expression, String value, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && value == null) return;
		
		this.params.add(new SQLDoubleParameter(expression, value));
	}
	
	public void addBooleanParameter(String expression, String value) throws SQLQueryException
	{
		this.params.add(new SQLBooleanParameter(expression, value));
	}

	public void addBooleanParameter(String expression, String value, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && value == null) return;
		
		this.params.add(new SQLBooleanParameter(expression, value));
	}
	
	public void addUrlParameter(String expression, String url) throws SQLQueryException
	{
		this.params.add(new SQLUrlParameter(expression, url));
	}

	public void addUrlParameter(String expression, String url, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && url == null) return;
		
		this.params.add(new SQLUrlParameter(expression, url));
	}
	
	public void addDateParameter(String expression, String date) throws SQLQueryException
	{
		this.params.add(new SQLDateParameter(expression, date));
	}

	public void addDateParameter(String expression, String date, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && date == null) return;
		
		this.params.add(new SQLDateParameter(expression, date));
	}
	
	public void addTimeParameter(String expression, String time) throws SQLQueryException
	{
		this.params.add(new SQLTimeParameter(expression, time));
	}

	public void addTimeParameter(String expression, String time, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && time == null) return;
		
		this.params.add(new SQLTimeParameter(expression, time));
	}
	
	public void addDatetimeParameter(String expression, String dateTime) throws SQLQueryException
	{
		this.params.add(new SQLDatetimeParameter(expression, dateTime));
	}

	public void addDatetimeParameter(String expression, String dateTime, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && dateTime == null) return;
		
		this.params.add(new SQLDatetimeParameter(expression, dateTime));
	}
	
	public void addTimestampParameter(String expression, String timeStamp) throws SQLQueryException
	{
		this.params.add(new SQLTimestampParameter(expression, timeStamp));
	}

	public void addTimestampParameter(String expression, String timeStamp, boolean isRequire) throws SQLQueryException
	{
		if ((!isRequire) && timeStamp == null) return;
		
		this.params.add(new SQLTimestampParameter(expression, timeStamp));
	}
	
	public void addEnumParameter(SQLEnumParameter enumeration) throws SQLQueryException
	{
		if (enumeration == null) throw new NullPointerException("param is null.");
		
		this.params.add(enumeration);
	}
	
}
