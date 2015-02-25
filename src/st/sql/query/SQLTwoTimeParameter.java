package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

// SQLクエリで使用される2個のバリューを持つ時間型のパラメータ
public class SQLTwoTimeParameter extends SQLParameter {

	private Time firstValue;
	private Time secondValue;
	
	public SQLTwoTimeParameter(String expression, String firstTime, String secondTime) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.firstValue = Time.valueOf(firstTime);
			this.secondValue = Time.valueOf(secondTime);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}

	public Time getFirstValue()
	{
		return this.firstValue;
	}

	public Time getSecondValue()
	{
		return this.secondValue;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLQueryException, SQLException
	{
		statement.setTime(index++, this.firstValue);
		
		statement.setTime(index++, this.secondValue);
		
		return index;
	}

}
