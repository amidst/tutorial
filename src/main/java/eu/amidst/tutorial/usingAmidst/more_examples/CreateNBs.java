package eu.amidst.tutorial.usingAmidst.more_examples;



import COM.hugin.HAPI.ExceptionHugin;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.BayesianNetworkWriter;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.latentvariablemodels.staticmodels.Model;
import eu.amidst.latentvariablemodels.staticmodels.classifiers.NaiveBayesClassifier;

import java.io.IOException;

/**
 * Created by rcabanas on 23/05/16.
 */
public class CreateNBs {
	public static void main(String[] args) throws ExceptionHugin, IOException {

		//Load the datastream
		String path = "datasets/simulated/";
		String filename = path+"BCC_month0.arff";
		DataStream<DataInstance> data =
				DataStreamLoader.open(filename);

		//Learn the model
		Model model = new NaiveBayesClassifier(data.getAttributes());
		((NaiveBayesClassifier)model).setClassName("Default");
		model.updateModel(data);
		BayesianNetwork bn = model.getModel();

		// Print the BN and save it
		System.out.println(bn);
		BayesianNetworkWriter.save(bn, "networks/simulated/BCCBN.bn");

		//Update the model with new information
		for(int i=1; i<12; i++) {
			filename = path+"BCC_month"+i+".arff";
			data = DataStreamLoader.open(filename);
			model.updateModel(data);
			System.out.println(model.getModel());
		}




	}

}
