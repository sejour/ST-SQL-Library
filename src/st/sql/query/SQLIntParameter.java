package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用される整数型のパラメータ
public class SQLIntParameter extends SQLParameter {

	private int value;
	
	public SQLIntParameter(String expression, String value) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = Integer.parseInt(value);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public SQLIntParameter(String expression, int value)
	{
		super(expression);
		
		this.value = value;
	}

	public int getValue()
	{
		return this.value;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setInt(index++, this.value);
		
		return index;
	}
	
}
