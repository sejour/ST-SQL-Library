package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;

// SQLクエリに使用される列挙型のパラメータ
public abstract class SQLEnumParameter extends SQLStringParameter {
	
	public SQLEnumParameter(String expression, String value) throws SQLQueryException
	{
		super(expression, value);
	}

	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLQueryException, SQLException 
	{
		if( !this.valueIsValid(this.getValue()) ) {
			throw new SQLQueryException(40002);
		}
		
		return super.putValues(statement, index);
	}
	
	// 有効な列挙値であるか判別します。
	protected abstract boolean valueIsValid(String value);
	
}
