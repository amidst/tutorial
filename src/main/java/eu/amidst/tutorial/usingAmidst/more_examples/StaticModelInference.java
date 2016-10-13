package eu.amidst.tutorial.usingAmidst.more_examples;

import eu.amidst.core.distribution.Distribution;
import eu.amidst.core.inference.InferenceAlgorithm;
import eu.amidst.core.inference.messagepassing.VMP;
import eu.amidst.core.io.BayesianNetworkLoader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.core.variables.Assignment;
import eu.amidst.core.variables.HashMapAssignment;
import eu.amidst.core.variables.Variable;
import eu.amidst.core.variables.Variables;

import java.io.IOException;

/**
 * Created by rcabanas on 23/05/16.
 */
public class StaticModelInference {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		BayesianNetwork bn  = BayesianNetworkLoader.loadFromFile("networks/simulated/BCCBN.bn");
		Variables variables = bn.getVariables();

		System.out.println(bn);

		//Target variable
		Variable varTarget = variables.getVariableByName("globalHiddenVar");

		//we set the evidence
		Assignment assignment = new HashMapAssignment(2);
		assignment.setValue(variables.getVariableByName("Income"), 180);
		assignment.setValue(variables.getVariableByName("Expenses"),100);
		assignment.setValue(variables.getVariableByName("TotalCredit"),80);

		//we set the algorithm
		InferenceAlgorithm infer = new VMP();
		infer.setModel(bn);
		infer.setEvidence(assignment);

		//query
		infer.runInference();
		Distribution p = infer.getPosterior(varTarget);
		System.out.println("P(HiddenVar|Evidence) = "+p);


	}

}
