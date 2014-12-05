//
// Network
//
// This class implements an artificial neural network.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


import java.io.*;
import java.util.*;


public class Network {

    public List<Layer> layers;  // layers, in order of activation propagation

    // Default constructor ...
    public Network() {
	layers = new LinkedList<Layer>();
    }

    // createLayer -- Create a new layer of the given size and add it to
    // the end of the cascade of layers.  In other words, layers must be
    // created in the order of activation flow.  Return the layer, or null
    // on error.
    public Layer createLayer(int size, double min, double max) {
	Layer lay = new Layer(size, min, max);
	layers.add(lay);
	return (lay);
    }
    
    // createLayer -- Create a new layer of the given size and add it to
    // the end of the cascade of layers.  In other words, layers must be
    // created in the order of activation flow.  Return the layer, or null
    // on error.
    public Layer createLayer(int size) {
	return (createLayer(size, 0.0, 1.0));
    }
    
    // createProjection -- Create a new projection between the two specified
    // layers.  Return the projection, or null on error.
    public Projection createProjection(Layer input, Layer output) {
	// This version of the Projection constructor automatically connects 
	// the projection to the appropriate layers ...
	Projection p = new Projection(input, output);
	return (p);
    }

    // computeActivation -- Propagate activation forward through the
    // network, updating layer activation vectors.
    public void computeActivation() {
	for (Layer lay : layers) {
	    lay.computeActivation();
	}
    }

    // computeDelta -- Propagate error backward through the network, updating
    // layer delta vectors.
    public void computeDelta() {
	// Using an iterator saves the cost of allocating a reversed list ...
	ListIterator<Layer> iterator = layers.listIterator(layers.size());
	Layer lay = null;
	// Traverse the list of layers in reverse order ...
	while (iterator.hasPrevious()) {
	    lay = iterator.previous();
	    lay.computeDelta();
	}
    }

    // clearWeightDeltas -- Zero out all weight delta values, including
    // those for bias weights.
    public void clearWeightDeltas() {
	for (Layer lay : layers) {
	    // Clear out bias weight deltas ...
	    lay.clearBiasDeltas();
	    // Look at all projections coming into this layer ...
	    for (Projection p : lay.inputs) {
		// Clear out weight deltas for this projection ...
		p.clearWeightDeltas();
	    }
	}
    }

    // incrementWeightDeltas -- Increment all weight delta values, including
    // those for bias weights, using the current activation state of the
    // network and the current unit delta values.
    public void incrementWeightDeltas() {
	for (Layer lay : layers) {
	    // Increment bias weight deltas ...
	    lay.incrementBiasDeltas();
	    // Look at all projections coming into this layer ...
	    for (Projection p : lay.inputs) {
		// Increment weight deltas for this projection ...
		p.incrementWeightDeltas();
	    }
	}
    }

    // updateWeights -- Apply the current weight deltas to the actual weight
    // values, including bias weights, using the given learning rate.
    public void updateWeights(double lrate) {
	for (Layer lay : layers) {
	    // Update bias weights ...
	    lay.updateBiases(lrate);
	    // Look at all projections coming into this layer ...
	    for (Projection p : lay.inputs) {
		// Update weights for this projection ...
		p.updateWeights(lrate);
	    }
	}
    }

    // randomizeWeights -- Randomize all weights, including bias weights,
    // sampling new values uniformly from the specified range.
    public void randomizeWeights(double min, double max) {
	for (Layer lay : layers) {
	    lay.randomizeBiases(min, max);
	    for (Projection p : lay.inputs) {
		p.randomizeWeights(min, max);
	    }
	}
    }

    // readWeights -- Read weights from the given file.  Read bias weights
    // first, in the same order in which layers are stored in the Network
    // object.  Then read projection weight matrices in the order of inputs
    // to the layers.  Return false on error.
    public boolean readWeights(String wtsFilename) {
	try {
	    File wtsFile = new File(wtsFilename);
	    if (wtsFile.exists() && wtsFile.canRead()) {
		// Set up Scanner object ...
		FileInputStream wtsFileIn = new FileInputStream(wtsFile);
		InputStreamReader wtsISReader 
		    = new InputStreamReader(wtsFileIn);
		BufferedReader wtsBufferedReader 
		    = new BufferedReader(wtsISReader);
		Scanner wtsScanner = new Scanner(wtsBufferedReader);
		// Read bias weights ...
		for (Layer lay : layers) {
		    lay.bias.read(wtsScanner);
		}
		// Read weight matrices ...
		for (Layer lay : layers)
		    for (Projection p : lay.inputs)
			p.W.read(wtsScanner);
		// Success!
		return (true);
	    } else {
		// The file cannot be read ...
		return (false);
	    }
	} catch (IOException e) {
	    // Something went wrong ...
	    return (false);
	}
    }

    // writeWeights -- Write weights to the given file.  Use the same format
    // as that expected by the "readWeights" method, above.  Return false
    // on error.
    public boolean writeWeights(String wtsFilename) {
	try {
	    File wtsFile = new File(wtsFilename);
	    if (wtsFile.createNewFile() && wtsFile.canWrite()) {
		// Set up output stream ...
		FileOutputStream wtsFileOut = new FileOutputStream(wtsFile);
		PrintWriter wtsOut = new PrintWriter(wtsFileOut, true);
		// Write bias weights ...
		for (Layer lay : layers) {
		    lay.bias.write(wtsFileOut);
		    wtsOut.println();
		}
		// Write weight matrices ...
		for (Layer lay : layers)
		    for (Projection p : lay.inputs)
			p.W.write(wtsFileOut);
		// Success!
		return (true);
	    } else {
		// The file cannot be created ...
		return (false);
	    }
	} catch (IOException e) {
	    // Something went wrong ...
	    return (false);
	}
    }


}

