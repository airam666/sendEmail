/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @brief clase que establece conexion con base de datos mysql. usa el mysql-connector-java-8.0.11
 * @author fpalacios
 */
public class ConnectionMSQL {

    private static ConnectionMSQL instance;
    private Connection conn = null;
    private String hostnameConn = "";
    private String portConn = "";
    private String dbnameConn = "";
    private String usernameConn = "";
    private String passwordConn = "";
    
    protected PreparedStatement preparedStmnt = null;
    private String query = "";
    private ResultSet rs = null;
    //    
    //Statement stmt = conn.createStatement() ;
    //String query = "select columnname from tablename ;" ;
    //ResultSet rs = stmt.executeQuery(query) ;

    private ConnectionMSQL() {
    }

    public static ConnectionMSQL getInstance() {
        if (instance == null) {
            instance = new ConnectionMSQL();
        }
        return instance;
    }

    public void setMSQLData(String hostname, String port, String dbname, String username, String password) {
        hostnameConn = hostname;
        portConn = port;
        dbnameConn = dbname;
        usernameConn = username;
        passwordConn = password;

    }

    public Connection getConnection() {
        //this.conn = DriverManager.getConnection("jdbc:mysql://hostname:port/dbname", "username", "password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("jdbc:mysql://"
                    + hostnameConn
                    + ":" + portConn
                    + "/" + dbnameConn + ", "+
                    usernameConn + ", " +
                    passwordConn );
            this.conn = DriverManager.getConnection("jdbc:mysql://"
                    + hostnameConn
                    + ":" + portConn
                    + "/" + dbnameConn,
                    usernameConn,
                    passwordConn);
        } catch (SQLException | ClassNotFoundException e) {
            e.getCause();
        }
        return conn;
    }

    protected void closeConnection() throws SQLException{
            conn.close();
    }
    

}
