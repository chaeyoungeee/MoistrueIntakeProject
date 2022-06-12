import java.io.*;
import java.util.*;

public class WorkMoistureIntake {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		
		System.out.println("**���� ���� ���뷮 ���**");
		
		File infoFile = new File("Info.txt");
		if(!infoFile.exists()) { // info������ ������ ���� ����
			System.out.println("������ �Է����ּ���.");
			System.out.print("�̸�: ");
			String name = input.next();
			
			System.out.print("Ű: ");
			double height = input.nextDouble();
			
			System.out.print("������: ");
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
