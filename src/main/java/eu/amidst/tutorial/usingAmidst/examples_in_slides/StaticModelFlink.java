package eu.amidst.tutorial.usingAmidst.examples_in_slides;

import COM.hugin.HAPI.ExceptionHugin;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.BayesianNetworkWriter;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.flinklink.core.data.DataFlink;
import eu.amidst.flinklink.core.io.DataFlinkLoader;
import eu.amidst.latentvariablemodels.staticmodels.CustomGaussianMixture;
import eu.amidst.latentvariablemodels.staticmodels.FactorAnalysis;
import eu.amidst.latentvariablemodels.staticmodels.GaussianMixture;
import eu.amidst.latentvariablemodels.staticmodels.Model;
import eu.amidst.tutorial.usingAmidst.Main;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;

import java.io.IOException;

/**
 * Created by rcabanas on 23/05/16.
 */
public class StaticModelFlink {
	public static void main(String[] args) throws IOException, ExceptionHugin {
		//Set-up Flink session.
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		//Load the data stream (with Flink)
		String path = "datasets/simulated/";
		String filename = path+"BCC_Flink_month0.arff";
		DataFlink<DataInstance> data =
				DataFlinkLoader.open(env, filename, false);

		//Build the model
		Model model = new CustomGaussianMixture(data.getAttributes())
						.setClassName("Default");

		//Learn the model
		model.updateModel(data);
		BayesianNetwork bn = model.getModel();

		// Print the BN and save it
		System.out.println(bn);
		BayesianNetworkWriter.save(bn, "networks/simulated/BCCBN.bn");


		//Update your model
		for(int i=1; i<12; i++) {
			filename = path+"BCC_Flink_month"+i+".arff";
			data = DataFlinkLoader.loadDataFromFolder(env, filename,false);
			model.updateModel(data);

		}


	}

}
