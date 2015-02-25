package st.sql.mapping;

import st.sql.STSQLException;

/** マッピングエラーが発生した際にスローされる例外 */
public class ResultSetMappingException extends STSQLException {
	public ResultSetMappingException() {
		super();
	}
	
	public ResultSetMappingException(Throwable e) {
		super(50000, e);
	}
	
	public ResultSetMappingException(int errorCode) {
		super(errorCode);
	}

	public ResultSetMappingException(int errorCode, Throwable exception) {
		super(errorCode, exception);
	}

	public ResultSetMappingException(int errorCode, String message) {
		super(errorCode, message);
	}

	public ResultSetMappingException(int errorCode, String message, Throwable exception) {
		super(errorCode, message, exception);
	}

}
