package taxCalculator;

import java.util.Scanner;

public class TaxCalculator {

	public static void main(String[] args) {
		//scanner
		Scanner scanner = new Scanner(System.in);
		
		//prompt user to enter salary
		System.out.println("Enter your salary");
		
		int salary = scanner.nextInt();
		
		//workout salary after tax and how much tax is being paid
		if (salary <= 12500) {
			System.out.println("You pay no tax");
			System.out.println("Your salary after tax is: " + salary);
		}
		
		if (salary > 150000) {
			double afterTax = (salary * 0.45);
			System.out.println("You will pay " + afterTax + " in tax");
			System.out.println("Your salary after tax is: " + (salary - afterTax));
		}
		
		if (salary > 12500 &&  salary < 50000) {
			double afterTax = (salary * 0.20);
			System.out.println("You will pay " + afterTax + " in tax");
			System.out.println("Your salary after tax is: " + (salary - afterTax));
		}
		if (salary > 50000 &&  salary < 150001) {
			double afterTax = (salary * 0.40);
			System.out.println("You will pay " + afterTax + " in tax");
			System.out.println("Your salary after tax is: " + (salary - afterTax));
		}
	}
}
