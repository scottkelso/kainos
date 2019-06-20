package employee_stuff.employee_payroll;

import employee_stuff.IPayable;

public class PayrollSystem {
	
	private float taxRate = 0.25f;

	public float netPay(IPayable payee) {
		float grossPay = payee.calcPay();
		float taxToPay = grossPay * taxRate;
		return grossPay - taxToPay;
	}

}
