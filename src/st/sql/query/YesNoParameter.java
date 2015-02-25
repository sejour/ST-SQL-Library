package st.sql.query;

// yes/noの列挙パラメータ
public class YesNoParameter extends SQLEnumParameter {

	public YesNoParameter(String expression, String value) throws SQLQueryException 
	{
		super(expression, value);
	}

	@Override
	protected boolean valueIsValid(String value) 
	{
		if( value.equals("no") || value.equals("yes") ){
			return true;
		}
		
		return false;
	}
}
