package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用される倍精度実数型のパラメータ
public class SQLDoubleParameter extends SQLParameter{

	private double value;
	
	public SQLDoubleParameter(String expression, String value) throws SQLQueryException
	{
		super(expression);
		
		try {
			this.value = Double.parseDouble(value);
		} catch (Throwable e) {
			throw new SQLQueryException(40002, e);
		}
	}
	
	public SQLDoubleParameter(String expression, double value)
	{
		super(expression);
		
		this.value = value;
	}

	public double getValue()
	{
		return getValue();
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		statement.setDouble(index++, this.value);
		
		return index;
	}

}
