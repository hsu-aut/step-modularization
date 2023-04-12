package ontology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.update.UpdateExecution;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import step.analysis.AnalyzerTool;
import step.event.Event;
import step.shell.ClosedShell;

public class VDI2206Graph {

    private Model model;
    private Dataset dataset;
    private final static String owlFileName = "src/main/resources/ontology/VDI2206_DOF_Extension.owl";
    
    public VDI2206Graph(String filename) {
      // use analyzer tool to generate event list
      AnalyzerTool tool = new AnalyzerTool();
      List<Event> eventList = tool.analyze(filename);
      List<ClosedShell> shellList = tool.getClosedShells(); // ToDo: use found shells to identify all components
      
      // convert evetn list to componennt and interface list
      EventToDOFConverter converter = new EventToDOFConverter(eventList, shellList);
      List<Component> componentList = converter.getComponentList();
      List<MechanicalInterface> mInterfaceList = converter.getMInterfaceList();
      //
      this.dataset = DatasetFactory.create();
      this.model = dataset.getDefaultModel();
      // create model and generate tbox from file
      model.read(owlFileName, "RDFXML");
      // add abox entities
      componentList.forEach(component -> {
        component.addToModel(model);
      });
      mInterfaceList.forEach(mInterface -> {
        mInterface.addToModel(model);
      });
    }
    
    public VDI2206Graph(List<Event> eventList, List<ClosedShell> shellList) {
      EventToDOFConverter converter = new EventToDOFConverter(eventList, shellList);
      List<Component> componentList = converter.getComponentList();
      List<MechanicalInterface> mInterfaceList = converter.getMInterfaceList();
      //
      this.dataset = DatasetFactory.create();
      this.model = dataset.getDefaultModel();
      // create model and generate tbox from file
      model.read(owlFileName, "RDFXML");
      // add abox entities
      componentList.forEach(component -> {
        component.addToModel(model);
      });
      mInterfaceList.forEach(mInterface -> {
        mInterface.addToModel(model);
      });
    }

    public void exportToRdfxml(String exportFilename) throws FileNotFoundException {
        // output file
		FileOutputStream outputStream = new FileOutputStream(exportFilename);
        // write        
        RDFWriter.create()
            .source(model)
            .lang(Lang.RDFXML)
            .output(outputStream);  
    }

    public void executeQuery(String queryFileName, Boolean isUpdate) {
        System.out.println("query: " + queryFileName);
        if (isUpdate) {
            // build and execute update request
            UpdateRequest updateRequest = UpdateFactory.read(queryFileName);
            UpdateExecution uExec = UpdateExecutionFactory.create(updateRequest, dataset);
            uExec.execute();
        } else {
            // build and execute query
            Query query = QueryFactory.read(queryFileName);
            QueryExecution qExec = QueryExecutionFactory.create(query, model);
            ResultSet results = qExec.execSelect();
            // print results to console
            PrefixMapping pfm = new PrefixMappingImpl().setNsPrefix("VDI2206", "http://www.hsu-ifa.de/ontologies/VDI2206#");
            ResultSetFormatter.out(System.out, results, pfm);
        }
    }
}
