package ontology;

public class TranslationalDOF implements DOF {

	private double[] directionalVector;
	
	public TranslationalDOF(double[] directionalVector) {
		this.directionalVector = directionalVector;
	}
	
	@Override
	public double[] getDirectionalVector() {
		return directionalVector;
	}
	
	@Override
	public boolean isTranslational() {
		return true;
	}

}
