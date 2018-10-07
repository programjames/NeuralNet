package camacho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadTests {
	public static Test[] load() {
		FileInputStream in = null;
		try {
			in = new FileInputStream("src\\camacho\\Test.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int numberOfInputs = 0;
		int numberOfTests = 0;
		try {
			numberOfInputs = in.read();
			numberOfTests = in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Test[] tests = new Test[numberOfTests];
		for (int i = 0; i < numberOfTests; i++) {
			boolean[] inputs = new boolean[numberOfInputs];
			for (int j = 0; j < numberOfInputs; j++) {
				try {
					inputs[j] = in.read() == 1 ? true : false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			boolean endResult = false;
			try {
				endResult = in.read() == 1 ? true : false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			tests[i] = new Test(inputs, endResult);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tests;

	}
}
