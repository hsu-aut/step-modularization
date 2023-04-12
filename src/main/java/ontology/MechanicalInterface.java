package ontology;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public class MechanicalInterface {

	private Component component1;
	private Component component2;
	private Resource resource;
	private List<DOF> dofs;
	
	public MechanicalInterface(Component component1, Component component2, List<DOF> dofs) {
		this.component1 = component1;
		this.component2 = component2;
		this.dofs = dofs;
	}

	public Component getComponent1() {
		return component1;
	}
	
	public Component getComponent2() {
		return component2;
	}
	
	public List<DOF> getDOFS() {
		return dofs;
	}
	
	public void addToModel(Model targetModel) {

		// add this mechanical interface as a resource to the model
		resource = targetModel.createResource(
				"http://www.hsu-ifa.de/ontologies/VDI2206#MechanicalInterface"
				+ component1.getComponentName() + "-" + component2.getComponentName());
		
		// add object property (rdf:type) to classify created individual
		resource.addProperty(targetModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type"), 
				targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#MechanicalInterface"));
		
		// add object property from the mechanical interface to component 1 and 2
		component1.getRessource().addProperty(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "hasMechanicalInterface"), resource);
		component2.getRessource().addProperty(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "hasMechanicalInterface"), resource);	
		
		// add data properties for dofs
		
		// return if no dofs given
		if (dofs==null || dofs.size()==0) { return; }

		// dof counter
		int i = 1;
				
		// for each dof
		for (DOF dof: dofs) {
			
			// create resource for dof
			Resource resourceDOF = addDOFToModel(targetModel, dof, i);

			// get directional vector (relevant for both translational and rotational)
			double[] directionalVector = dof.getDirectionalVector();
			
			// add data properties to dof resource
			resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "directionalVectorX"), directionalVector[0]);
			resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "directionalVectorY"), directionalVector[1]);
			resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "directionalVectorZ"), directionalVector[2]);
			
			// if rotational dof, add properties for location vector too
			if (!(dof.isTranslational())) {
				
				// get location vector 
				double[] locationVector = ((RotationalDOF) dof).getLocationVector();
				
				// add data properties to dof resource
				resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "locationVectorX"), locationVector[0]);
				resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "locationVectorY"), locationVector[1]);
				resourceDOF.addLiteral(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "locationVectorZ"), locationVector[2]);
				
			}
			
			// increase dof counter
			i++;
			
		}
		
	}
	
	private Resource addDOFToModel(Model targetModel, DOF dof, int dofNumber) {
		
		// add resource (string componentName should be unique)
		Resource resourceDOF = targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#DOF-" + dofNumber + "-" + component1.getComponentName() + "-" + component2.getComponentName());
		
		// add object property (rdf:type) to classify created individual
		if (dof.isTranslational() ) {
			resourceDOF.addProperty(targetModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type"), 
				targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#TranslationalDOF"));
		} else {
			resourceDOF.addProperty(targetModel.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type"), 
					targetModel.createResource("http://www.hsu-ifa.de/ontologies/VDI2206#RotationalDOF"));
		}
		
		// add object property between mechanical interface and dof
		resource.addProperty(targetModel.createProperty("http://www.hsu-ifa.de/ontologies/VDI2206#", "hasDOF"), resourceDOF);
		
		// return ressourceDOF for further addToModel code
		return resourceDOF;
		
	}
	
}
