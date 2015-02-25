package st.sql.query;

import st.sql.STSQLException;

public class SQLQueryException extends STSQLException {
	public SQLQueryException()
	{
		super();
	}
	
	public SQLQueryException(int errorCode)
	{
		super(errorCode);
	}
	
	public SQLQueryException(int errorCode, Throwable exception)
	{
		super(errorCode, exception);
	}
	
	public SQLQueryException(int errorCode, String message)
	{
		super(errorCode, message);
	}
	
	public SQLQueryException(int errorCode, String message, Throwable exception)
	{
		super(errorCode, message, exception);
	}
}

