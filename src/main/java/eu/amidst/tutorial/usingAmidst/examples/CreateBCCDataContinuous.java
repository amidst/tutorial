package eu.amidst.tutorial.usingAmidst.examples;

import eu.amidst.core.datastream.*;
import eu.amidst.core.io.DataStreamWriter;
import eu.amidst.core.variables.StateSpaceType;
import eu.amidst.dynamic.datastream.DynamicDataInstance;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;
import eu.amidst.dynamic.utils.DataSetGenerator;
import eu.amidst.flinklink.core.data.DataFlink;
import eu.amidst.flinklink.core.io.DataFlinkLoader;
import eu.amidst.flinklink.core.io.DataFlinkWriter;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Created by rcabanas on 20/05/16.
 */
public class CreateBCCDataContinuous {

    public static void main(String[] args) throws Exception{


		String names1[] = {"SEQUENCE_ID", "TIME_ID", "Default", "Income","Expenses","TotalCredit"};
		String names2[] = {"SEQUENCE_ID", "TIME_ID", "Income","Expenses","TotalCredit"};


        String path = "datasets/simulated/";


        for(int i=0; i<12; i++) {
            generate(path, "BCCDefault_month"+i+".arff",500, 1,3, names1, i);
			generate(path, "BCC_month"+i+".arff",500, 0,3, names2, i);

        }



        DataStream<DynamicDataInstance> d1 = DynamicDataStreamLoader.loadFromFile(path+"BCCDefault_month0.arff");

        for(int i=1; i<12; i++) {
            DataStream<DynamicDataInstance> d2 = DynamicDataStreamLoader.loadFromFile(path+"BCCDefault_month"+i+".arff");
            d1 = concat(d1,d2);

        }

         DataStreamWriter.writeDataToFile(d1, path + "BCCDefault.arff");


		//generate(path, "BCCDefault.arff",6000, 1,3, names1, 1);





        makeDistributed(path, "BCC_month0.arff", "BCCDist_month0.arff");









    }



    public static void generate(String path, String filename, int nSamples, int nDiscr, int nCont, String names[], int month) throws IOException {
     //   int nContinuousAttributes=4;
     //   int nDiscreteAttributes=1;
     //   String names[] = {"SEQUENCE_ID", "TIME_ID", "Default", "Income","Expenses","Balance","TotalCredit"};
       // String path = "datasets/simulated/";
       // int nSamples=1000;


        //Generate random dynamic data
		int seed = 1234;
		System.out.println(seed);
        DataStream<DynamicDataInstance> data  = DataSetGenerator.generate(seed,nSamples,nDiscr,nCont);
        List<Attribute> list = new ArrayList<Attribute>();



        //Replace the names
        IntStream.range(0, data.getAttributes().getNumberOfAttributes())
                .forEach(i -> {
                    Attribute a = data.getAttributes().getFullListOfAttributes().get(i);
                    StateSpaceType s = a.getStateSpaceType();

                    System.out.println(s);

                    Attribute a2 = new Attribute(a.getIndex(), names[i],s);
                    list.add(a2);
                });


        //New list of attributes
        Attributes att2 = new Attributes(list);



        List<DynamicDataInstance> listData = data.stream().collect(Collectors.toList());


        Random random = new Random();
        //double b = 50;
        listData.forEach(d->{

            double k = 1.5;
			double x = 100;

			if(month == 4) {
				k = 1.5;
				x = x*0.8;
			}

			if(month == 6) {
				k = 1.5;
				x = x*1.3;
			}

            if(month == 7) {
				k = 1.5;
				x = x*1.5;
			}

            double income = Math.abs(150+d.getValue(att2.getAttributeByName("Income")));
            double expenses = Math.abs(x+k*d.getValue(att2.getAttributeByName("Expenses")));
            double credit = Math.abs(x+k*d.getValue(att2.getAttributeByName("TotalCredit")));
        //    double balance = income - expenses;

            d.setValue(att2.getAttributeByName("Income"),income);
            d.setValue(att2.getAttributeByName("Expenses"),expenses);
            d.setValue(att2.getAttributeByName("TotalCredit"),credit);
          //  d.setValue(att2.getAttributeByName("Balance"),balance);
        });//se guarda??


        //Datastream with the new attribute names
        DataStream<DynamicDataInstance> data2 =
                new DataOnMemoryListContainer<DynamicDataInstance>(att2,listData);



        //Write to a single file
        DataStreamWriter.writeDataToFile(data2, path + filename);



    }


    public static DataStream<DynamicDataInstance> concat (DataStream<DynamicDataInstance> d1, DataStream<DynamicDataInstance> d2) {

        Attributes att = d1.getAttributes();



        Stream<DynamicDataInstance> stream12 = Stream.concat(d1.stream(), d2.stream());
        List<DynamicDataInstance> listData = stream12.collect(Collectors.toList());

        DataStream<DynamicDataInstance> data =
                new DataOnMemoryListContainer<DynamicDataInstance>(att,listData);

        return data;

    }


    public static void makeDistributed(String path, String localfile, String distributedfile) throws Exception {
        //Write to a distributed folder
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataFlink<DataInstance> data2Flink = DataFlinkLoader.loadDataFromFile(env, path + localfile, false);
        DataFlinkWriter.writeDataToARFFFolder(data2Flink, path + distributedfile);


    }



	public static void generateNaiveBayes(Attributes att) {


	}


}
