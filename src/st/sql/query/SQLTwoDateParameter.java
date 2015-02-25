package st.sql.query;



import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリで使用される2個のバリューを持つ日付型のパラメータ
public class SQLTwoDateParameter extends SQLParameter {

	private Date firstValue;
	private Date secondValue;
	
	public SQLTwoDateParameter(String expression, String firstDate, String secondDate) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.firstValue = Date.valueOf(firstDate);
			this.secondValue = Date.valueOf(secondDate);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}

	public Date getFirstValue()
	{
		return this.firstValue;
	}

	public Date getSecondValue()
	{
		return this.secondValue;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLQueryException, SQLException
	{
		statement.setDate(index++, this.firstValue);
		
		statement.setDate(index++, this.secondValue);
		
		return index;
	}

}
