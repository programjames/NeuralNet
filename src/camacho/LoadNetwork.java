package camacho;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class LoadNetwork {

	public static Network loadFromFile(FileInputStream inputstream, Test[] tests) {
		DataInputStream in = new DataInputStream(inputstream);
		Network network = null;
		int layers = -1;
		int numberOfInputs = -1;
		try {
			numberOfInputs = in.readInt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			layers = in.readInt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (layers == -1) {
			return Network();
		}
		Neuron[][] neurons = new Neuron[layers + 1][numberOfInputs];
		for (int h = 0; h < layers; h++) {
			for (int i = 0; i < numberOfInputs; i++) {
				float[] coefficients = new float[numberOfInputs];
				float[] steps = new float[numberOfInputs + 1];
				for (int j = 0; j < numberOfInputs; j++) {
					try {
						coefficients[j] = in.readFloat();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				for (int j = 0; j < numberOfInputs + 1; j++) {
					try {
						steps[j] = in.readFloat();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				float constant = 0f;
				try {
					constant = in.readFloat();
				} catch (IOException e) {
					e.printStackTrace();
				}
				neurons[h][i] = new Neuron(numberOfInputs, coefficients, constant, steps);

			}

		}

		float[] coefficients = new float[numberOfInputs];
		float[] steps = new float[numberOfInputs + 1];
		for (int j = 0; j < numberOfInputs; j++) {
			try {
				coefficients[j] = in.readFloat();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int j = 0; j < numberOfInputs + 1; j++) {
			try {
				steps[j] = in.readFloat();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		float constant = 0f;
		try {
			constant = in.readFloat();
		} catch (IOException e) {
			e.printStackTrace();
		}
		neurons[layers][0] = new Neuron(numberOfInputs, coefficients, constant, steps);

		if (tests != null) {
			network = new Network(layers, numberOfInputs, tests, neurons);
		} else {
			network = new Network(layers, numberOfInputs, neurons);

		}

		return network;
	}

	private static Network Network() {
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