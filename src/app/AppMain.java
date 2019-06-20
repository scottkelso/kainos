package app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

import java.util.Scanner;

import employee_stuff.Employee;

public class AppMain {

	private static Connection conn;
	private final String dbname = "TPS_database";
	
	public static void main(String[] args) 
	{
		// Basic I/O for spike demo
		System.out.println("Welcome to the spike demo!\n\n");
		System.out.println("Select option \n1 - Run sample query \n2 - exit");
		Scanner sc = new Scanner(System.in);
		
		int option = sc.nextInt();
		
		switch(option)
		{
		case 1:
			System.out.println("Running query...\n");
			try {
				conn = getConnection();
				
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(
				                "SELECT emp_no AS `number`, "
				                + "CONCAT_WS (' ', first_name, last_name) AS `name`, "
				                + "salary FROM employees JOIN salaries USING(emp_no) "
				                + "WHERE to_date > NOW() AND salary = 100000");
			
				while (rs.next()) {
				        Employee dbEmp = new Employee(rs.getInt("number"), rs.getFloat("salary"), rs.getString("name"));
				        System.out.println(dbEmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("\nQuery complete!");
			System.out.println("Goodbye!");
			break;
		case 2:
			System.out.println("\nGoodbye!");
			System.exit(0);
			break;
		}
		
		sc.close();
	}

	private static void printElements(Collection col) {
		for (Object x : col) {
			Employee e = (Employee) x;
			System.out.println(e.getName() + " earns " + e.calcPay());
		}
		System.out.println();
	}
	
	private static Connection getConnection() throws IOException, SQLException {
		if (conn != null) {
			return conn;
		}
		
		FileInputStream propsStream = new FileInputStream("employeesdb.properties");
		
		Properties props = new Properties();
		props.load(propsStream);
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String host = props.getProperty("host");
		
		if (user == null || password == null || host == null) {
			throw new IllegalArgumentException(
					"Properties file must exist and must contain user, password, and host properties");
		}
	
		return conn = DriverManager.getConnection("jdbc:mysql://" + host + "/employees?useSSL=false", user, password);
	}

}
