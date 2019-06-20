package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import employee_stuff.Employee;
import employee_stuff.SalesEmployee;

public class AppMain {

	private static Connection conn;
	private final String dbname = "TPS_database";
	//update
	private static ArrayList<Employee> employees = new ArrayList<Employee>(); 

	private static Employee loggedin;

	private static ArrayList<SalesEmployee> salesEmployee = new ArrayList<SalesEmployee>();

	
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
		
		int option = 0;
		try {
			option = sc.nextInt();
		} catch (Exception e) {
			System.out.println("That is an invalid entry.");
		}
		
		while (option < 1 || option > 3 ) {
			try {
				sc.next();
				System.out.println("Please enter a number between 1 and 3");
				option = sc.nextInt();
			} catch (Exception e) {
				System.out.println("That is an invalid entry.");
			}
		}
				
		switch(option)
		{
		case 1:
			//HR password 
			Employee newEmp;

			
			System.out.println("Add new employee \n\n");
			
			System.out.println("Enter new employee name: \n");
			String name = sc.next();
			sc.nextLine();
			
			while (name.length() == 0) {
				System.out.println("name must be greater than 0.");
				System.out.println("Enter new employee name: \n");
				name = sc.next();
			}
			
			System.out.println("Enter new employee's address: \n");
			String address = sc.nextLine();
			
			while (address.length() == 0) {
				System.out.println("name must be greater than 0.");
				System.out.println("Enter new employee's address: \n");
				address = sc.nextLine();
			};
			
			System.out.println("Enter new employee's National insurance number without spaces: \n");
			String ninum = sc.nextLine();
			
			while (ninum.length() < 8 || ninum.length() > 13 ) {
				System.out.println("name must be greater than 8 and less than 14.");
				System.out.println("Enter new employee's National insurance number without spaces: \n");
				ninum = sc.nextLine();
			};
			
			
			System.out.println("Enter new employee's IBAN without spaces: \n ");
			String iban = sc.nextLine();
			
			while (iban.length() > 34) {
				System.out.println("IBAN must be 34 characters long");
				System.out.println("Enter new employee's IBAN without spaces: \n ");
				iban = sc.nextLine();
			};
			
			
			
			System.out.println("Enter new employee's BIC without spaces: \n ");
			String bic = sc.nextLine();
			
			while (bic.length() != 11) {
				System.out.println("BIC must be 11 characters long.");
				System.out.println("Enter new employee's BIC without spaces: \n ");
				bic = sc.nextLine();
			};
			
			System.out.println("Enter new employee's starting salary like 12000.0: \n"); 
			float salary = -1;
			
			try {
				 salary = sc.nextFloat();
			}
			catch(Exception e)
			{
				System.out.println("Invalid number entered must be a two digit decimal number.");
				runInterface();
			}
			
			while (salary < 0.0f) {
				try {
					 salary = sc.nextFloat();
				}
				catch(Exception e)
				{
					System.out.println("Invalid number entered must be a two digit decimal number.");
					runInterface();
				}
			}
			
			newEmp = new Employee(salary,name,ninum,address,iban, bic);
			employees.add(newEmp);
			System.out.println("New user details \n" + newEmp);
			
			//SQL stuff
			try {
				addUser(newEmp);
			} catch (SQLIntegrityConstraintViolationException e) {
				System.out.println("WARNING!");
				System.out.println("This national insurance number is already in use!");
				System.out.println("");
				runInterface();
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
		case 4:
			login();
		}
		
		runInterface();
		
		sc.close();
	}
	


	private static void login() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter users id:");
		int id = sc.nextInt();
		
		System.out.println("Enter name:");
		String name = sc.nextLine();
		
		System.out.println(id + name);
		
		boolean found = false;
		for (Object x : getEmployees(conn)) {
			Employee emp = (Employee) x;
			if (emp.getName() == name && emp.getNumber() == id) {
				found = true;
				loggedin = emp;
			}
		}
		
		if (found) {
			System.out.println("Successfully logged in!");
		} else {
			System.out.println("Access denied!");
		}
		
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
//		System.out.println(query);
		int success = st.executeUpdate(query);
//		System.out.println(success);
	}
	
	private static void addSalesEmployee(SalesEmployee newSalEmp) throws SQLException {
		Statement st = conn.createStatement();
		String query = "INSERT INTO salesEmployee (employeeID, commissionRate, salesTotal)\n" + 
		"VALUES ('" + newSalEmp.getNumber() + "', '" + newSalEmp.getCommissionRate() + "', '" + newSalEmp.getSalesTotal() + ");";
		System.out.println(query);
		int success = st.executeUpdate(query);
		System.out.println(success);
	}

	// gets list of employees from sql (CHANGE) 
	private static List getEmployees(Connection conn)
	{
		ArrayList<Employee> emps = new ArrayList<Employee>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
			                "SELECT * FROM employee");
		
			while (rs.next()) {
				Employee dbEmp = new Employee(rs.getFloat("salary"), rs.getString("name"), rs.getString("NiN"), rs.getString("address"), rs.getString("iBan"), rs.getString("bic"));
			    emps.add(dbEmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emps;
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
