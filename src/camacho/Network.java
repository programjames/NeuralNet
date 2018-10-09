package camacho;

import java.util.Arrays;

public class Network {
	int layers;
	int numberOfInputs;
	Test[] tests;
	int testsLength;
	Neuron[][] connections;
	boolean isNull = false;

	/* A "null" network */
	public Network() {
		isNull = true;
	}

	/*
	 * This following network is only for trying the network out, not for training
	 * it
	 */
	public Network(int arg1, int arg2, Neuron[][] arg4) {
		layers = arg1;
		numberOfInputs = arg2;
		connections = new Neuron[layers + 1][numberOfInputs];
		for (int i = 0; i < layers; i++) {
			for (int j = 0; j < numberOfInputs; j++) {
				connections[i][j] = arg4[i][j];
			}
		}
		connections[layers][0] = arg4[layers][0];
	}

//Used to create a new network
	public Network(int arg1, int arg2, Test[] arg3) {
		layers = arg1;
		numberOfInputs = arg2;
		connections = new Neuron[layers + 1][numberOfInputs];
		for (int i = 0; i < layers; i++) {
			for (int j = 0; j < numberOfInputs; j++) {
				connections[i][j] = new Neuron(numberOfInputs);
			}
		}
		connections[layers][0] = new Neuron(numberOfInputs);
		tests = arg3;
		testsLength = tests.length;

	}

//Used to recreate a saved network
	public Network(int arg1, int arg2, Test[] arg3, Neuron[][] arg4) {
		layers = arg1;
		numberOfInputs = arg2;
		connections = new Neuron[layers + 1][numberOfInputs];
		for (int i = 0; i < layers; i++) {
			for (int j = 0; j < numberOfInputs; j++) {
				connections[i][j] = arg4[i][j];
			}
		}
		connections[layers][0] = arg4[layers][0];
		tests = arg3;
		testsLength = tests.length;
	}

	public float test() {
		int right = 0;
		for (int i = 0; i < testsLength; i++) {
			boolean[] inputs = tests[i].inputs;
			for (int j = 0; j < layers; j++) {
				boolean[] newInputs = new boolean[numberOfInputs];
				for (int k = 0; k < numberOfInputs; k++) {
					newInputs[k] = connections[j][k].function(inputs);
				}
				inputs = newInputs.clone();
			}
			boolean endResult = connections[layers][0].function(inputs);
			if (endResult == tests[i].endResult) {
				right += 1;
			}
		}
		return (float) right / (float) testsLength;
	}

	public void printTest(boolean[] ins) {
		boolean[] inputs = ins;
		for (int j = 0; j < layers; j++) {
			boolean[] newInputs = new boolean[numberOfInputs];
			System.out.println("Layer " + String.valueOf(j));

			for (int k = 0; k < numberOfInputs; k++) {
				newInputs[k] = connections[j][k].function(inputs);
			}
			inputs = newInputs.clone();

			System.out.println(Arrays.toString(inputs));
		}
		boolean endResult = connections[layers][0].function(inputs);
		System.out.println("Final value: " + String.valueOf(endResult));
	}

	public boolean testInput(boolean[] ins, boolean end) {
		boolean[] inputs = ins;
		for (int j = 0; j < layers; j++) {
			boolean[] newInputs = new boolean[numberOfInputs];
			for (int k = 0; k < numberOfInputs; k++) {
				newInputs[k] = connections[j][k].function(inputs);
			}
			inputs = newInputs.clone();
		}
		boolean endResult = connections[layers][0].function(inputs);
		return endResult == end;
	}
}
