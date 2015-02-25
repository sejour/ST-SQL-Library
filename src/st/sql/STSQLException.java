package st.sql;

public class STSQLException extends Exception{
	private int errorCode;
	
	public STSQLException()
	{
		this.errorCode = 50000;
	}
	
	public STSQLException(int errorCode)
	{
		super(String.format("[ERROR:%d]", errorCode));
		this.errorCode = errorCode;
	}
	
	public STSQLException(int errorCode, Throwable exception)
	{
		super(String.format("[ERROR:%d]", errorCode), exception);
		this.errorCode = errorCode;
	}
	
	public STSQLException(int errorCode, String message)
	{
		super(String.format("[ERROR:%d] %s", errorCode, message == null ? "<Unknown>" : message));
		this.errorCode = errorCode;
	}
	
	public STSQLException(int errorCode, String message, Throwable exception)
	{
		super(String.format("[ERROR:%d] %s", errorCode, message == null ? "<Unknown>" : message), exception);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode()
	{
		return this.errorCode;
	}
}
