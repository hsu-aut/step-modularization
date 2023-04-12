package ontology;

public class RotationalDOF implements DOF {

	private double[] directionalVector;
	private double[] locationVector;
	
	public RotationalDOF(double[] directionalVector, double[] locationVector) {
		this.directionalVector = directionalVector;
		this.locationVector = locationVector;
	}
	
	@Override
	public double[] getDirectionalVector() {
		return directionalVector;
	}
	
	public double[] getLocationVector() {
		return locationVector;
	}
	
	@Override
	public boolean isTranslational() {
		return false;
	}

}