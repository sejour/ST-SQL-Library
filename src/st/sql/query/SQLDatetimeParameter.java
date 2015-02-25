package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

// SQLクエリで使用される2個のバリューを持つ日時型のパラメータ
public class SQLDatetimeParameter extends SQLParameter {

	private Timestamp value;
	
	public SQLDatetimeParameter(String expression, String dateTime) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = Timestamp.valueOf(dateTime);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public Timestamp getValue()
	{
		return this.value;
	}

	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setTimestamp(index++, this.value);
		
		return index;
	}
	
}
