/**
 * 
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author fpalacios
 * @brief class that generates csv files from a query
 */
public class CSVWriter {

	/**
	 * 
	 */
	public CSVWriter() {
		// TODO Auto-generated constructor stub
	}
	
	public static void convertToCsv(ResultSet rs, String filename) throws SQLException, FileNotFoundException {
        PrintWriter csvWriter = new PrintWriter(new File(filename)) ;
        ResultSetMetaData meta = rs.getMetaData(); 
        int numberOfColumns = meta.getColumnCount() ; 
        String dataHeaders = "\"" + meta.getColumnName(1) + "\"" ; 
        for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) { 
                dataHeaders += ",\"" + meta.getColumnName(i).replaceAll("\"","\\\"") + "\"" ;
        }
        csvWriter.println(dataHeaders) ;
        while (rs.next()) {
            String row = "\"" + rs.getString(1).replaceAll("\"","\\\"") + "\""  ; 
            for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
                row += ",\"" + rs.getString(i).replaceAll("\"","\\\"") + "\"" ;
            }
        csvWriter.println(row) ;
        }
        csvWriter.close();
    }

}
