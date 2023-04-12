package step.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.primitives.Doubles;

import step.Axis2Placement3D;
import step.CylindricalSurface;
import step.Entity;
import step.Plane;
import step._ast.ASTStepExchangeFile;
import step.edge.Direction;
import step.face.AdvancedFace;
import step.point.CartesianPoint;
import step.shell.ClosedShell;
import visitor.GeometryVisitor;

public class Preprocessor {
  
  
  private final ASTStepExchangeFile ast;
  private GeometryVisitor visitor;
  
  public Preprocessor(ASTStepExchangeFile ast) {
    this.ast = ast;
    visitor = new GeometryVisitor();
  }
  
  
  public void preprocess() {
    
    ast.accept(visitor);
    
    // prepare data for analysis
    refineReferences();
    
  }
  
  public List<ClosedShell> getClosedShells() {
	  return getClosedShells(visitor.getEntities().values());
  }
  
  protected void refineReferences() {
    Map<String, Entity> entities = visitor.getEntities();
    
    // attach faces to shell
    List<ClosedShell> shells = getClosedShells(entities.values());
    shells.forEach(cs -> shellFaces(cs));
    
    // attach vertices to faces and derive normals
    List<AdvancedFace> faces = getFaces(entities.values());
    faces.forEach(f -> planeFaces(f));
    faces.forEach(f -> cylindricalFaces(f));
    faces.forEach(f -> faceVertices(f));
    faces.forEach(f -> faceNormal(f));
    
    
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
  
  protected void shellFaces(ClosedShell cs) {
    List<String> refFaceIds = visitor.getReferences().get(cs.getId());
    Set<AdvancedFace> refCsFaces = new HashSet<AdvancedFace>();
    
    for (String ident : refFaceIds) {
      Entity potentialFace = visitor.getEntities().get(ident);
      if (potentialFace instanceof AdvancedFace) {
        refCsFaces.add((AdvancedFace) potentialFace);
      }
    }
    
    cs.setCfs_faces(refCsFaces);
  }
  
  protected List<AdvancedFace> getFaces(Collection<Entity> entities) {
    List<AdvancedFace> faces = new ArrayList<AdvancedFace>();
    for (Entity e : entities) {
      if (e instanceof AdvancedFace) {
        faces.add((AdvancedFace) e);
      }
    }
    return faces;
  }
  
  protected void faceVertices(AdvancedFace face) {
    Map<String, List<String>> references = visitor.getReferences();
    List<CartesianPoint> points = getCartesianPoints(visitor.getEntities().values());
    List<CartesianPoint> refPoints = computeReferencedPoints(face.getId(), references, points);
    face.setVertices(refPoints);
  }
  
  protected void faceNormal(AdvancedFace face) {
    Map<String, List<String>> references = visitor.getReferences();
    
    // stop if reference is invalid
    if (references.get(face.getNormalRef()) == null) {
      return;
    }
    String axisPlacementID = references.get(face.getNormalRef()).get(0);
    // stop if reference is invalid
    if (references.get(axisPlacementID) == null) {
      return;
    }
    String directionID = references.get(axisPlacementID).get(1);
    Entity dir = visitor.getEntities().get(directionID);
    
    if (dir instanceof Direction) {
      List<Double> normalList = ((Direction) dir).getDirection_ratios();
      double[] normal = new double[normalList.size()];
      for (int i = 0; i < normalList.size(); i++) {
        normal[i] = normalList.get(i);
      }
      face.setNormal(normal);
    }
  }
  
  protected List<CartesianPoint> getCartesianPoints(Collection<Entity> entities) {
    List<CartesianPoint> CartesianPoints = new ArrayList<CartesianPoint>();
    for (Entity e : entities) {
      if (e instanceof CartesianPoint) {
        CartesianPoints.add((CartesianPoint) e);
      }
    }
    return CartesianPoints;
  }
  
  protected List<CartesianPoint> computeReferencedPoints(String id, Map<String, List<String>> allRefs, List<CartesianPoint> points) {
    Set<CartesianPoint> res = new HashSet<CartesianPoint>();
    
    List<String> localRefs = allRefs.get(id);
    if (localRefs == null) {
      // fallback for non-handled element
      return new ArrayList<CartesianPoint>(res);
    }
    for (String ref : localRefs) {
      boolean isPoint = false;
      // check if referenced element is a point
      for (CartesianPoint p : points) {
        if (p.getId().equals(ref)) {
          res.add(p);
          isPoint = true;
        }
      }
      if (!isPoint) {
        // if no point is found continue recursively
        res.addAll(computeReferencedPoints(ref, allRefs, points));
      }
    }
    
    return new ArrayList<CartesianPoint>(res);
  }
  
  protected void cylindricalFaces(AdvancedFace face) {
    Map<String, List<String>> references = visitor.getReferences();
    Map<String, Entity> entities = visitor.getEntities();
    
    // stop if reference is invalid
    if (references.get(face.getNormalRef()) == null) {
      return;
    }
    String axisPlacementID = references.get(face.getNormalRef()).get(0);
    Entity surface = entities.get(face.getNormalRef());
    
    if (surface instanceof CylindricalSurface) {
      face.setCylindrical(true);
      face.setSurfaceRef(surface.getId());
      face.setCylSurface((CylindricalSurface) surface); 
      
      Axis2Placement3D axisPlace = (Axis2Placement3D) entities.get(axisPlacementID);
      ((CylindricalSurface) surface).setPosition(axisPlace);
      
      List<String> axisPlaceRefs = references.get(axisPlace.getId());
      if (axisPlaceRefs.size() == 3) {
        CartesianPoint origin = (CartesianPoint) entities.get(axisPlaceRefs.get(0));
        Direction direction = (Direction) entities.get(axisPlaceRefs.get(1));
        ((CylindricalSurface) surface).setOrigin(Doubles.toArray(origin.getCoordinates()));
        ((CylindricalSurface) surface).setDirection(Doubles.toArray(direction.getDirection_ratios()));
      }
    }
    
  }
  
  protected void planeFaces(AdvancedFace face) {
    Map<String, Entity> entities = visitor.getEntities();
    Entity surface = entities.get(face.getNormalRef());
    if (surface instanceof Plane) {
      face.setPlane(true);
    }
  }
  
  public GeometryVisitor getVisitor() {
    return visitor;
  }
  
}
