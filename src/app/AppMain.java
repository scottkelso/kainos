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

import employee_stuff.DepartmentEnum;
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
		System.out.println("Select option \n1 - Add new employee \n2 - Department Report\n3 - exit \n4 - Login \n5 - See earnings \n6 - Add sales employee \n7 - See highest earning sales employee");
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
			
			System.out.println("Enter new employee's National insurance number without spaces: \n");
			String ninum = sc.nextLine();
			

//			while (ninum.length() > 34 || !checkNiNUniquness(ninum)) {
//				if (ninum.length() > 34) {
//					System.out.println("National insurence number has to be between 8 and 13 characters and must have the correct suffix letter.");
//				} else if (!checkNiNUniquness(ninum)) {
//					System.out.println("That national insurance number is not unique!");
//				}
//				System.out.println("Enter new employee's National insurance number without spaces: \n");
//				ninum = sc.nextLine();
//			};
//			
			while (ninum.length() > 34) {
				System.out.println("National insurence number has to be between 8 and 13 characters and must have the correct suffix letter.");
				System.out.println("Enter new employee's National insurance number without spaces: \n");
				ninum = sc.nextLine();
			};
			
			
			
			System.out.println("Enter new employee's IBAN without spaces: \n ");
			String iban = sc.nextLine();
			
//			while (iban.length() > 34 || !checkIbanUniquness(iban)) {
//				if (iban.length() > 34) {
//					System.out.println("IBAN must be 34 characters long");
//				} else if (!checkIbanUniquness(iban)) {
//					System.out.println("That IBAN code number is not unique!");
//				}
//				System.out.println("Enter new employee's IBAN without spaces: \n ");
//				iban = sc.nextLine();
//			};
			
			while (iban.length() > 34) {
				System.out.println("IBAN must be 34 characters long");
				System.out.println("Enter new employee's IBAN without spaces: \n ");
				iban = sc.nextLine();
			};
			

			System.out.println("Enter new employee's BIC without spaces: \n ");
			String bic = sc.nextLine();
			
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
			
			while (salary < 0.0f && salary > 1e8) {
				try {
					 salary = sc.nextFloat();
				}
				catch(Exception e)
				{
					System.out.println("Invalid number entered must be a two digit decimal number less than 100 million.");
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
				System.out.println("The national insurance or IBAN code is already in use!");
				System.out.println("");
				e.printStackTrace();
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
		case 5:
			for(Employee e : employees)
			{
				if(e.getDepartment() != DepartmentEnum.SALES)
				{
					System.out.println("Employee: " + e.getName() + " earns £" + ((e.getSalary())* 0.3) + ".");
				}
			}
			
			for(SalesEmployee e : salesEmployee)
			{
				System.out.println("Employee: " + e.getName() + " earns £" + ((e.getSalary() + e.getSalesTotal() * e.getCommissionRate())* 0.3) + ".");
			}
			break;
		case 7:
			int highestEarned = 0; //array index
			int current = 0;
			for(SalesEmployee e : salesEmployee)
			{
				current ++;
				if(e.getSalesTotal() > salesEmployee.get(0).getSalesTotal())
				{
					highestEarned = current;
				}
				
			}
			
			System.out.println("Employee: " + salesEmployee.get(highestEarned).getName() + " earned the most at £" + salesEmployee.get(highestEarned).getSalesTotal() + " worth of sales.");
			break;
		case 6:
			SalesEmployee newSalesEmp;

			
			System.out.println("Add new sales employee \n\n");
			
			System.out.println("Enter employee ID: \n");
			String number = sc.next();
			sc.nextLine();
			
			System.out.println("Enter sales employee commission rate: \n");
			String commissionRate = sc.nextLine();
			
			System.out.println("Enter sales employee's total sales: \n");
			String totalSales = sc.nextLine();
		
			int numberInt = Integer.parseInt(number);
		    float cR = Float.valueOf(commissionRate.trim()).floatValue();
		    float sT = Float.valueOf(totalSales.trim()).floatValue();
			
			newSalesEmp = new SalesEmployee(numberInt, cR, sT);
			
			//SQL stuff
			try {
				addSalesUser(newSalesEmp, numberInt);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;

		}
		
		
		runInterface();
		
		sc.close();
	}



	private static boolean checkIbanUniquness(String iban) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM employee WHERE iBan = '" + iban + "';");
			return (rs == null);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return false;
	}

	private static boolean checkNiNUniquness(String ninum) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM employee WHERE NiN = '" + ninum + "';");
			return (rs == null);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return false;
	}

	private static boolean ninVal(String ninum) {
		String x = ninum.substring(ninum.length() - 1).toLowerCase();
		return x.equals("a") || x.equals("b") || x.equals("c") || x.equals("d") || x.equals("f") || x.equals("m") || x.equals("p") ;
	}

	private static void login() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter users id:");
		int id = sc.nextInt();
		
		System.out.println("Enter name:");
		String name = sc.nextLine();
		
		System.out.println(id + name);
		
		boolean found = false;
		for (Object x : employees) {
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
                    Department dept = new Department(rs.getString("department"), rs.getInt("employeeCount"));
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
	
	private static void addSalesUser(SalesEmployee newSalesEmp, int employeeNumber) throws SQLException {
		getEmployees(conn);
		for (Object x : employees) {
			Employee emp = (Employee) x;
			if (emp.getNumber() == employeeNumber) {
				Statement st = conn.createStatement();
				String query = "INSERT INTO salesEmployee (employeeID, commissionRate, salesTotal)\n" + 
				"VALUES ('" + newSalesEmp.getNumber() + "', '" + newSalesEmp.getCommissionRate() + "', '" + newSalesEmp.getSalesTotal() + "', '" + newSalesEmp.getIban() + ");";
				int success = st.executeUpdate(query);
				System.out.println(success);
			}
		}
		
		

	}

	// gets list of employees from sql (CHANGE) 
	private static void getEmployees(Connection conn)
	{
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
			                "SELECT * FROM employee");
			
			while (rs.next() ) {
//				new Employee(salary,name,ninum,address,iban, bic);
				Employee dbEmp = new Employee(rs.getFloat("salary"), rs.getString("name"), rs.getString("NiN"), rs.getString("address"), rs.getString("iBan"), rs.getString("bic"));
			    dbEmp.setNumber(rs.getInt("employeeID"));
				dbEmp.setDepartment(DepartmentEnum.valueOf(rs.getString("department").toUpperCase()));
				
				employees.add(dbEmp);
			}
			
			rs.close();
			
			for(Employee dbEmp : employees)
			{
				if(dbEmp.getDepartment() == DepartmentEnum.SALES)
			    {
			    	//SalesEmployee newSales = new (rs.getFloat("salary"), rs.getString("name"), rs.getString("NiN"), rs.getString("address"), rs.getString("iBan"), rs.getString("bic"));
			    	float commissionRate, salesTotal;
			    	ResultSet sales = st.executeQuery("SELECT * FROM salesEmployee WHERE employeeID = " + dbEmp.getNumber() + " ;");
			    
			    	sales.next();
			    	salesTotal = sales.getFloat("salesTotal");
			    	commissionRate = sales.getFloat("commissionRate");
			    	
			    	//new sales employeee
			    	SalesEmployee newSales = new SalesEmployee(dbEmp.getSalary(), dbEmp.getName(), dbEmp.getNationalInsurance(), dbEmp.getAddress(), dbEmp.getIban(), dbEmp.getBic(),commissionRate,salesTotal );
			    	newSales.setCommissionRate((float) commissionRate);
			    	newSales.setSalesTotal(salesTotal);
			    	
			    	salesEmployee.add(newSales);
			    	sales.close();
			    }	
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
