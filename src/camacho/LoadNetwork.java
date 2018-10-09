package camacho;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class LoadNetwork {

	public static Network loadFromFile(FileInputStream in, Test[] tests) {
		Network network = null;
		int layers = -1;
		int numberOfInputs = -1;
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
		if (layers == -1) {
			return Network();
		}
		Neuron[][] neurons = new Neuron[layers + 1][numberOfInputs];
		int bytePosition = 2;
		for (int h = 0; h < layers; h++) {
			for (int i = 0; i < numberOfInputs; i++) {
				float[] coefficients = new float[numberOfInputs];
				float[] steps = new float[numberOfInputs];
				byte[][] temp = new byte[numberOfInputs + 1][4];
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
				for (int j = 0; j < numberOfInputs + 1; j++) {
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
				byte[] temp1 = new byte[4];
				try {
					in.read(temp1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				float constant = decode(temp1);
				neurons[h][i] = new Neuron(numberOfInputs, coefficients, constant, steps);

			}

		}

		float[] coefficients = new float[numberOfInputs];
		float[] steps = new float[numberOfInputs];
		byte[][] temp = new byte[numberOfInputs + 1][4];
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
		for (int j = 0; j < numberOfInputs + 1; j++) {
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
		byte[] temp1 = new byte[4];
		try {
			in.read(temp1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		float constant = decode(temp1);
		neurons[layers][0] = new Neuron(numberOfInputs, coefficients, constant, steps);
		if (tests != null) {
			network = new Network(layers, numberOfInputs, tests, neurons);
		} else {
			network = new Network(layers, numberOfInputs, neurons);

		}

		return network;
	}

	private static camacho.Network Network() {
		// TODO Auto-generated method stub
		return null;
	}

	private static Network Network(boolean b) {
		// TODO Auto-generated method stub
		return null;
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
}