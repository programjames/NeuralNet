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
			try {
				numberOfInputs = in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				layers = in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (layers != -1) {

				System.out.println(layers);
				Neuron[][] neurons = new Neuron[layers + 1][numberOfInputs];
				int bytePosition = 2;
				for (int h = 0; h < layers; h++) {
					for (int i = 0; i < numberOfInputs; i++) {
						float[] coefficients = new float[numberOfInputs];
						float[] steps = new float[numberOfInputs];
						byte[][] temp = new byte[numberOfInputs][4];
						// temp will be transferred to either coefficients or steps.
						for (int j = 0; j < numberOfInputs; j++) {
							try {
								in.read(temp[j]);
							} catch (IOException e) {
								e.printStackTrace();
							}
							bytePosition += 4;
						}
						for (int j = 0; j < numberOfInputs; j++) {
							coefficients[j] = decode(temp[j]);
						}
						for (int j = 0; j < numberOfInputs; j++) {
							try {
								in.read(temp[j]);
							} catch (IOException e) {
								e.printStackTrace();
							}
							bytePosition += 4;
						}
						for (int j = 0; j < numberOfInputs; j++) {
							steps[j] = decode(temp[j]);
						}
						neurons[h][i] = new Neuron(numberOfInputs, coefficients, steps);

					}

				}

				float[] coefficients = new float[numberOfInputs];
				float[] steps = new float[numberOfInputs];
				byte[][] temp = new byte[numberOfInputs][4];
				for (int j = 0; j < numberOfInputs; j++) {
					try {
						in.read(temp[j]);
					} catch (IOException e) {
						e.printStackTrace();
					}
					bytePosition += 4;
				}
				for (int j = 0; j < numberOfInputs; j++) {
					coefficients[j] = decode(temp[j]);
				}
				for (int j = 0; j < numberOfInputs; j++) {
					try {
						in.read(temp[j]);
					} catch (IOException e) {
						e.printStackTrace();
					}
					bytePosition += 4;
				}
				for (int j = 0; j < numberOfInputs; j++) {
					steps[j] = decode(temp[j]);
				}
				neurons[layers][0] = new Neuron(numberOfInputs, coefficients, steps);
				network = new Network(layers, numberOfInputs, neurons);
			}
		}
		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (network != null) {
			for(int i=0;i<layers;i++) {
				System.out.println("Layer "+String.valueOf(i));
				for(int j=0;j<numberOfInputs;j++) {
					System.out.println("    "+Arrays.toString(network.connections[i][j].coefficients));
				}
			}
			System.out.println("Final layer");
			System.out.println("    "+Arrays.toString(network.connections[layers][0].coefficients));
			System.out.println("\nRunning Test (false,false,false,false,false)...\n");
			boolean[] inputs= {true,true,false,false,false};
			network.printTest(inputs);
			/*
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine();
			String[] ins = line.split(" ");
			boolean[] inputs = new boolean[numberOfInputs];
			for (int i = 0; i < numberOfInputs; i++) {
				inputs[i] = Integer.parseInt(ins[i])==1?true:false;
			}
			System.out.println(network.testInput(inputs,true));*/
		}
	}

	private static float decode(byte[] bs) {
		// TODO Auto-generated method stub
		return TrainNetwork.decode(bs);
	}
}
