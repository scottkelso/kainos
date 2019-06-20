package employee_stuff;

public class Employee implements IPayable, Comparable<Employee> {

	private int number;
	private float salary;
	private String name;
	private final int MIN_SALARY = 7000;
	
	public Employee() {
		this.number = -1;
	}
	
	public Employee(int number) {
		setNumber(number);
	}
	
	public Employee(int number, float salary) {
		this(number);
		setSalary(salary);
	}
	
	public Employee(int number, float salary, String name) {
		this(number, salary);
		setName(name);
	}
	
	public Employee(Employee emp) {
		this(emp.getNumber(), emp.getSalary(), emp.getName());
	}
	
	public float calcPay() {
		return this.salary / 12.0f;
	}
	
	public int getNumber() {
		return number;
	}

	public boolean setNumber(int number) {
		if (number > 0 ) {
			this.number = number;
			return true;
		} else {
			return false;
		}
	}

	public boolean setNumber(String thisNumber) throws BadNumber {
		try {
			int i = Integer.parseInt(thisNumber);
			return setNumber(i);
		} catch (Exception e) {
			throw new BadNumber(e);
		}
	}

	public float getSalary() {
		return salary;
	}

	public boolean setSalary(float salary) {
		if (salary <= MIN_SALARY) {
			this.salary = salary;
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		this.name = name;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Employee %d: %s, £%.2f. Monthly gross pay: £%.2f.", 
				this.number, this.name, this.salary, this.calcPay());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Employee) {
			Employee emp = (Employee) obj;
			return emp.getName() == this.getName() &&
					emp.getNumber() == this.getNumber() &&
					emp.getSalary() == this.getSalary();
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Employee x) {
		return Float.compare(this.getSalary(), x.getSalary());
	}
	
}
