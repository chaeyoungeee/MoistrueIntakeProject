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
	private ArrayList<Double> sumDrinkIntakes = new ArrayList<Double>(Collections.nCopies(30, 0.0)); // 음료별 섭취량 합
	private LocalDate date = LocalDate.now(); 
	private double recommendedMoisture;
	private double percentOfMoisture;
	private double monthIntake;
	private double sumMoistureIntake; // 하루 수분섭취량 합
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
	
	public void setting() throws IOException { // 음료 목록, 기록 초기 세팅
		File drinkListFile = new File("drinkList.txt"); 
		Scanner input2 = new Scanner(drinkListFile);
		while(input2.hasNext()) { // drinkListFile에서 음료 목록을 불러와서 drinkList에 추가
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
				sumDrinkIntakes.add(n, input.nextDouble()); // 음료별 수분 섭취량 sumDrinkIntakes에 추가
				sumMoistureIntake = input.nextDouble();
				n++;
			}
			
			input.close();
		}
		
	}	

	public double moistureIntakePerDay() { // 하루 권장 수분 섭취량
		return recommendedMoisture = (height+weight) * 10; // ml 단위로 표시
	}
	
	public void addDrink() throws IOException { // 음료 종류 추가
		Scanner input = new Scanner(System.in);
		System.out.println("추가하실 음료 종류를 입력해주세요.");
		String drinkType = input.next();
		drinkList.add(drinkType);
		
		File drinkListFile = new File("drinkList.txt");
		PrintWriter output = new PrintWriter(drinkListFile);
		for(int i = 0 ; i < drinkList.size() ; i++)
			output.println(drinkList.get(i));
		
		output.close();
	}
	
	public void printMenu() { // 메뉴 출력
		System.out.println();
		System.out.println("메뉴를 선택하세요.");
		System.out.println("0. 나의 수분 권장 섭취량 확인하기");
		System.out.println("1. 수분 섭취 기록하기");
		System.out.println("2. 오늘 수분 섭취량 보기");
		System.out.println("3. 최근 7일 간 수분 섭취량 보기");
		System.out.println("4. 한달 수분 섭취량 보기");
		System.out.println("5. 음료 추가");
		System.out.println("6. 정보 변경");
		System.out.println("7. 종료");
	}
	
	public void menuWork() throws IOException { // 메뉴 선택
		Scanner input = new Scanner(System.in);
		
		while(true) {
			printMenu();
			int index = input.nextInt();
			System.out.println();
				switch(index) {
				case 0: System.out.println(name + "님의 하루 권장 수분 섭취량은 " + moistureIntakePerDay() + "ml 입니다.");
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
				default: System.out.println("0~7번 중에서 선택해주세요.");
				}
		}
	}
	
	
	public void recordMoistureIntake() throws IOException { // 수분 섭취량 기록
		Scanner input = new Scanner(System.in);
		File recordFile = new File("record_" + date + ".txt");
		
		int index = selectDrink();
		String type = drinkList.get(index);
		System.out.println(type + "을(를) 마신 양(ml)을 입력해주세요.");
		double amount = input.nextDouble();
		
		sumMoistureIntake += amount;
		
		sumDrinkIntakes.set(index, sumDrinkIntakes.get(index)+amount);
		
		FileWriter fw = new FileWriter(recordFile, false);
		
		for(int i = 0 ; i < drinkList.size() ; i++) // 음료명, 음료별 총 섭취량, 총 수분섭취량 순으로 기록
			fw.write(drinkList.get(i) + " " + sumDrinkIntakes.get(i) + " " + sumMoistureIntake + "\n");
		fw.close();
	}
	
	public int selectDrink() { // 기록할 음료 선택
		Scanner input = new Scanner(System.in);
		System.out.println("기록할 음료를 선택해주세요.");
		for(int i = 0 ; i <drinkList.size() ; i++) {
			System.out.println(i + ". " + drinkList.get(i));
		}
		
		int index = input.nextInt();
		
		return index;
	}
	
	public double percentOfMoistureIntake(double recommendedMoisture) { // 하루 권장 수분 섭취량 대비 하루 수분 섭취량 퍼센트 계산
		percentOfMoisture = sumMoistureIntake/recommendedMoisture * 100;
		return percentOfMoisture;
	}
	
	public void printDayMoistureIntake() { // 하루 수분 섭취량 출력
		recommendedMoisture = moistureIntakePerDay();
		
		for(int i = 0 ; i < drinkList.size() ; i++) // 음료별 총 섭취량 출력
			System.out.println(drinkList.get(i) + ": " + sumDrinkIntakes.get(i) + "ml");
		
		System.out.println(date + " 총 수분 섭취량은 " + sumMoistureIntake + "ml입니다."); // 총 섭취량 출력
		
		// 하루 권장 수분 섭취량 대비 하루 수분 섭취량 정보 출
		if(recommendedMoisture > sumMoistureIntake) 
			System.out.println("하루 권장 수분 섭취량을 달성하기 위해서 " + (recommendedMoisture - sumMoistureIntake) + "ml의 수분을 더 섭취하셔야 합니다.");
		else if(recommendedMoisture < sumMoistureIntake)
			System.out.println("하루 권장 수분 섭취량보다 " + (sumMoistureIntake - recommendedMoisture) + "ml 많은 수분을 섭취하셨습니다.");
		else
			System.out.println("하루 권장 수분 섭취량을 모두 섭취하셨습니다.");
		System.out.println("권장 수분 섭취량 달성이 " + percentOfMoistureIntake(recommendedMoisture) + "% 완료되었습니다.");
	}
	
	public void printWeekMoistureIntake() throws IOException { // 최근 7일 수분 섭취량 출력
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
			
		System.out.println(date.minusDays(6) + "~" + date + " 동안 평균 수분 섭취량은 " + sumIntake/7 + "ml입니다.");
	}
	
	public void printMonthMoistureIntake() throws IOException { // 한달 수분 섭취량 출력
		Scanner input = new Scanner(System.in);
		int year;
		int month;
		System.out.println("주간 수분 섭취량을 확인하고 싶은 년도를 입력하세요.");
		year = input.nextInt();
		System.out.println("주간 수분 섭취량을 확인하고 싶은 달을 입력하세요.");
		month = input.nextInt();
		
		switch(month) {
		case 1: case 3: case 5: case 7: case 8: case 10: case 12: // 31일인 달
			weekMoistureIntake31(year, month);
            break;
        case 4: case 6: case 11: case 9: // 30일인 달
        	weekMoistureIntake30(year, month);
            break;
        case 2: // 2월
        	weekMoistureIntakeM2(year, month);
            break;
		}
	}
	
	public void weekMoistureIntake30(int year, int month) throws IOException { // 30일인 달 수분섭취량 합 출력
		LocalDate targetDate = LocalDate.of(year, month, 1);
	
		monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 7)
		+ weekMoistureIntake(targetDate.plusDays(14), 8)
		+ weekMoistureIntake(targetDate.plusDays(22), 8);
		System.out.println(year + "년 " + month + "월 총 수분 섭취량은 " + monthIntake + "ml 이고,");
		System.out.println(year + "년 " + month + "월 하루 평균 수분 섭취량은 " + monthIntake/30 + "ml입니다.");
	}
	
	public void weekMoistureIntake31(int year, int month) throws IOException { // 31일인 달 수분섭취량 합 출력
		LocalDate targetDate = LocalDate.of(year, month, 1);
		monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 8)
		+ weekMoistureIntake(targetDate.plusDays(15), 8)
		+ weekMoistureIntake(targetDate.plusDays(23), 8);
		
		System.out.println(year + "년 " + month + "월 총 수분 섭취량은 " + monthIntake + "ml 이고,");
		System.out.println(year + "년 " + month + "월 하루 평균 수분 섭취량은 " + monthIntake/31 + "ml입니다.");
	}
	
	public void weekMoistureIntakeM2(int year, int month) throws IOException { // 2월 수분섭취량 합 출력
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1);
		
		LocalDate targetDate = LocalDate.of(year, month, 1);
		double monthIntake = weekMoistureIntake(targetDate, 7)
		+ weekMoistureIntake(targetDate.plusDays(7), 7)
		+ weekMoistureIntake(targetDate.plusDays(14), 7);

		
		if(cal.getActualMaximum(Calendar.DAY_OF_MONTH) == 28) { 
			monthIntake += weekMoistureIntake(targetDate.plusDays(21), 7);
			System.out.println(year + "년 " + month + "월 총 수분 섭취량은 " + monthIntake + "ml 이고,");
			System.out.println(year + "년 " + month + "월 하루 평균 수분 섭취량은 " + monthIntake/28 + "ml입니다.");
			
		}
		else {
			monthIntake += weekMoistureIntake(targetDate.plusDays(21), 8);
			System.out.println(year + "년 " + month + "월 총 수분 섭취량은 " + monthIntake + "ml 이고,");
			System.out.println(year + "년 " + month + "월 하루 평균 수분 섭취량은 " + monthIntake/29 + "ml입니다.");
		}
	}
	
	public double weekMoistureIntake(LocalDate targetDate, int n) throws IOException { // 7~8일 간격 수분 섭취량 합 출력
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

	public void updateInfo() throws IOException { // 정보 수정
		Scanner input = new Scanner(System.in);
		System.out.println("변경을 원하시는 정보를 선택하세요.");
		System.out.println("0. 이름");
		System.out.println("1. 키");
		System.out.println("2. 몸무게");
		
		int index;
		
		do {
		index = input.nextInt();
		switch(index) {
		case 0:
			System.out.print("이름: ");
			String name = input.next();
			this.name = name;
			break;
		
		case 1:
			System.out.print("키: ");
			double height = input.nextDouble();
			this.height = height;
			break;
				
		case 2:
			System.out.print("몸무게: ");
			double weight = input.nextDouble();
			this.weight = weight;
			break;
		default: 
			System.out.println("0~2번 중에서 선택해주세요.");
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
