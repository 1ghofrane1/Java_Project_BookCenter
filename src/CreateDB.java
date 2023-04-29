package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB {
	private static Connection connection;
	
	private CreateDB()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/BookCenter", "root", "");
			System.out.println("Connection etablie");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static Connection getInstance()
	{
		if(connection==null)
			new CreateDB();
		
		return connection;
		
	}
	

}