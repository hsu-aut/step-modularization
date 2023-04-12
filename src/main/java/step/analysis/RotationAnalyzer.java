package step.analysis;

import static step.utility.VectorMath.crossProd;
import static step.utility.VectorMath.isNullVec;
import static step.utility.VectorMath.vecMinus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import step.CylindricalSurface;
import step.Entity;
import step.event.Event;
import step.event.RotationalUnit;
import step.face.AdvancedFace;
import step.shell.ClosedShell;

public class RotationAnalyzer {
  
  /**
   * Translational and rotational allowed deltas.
   */
  private final double deltaT = 0.00001;
  
  public List<Event> analyze(List<ClosedShell> shells) {
    List<Event> collisions = new ArrayList<Event>();
    
    // pairwise compare shells
    for (int i = 0; i < shells.size(); i++) {
      for (int j = i+1; j < shells.size(); j++) {
        collisions.addAll(extractRotationalUnits(shells.get(i), shells.get(j)));
      }
    }
    
    return collisions;
  }
  
  protected List<Event> extractRotationalUnits(ClosedShell shell1, ClosedShell shell2) {
    List<Event> res = new ArrayList<Event>();
    
    List<AdvancedFace> cylFaces1 = shell1.getCfs_faces().stream().filter(f -> f.isCylindrical()).collect(Collectors.toList());
    List<AdvancedFace> cylFaces2 = shell2.getCfs_faces().stream().filter(f -> f.isCylindrical()).collect(Collectors.toList());
    
    // pairwise compare rotation
    for (int i = 0; i < cylFaces1.size(); i++) {
      for (int j = 0; j < cylFaces2.size(); j++) {
        // extract surfaces
        CylindricalSurface cs1 = cylFaces1.get(i).getCylSurface().get();
        CylindricalSurface cs2 = cylFaces2.get(j).getCylSurface().get();
        
        // cond1: same direction and origin on direction vector
        boolean cond1 = dirAndOrigin(cs1, cs2);
        
        // cond2: same radius
        double r1 = cs1.getRadius();
        double r2 = cs2.getRadius();
        boolean cond2 = (r1 + deltaT >= r2 && r1 - deltaT <= r2);
        
        // cond3: intersecting heights
        boolean cond3 = true;
        
        if (cond1 && cond2 && cond3) {
          res.add(new RotationalUnit(shell1, shell2, cs1.getDirection(), cs1.getOrigin()));
        }
      }
    }
    
    return res;
  }
  
  protected boolean dirAndOrigin(CylindricalSurface cs1, CylindricalSurface cs2) {
    // direction
    boolean sameDir = isNullVec(crossProd(cs1.getDirection(), cs2.getDirection()), deltaT);
    
    // origin
    double[] expectedDir = cs1.getDirection();
    double[] actualDir = vecMinus(cs1.getOrigin(), cs2.getOrigin());
    boolean originAtDir = isNullVec(crossProd(expectedDir, actualDir), deltaT);
    
    return sameDir && originAtDir;
  }
  
  protected boolean sameRadius(CylindricalSurface cs1, CylindricalSurface cs2) {
    return false;
  }
  
  protected List<ClosedShell> getClosedShells(Collection<Entity> entities) {
    List<ClosedShell> faces = new ArrayList<ClosedShell>();
    for (Entity e : entities) {
      if (e instanceof ClosedShell) {
        faces.add((ClosedShell) e);
      }
    }
    return faces;
  }
  
}
