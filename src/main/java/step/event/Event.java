package step.event;

import step.shell.ClosedShell;

public interface Event {
  
  public ClosedShell[] getAssemblies();
  
  public String print();
  
}
