package themoles.dataprocess;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * created by Ying Meng
 */
public class Demo {
	public static void main(String[] args) {
		String fileName = "dataset/training/train/academic.csv";
		try {
			File dataFile = new File(fileName);
//			Scanner fileScan = new Scanner(dataFile);
			PrintWriter writer = new PrintWriter(new File("testing111.txt"));
			writer.println("hello");
//			if (! fileScan.hasNextLine()) {
//
//				System.out.println("no lines in file");
//			}
//			while (fileScan.hasNextLine()) {
//				System.out.println(fileScan.nextLine());
//			}
//			System.out.println("separator " + dataFile.separator + ", " + dataFile.isFile() + ", " + dataFile.canRead());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
