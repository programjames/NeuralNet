package camacho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TestNetwork {
	public static void main(String[] args) {
		FileInputStream in = null;
		try {
			in = new FileInputStream("src\\camacho\\Network.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Network network = null;
		int layers = -1;
		int numberOfInputs = -1;
		if (in != null) {

			/*
			 * If we have already started training a network (and it is saved to
			 * Network.data), we want to read the file and get our data back for the
			 * network.
			 */
			network = LoadNetwork.loadFromFile(in, null);
			layers = network.layers;
			numberOfInputs = network.numberOfInputs;
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (network != null) {
			for (int i = 0; i < layers; i++) {
				System.out.println("Layer " + String.valueOf(i));
				for (int j = 0; j < numberOfInputs; j++) {
					System.out.println("    Coefficients:  " + Arrays.toString(network.connections[i][j].coefficients));
					System.out.println("    Constant:      " + network.connections[i][j].constant);
				}
			}
			System.out.println("Final layer");
			System.out.println("    Coefficients:  " + Arrays.toString(network.connections[layers][0].coefficients));
			System.out.println("    Constant:      " + network.connections[layers][0].constant);
			System.out.println("\nRunning Test...\n");
			boolean[] inputs = { false,false,false,false,false };
			network.printTest(inputs);
			/*
			 * Scanner scanner = new Scanner(System.in); String line = scanner.nextLine();
			 * String[] ins = line.split(" "); boolean[] inputs = new
			 * boolean[numberOfInputs]; for (int i = 0; i < numberOfInputs; i++) { inputs[i]
			 * = Integer.parseInt(ins[i])==1?true:false; }
			 * System.out.println(network.testInput(inputs,true));
			 */
		}
	}

	private static float decode(byte[] b) {
		return LoadNetwork.decode(b);
	}
}
