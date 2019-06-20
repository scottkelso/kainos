package employee_stuff;

public class Consultant implements IPayable {
	
	private String name;
	private float dailyRate;
	private int daysWorked;

	public Consultant(String name, float dailyRate, int daysWorked) {
		this.name = name;
		this.dailyRate = dailyRate;
		this.daysWorked = daysWorked;
	}

	@Override
	public float calcPay() {
		return 4000;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean setName(String name) {
		this.name = name;
		return true;
	}

}
