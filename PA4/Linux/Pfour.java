//
// Pfour
//
// This class provides a "main" method that acts as a driver program for
// a very simple "batch mode" backpropagtion artificial neural network.
//
// David Noelle -- Tue Nov 20 21:08:51 PST 2012
//


import java.io.*;
import java.util.*;


public class Pfour {

    public static void main(String[] args) {
	try {
	    BP backprop = new BP();
	    InputStreamReader converter = new InputStreamReader(System.in);
	    BufferedReader in = new BufferedReader(converter);
	    Scanner inScanner = new Scanner(in);
	    int numInputUnits = 2;
	    int numHiddenUnits = 2;
	    int numOutputUnits = 1;
	    double lRate = 0.1;
	    double initialWeightRange = 1.0;
	    int epochReportBlock = 10;
	    int epochCriterion = 1000;
	    double stoppingCriterion = 0.05;
	    String trainingFilename;
	    String testingFilename;

	    System.out.println("BACKPROPAGATION LEARNING ALGORITHM");
	    // Get network information ...
	    System.out.println("Enter the number of input units:");
	    if (!(inScanner.hasNextInt())) {
		System.err.println("Invalid number of input units.");
		return;
	    }
	    numInputUnits = inScanner.nextInt();
	    if (numInputUnits < 1) {
		System.err.println("Invalid number of input units.");
		return;
	    }
	    System.out.println("Enter the number of hidden units:");
	    if (!(inScanner.hasNextInt())) {
		System.err.println("Invalid number of hidden units.");
		return;
	    }
	    numHiddenUnits = inScanner.nextInt();
	    if (numHiddenUnits < 1) {
		System.err.println("Invalid number of hidden units.");
		return;
	    }
	    System.out.println("Enter the number of output units:");
	    if (!(inScanner.hasNextInt())) {
		System.err.println("Invalid number of output units.");
		return;
	    }
	    numOutputUnits = inScanner.nextInt();
	    if (numOutputUnits < 1) {
		System.err.println("Invalid number of output units.");
		return;
	    }
	    // Get training parameter information ...
	    System.out.println("Enter the learning rate:");
	    if (!(inScanner.hasNextDouble())) {
		System.err.println("Invalid learning rate.");
		return;
	    }
	    lRate = inScanner.nextDouble();
	    if (lRate < 0.0) {
		System.err.println("Invalid learning rate.");
		return;
	    }
	    System.out.println("Enter the maximum number of training epochs:");
	    if (!(inScanner.hasNextInt())) {
		System.err.println("Invalid epoch count stopping criterion.");
		return;
	    }
	    epochCriterion = inScanner.nextInt();
	    if (epochCriterion < 1) {
		System.err.println("Invalid epoch count stopping criterion.");
		return;
	    }
	    System.out.println("Enter the SSE stopping criterion:");
	    if (!(inScanner.hasNextDouble())) {
		System.err.println("Invalid SSE stopping criterion.");
		return;
	    }
	    stoppingCriterion = inScanner.nextDouble();
	    if (stoppingCriterion < 0.0) {
		System.err.println("Invalid SSE stopping criterion.");
		return;
	    }
	    // Get pattern set information ...
	    System.out.println("Enter the training set file name:");
	    if (!(inScanner.hasNext())) {
		System.err.println("Invalid training set file name.");
		return;
	    }
	    trainingFilename = inScanner.next();
	    System.out.println("Enter the testing set file name:");
	    if (!(inScanner.hasNext())) {
		System.err.println("Invalid testing set file name.");
		return;
	    }
	    testingFilename = inScanner.next();
	    // Build the network ...
	    Layer inLayer = backprop.net.createLayer(numInputUnits);
	    Layer hidLayer = backprop.net.createLayer(numHiddenUnits);
	    Layer outLayer = backprop.net.createLayer(numOutputUnits);
	    if ((inLayer == null) || 
		(hidLayer == null) || 
		(outLayer == null)) {
		System.err.println("Unable to create layers.");
		return;
	    }
	    if ((backprop.net.createProjection(inLayer, hidLayer) == null) ||
		(backprop.net.createProjection(hidLayer, outLayer) == null)) {
		System.err.println("Unable to create projections.");
		return;
	    }
	    // Read the pattern sets ...
	    if (!(backprop.readTrainingPatterns(trainingFilename))) {
		System.err.println("Unable to read training pattern file.");
		return;
	    }
	    if (!(backprop.readTestingPatterns(testingFilename))) {
		System.err.println("Unable to read testing pattern file.");
		return;
	    }
	    // Set learning parameters ...
	    backprop.setLearningRate(lRate);
	    backprop.setInitialWeightRange(initialWeightRange);
	    // Perform training ...
	    double thisSSE = stoppingCriterion + 1.0;
	    backprop.initNetwork();
	    while ((backprop.epochs < epochCriterion) &&
		   (thisSSE > stoppingCriterion)) {
		thisSSE = backprop.runTrainingEpoch();
		if (thisSSE < 0.0) {
		    System.err.println("Training epoch failed.");
		    return;
		}
		if (backprop.epochs % epochReportBlock == 0) {
		    // Report on training progress ...
		    System.out.println("Epoch " + 
				       String.valueOf(backprop.epochs) + 
				       ":  SSE = " + 
				       String.valueOf(thisSSE) + 
				       ".");
		}
	    }
	    System.out.println("Final Epoch " +
			       String.valueOf(backprop.epochs) + 
			       ":  SSE = " + 
			       String.valueOf(thisSSE) + 
			       ".");
	    // Perform testing ...
	    System.out.println();
	    thisSSE = backprop.runTestingEpoch(System.out);
	    if (thisSSE < 0.0) {
		System.err.println("Testing epoch failed.");
		return;
	    }
	    System.out.println("Testing SSE = " + 
			       String.valueOf(thisSSE) + ".");
	    System.out.println();
	    // Done ...
	    System.out.println("ARTIFICIAL NEURAL NETWORK RUN COMPLETE");
	} catch (Exception e) {
	    // Something went wrong ...
	}
    }

    
}
