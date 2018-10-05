package camacho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TrainNetwork {
	public static void main(String[] args) {
		Test[] tests = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream("src\\camacho\\Network.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Network network = null;
		byte[] n = new byte[2]; // its zero entry will be converted to a byte, and will be numberOfInputs
		if (in != null) {
			try {
				in.read(n, 0, 1);
			} catch (IOException e) {
				e.printStackTrace();
				n = null;
			}
			/* If we have already started training a network (and it is saved to Network.data), we want to read the file and get our data back for the network.*/
			if (n != null) {
				int numberOfInputs = (int) n[0];
				try {
					in.read(n, 1, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int layers = (int) n[1];
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
								in.read(temp[j], bytePosition, 4);
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
								in.read(temp[j], bytePosition, 4);
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
			network=new Network(layers,numberOfInputs,tests,neurons);
			}
		}
		/* If no file exists (in==null), or nothing is in the file (n==null), then we want to create our own network.*/
		if(in==null || n==null) {
			if(args.length<2) {
				try {
					throw new Exception("Error! Must specify number of inputs and the number of layers.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			int numberOfInputs=Integer.parseInt(args[0]);
			int layers=Integer.parseInt(args[1]);
			network=new Network(layers,numberOfInputs,tests);
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
}
