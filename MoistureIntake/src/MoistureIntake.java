import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

public class MoistureIntake {
	private ArrayList<String> drinkList = new ArrayList<String>();
	private ArrayList<Double> sumDrinkIntakes = new ArrayList<Double>(Collections.nCopies(30, 0.0)); // ���Ằ ���뷮 ��
	private LocalDate date = LocalDate.now(); 
	private double recommendedMoisture;
	private double percentOfMoisture;
	private double monthIntake;
	private double sumMoistureIntake; // �Ϸ� ���м��뷮 ��
	protected String name;
	protected double height;
	protected double weight;

	

	
	protected MoistureIntake() throws IOException {
		File infoFile = new File("Info.txt");
		
		Scanner input1 = new Scanner(infoFile);
		
		this.name = input1.next();
		this.height = input1.nextDouble();
		this.weight = input1.nextDouble();
		
		input1.close();
		
		setting();
	}

	protected MoistureIntake(String name, double height, double weight) throws IOException {
		File infoFile = new File("Info.txt");

		PrintWriter output = new PrintWriter(infoFile);
		output.println(name);
		output.println(height);
		output.println(weight);
		
		output.close();
		
		this.name = name;
		this.height = height;
		this.weight = weight;
		
		setting();
	}
	
	public void setting() throws IOException { // ���� ���, ��� �ʱ� ����
		File drinkListFile = new File("drinkList.txt"); 
		Scanner input2 = new Scanner(drinkListFile);
		while(input2.hasNext()) { // drinkListFile���� ���� ����� �ҷ��ͼ� drinkList�� �߰�
			drinkList.add(input2.next());
		}
		input2.close(); 
		
		int n = 0;
		File recordFile = new File("record_" + date + ".txt");
		if(!recordFile.exists()) {
			sumMoistureIntake = 0;
			sumDrinkIntakes = new ArrayList<Double>(Collections.nCopies(30, 0.0));
		}
		else {
			Scanner input = new Scanner(recordFile);
			while(input.hasNext()) { 
				String garbage = input.next();
				sumDrinkIntakes.add(n, input.nextDouble()); // ���Ằ ���� ���뷮 sumDrinkIntakes�� �߰�
				sumMoistureIntake = input.nextDouble();
				n++;
			}
			
			input.close();
		}
		
	}	

	public double moistureIntakePerDay() { // �Ϸ� ���� ���� ���뷮
		return recommendedMoisture = (height+weight) * 10; // ml ������ ǥ��
	}
	
	public void addDrink() throws IOException { // ���� ���� �߰�
		Scanner input = new Scanner(System.in);
		System.out.println("�߰��Ͻ� ���� ������ �Է����ּ���.");
		String drinkType = input.next();
		drinkList.add(drinkType);
		
		File drinkListFile = new File("drinkList.txt");
		PrintWriter output = new PrintWriter(drinkListFile);
		for(int i = 0 ; i < drinkList.size() ; i++)
			output.println(drinkList.get(i));
		
		output.close();
	}
	
	public void printMenu() { // �޴� ���
		System.out.println();
		System.out.println("�޴��� �����ϼ���.");
		System.out.println("0. ���� ���� ���� ���뷮 Ȯ���ϱ�");
		System.out.println("1. ���� ���� ����ϱ�");
		System.out.println("2. ���� ���� ���뷮 ����");
		System.out.println("3. �ֱ� 7�� �� ���� ���뷮 ����");
		System.out.println("4. �Ѵ� ���� ���뷮 ����");
		System.out.println("5. ���� �߰�");
		System.out.println("6. ���� ����");
		System.out.println("7. ����");
	}
	
	public void menuWork() throws IOException { // �޴� ����
		Scanner input = new Scanner(System.in);
		
		while(true) {
			printMenu();
			int index = input.nextInt();
			System.out.println();
				switch(index) {
				case 0: System.out.println(name + "���� �Ϸ� ���� ���� ���뷮�� " + moistureIntakePerDay() + "ml �Դϴ�.");
					break;
				case 1: recordMoistureIntake();
					break;
				case 2: printDayMoistureIntake();
					break;
				case 3: printWeekMoistureIntake();
					break;
				case 4: printMonthMoistureIntake();
					break;
				case 5: addDrink();
					break;
				case 6: updateInfo();
					break;
				case 7: System.exit(0);
					break;
				default: System.out.println("0~7�� �߿��� �������ּ���.");
				}
		}
	}
	
	
	public void recordMoistureIntake() throws IOException { // ���� ���뷮 ���
		Scanner input = new Scanner(System.in);
		File recordFile = new File("record_" + date + ".txt");
		
		int index = selectDrink();
		String type = drinkList.get(index);
		System.out.println(type + "��(��) ���� ��(ml)�� �Է����ּ���.");
		double amount = input.nextDouble();
		
		sumMoistureIntake += amount;
		
		sumDrinkIntakes.set(index, sumDrinkIntakes.get(index)+amount);
		
		FileWriter fw = new FileWriter(recordFile, false);
		
		for(int i = 0 ; i < drinkList.size() ; i++) // �����, ���Ằ �� ���뷮, �� ���м��뷮 ������ ���
			fw.write(drinkList.get(i) + " " + sumDrinkIntakes.get(i) + " " + sumMoistureIntake + "\n");
		fw.close();
	}
	
	public int selectDrink() { // ����� ���� ����
		Scanner input = new Scanner(System.in);
		System.out.println("����� ���Ḧ �������ּ���.");
		for(int i = 0 ; i <drinkList.size() ; i++) {
			System.out.println(i + ". " + drinkList.get(i));
		}
		
		int index = input.nextInt();
		
		return index;
	}
	
	public double percentOfMoistureIntake(double recommendedMoisture) { // �Ϸ� ���� ���� ���뷮 ��� �Ϸ� ���� ���뷮 �ۼ�Ʈ ���
		percentOfMoisture = sumMoistureIntake/recommendedMoisture * 100;
		return percentOfMoisture;
	}
	
	public void printDayMoistureIntake() { // �Ϸ� ���� ���뷮 ���
		recommendedMoisture = moistureIntakePerDay();
		
		for(int i = 0 ; i < drinkList.size() ; i++) // ���Ằ �� ���뷮 ���
			System.out.println(drinkList.get(i) + ": " + sumDrinkIntakes.get(i) + "ml");
		
		System.out.println(date + " �� ���� ���뷮�� " + sumMoistureIntake + "ml�Դϴ�."); // �� ���뷮 ���
		
		// �Ϸ� ���� ���� ���뷮 ��� �Ϸ� ���� ���뷮 ���� ��
		if(recommendedMoisture > sumMoistureIntake) 
			System.out.println("�Ϸ� ���� ���� ���뷮�� �޼��ϱ� ���ؼ� " + (recommendedMoisture - sumMoistureIntake) + "ml�� ������ �� �����ϼž� �մϴ�.");
		else if(recommendedMoisture < sumMoistureIntake)
			System.out.println("�Ϸ� ���� ���� ���뷮���� " + (sumMoistureIntake - recommendedMoisture) + "ml ���� ������ �����ϼ̽��ϴ�.");
		else
			System.out.println("�Ϸ� ���� ���� ���뷮�� ��� �����ϼ̽��ϴ�.");
		System.out.println("���� ���� ���뷮 �޼��� " + percentOfMoistureIntake(recommendedMoisture) + "% �Ϸ�Ǿ����ϴ�.");
	}
	
	public void printWeekMoistureIntake() throws IOException { // �ֱ� 7�� ���� ���뷮 ���
		double sumIntake = 0;
		for(int i = 6 ; i >= 0 ; i--) {
			LocalDate DaysAgo = date.minusDays(i);
			
			File recordFile = new File("record_" + DaysAgo + ".txt");
			
			if(!recordFile.exists())
				System.out.println(DaysAgo + ": " + "0ml");
			else {
				Scanner input = new Scanner(recordFile);
				String garbage1 = input.next();
				double garbage2 = input.nextDouble();
				double amount = input.nextDouble();
				sumIntake += amount;
				System.out.println(DaysAgo + ": " + amount + "ml");
				input.close();
			}
		}
			
		System.out.println(date.minusDays(6) + "~" + date + " ���� ��� ���� ���뷮�� " + sumIntake/7 + "ml�Դϴ�.");
	}
	
	public void printMonthMoistureIntake() throws IOException { // �Ѵ� ���� ���뷮 ���
		Scanner input = new Scanner(System.in);
		int year;
		int month;
		System.out.println("�ְ� ���� ���뷮�� Ȯ���ϰ� ���� �⵵�� �Է��ϼ���.");
		year = input.nextInt();
		System.out.println("�ְ� ���� ���뷮�� Ȯ���ϰ� ���� ���� �Է��ϼ���.");
		month = input.nextInt();
		
		switch(month) {
		case 1: case 3: case 5: case 7: case 8: case 10: case 12: // 31���� ��
			weekMoistureIntake31(year, month);
            break;
        case 4: case 6: case 11: case 9: // 30���� ��
        	weekMoistureIntake30(year, month);
            break;
        case 2: // 2��
        	weekMoistureIntakeM2(year, month);
            break;
		}
	}
	
	public void weekMoistureIntake30(int year, int month) throws IOException { // 30���� �� ���м��뷮 �� ���
		LocalDate targetDate = LocalDate.of(year, month, 1);
	
		monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 7)
		+ weekMoistureIntake(targetDate.plusDays(14), 8)
		+ weekMoistureIntake(targetDate.plusDays(22), 8);
		System.out.println(year + "�� " + month + "�� �� ���� ���뷮�� " + monthIntake + "ml �̰�,");
		System.out.println(year + "�� " + month + "�� �Ϸ� ��� ���� ���뷮�� " + monthIntake/30 + "ml�Դϴ�.");
	}
	
	public void weekMoistureIntake31(int year, int month) throws IOException { // 31���� �� ���м��뷮 �� ���
		LocalDate targetDate = LocalDate.of(year, month, 1);
		monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 8)
		+ weekMoistureIntake(targetDate.plusDays(15), 8)
		+ weekMoistureIntake(targetDate.plusDays(23), 8);
		
		System.out.println(year + "�� " + month + "�� �� ���� ���뷮�� " + monthIntake + "ml �̰�,");
		System.out.println(year + "�� " + month + "�� �Ϸ� ��� ���� ���뷮�� " + monthIntake/31 + "ml�Դϴ�.");
	}
	
	public void weekMoistureIntakeM2(int year, int month) throws IOException { // 2�� ���м��뷮 �� ���
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);
		
		LocalDate targetDate = LocalDate.of(year, month, 1);
		double monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 7)
		+ weekMoistureIntake(targetDate.plusDays(14), 7);

		
		if(cal.getActualMaximum(Calendar.DAY_OF_MONTH) == 28) { 
			monthIntake += weekMoistureIntake(targetDate.plusDays(21), 7);
			System.out.println(year + "�� " + month + "�� �� ���� ���뷮�� " + monthIntake + "ml �̰�,");
			System.out.println(year + "�� " + month + "�� �Ϸ� ��� ���� ���뷮�� " + monthIntake/28 + "ml�Դϴ�.");
			
		}
		else {
			monthIntake += weekMoistureIntake(targetDate.plusDays(21), 8);
			System.out.println(year + "�� " + month + "�� �� ���� ���뷮�� " + monthIntake + "ml �̰�,");
			System.out.println(year + "�� " + month + "�� �Ϸ� ��� ���� ���뷮�� " + monthIntake/29 + "ml�Դϴ�.");
		}
	}
	
	public double weekMoistureIntake(LocalDate targetDate, int n) throws IOException { // 7~8�� ���� ���� ���뷮 �� ���
		double sumIntake = 0;
		LocalDate firstDate = targetDate;
		for(int i=0;i<n;i++) {
			File recordFile = new File("record_" + targetDate + ".txt");
			
			if(recordFile.exists()){
				Scanner input = new Scanner(recordFile);
				String garbage1 = input.next();
				double garbage2 = input.nextDouble();
				double amount = input.nextDouble();
				sumIntake += amount;
				input.close();
			}
			targetDate = targetDate.plusDays(1);
		}
		System.out.println(firstDate + "~" + targetDate.minusDays(1) + ": " + sumIntake + "ml");
		
		return sumIntake;
	}

	public void updateInfo() throws IOException { // ���� ����
		Scanner input = new Scanner(System.in);
		System.out.println("������ ���Ͻô� ������ �����ϼ���.");
		System.out.println("0. �̸�");
		System.out.println("1. Ű");
		System.out.println("2. ������");
		
		int index;
		
		do {
		index = input.nextInt();
		switch(index) {
		case 0:
			System.out.print("�̸�: ");
			String name = input.next();
			this.name = name;
			break;
		
		case 1:
			System.out.print("Ű: ");
			double height = input.nextDouble();
			this.height = height;
			break;
				
		case 2:
			System.out.print("������: ");
			double weight = input.nextDouble();
			this.weight = weight;
			break;
		default: 
			System.out.println("0~2�� �߿��� �������ּ���.");
			break;
		}
		} while(index != 0 && index != 1 && index != 2 );
		
		File infoFile = new File("Info.txt");
		infoFile.delete();
		PrintWriter output = new PrintWriter(infoFile);
		output.println(name);
		output.println(height);
		output.println(weight);
		
		output.close();
	}
}
