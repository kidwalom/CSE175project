//
// PatternSet
//
// This class implements a collection of input-output patterns.
//
// David Noelle -- Tue Apr 24 15:51:19 PDT 2007
//


import java.io.*;
import java.util.*;


public class PatternSet {

    public int inputN;              // dimensionality of input vectors
    public int outputN;             // dimensionality of output vectors
    public List<Pattern> patterns;  // list of input-output patterns

    // Default constructor ...
    public PatternSet() {
	this.inputN = 0;
	this.outputN = 0;
	this.patterns = new LinkedList<Pattern>();
    }

    // Copy constructor ...
    public PatternSet(PatternSet ps) {
	this.inputN = ps.inputN;
	this.outputN = ps.outputN;
	this.patterns = new LinkedList<Pattern>();
	for (Pattern p : ps.patterns) {
	    Pattern newPat = new Pattern(p);
	    this.patterns.add(newPat);
	}
    }

    // numPatterns -- Return the number of patterns in the pattern set.
    public int numPatterns() {
	return (patterns.size());
    }

    // readPatterns -- Read a pattern set from the given file.  The first
    // entry in this file should be a number of patterns.  The next should
    // be the number of inputs per pattern.  This should be followed by 
    // the number outputs per pattern.  The remaining values should be
    // real numbers composing the patterns, themselves.  Return false on
    // error.
    public boolean readPatterns(String patsFilename) {
	try {
	    File patsFile = new File(patsFilename);
	    if (patsFile.exists() && patsFile.canRead()) {
		// Set up Scanner object ...
		FileInputStream patsFileIn = new FileInputStream(patsFile);
		InputStreamReader patsISReader 
		    = new InputStreamReader(patsFileIn);
		BufferedReader patsBufferedReader 
		    = new BufferedReader(patsISReader);
		Scanner patsScanner = new Scanner(patsBufferedReader);
		// Read pattern set parameters ...
		if (!(patsScanner.hasNextInt()))
		    return (false);
		int numPats = patsScanner.nextInt();
		if (!(patsScanner.hasNextInt()))
		    return (false);
		inputN = patsScanner.nextInt();
		if (!(patsScanner.hasNextInt()))
		    return (false);
		outputN = patsScanner.nextInt();
		// Read the patterns into a matrix ...
		Matrix patsM = new Matrix(numPats, (inputN + outputN));
		if (!(patsM.read(patsScanner)))
		    return (false);
		// Translate the matrix into Pattern objects ...
		for (int pat = 0; pat < numPats; pat++) {
		    patterns.add(new Pattern(inputN, outputN, 
					     patsM.extractRow(pat)));
		}
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


}

