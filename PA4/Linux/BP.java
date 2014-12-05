//
// BP
//
// This class implements the backpropagation learning algorithm.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


import java.io.*;


public class BP {

    public Network net;           // the neural network
    public PatternSet trainPats;  // the training set of patterns
    public PatternSet testPats;   // the testing set of patterns
    Layer inputLayer;             // the network input layer
    Layer outputLayer;            // the network output layer
    double lrate;                 // the network learning rate
    double initialWtRange;        // range of initial weight values
    int epochs;                   // current number of training epochs

    // Default constructor ...
    public BP() {
	this.net = new Network();
	this.trainPats = null;
	this.testPats = null;
	this.inputLayer = null;
	this.outputLayer = null;
	this.lrate = 0.1;
	this.initialWtRange = 1.0;
	this.epochs = 0;
    }

    // readTrainingPatterns -- Read training patterns from the specified
    // file.  Return false on error.
    public boolean readTrainingPatterns(String filename) {
	this.trainPats = new PatternSet();
	return (this.trainPats.readPatterns(filename));
    }

    // readTestingPatterns -- Read testing patterns from the specified file.
    // Return false on error.
    public boolean readTestingPatterns(String filename) {
	this.testPats = new PatternSet();
	return (this.testPats.readPatterns(filename));
    }
    
    // learningRate -- Return the current learning rate.
    public double learningRate() {
	return (lrate);
    }

    // setLearningRate -- Set the learning rate to the given value.  Return
    // the new learning rate.
    public double setLearningRate(double val) {
	if (val > 0.0)
	    lrate = val;
	return (lrate);
    }

    // initialWeightRange -- Return the size of the current range from
    // which random initial weight values are sampled.
    public double initialWeightRange() {
	return (initialWtRange);
    }

    // setInitialWeightRange -- Set the size of the current range from
    // which random initial weight values are sampled.  Return the new
    // value of this parameter.
    public double setInitialWeightRange(double val) {
	if (val > 0.0)
	    initialWtRange = val;
	return (initialWtRange);
    }

    // initNetwork -- Initialize the network for training.
    public boolean initNetwork() {
	// Find the input and output layers ...
	inputLayer = null;
	outputLayer = null;
	for (Layer lay : net.layers) {
	    // The first layer with no inputs becomes the input layer ...
	    if ((inputLayer == null) && (lay.inputs.isEmpty()))
		inputLayer = lay;
	    // The last layer with no outputs becomes the output layer ...
	    if (lay.outputs.isEmpty())
		outputLayer = lay;
	}
	if ((inputLayer == null) || (outputLayer == null))
	    return (false);
	// Randomize weights ...
	net.randomizeWeights((-0.5 * initialWtRange), (0.5 * initialWtRange));
	// Reset the training epoch counter ...
	epochs = 0;
	// Success!
	return (true);
    }

    // runTrainingEpoch -- Train the network on all of the patterns in the
    // training set, exposing the network to each pattern once and updating
    // connection weights only once (i.e., in "batch mode").  Return the
    // sum-squared error of the network, summed over all of the patterns
    // in the training set.  Return a negative value on error.
    public double runTrainingEpoch() {
	// Initialize the sum squared error value for this epoch ...
	double thisSSE = 0.0;
	double totalSSE = 0.0;
	// First, we need to clear out any old weight-change values ...
	net.clearWeightDeltas();
	// Iterate over all of the training patterns ...
	for (Pattern pat : trainPats.patterns) {
	    // Load the pattern into the network layers ...
	    if (!(inputLayer.loadInput(pat.input)))
		return (-1.0);
	    if (!(outputLayer.loadTarget(pat.target)))
		return (-1.0);
	    // Propagate activation forward ...
	    net.computeActivation();
	    // Record the error on this pattern ...
	    thisSSE = outputLayer.act.squaredError(outputLayer.targ);
	    if (thisSSE < 0.0)
		return (-1.0);
	    totalSSE = totalSSE + thisSSE;
	    // Propagate error backward ...
	    net.computeDelta();
	    // Calculate associated weight changes, incrementing weight 
	    // delta values ...
	    net.incrementWeightDeltas();
	    // Done with this pattern ...
	}
	// Now that all patterns have been seen, update weight values ...
	net.updateWeights(lrate);
	// Increment the epoch counter ...
	epochs = epochs + 1;
	// We're done ...
	return (totalSSE);
    }

    // runTestingEpoch -- Test the network on all of the patterns in the
    // testing set, exposing the network to each pattern once and writing
    // the resulting output activation vector to the given stream.  Do not
    // change the network weight values in any way.  Return the sum-squared
    // error of the network, summed over all of the patterns in the testing
    // set.  Return a negative value on error.
    public double runTestingEpoch(OutputStream str) {
	// Set up the output stream as a PrintWriter object ...
	PrintWriter out = null;
	if (str != null) {
	    out = new PrintWriter(str, true);
	}
	// Initialize the sum squared error value for this epoch ...
	double thisSSE = 0.0;
	double totalSSE = 0.0;
	// Iterate over all of the testing patterns ...
	for (Pattern pat : testPats.patterns) {
	    // Load the pattern into the network layers ...
	    if (!(inputLayer.loadInput(pat.input)))
		return (-1.0);
	    if (!(outputLayer.loadTarget(pat.target)))
		return (-1.0);
	    // Propagate activation forward ...
	    net.computeActivation();
	    // Record the error on this pattern ...
	    thisSSE = outputLayer.act.squaredError(outputLayer.targ);
	    if (thisSSE < 0.0)
		return (-1.0);
	    totalSSE = totalSSE + thisSSE;
	    // Output the result for this pattern ...
	    if (out != null) {
		out.printf("\n");
		out.printf("INPUT:   ");
		inputLayer.act.write(str);
		out.printf("\n");
		out.printf("OUTPUT:  ");
		outputLayer.act.write(str);
		out.printf("\n");
		out.printf("TARGET:  ");
		outputLayer.targ.write(str);
		out.printf("\n");
		out.printf("SSE = %f\n", thisSSE);
	    }
	    // Done with this pattern ...
	}
	// We're done ...
	return (totalSSE);
    }

    // runTestingEpoch -- Test the network on all of the patterns in the
    // testing set, exposing the network to each pattern once.  Do not
    // change the network weight values in any way.  Return the sum-squared
    // error of the network, summed over all of the patterns in the testing
    // set.  Return a negative value on error.
    public double runTestingEpoch() {
	return (runTestingEpoch(null));
    }


}

