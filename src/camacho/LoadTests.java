package camacho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoadTests {
	public static Test[] load() {
		FileInputStream in=null;
		try {
			in = new FileInputStream("src\\camacho\\Test.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Test[] tests=new Test[1];
		return null;

	}
}
