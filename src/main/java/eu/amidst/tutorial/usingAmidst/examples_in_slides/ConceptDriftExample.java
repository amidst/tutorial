package eu.amidst.tutorial.usingAmidst.examples_in_slides;



import COM.hugin.HAPI.ExceptionHugin;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.BayesianNetworkWriter;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.latentvariablemodels.staticmodels.Model;
import eu.amidst.latentvariablemodels.staticmodels.ConceptDriftDetector;

import java.io.IOException;

/**
 * Created by rcabanas on 23/05/16.
 */
public class ConceptDriftExample {
	public static void main(String[] args) throws ExceptionHugin, IOException {

		//Load the datastream
		String path = "datasets/simulated/";
		String filename = path+"BCC_month0.arff";
		DataStream<DataInstance> data =
				DataStreamLoader.open(filename);

		//Build the model
		Model model =
				new ConceptDriftDetector(data.getAttributes())
				.setClassIndex(2);

		//Learn the model
		model.updateModel(data);

		//Update the model with new information
		for(int i=1; i<12; i++) {
			filename = path+"BCC_month"+i+".arff";
			data = DataStreamLoader.open(filename);
			model.updateModel(data);
			System.out.println(model.getPosteriorDistribution("GlobalHidden_0").toString());
		}




	}

}
