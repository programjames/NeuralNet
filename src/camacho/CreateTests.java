package camacho;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CreateTests {
	public static void main(String[] args) {

		FileOutputStream stream=null;
		try {
			stream=new FileOutputStream("src\\camacho\\Test.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(int i=0;i<100;i++) {
			try {
				byte[] b= {(byte) 255};
				stream.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
