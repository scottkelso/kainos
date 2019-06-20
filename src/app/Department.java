package app;

public class Department {
	
	String departmentName;
	int employeeNumber;
	
	public Department(String departmentName, int employeeNumber) {
		super();
		this.departmentName = departmentName;
		this.employeeNumber = employeeNumber;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	
	@Override
	public String toString() {
		return "Department name is " + departmentName + ", number of employees is " + employeeNumber + "";
	}

}
