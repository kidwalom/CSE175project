//
// Pattern
//
// This class implements a single input-output pattern.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


public class Pattern {

    public Vector input;        // the input pattern
    public Vector target;       // the target pattern

    // Default constructor ...
    public Pattern() {
	this.input = new Vector();
	this.target = new Vector();
    }

    // Constructor with sizes specified ...
    public Pattern(int inputDim, int outputDim) {
	this.input = new Vector(inputDim);
	this.target = new Vector(outputDim);
    }

    // Constructor with sizes and contents specified ...
    public Pattern(int inputDim, int outputDim, double[] pat) {
	this.input = new Vector(inputDim);
	this.target = new Vector(outputDim);
	for (int i = 0; i < inputDim; i++)
	    this.input.set(i, pat[i]);
	for (int i = 0; i < outputDim; i++)
	    this.target.set(i, pat[i+inputDim]);
    }

    // Constructor with sizes and contents specified as a vector ...
    public Pattern(int inputDim, int outputDim, Vector v) {
	this.input = new Vector(inputDim);
	this.target = new Vector(outputDim);
	for (int i = 0; i < inputDim; i++)
	    this.input.set(i, v.get(i));
	for (int i = 0; i < outputDim; i++)
	    this.target.set(i, v.get(i+inputDim));
    }

    // Copy constructor ...
    public Pattern(Pattern p) {
	this.input = new Vector(p.input);
	this.target = new Vector(p.target);
    }

    // set -- Set the pattern to match the given vector values.
    public void set(int inputDim, int outputDim, Vector v) {
	input.resize(inputDim);
	target.resize(outputDim);
	for (int i = 0; i < inputDim; i++)
	    input.set(i, v.get(i));
	for (int i = 0; i < outputDim; i++)
	    target.set(i, v.get(i+inputDim));
    }


}

