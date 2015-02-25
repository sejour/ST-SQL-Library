package st.sql.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import st.sql.STSQLException;
import st.sql.query.DeleteQuery;
import st.sql.query.InsertQuery;
import st.sql.query.SelectQuery;
import st.sql.query.UpdateQuery;


/**
 	データベース
 */
public class Database {

	// コネクタドライバ名
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	
	private String hostName;
	private String databaseName;
	private String userName;
	private String password;
	private String charSet;
	private boolean useUnicode;
	
	// 接続URL
	private String connectionUrl;
	
	// データベースのコネクション
	private Connection connection;
	
	public Database(String connectionUrl) throws STSQLException
	{
		if (connectionUrl == null || connectionUrl.equals("")) throw new STSQLException(50000, "ConnectionUrl is null or empty.");
		
		this.connectionUrl = connectionUrl;
	}
	
	public Database(String hostName, String databaseName, String userName, String password) throws ClassNotFoundException, SQLException, STSQLException
	{
		this(hostName, databaseName, userName, password, null, true);
	}
		 
	public Database(String hostName, String databaseName, String userName, String password, String charSet, boolean useUnicode) throws STSQLException
	{
		if (hostName == null || hostName.equals("")) throw new STSQLException(50000, "Host name is null or empty.");
		if (databaseName == null || databaseName.equals("")) throw new STSQLException(50000, "Database name is null or empty.");
		
		this.hostName = hostName;
		this.databaseName = databaseName;
		this.userName = userName;
		this.password = password;
		this.charSet = charSet;
		this.useUnicode = useUnicode;
		
		//URL生成
		StringBuilder urlBuilder = new StringBuilder(String.format("jdbc:mysql://%s/%s", this.hostName, this.databaseName));
		boolean firstParam = true;
		if (this.userName != null) {
			urlBuilder.append(String.format("%suser=%s", (firstParam ? "?" : "&"), this.userName));
			firstParam = false;
		}
		if (this.password != null) {
			urlBuilder.append(String.format("%spassword=%s", (firstParam ? "?" : "&"), this.password));
			firstParam = false;
		}
		if (this.useUnicode == true) {
			urlBuilder.append(String.format("%suseUnicode=true", (firstParam ? "?" : "&")));
			firstParam = false;
		}
		if (this.charSet != null) {
			urlBuilder.append(String.format("%scharacterEncoding=%s", (firstParam ? "?" : "&"), this.charSet));
			firstParam = false;
		}
	
		this.connectionUrl = urlBuilder.toString();
	}
	
	// データベースへのコネクションを確立します。
	public void connect() throws ClassNotFoundException, SQLException, STSQLException
	{
		if (this.connection != null) throw new STSQLException(50000);
		
		//ドライバのロード
		Class.forName(DRIVER_NAME);
		
		//データベースに接続
		this.connection = DriverManager.getConnection(this.connectionUrl);
	}
	
	// ホスト名
	public String getHostName()
	{
		return this.hostName;
	}
	
	// データベース名
	public String getDataBaseName()
	{
		return this.databaseName;
	}
	
	// コネクションユーザーのユーザー名を取得します。
	public String getUserName()
	{
		return this.userName;
	}
	
	// コネクションユーザーのユーザーパスを取得します。
	protected String getPassword()
	{
		return this.password;
	}
	
	// Unicodeを使用するかどうか。
	public boolean isUsingUnicode()
	{
		return this.useUnicode;
	}
	
	// 使用する文字セットを取得します。
	public String getCharSet()
	{
		return this.charSet;
	}
	
	// コネクションURLを取得する
	public String getConnectionUrl()
	{
		return this.connectionUrl;
	}
	
	// データベースのコネクションを取得します。
	public Connection getConnection() throws STSQLException
	{
		if( this.connection == null ){
			throw new STSQLException(50000);
		}
		
		return this.connection;
	}
	
	// SQLステートメントを生成します。
	public Statement createStatement() throws SQLException
	{
		return this.connection.createStatement();
	}
	
	// SQLステートメントをプリペアします。
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return this.connection.prepareStatement(sql);
	}
	
	// ストアドプロシージャステートメントをプリペアします。
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return this.connection.prepareCall(sql);
	}
	
	// SELECT句から成るSQLクエリを実行してResultSetを取得します。
	public ResultSet executeQuery(String sql) throws SQLException
	{
		PreparedStatement statement = this.connection.prepareStatement(sql);
		
		return statement.executeQuery();
	}
	

	// SELECT句以外のSQLクエリを実行します。
	public void executeUpdate(String sql) throws SQLException, STSQLException
	{
		PreparedStatement statement = this.connection.prepareStatement(sql);
		
		if ( statement.executeUpdate() == 0 ) {
			throw new STSQLException(40000);
		}
	}
	
	// SELECT句から成るのSQLクエリを実行します。
	public ResultSet select(SelectQuery sql) throws SQLException, STSQLException
	{
		PreparedStatement statement = sql.generateStatement(this.connection);
		
		return statement.executeQuery();
	}
	
	// UPDATE句から成るSQLクエリを実行します。
	public void update(UpdateQuery sql) throws SQLException, STSQLException
	{
		PreparedStatement statement = sql.generateStatement(this.connection);
		
		if ( statement.executeUpdate() == 0 ) {
			throw new STSQLException(40000);
		}
	}
	
	// INSERT句から成るSQLクエリを実行します。
	public void insert(InsertQuery sql) throws SQLException, STSQLException
	{
		PreparedStatement statement = sql.generateStatement(this.connection);
		
		if ( statement.executeUpdate() == 0 ) {
			throw new STSQLException(40000);
		}
	}
	
	// DELETE句から成るSQLクエリを実行します。
	public void delete(DeleteQuery sql) throws SQLException, STSQLException
	{
		PreparedStatement statement = sql.generateStatement(this.connection);
		
		if ( statement.executeUpdate() == 0 ) {
			throw new STSQLException(40000);
		}
	}
	
	// データベースをコミットします。
	public void commit() throws SQLException
	{
		this.connection.commit();
	}
	
	// データベースへのコネクションを破棄します。
	public void close() throws SQLException
	{
		if( this.connection != null ){
			this.connection.close();
			this.connection = null;
		}
	}
	
	// データベースのコネクションが確立されているかどうか
	public boolean isConnected()
	{
		return this.connection != null;
	}
	
}


