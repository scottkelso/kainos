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
		System.out.println("Select option \n1 - Add new employee \n2 - Department Report\n3 - exit");
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
			
			System.out.println("Enter new employee name: \n");
			String name = sc.next();
			sc.nextLine();
			
			System.out.println("Enter new employee's address: \n");
			String address = sc.nextLine();
			
			System.out.println("Enter new employee's National insurance number: \n");
			String ninum = sc.nextLine();
			
			System.out.println("Enter new employee's IBAN : \n ");
			String iban = sc.nextLine();
			
			System.out.println("Enter new employee's BIC : \n ");
			String bic = sc.nextLine();
			
			System.out.println("Enter new employee's starting salary: \n"); 
			float salary = sc.nextFloat();
			
			newEmp = new Employee(salary,name,ninum,address,iban, bic);
			employees.add(newEmp);
			System.out.println("New user details \n" + newEmp);
			
			//SQL stuff
			try {
				addUser(newEmp);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case 2:
			departmentReport();
			
			break;
		case 3:
			System.out.println("\nGoodbye!");
			System.exit(0);
			break;
		}
		
		runInterface();
		
		sc.close();
	}
	
	private static void departmentReport() {
		try {

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("CALL employeesPerDepartment()");

      

            while (rs.next()) {

                    Department dept = new Department(rs.getString("department"), rs.getInt("COUNT(*)"));
                    System.out.println(dept);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
		
	}

	private static void addUser(Employee newEmp) throws SQLException {
		
		Statement st = conn.createStatement();
		String query = "INSERT INTO employee (name, address, NiN, iBan, bic, salary, department)\n" + 
		"VALUES ('" + newEmp.getName() + "', '" + newEmp.getAddress() + "', '" + newEmp.getNationalInsurance() + "', '" + newEmp.getIban() + "', '"
				+ newEmp.getBic() + "', " + newEmp.getSalary() + ", 'hr');";
		System.out.println(query);
		int success = st.executeUpdate(query);
		System.out.println(success);
	}

	// gets list of employees from sql (CHANGE) 
	private static void getEmployees(Connection conn)
	{
	
		try {
			
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
			                "SELECT * FROM employee");
		
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
