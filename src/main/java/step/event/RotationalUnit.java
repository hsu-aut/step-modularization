package step.event;

import step.shell.ClosedShell;

public class RotationalUnit implements Event {

  private ClosedShell[] assemblies;
  private double[] axisVec;
  private double[] axisOrigin;
  
  
  public RotationalUnit(ClosedShell assembly1, ClosedShell assembly2, double[] axisVec, double[] axisOrigin) {
    assemblies = new ClosedShell[] { assembly1, assembly2 };
    this.axisVec = axisVec;
    this.axisOrigin = axisOrigin;
  }
  
  @Override
  public ClosedShell[] getAssemblies() {
    return assemblies;
  }
  
  public double[] getAxisVec() {
    return axisVec;
  }
  
  public double[] getAxisOrigin() {
    return axisOrigin;
  }

  @Override
  public String print() {
    return "rot: " + assemblies[0].getId() + ", " + assemblies[1].getId() + ": " 
        + "Axis Origin = " + "[" + axisOrigin[0] + ", " + axisOrigin[1] + ", " + axisOrigin[2] + "], "
        + "Axis Vector = " + "[" + axisVec[0] + ", " + axisVec[1] + ", " + axisVec[2] + "]";
  }
  
}
