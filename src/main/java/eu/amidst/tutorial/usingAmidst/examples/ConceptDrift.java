package eu.amidst.tutorial.usingAmidst.examples;

import eu.amidst.core.conceptdrift.NaiveBayesVirtualConceptDriftDetector;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataOnMemory;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.variables.Variable;

/**
 * Created by rcabanas on 12/08/16.
 */
public class ConceptDrift {

	public static void main(String[] args) {


		String path = "datasets/simulated/";
		String filename = path+"BCCDefault.arff";
		int windowSize = 500; // instances per month

		//We open the data stream
		DataStream<DataInstance> data =
						DataStreamLoader.open(filename);

		//We create a NaiveBayesVirtualConceptDriftDetector object
		NaiveBayesVirtualConceptDriftDetector virtualDriftDetector = new NaiveBayesVirtualConceptDriftDetector();

		//We associate the stream to the detector
		virtualDriftDetector.setData(data);

		//We set class variable
		virtualDriftDetector.setClassIndex(2);
		virtualDriftDetector.setWindowsSize(500);
		virtualDriftDetector.setNumberOfGlobalVars(1);
		virtualDriftDetector.setTransitionVariance(0.1);

		//We should invoke this method before processing any data
		virtualDriftDetector.initLearning();

		//At each iteration, a value for the hidden variable
		//is obtained
		int month = 0;
		for (DataOnMemory<DataInstance> batch :
						data.iterableOverBatches(windowSize)){
			double[] H = virtualDriftDetector.updateModel(batch);
			System.out.println(H[0]);
			month++;
		}



	}


}
