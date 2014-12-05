//
// Vector
//
// This class implements a real vector.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


import java.io.*;
import java.util.*;


public class Vector {

    int n;         // number of elements in the vector
    double[] el;   // the elements of the vector

    // Default constructor ...
    public Vector() {
	this.n = -1;
	this.el = null;
    }

    // Constructor with dimensionality and initial value specified ...
    public Vector(int dim, double initialValue) {
	if (dim > 0) {
	    this.n = dim;
	    this.el = new double[dim];
	    for (int i = 0; i < this.n; i++)
		this.el[i] = initialValue;
	} else {
	    this.n = -1;
	    this.el = null;
	}
    }

    // Constructor with dimensionality specified ...
    public Vector(int dim) {
	this(dim, 0.0);
    }

    // Copy constructor ...
    public Vector(Vector v) {
	this.n = v.n;
	this.el = new double[this.n];
	for (int i = 0; i < this.n; i++)
	    this.el[i] = v.el[i];
    }

    // dim -- Return the dimensionality of the vector, or a negative value
    // on error.
    public int dim() {
	if (n > 0)
	    return (n);
	else
	    return (-1);
    }

    // valid -- Return true if and only if the vector has at least one
    // element and is valid.
    public boolean valid() {
	return (n > 0);
    }

    // invalidate -- Mark this vector as invalid.
    public Vector invalidate() {
	n = -1;
	el = null;
	return (this);
    }

    // copy -- Make this vector be a copy of the given argument vector.
    // Do not allocate new storage, unless such is necessary.  Return this
    // vector, which should be invalidated on error.
    public Vector copy(Vector v) {
	if (this.n != v.n)
	    resize(v.n);
	for (int i = 0; i < this.n; i++)
	    this.el[i] = v.el[i];
	return (this);
    }

    // resize -- Change the dimensionality of the given vector to the
    // specified new value.  Vector element values may be lost in this
    // process.
    public void resize(int dim) {
	if (dim > 0) {
	    double[] newVec = new double[dim];
	    for (int i = 0; (i < dim) && (i < this.n); i++)
		newVec[i] = el[i];
	    el = newVec;
	} else {
	    invalidate();
	}
    }

    // get -- Return the vector element at the given location (zero-indexed).
    // Return 0.0 on error.
    public double get(int i) {
	if ((i >= 0) && (i < n)) {
	    return (el[i]);
	} else {
	    return (0.0);
	}
    }

    // set -- Set the vector element at the given location (zero-indexed) to
    // the given value.  Return the value, or 0.0 on error.
    public double set(int i, double val) {
	if ((i >= 0) && (i < n)) {
	    el[i] = val;
	    return (val);
	} else {
	    return (0.0);
	}
    }
	
    // multiplyByScalar -- Return a newly allocated vector with elements
    // equal to the elements of this vector multiplied by the given scalar 
    // factor.  Return an invalid vector on error.
    public Vector multiplyByScalar(double val) {
	if (!(valid())) {
	    return (new Vector().invalidate());
	} else {
	    Vector newV = new Vector(this);
	    for (int i = 0; i < n; i++)
		newV.el[i] = el[i] * val;
	    return (newV);
	}
    }

    // sum -- Return a newly allocated vector which is the sum of this
    // vector and the specified argument vector.  Return an invalid vector
    // on error.
    public Vector sum(Vector v) {
	if ((!(valid())) || (!(v.valid())) || (n != v.n)) {
	    return (new Vector().invalidate());
	} else {
	    Vector newV = new Vector(this);
	    for (int i = 0; i < n; i++)
		newV.el[i] = newV.el[i] + v.el[i];
	    return (newV);
	}
    }

    // difference -- Return a newly allocated vector which is the result of
    // taking this vector and subtracting off the specified argument vector.
    // Return an invalid vector on error.
    public Vector difference(Vector v) {
	return (sum(v.multiplyByScalar(-1.0)));
    }

    // sumOfElements -- Return the scalar sum of all of this vector's 
    // elements.  Return 0.0 on error.
    public double sumOfElements() {
	if (!(valid())) {
	    return (0.0);
	} else {
	    double sum = 0.0;
	    for (int i = 0; i < n; i++)
		sum = sum + el[i];
	    return (sum);
	}
    }

    // innerProduct -- Return the scalar inner product (i.e., dot product)
    // of this vector and the given argument vector.  Return 0.0 on error.
    public double innerProduct(Vector v) {
	if ((!(valid())) || (!(v.valid())) || (n != v.n)) {
	    return (0.0);
	} else {
	    Vector prodV = new Vector(this);
	    for (int i = 0; i < n; i++)
		prodV.el[i] = prodV.el[i] * v.el[i];
	    return (prodV.sumOfElements());
	}
    }

    // outerProduct -- Return the matrix outer product of this vector and
    // the given argument vector, allocating the matrix in the process.
    // Return an invalid matrix on error.
    public Matrix outerProduct(Vector v) {
	if ((!(valid())) || (!(v.valid()))) {
	    return (new Matrix().invalidate());
	} else {
	    Matrix resultM = new Matrix(this.n, v.n);
	    for (int i = 0; i < this.n; i++)
		for (int j = 0; j < v.n; j++)
		    resultM.set(i, j, (this.get(i) * v.get(j)));
	    return (resultM);
	}
    }

    // squaredError -- Return one half of the summed squared deviation 
    // between this vector and a given target vector.  Return a negative
    // value on error.
    public double squaredError(Vector target) {
	if ((!(valid())) || (!(target.valid())) || (n != target.n)) {
	    return (-1.0);
	} else {
	    Vector deviationV = difference(target);
	    if (deviationV.valid()) {
		return (0.5 * deviationV.innerProduct(deviationV));
	    } else {
		return (-1.0);
	    }
	}
    }

    // squash -- Apply a logistic sigmoid function, scaled between the
    // given minimum and maximum values, to each of the elements of this
    // vector, returning a newly allocated vector as a result.  Return an
    // invalid vector on error.
    public Vector squash(double min, double max) {
	if ((!(valid())) || (!(min < max))) {
	    return (new Vector().invalidate());
	} else {
	    Vector squashedV = new Vector(this);
	    double logisticValue;
	    for (int i = 0; i < n; i++) {
		// Generate the zero-to-one squashed value ...
		logisticValue = 1.0 / (1.0 + Math.exp(- squashedV.el[i]));
		// Rescale to between min and max ...
		squashedV.el[i] = (logisticValue * (max - min)) + min;
	    }
	    return (squashedV);
	}
    }

    // derivative -- Apply the derivative of the logistic sigmoid function,
    // scaled between the given minimum and maximum values, to each of the 
    // elements of this vector, returning a newly allocated vector as a 
    // result.  Return an invalid vector on error.
    public Vector derivative(double min, double max) {
	if ((!(valid())) || (!(min < max))) {
	    return (new Vector().invalidate());
	} else {
	    Vector derV = new Vector(this);
	    double logisticValue;
	    for (int i = 0; i < n; i++) {
		// Generate the zero-to-one squashed value ...
		logisticValue = 1.0 / (1.0 + Math.exp(- derV.el[i]));
		// Calculate the derivative of the logisitc, and rescale it 
		// to between min and max ...
		derV.el[i] 
		    = (logisticValue * (1.0 - logisticValue)) * (max - min);
	    }
	    return (derV);
	}
    }

    // randomize -- Replace the elements of this vector with random values
    // sampled uniformly from the given range.
    public void randomize(double min, double max) {
	Random generator = new Random();
	if (valid()) {
	    for (int i = 0; i < n; i++)
		el[i] = (generator.nextDouble() * (max - min)) + min;
	}
    }

    // read -- Read a vector from the given scanner, reading a number of
    // elements equal to the dimensionality of this Vector object.  Return
    // false on error.
    public boolean read(Scanner inScanner) {
	if (valid()) {
	    inScanner.useDelimiter("[\\s]+");
	    for (int i = 0; i < n; i++) {
		if (inScanner.hasNextDouble()) {
		    // There is a value ...
		    el[i] = inScanner.nextDouble();
		} else {
		    // There is nothing to read ...
		    return (false);
		}
	    }
	    // Read all of the values ...
	    return (true);
	} else {
	    // This vector is invalid ...
	    return (false);
	}
    }

    // write -- Write this vector to the given stream, separating elements
    // with a single space.
    public void write(OutputStream str) {
	if (valid()) {
	    PrintWriter out = new PrintWriter(str, true);
	    out.printf("%f", el[0]);
	    for (int i = 1; i < n; i++)
		out.printf(" %f", el[i]);
	}
    }


}

