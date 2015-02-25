package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリで使用される2個のバリューを持つ文字列型のパラメータ
public class SQLTwoStringParameter extends SQLParameter {

	private String firstValue;
	private String secondValue;
	
	public SQLTwoStringParameter(String expression, String firstValue, String secondValue) throws SQLQueryException
	{
		super(expression);
		
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	public String getFirstValue()
	{
		return this.firstValue;
	}

	public String getSecondValue()
	{
		return this.secondValue;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLQueryException, SQLException
	{
		statement.setString(index++, this.firstValue);
		
		statement.setString(index++, this.secondValue);
		
		return index;
	}
	
}
