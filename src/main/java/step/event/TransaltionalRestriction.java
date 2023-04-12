package step.event;

import step.shell.ClosedShell;

public class TransaltionalRestriction implements Event {
  
  private ClosedShell[] assemblies;
  private double[] translationVec;
  
  public TransaltionalRestriction(ClosedShell assembly1, ClosedShell assembly2, double[] vector) {
    assemblies = new ClosedShell[] { assembly1, assembly2 };
    translationVec = vector;
  }

  @Override
  public ClosedShell[] getAssemblies() {
    return assemblies;
  }
  
  public double[] getTranslationVec() {
    return translationVec;
  }
  
  @Override
  public String print() {
    return "trans: " + assemblies[0].getId() + ", " + assemblies[1].getId() + ": " 
      + "[" + translationVec[0] + ", " + translationVec[1] + ", " + translationVec[2] + "]";
  }
  
}
