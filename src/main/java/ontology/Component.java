package ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public class Component {
	
	private String componentName;
	private Resource resource;
	
	public Component(String componentName) {
		this.componentName = componentName;
	}
	
	public Resource getRessource() {
		return resource;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public void addToModel(Model targetModel) {
		
		// add resource (string componentName should be unique)
		resource = targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#" + componentName);
		
		// add object property (rdf:type) to classify created individual
		resource.addProperty(targetModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type"), 
				targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#Component"));
		
	}
	
}
