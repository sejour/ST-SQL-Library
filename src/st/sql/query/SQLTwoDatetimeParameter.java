package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

// SQLクエリで使用される2個のバリューを持つ日時型のパラメータ
public class SQLTwoDatetimeParameter extends SQLParameter {

	private Timestamp firstValue;
	private Timestamp secondValue;
	
	public SQLTwoDatetimeParameter(String expression, String firstDatetime, String secondDatetime) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.firstValue = Timestamp.valueOf(firstDatetime);
			this.secondValue = Timestamp.valueOf(secondDatetime);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}

	public Timestamp getFirstValue()
	{
		return this.firstValue;
	}

	public Timestamp getSecondValue()
	{
		return this.secondValue;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLQueryException, SQLException
	{
		statement.setTimestamp(index++, this.firstValue);
		
		statement.setTimestamp(index++, this.secondValue);
		
		return index;
	}

}
