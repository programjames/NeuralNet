package camacho;

import java.util.Arrays;

public class Neuron {
	int numberOfInputs;
	float[] steps;
	float[] coefficients;

	public Neuron(int arg1) {
		numberOfInputs = arg1;
		coefficients = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			coefficients[i] = (float) Math.random()*2-1;
		}
		steps = new float[numberOfInputs];
		Arrays.fill(steps, 0.1f);
	}

	public Neuron(int arg1, float[] arg2, float[] arg3) {
		numberOfInputs = arg1;
		coefficients = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			coefficients[i] = arg2[i];
		}
		steps = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			steps[i] = arg3[i];
		}
	}

	public boolean function(boolean[] args) {
		float value = 0; // return Math.round(value) at the end.
		for (int i = 0; i < numberOfInputs; i++) {
			value += coefficients[i] * (args[i] ? 1f : 0f);
		}
		value /= (float) numberOfInputs;
		if (value < 0.5) {
			return false;
		}
		return true;
	}

	public void changeSteps(boolean b) {
		// If the neuron worked better, b should be positive, else b will be negative.

		if (b) {
			for (int i = 0; i < numberOfInputs; i++) {
				coefficients[i] += steps[i];
			}
		} else {
			for (int i = 0; i < numberOfInputs; i++) {
				steps[i] *= -0.5f;
				coefficients[i] += steps[i];
			}
		}
	}
}
