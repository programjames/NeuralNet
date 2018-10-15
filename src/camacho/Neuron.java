package camacho;

import java.util.Arrays;

public class Neuron {
	int numberOfInputs;
	float[] steps;
	float[] coefficients;
	float constant;
	float[] prevCoefficients;

	public Neuron(int arg1) {
		numberOfInputs = arg1;
		coefficients = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			coefficients[i] = (float) (Math.random() * 2 - 1);
		}
		steps = new float[numberOfInputs + 1];
		for (int i = 0; i < numberOfInputs; i++) {
			steps[i] = 0.1f * (float) (Math.floor(Math.random() * 2) * 2 - 1);
		}
		steps[numberOfInputs] = ((float) numberOfInputs) / 10f * (float) (Math.floor(Math.random() * 2) * 2 - 1);
		Arrays.fill(steps, 0.1f * (float) (Math.floor(Math.random() * 2) * 2 - 1));
		constant = (float) (Math.random() * 2 - 1) * numberOfInputs;
	}

	public Neuron(int arg1, float[] arg2, float arg3, float[] arg4) {
		numberOfInputs = arg1;
		coefficients = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			coefficients[i] = arg2[i];
		}
		constant = arg3;
		steps = new float[numberOfInputs];
		for (int i = 0; i < numberOfInputs; i++) {
			steps[i] = arg4[i];
		}
	}

	public boolean function(boolean[] args) {
		float value = 0; // return Math.round(value) at the end.
		for (int i = 0; i < numberOfInputs; i++) {
			value += coefficients[i] * (args[i] ? 1f : -1f);
		}
		if (value < 0.5) {
			return false;
		}
		return true;
	}

	public void step() {
		prevCoefficients = Arrays.copyOf(coefficients, numberOfInputs);
		for (int i = 0; i < numberOfInputs; i++) {
			coefficients[i] += steps[i];
		}
	}

	public void changeSteps(boolean b) {
		// If the neuron worked better, b should be positive, else b will be negative.

		if (b) {
			for (int i = 0; i < numberOfInputs; i++) {
				coefficients[i] = prevCoefficients[i] + steps[i];
			}
		} else {
			for (int i = 0; i < numberOfInputs; i++) {
				steps[i] *= -0.7f;
				coefficients[i] = prevCoefficients[i] + steps[i];
			}
		}
	}
}
