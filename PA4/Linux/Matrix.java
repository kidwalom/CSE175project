//
// Matrix
//
// This class implements a real matrix.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


import java.io.*;
import java.util.*;


public class Matrix {

    int rows;       // number of rows in the matrix
    int cols;       // number of columns in the matrix
    double[][] el;  // the elements of the matrix

    // Default constructor ...
    public Matrix() {
	this.rows = -1;
	this.cols = -1;
	this.el = null;
    }

    // Constructor with dimensionality and initial value specified ...
    public Matrix(int rows, int cols, double initialValue) {
	if ((rows > 0) && (cols > 0)) {
	    this.rows = rows;
	    this.cols = cols;
	    this.el = new double[rows][cols];
	    for (int i = 0; i < this.rows; i++)
		for (int j = 0; j < this.cols; j++)
		    this.el[i][j] = initialValue;
	} else {
	    this.rows = -1;
	    this.cols = -1;
	    this.el = null;
	}
    }

    // Constructor with dimensionality specified ...
    public Matrix(int rows, int cols) {
	this(rows, cols, 0.0);
    }

    // Copy constructor ...
    public Matrix(Matrix m) {
	this.rows = m.rows;
	this.cols = m.cols;
	this.el = new double[this.rows][this.cols];
	for (int i = 0; i < this.rows; i++)
	    for (int j = 0; j < this.cols; j++)
		this.el[i][j] = m.el[i][j];
    }

    // numRows -- Return the number of rows in this matrix, or a negative
    // value on error.
    public int numRows() {
	if (cols > 0)
	    return (rows);
	else
	    return (-1);
    }

    // numColumns -- Return the number of columns in this matrix, or a 
    // negative value on error.
    public int numColumns() {
	if (rows > 0)
	    return (cols);
	else
	    return (-1);
    }

    // valid -- Return true if and only if this matrix has at least one
    // element and is valid.
    public boolean valid() {
	return ((rows > 0) && (cols > 0));
    }

    // invalidate -- Mark this matrix as invalid.
    public Matrix invalidate() {
	rows = -1;
	cols = -1;
	el = null;
	return (this);
    }

    // resize -- Change the dimensionality of the given matrix to the
    // specified new number of rows and columns.  Matrix element values may 
    // be lost in this process.
    public void resize(int rows, int cols) {
	if ((rows > 0) && (cols > 0)) {
	    double[][] newArray = new double[rows][cols];
	    for (int i = 0; (i < rows) && (i < this.rows); i++)
		for (int j = 0; (j < cols) && (j < this.cols); j++)
		    newArray[i][j] = el[i][j];
	    el = newArray;
	} else {
	    invalidate();
	}
    }

    // get -- Return the matrix element at the given location (zero-indexed).
    // Return 0.0 on error.
    public double get(int i, int j) {
	if ((i >= 0) && (i < rows) && (j >= 0) && (j < cols)) {
	    return (el[i][j]);
	} else {
	    return (0.0);
	}
    }

    // set -- Set the matrix element at the given location (zero-indexed) to
    // the given value.  Return the value, or 0.0 on error.
    public double set(int i, int j, double val) {
	if ((i >= 0) && (i < rows) && (j >= 0) && (j < cols)) {
	    el[i][j] = val;
	    return (val);
	} else {
	    return (0.0);
	}
    }

    // extractRow -- Return a freshly allocated vector containing the values
    // in the given row of this matrix.
    public Vector extractRow(int i) {
	Vector resultV = new Vector(cols);
	for (int j = 0; j < cols; j++)
	    resultV.set(j, el[i][j]);
	return (resultV);
    }
	
    // extractColumn -- Return a freshly allocated vector containing the
    // values in the given column of this matrix.
    public Vector extractColumn(int j) {
	Vector resultV = new Vector(rows);
	for (int i = 0; i < rows; i++)
	    resultV.set(i, el[i][j]);
	return (resultV);
    }

    // multiplyByScalar -- Return a newly allocated matrix with elements
    // equal to the elements of this matrix multiplied by the given scalar 
    // factor.  Return an invalid matrix on error.
    public Matrix multiplyByScalar(double val) {
	if (!(valid())) {
	    return (new Matrix().invalidate());
	} else {
	    Matrix newM = new Matrix(this);
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++)
		    newM.el[i][j] = el[i][j] * val;
	    return (newM);
	}
    }

    // sum -- Return a newly allocated matrix which is the sum of this
    // matrix and the specified argument matrix.  Return an invalid matrix
    // on error.
    public Matrix sum(Matrix m) {
	if ((!(valid())) || (!(m.valid())) || 
	    (rows != m.rows) || (cols != m.cols)) {
	    return (new Matrix().invalidate());
	} else {
	    Matrix newM = new Matrix(this);
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++)
		    newM.el[i][j] = newM.el[i][j] + m.el[i][j];
	    return (newM);
	}
    }

    // difference -- Return a newly allocated matrix which is the result of
    // taking this matrix and subtracting off the specified argument matrix.
    // Return an invalid matrix on error.
    public Matrix difference(Matrix m) {
	return (sum(m.multiplyByScalar(-1.0)));
    }

    // transpose -- Return a newly allocated matrix which is the transpose
    // of this matrix.  Return an invalid matrix on error.
    public Matrix transpose() {
	if (!(valid())) {
	    return (new Matrix().invalidate());
	} else {
	    Matrix transM = new Matrix(cols, rows);
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++)
		    transM.el[j][i] = el[i][j];
	    return (transM);
	}
    }

    // product -- Return a newly allocated vector which is the result of
    // multiplying this matrix by the given argument vector.  Return an
    // invalid vector on error.
    public Vector product(Vector v) {
	if ((!(valid())) || (!(v.valid())) || (numColumns() != v.dim())) {
	    return (new Vector().invalidate());
	} else {
	    Vector resultV = new Vector(numRows());
	    double currentValue;
	    for (int i = 0; i < rows; i++) {
		currentValue = 0.0;
		for (int j = 0; j < cols; j++)
		    currentValue = currentValue + (v.get(j) * el[i][j]);
		resultV.set(i, currentValue);
	    }
	    return (resultV);
	}
    }

    // randomize -- Replace the elements of this matrix with random values
    // sampled uniformly from the given range.
    public void randomize(double min, double max) {
	Random generator = new Random();
	if (valid()) {
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++)
		    el[i][j] = (generator.nextDouble() * (max - min)) + min;
	}
    }

    // read -- Read a matrix from the given scanner, reading a number of
    // elements equal to the dimensionality of this Matrix object.  Return
    // false on error.
    public boolean read(Scanner inScanner) {
	if (valid()) {
	    inScanner.useDelimiter("[\\s]+");
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++) {
		    if (inScanner.hasNextDouble()) {
			// There is a value ...
			el[i][j] = inScanner.nextDouble();
		    } else {
			// There is nothing to read ...
			return (false);
		    }
		}
	    // Read all of the values ...
	    return (true);
	} else {
	    // This matrix is invalid ...
	    return (false);
	}
    }

    // write -- Write this matrix to the given stream, separating elements
    // with single spaces but separating rows with line breaks.
    public void write(OutputStream str) {
	if (valid()) {
	    PrintWriter out = new PrintWriter(str, true);
	    for (int i = 0; i < rows; i++) {
		out.printf("%f", el[i][0]);
		for (int j = 1; j < cols; j++) {
		    out.printf(" %f", el[i][j]);
		}
		out.printf("\n");
	    }
	}
    }


}

