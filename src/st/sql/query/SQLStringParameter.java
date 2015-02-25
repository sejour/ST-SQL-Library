package st.sql.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;



// SQLクエリに使用される文字列型のパラメータ
public class SQLStringParameter extends SQLParameter {
	
	private String value;
	
	public SQLStringParameter(String expression, String value) throws SQLQueryException
	{
		super(expression);
		
		if( value == null || value.equals("") )
		{
			throw new SQLQueryException(40002);
		}
		
		this.value = value;
	}
	
	public String getValue()
	{
		return this.value;
	}

	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException, SQLQueryException
	{
		statement.setString(index++, this.value);
		
		return index;
	}
	
}
