package camacho;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CreateTests {
	public static void main(String[] args) {

		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream("src\\camacho\\Test.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			stream.write(5);
			stream.write(2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < 6; i++) {
			try {
				stream.write(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 6; i++) {
			try {
				stream.write(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
