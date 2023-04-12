package ontology;

import java.io.FileNotFoundException;

public class GraphAnalyzer {
		
	public static void main(String[] args) throws FileNotFoundException {

		// set up graph and model
		// path to step file
		// default model
	    String pathName = "src/test/resources/";
		String fileName = "drehtisch-mw-verschoben-xyz-ap214.stp";

		// custom model from args
		if (args.length == 2) {
			pathName = args[0];
			fileName = args[1];
		}

		// create graph from step file
		VDI2206Graph graph = new VDI2206Graph(pathName + fileName);

		// queries
		// path name
		String queryPathName = "src/main/resources/ontology/";
		// list of sparql queries to be executed (in the right order!)
		String[] queryFileNames = {
			"u_temp_object_prop",
			"u_add_modules",
			"u_static_list",
			"u_static_components",
			"u_clean_static_list",
			"u_undefined_list",
			"s_modules_and_comps",
			"s_undefined_list"
		};
		// execute all queries
		for (String queryFileName: queryFileNames) {
			Boolean isUpdate = queryFileName.substring(0, 1).equals("u");
			// get query string from query file
			String queryPathFileName = queryPathName + queryFileName + ".sparql";
			// execute query 
			graph.executeQuery(queryPathFileName, isUpdate);
		}

		// export to rdf xml
		graph.exportToRdfxml(pathName + fileName + "_output.rdf");
		
	}

}