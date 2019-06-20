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
import java.util.Collections;
import java.util.Properties;

import employee_stuff.Employee;

public class Blah {


	private static Connection conn;

	public static void main(String[] args) {
		Employee a = new Employee(1, 4000, "A. Smith");
		Employee b = new Employee(2, 3000, "B. Jones");
		Employee c = new Employee(3, 2000, "C. Doe");
		Employee d = new Employee(4, 1000, "D. Wong");
		
		String s = "Hello world!";
		
		ArrayList emps = new ArrayList();
		emps.add(a);
		emps.add(b);
		emps.add(c);
		emps.add(d);
		printElements(emps);
		
		Collections.sort(emps);
		printElements(emps);

		System.out.println("Employee with highest salary is " + Collections.max(emps));
		System.out.println("Employee with lowest salary is " + Collections.min(emps));
		
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
