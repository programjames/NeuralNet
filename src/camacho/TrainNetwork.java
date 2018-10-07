package camacho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TrainNetwork {
	public static void main(String[] args) {
		Test[] tests = LoadTests.load();
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
				network = new Network(layers, numberOfInputs, tests, neurons);
			}
		}
		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*
		 * If no file exists (in==null), or nothing is in the file (n==null), then we
		 * want to create our own network.
		 */
		if (layers == -1)

		{
			if (args.length < 2) {
				try {
					throw new Exception("Error! Must specify number of inputs and the number of layers.");
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
						save(network);
						counter = 100000;
						System.out.println("Perfected it early.");
						layer = layers;
						break;
					}
					network.connections[layer][neuron].step();
					float endFitness = network.test();
					if (endFitness > startFitness) {
						network.connections[layer][neuron].changeSteps(true);
					} else {
						network.connections[layer][neuron].changeSteps(false);
					}
				}
			}
			if (counter % 1000 == 0) {
				System.out.println(counter);
				save(network);
			}
		}
	}

	private static void save(Network network) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("src\\camacho\\Network.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] temp = new byte[2];
		temp[0] = (byte) network.numberOfInputs;
		temp[1] = (byte) network.layers;
		try {
			out.write(temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		temp = new byte[4];
		for (int layer = 0; layer < network.layers; layer++) {
			for (int neuron = 0; neuron < network.numberOfInputs; neuron++) {
				for (int coeff = 0; coeff < network.numberOfInputs; coeff++) {
					temp = encode(network.connections[layer][neuron].coefficients[coeff]);
					try {
						out.write(temp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					temp = encode(network.connections[layer][neuron].steps[coeff]);
					try {
						out.write(temp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (int coeff = 0; coeff < network.numberOfInputs; coeff++) {
			temp = encode(network.connections[network.layers][0].coefficients[coeff]);
			try {
				out.write(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static float decode(byte[] arg1) {
		float r = arg1[0] / 64f;
		r += arg1[1];
		r /= 64f;
		r += arg1[2];
		r /= 64f;
		r += arg1[3];
		r /= 64f;
		return r;
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
