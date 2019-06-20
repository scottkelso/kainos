package employee_stuff;

public class SalesEmployee extends Employee {
	
	private float commissionRate, salesTotal;

	public SalesEmployee() {
		setupSalesEmployee();
	}

	public SalesEmployee(int number) {
		super(number);
		setupSalesEmployee();
	}

	public SalesEmployee(int number, float salary) {
		super(number, salary);
		setupSalesEmployee();
	}

	public SalesEmployee(int number, float salary, String name) {
		super(number, salary, name);
		setupSalesEmployee();
	}

	public SalesEmployee(Employee emp) {
		super(emp);
		setupSalesEmployee();
	}

	private void setupSalesEmployee() {
		this.commissionRate = 0.015f;
		this.salesTotal = 15;
	}

	public float getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(float commissionRate) {
		this.commissionRate = commissionRate;
	}
	
	public float calcPay() {
		return super.calcPay() + (this.commissionRate * this.salesTotal);
	}

	public float getSalesTotal() {
		return salesTotal;
	}

	public void setSalesTotal(float salesTotal) {
		this.salesTotal = salesTotal;
	}

	@Override
	public String toString() {
		return String.format(
				"Employee %d: %s, £%.2f. Monthly gross pay: £%.2f. Commission Rate: %.2f. Sales Total: £%.2f", 
				super.getNumber(), super.getName(), super.getSalary(), 
				this.calcPay(), this.getCommissionRate(), this.getSalesTotal());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SalesEmployee) {
			SalesEmployee emp = (SalesEmployee) obj;
			return super.equals(emp) &&
					emp.getCommissionRate() == this.getCommissionRate() &&
					emp.getSalesTotal() == this.getSalesTotal();
		} else {
			return false;
		}
	}

}
