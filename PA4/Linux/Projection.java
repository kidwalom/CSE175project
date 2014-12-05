//
// Projection
//
// This class implements a projection of connections between two layers.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


public class Projection {
    
    public int inputN;                  // number of units on the input side
    public int outputN;                 // number of units on the output side
    public Layer input;                 // the layer providing input
    public Layer output;                // the layer receiving output
    public Matrix W;                    // the actual weight matrix
    public Matrix deltaW;               // weight delta values

    // Default constructor ...
    public Projection() {
	this.inputN = -1;
	this.outputN = -1;
	this.input = null;
	this.output = null;
	this.W = null;
	this.deltaW = null;
    }

    // Constructor with size specified ...
    public Projection(int inputN, int outputN) {
	if ((inputN > 0) && (outputN > 0)) {
	    this.inputN = inputN;
	    this.outputN = outputN;
	    this.input = null;
	    this.output = null;
	    this.W = new Matrix(outputN, inputN);
	    this.deltaW = new Matrix(outputN, inputN);
	} else {
	    this.inputN = -1;
	    this.outputN = -1;
	    this.input = null;
	    this.output = null;
	    this.W = null;
	    this.deltaW = null;
	}
    }

    // Constructor with layers specified ...
    public Projection(Layer input, Layer output) {
	if ((input != null) && (output != null) &&
	    (input.n > 0) && (output.n > 0)) {
	    this.inputN = input.n;
	    this.outputN = output.n;
	    this.W = new Matrix(this.outputN, this.inputN);
	    this.deltaW = new Matrix(this.outputN, this.inputN);
	    input.addOutputProjection(this);
	    output.addInputProjection(this);
	} else {
	    this.inputN = -1;
	    this.outputN = -1;
	    this.input = input;
	    this.output = output;
	    this.W = null;
	    this.deltaW = null;
	}
    }

    // resize -- Change the size of this projection.  All weight values and
    // weight deltas are lost in this process.  Connections to input and
    // output layers are also severed.
    public void resize(int inputN, int outputN) {
	if ((inputN > 0) && (outputN > 0)) {
	    this.inputN = inputN;
	    this.outputN = outputN;
	    this.input = null;
	    this.output = null;
	    this.W = new Matrix(outputN, inputN);
	    this.deltaW = new Matrix(outputN, inputN);
	}
    }

    // clearWeightDeltas -- Set all weight deltas in the projection to zero.
    public void clearWeightDeltas() {
	for (int i = 0; i < deltaW.numRows(); i++)
	    for (int j = 0; j < deltaW.numColumns(); j++)
		deltaW.set(i, j, 0.0);
    }

    // randomizeWeights -- Set the weights to random values sampled
    // uniformly from the given range.
    public void randomizeWeights(double min, double max) {
	W.randomize(min, max);
    }

    // incrementWeightDeltas -- Update how much the weights should
    // change, as a function of the current unit delta values.  Note that
    // this increments the current weight delta values, allowing multiple 
    // weight changes to be "summed up".  Note also that these weight delta
    // values are prior to the application of the learning rate.
    public void incrementWeightDeltas() {
	deltaW = deltaW.sum(output.delta.outerProduct(input.act));
    }

    // updateWeights -- Update the weights.
    public void updateWeights(double learningRate) {
	W = W.sum(deltaW.multiplyByScalar(learningRate));
    }


}

