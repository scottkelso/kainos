package app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Scanner;

import employee_stuff.Employee;

public class AppMain {

	private static Connection conn;
	private final String dbname = "TPS_database";
	//update
	private static ArrayList<Employee> employees = new ArrayList<Employee>(); 
	
	public static void main(String[] args) 
	{
		runInterface();
	}
	
	//private static void runInterface(int option);
	
	// interface methods 
	private static void runInterface()
	{
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(conn == null) 
		{
			System.out.println("Failed to connect databse exiting.");
			return;
		}
		
		// Basic I/O for spike demo
		System.out.println("Welcome to the sprint 1 demo!\n\n");
		System.out.println("Select option \n1 - Add new employee \n2 - exit");
		getEmployees(conn);
				
		//input 
		Scanner sc = new Scanner(System.in);
		int option = sc.nextInt();
				
		switch(option)
		{
		case 1:
			//HR password 
			Employee newEmp;

			
			System.out.println("Add new employee \n\n");
			
			System.out.println("Enter new employee ID: \n");
			int newID = sc.nextInt();
			
			System.out.println("Enter new employee name: \n");
			String name = sc.nextLine();
			
			System.out.println("Enter new employee's address: \n");
			String address = sc.nextLine();
			
			System.out.println("Enter new employee's National insurance number: \n");
			String ninum = sc.nextLine();
			
			System.out.println("Enter new employee's IBAN/BIC : \n ");
			String bankDetails = sc.nextLine();
			
			System.out.println("Enter new employee's starting salary: \n"); 
			float salary = sc.nextFloat();
			
			newEmp = new Employee(newID,salary,name,ninum,address,bankDetails);
			employees.add(newEmp);
			System.out.println("New user details \n" + newEmp);
			
			//SQL stuff
			
			break;
		case 2:
			System.out.println("\nGoodbye!");
			System.exit(0);
			break;
		}
		
		runInterface();
		
		sc.close();
	}
	
	// gets list of employees from sql (CHANGE) 
	private static void getEmployees(Connection conn)
	{
	
		try {
			
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
			                "SELECT emp_no AS `number`, "
			                + "CONCAT_WS (' ', first_name, last_name) AS `name`, " 
			                + ","
			                + "salary FROM employees JOIN salaries USING(emp_no) "
			                + "WHERE to_date > NOW() AND salary = 100000");
		
			while (rs.next()) {
			       // Employee dbEmp = new Employee(rs.getInt("number"), rs.getFloat("salary"), rs.getString("name"));
			        // employees.add(dbEmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		return conn = DriverManager.getConnection("jdbc:mysql://" + host + "/TPS_database?useSSL=false", user, password);
	}

}
