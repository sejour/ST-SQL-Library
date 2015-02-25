package st.sql.query;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

// SQLクエリで使用される複数個の文字列型のバリューから成るパラメータ
public class SQLSeveralStringParameter extends SQLParameter {

	private List<String> values;
	
	public SQLSeveralStringParameter(String expression, List<String> values) throws SQLQueryException
	{
		super(expression);
		
		if (values == null || values.isEmpty()) {
			throw new SQLQueryException(40002, "バリューが存在しません。");
		}
		
		this.values = values;
	}
	
	@Override
	public int putValues(PreparedStatement statement, int index) throws SQLException 
	{
		for (String val : this.values) {
			statement.setString(index++, val);
		}
		
		return index;
	}

}
