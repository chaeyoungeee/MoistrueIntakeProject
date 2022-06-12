import java.io.*;
import java.util.*;

public class WorkMoistureIntake {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		
		System.out.println("**나의 수분 섭취량 기록**");
		
		File infoFile = new File("Info.txt");
		if(!infoFile.exists()) { // info파일이 없으면 새로 생성
			System.out.println("정보를 입력해주세요.");
			System.out.print("이름: ");
			String name = input.next();
			
			System.out.print("키: ");
			double height = input.nextDouble();
			
			System.out.print("몸무게: ");
			double weight = input.nextDouble();
			
			MoistureIntake user = new MoistureIntake(name, height, weight);
			
			user.menuWork();
		}
		
		else {
			MoistureIntake user = new MoistureIntake();
			
			user.menuWork();
		}
			
	}
	
}
