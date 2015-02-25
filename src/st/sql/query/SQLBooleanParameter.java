package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用される論理型のパラメータ
public class SQLBooleanParameter extends SQLParameter {

	private boolean value;
	
	public SQLBooleanParameter(String expression, String value) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = Boolean.parseBoolean(value);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public SQLBooleanParameter(String expression, boolean value)
	{
		super(expression);
		
		this.value = value;
	}
	
	public boolean getValue()
	{
		return this.value;
	}

	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setBoolean(index++, this.value);
		
		return index;
	}

}
