package st.sql.query;



import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用される日付型のパラメータ
public class SQLDateParameter extends SQLParameter {

	private Date value;
	
	public SQLDateParameter(String expression, String date) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = Date.valueOf(date);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public Date getValue()
	{
		return this.value;
	}

	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setDate(index++, this.value);
		
		return index;
	}

}
