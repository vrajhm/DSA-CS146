package test;

public class TestOOP {
	
	private int x;
	
	public int getSalary() {
		return x;
	}
	
	public void increaseSalary(int y) {
		this.x += y;
	}
	
	public static void main(String[] args) {
		TestOOP x = new TestOOP();
		x.increaseSalary(7);
		System.out.print(x.getSalary());
	}

}
