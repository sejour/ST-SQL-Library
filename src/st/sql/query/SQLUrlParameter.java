package st.sql.query;



import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用されるURL型のパラメータ
public class SQLUrlParameter extends SQLParameter {

	private URL value;
	
	public SQLUrlParameter(String expression, URL value)
	{
		super(expression);
		
		this.value = value;
	}
	
	public SQLUrlParameter(String expression, String value) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = new URL(value);
		} catch (MalformedURLException e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public URL getValue()
	{
		return this.value;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setURL(index++, this.value);
		
		return index;
	}

}
