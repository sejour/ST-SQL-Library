package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

// SQLクエリで使用される2個のバリューを持つタイムスタンプ型のパラメータ
public class SQLTwoTimestampParameter extends SQLParameter {

	private Timestamp firstValue;
	private Timestamp secondValue;
	
	public SQLTwoTimestampParameter(String expression, String firstTimestamp, String secondTimestamp) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.firstValue = Timestamp.valueOf(firstTimestamp);
			this.secondValue = Timestamp.valueOf(secondTimestamp);
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
