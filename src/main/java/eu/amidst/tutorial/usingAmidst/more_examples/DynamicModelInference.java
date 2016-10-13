package eu.amidst.tutorial.usingAmidst.more_examples;


import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.distribution.UnivariateDistribution;
import eu.amidst.core.inference.ImportanceSampling;
import eu.amidst.core.variables.Variable;
import eu.amidst.dynamic.datastream.DynamicDataInstance;
import eu.amidst.dynamic.inference.FactoredFrontierForDBN;
import eu.amidst.dynamic.inference.InferenceAlgorithmForDBN;
import eu.amidst.dynamic.io.DynamicBayesianNetworkLoader;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;
import eu.amidst.dynamic.models.DynamicBayesianNetwork;

import java.io.IOException;

/**
 * Created by rcabanas on 23/05/16.
 */
public class DynamicModelInference {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		//Load the DBN
		DynamicBayesianNetwork dbn = DynamicBayesianNetworkLoader
				.loadFromFile("networks/simulated/BCCDBN.dbn");

		//Testing dataset to predict
		String path = "datasets/simulated/";
		String filename = "BCC_month1.arff";
		DataStream<DynamicDataInstance> dataPredict =
				DynamicDataStreamLoader.loadFromFile(path+filename);

		//Select the inference algorithm
		InferenceAlgorithmForDBN infer =
				new FactoredFrontierForDBN(new ImportanceSampling());
		infer.setModel(dbn);

		// Set the target variables
		Variable varTarget = dbn.getDynamicVariables()
                    .getVariableByName("discreteHiddenVar");

		//Predict the hidden variable at time t and t+2
		UnivariateDistribution p = null;
		int t = 0;
		for (DynamicDataInstance instance : dataPredict) {

			infer.addDynamicEvidence(instance);
			infer.runInference();

			p = infer.getFilteredPosterior(varTarget);
			System.out.println("t="+t+", P(discreteHiddenVar|E)="+ p);

			p = infer.getPredictivePosterior(varTarget, 2);
			System.out.println("t="+t+"+2, P(discreteHiddenVar|E)="+p);

			t++;
		}





	}


}
