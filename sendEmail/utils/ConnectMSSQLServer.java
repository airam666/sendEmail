package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.*; 

/**
 * @brief clase que establece conexion con base de datos SQL Server.
 * @author fpalacios
 */
public class ConnectMSSQLServer {


	private static ConnectMSSQLServer instance;
    private Connection conn = null;
    private String hostnameConn = "";
    private String portConn = "";
    private String dbnameConn = "";
    private String usernameConn = "";
    private String passwordConn = "";
    protected PreparedStatement preparedStmnt = null;
	private ConnectMSSQLServer() {
    }

    public static ConnectMSSQLServer getInstance() {
        if (instance == null) {
            instance = new ConnectMSSQLServer();
        }
        return instance;
    }
    
    public void setMSSQLServerData(String hostname, String port, String dbname, String username, String password) {
        hostnameConn = hostname;
        portConn = port;
        dbnameConn = dbname;
        usernameConn = username;
        passwordConn = password;

    }
    public Connection getConnection() {
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("jdbc:sqlserver://"
                    + hostnameConn
                    + ":" + portConn
                    + "/" + dbnameConn + ", "+
                    usernameConn + ", " +
                    passwordConn );
            String connectionUrl = "jdbc:sqlserver://"+hostnameConn+":"+portConn+";" +  
       	         "databaseName="+dbnameConn+";user="+usernameConn+";password="+passwordConn; 
            
            this.conn = DriverManager.getConnection(connectionUrl);  
            
        } catch (Exception  e) {
            e.getCause();
        }
        return conn;
    }

    protected void closeConnection() throws SQLException{
            conn.close();
    }

}
