package camacho;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TrainNetwork {
	public static void main(String[] args) {
		Test[] tests = LoadTests.load();
		for (int i = 0; i < tests.length; i++) {
			System.out.println(Arrays.toString(tests[i].inputs));
			System.out.println(tests[i].endResult);
		}
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
			network = LoadNetwork.loadFromFile(in, tests);
			if (network.isNull == false) {
				layers = network.layers;
				numberOfInputs = network.numberOfInputs;
			} else {
				System.out.println("Failed to load from a file!");
			}
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		/*
		 * If no file exists (in==null), or nothing is in the file (n==null), then we
		 * want to create our own network.
		 */
		if (layers == -1)

		{
			if (args.length < 2) {
				try {
					throw new Exception(
							"Error! Must specify number of inputs (first) and the number of layers (second).");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			numberOfInputs = Integer.parseInt(args[0]);
			layers = Integer.parseInt(args[1]);
			network = new Network(layers, numberOfInputs, tests);
		}

		for (int counter = 0; counter < 100000; counter++) {
			for (int layer = 0; layer < layers; layer++) {
				for (int neuron = 0; neuron < numberOfInputs; neuron++) {
					float startFitness = network.test();
					if (startFitness == 1) {
						layer = layers;
						break;
					}
					network.connections[layer][neuron].step();
					float endFitness = network.test();
					if (endFitness >= startFitness) {
						network.connections[layer][neuron].changeSteps(true);
					} else {
						network.connections[layer][neuron].changeSteps(false);
					}
				}
			}
			float startFitness = network.test();
			if (startFitness == 1) {
				save(network);
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
				counter = 100000;
				System.out.println("Perfected it early.");
				System.out.println(startFitness);
				break;
			}
			network.connections[layers][0].step();
			float endFitness = network.test();
			if (endFitness >= startFitness) {
				network.connections[layers][0].changeSteps(true);
			} else {
				network.connections[layers][0].changeSteps(false);
			}
			
			
			if (counter % 1000 == 0 && counter>0) {
				System.out.println(counter);
				save(network);
			}
		}
	}

	private static void save(Network network) {
		FileOutputStream o = null;
		try {
			o = new FileOutputStream("src\\camacho\\Network.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataOutputStream out=new DataOutputStream(o);
		byte[] temp = new byte[2];
		temp[0] = (byte) network.numberOfInputs;
		temp[1] = (byte) network.layers;
		try {
			out.writeInt(network.numberOfInputs);
			out.writeInt(network.layers);
		} catch (IOException e) {
			e.printStackTrace();
		}
		temp = new byte[4];
		for (int layer = 0; layer < network.layers; layer++) {
			for (int neuron = 0; neuron < network.numberOfInputs; neuron++) {
				for (int coeff = 0; coeff < network.numberOfInputs; coeff++) {
					// Writes out all of the coefficients for the inputs:
					try {
						out.writeFloat(network.connections[layer][neuron].coefficients[coeff]);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// Writes out all of the steps for the coefficients and constant:
					}
				for(int coeff=0;coeff<network.numberOfInputs+1;coeff++) {
					try {
						out.writeFloat(network.connections[layer][neuron].steps[coeff]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Writes out the constant adder for the neuron:
				try {
					out.writeFloat(network.connections[layer][neuron].constant);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// The following is for the very final neuron:
		for (int coeff = 0; coeff < network.numberOfInputs; coeff++) {
			// Writes out all of the coefficients for the inputs:
			try {
				out.writeFloat(network.connections[network.layers][0].coefficients[coeff]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Writes out all of the steps for the coefficients and constant:
			}
		for(int coeff=0;coeff<network.numberOfInputs+1;coeff++) {
			try {
				out.writeFloat(network.connections[network.layers][0].steps[coeff]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Writes out the constant adder for the neuron:
		try {
			out.writeFloat(network.connections[network.layers][0].constant);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] encode(float arg1) {
		byte[] b = new byte[4];
		arg1 *= 64f;
		b[0] = (byte) arg1;
		arg1 -= Math.floor(arg1);
		arg1 *= 64f;
		b[1] = (byte) arg1;
		arg1 -= Math.floor(arg1);
		arg1 *= 64f;
		b[2] = (byte) arg1;
		arg1 -= Math.floor(arg1);
		arg1 *= 64f;
		b[3] = (byte) arg1;
		return b;
	}
}
