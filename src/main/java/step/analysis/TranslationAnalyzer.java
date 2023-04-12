package step.analysis;

import static step.utility.VectorMath.dotProd;
import static step.utility.VectorMath.vecLength;
import static step.utility.VectorMath.vecMinus;
import static step.utility.VectorMath.ORIGIN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import step.event.Event;
import step.event.TransaltionalRestriction;
import step.face.AdvancedFace;
import step.shell.ClosedShell;
import step.utility.VectorMath;

public class TranslationAnalyzer {
  
  /**
   * Translational and rotational allowed deltas.
   */
  private final double deltaT = 0.00001;
  private final double deltaR = 5.0;
  public static final double[] INF_2D = {-267.0, -1300.0};
  
  
  public List<Event> analyze(List<ClosedShell> shells) {
    List<Event> collisions = new ArrayList<Event>();
    
    // pairwise compare shells
    for (int i = 0; i < shells.size(); i++) {
      for (int j = i+1; j < shells.size(); j++) {
        collisions.addAll(checkShellCollision(shells.get(i), shells.get(j)));
      }
    }
    
    return collisions;
  }

  protected List<Event> checkShellCollision(ClosedShell shell1, ClosedShell shell2) {
    
    List<Event> collisions = new ArrayList<Event>();
    
    for (AdvancedFace face1 : shell1.getCfs_faces()) {
      for (AdvancedFace face2 : shell2.getCfs_faces()) {
    	  
    	  if (face1.isPlane() && face2.isPlane() && face1.getVertices().size() >= 3 && face2.getVertices().size() >= 3) {
    			
    			double[] n_1 = face1.getNormal();
    			double[] n_2 = face2.getNormal();
    			double angle = Math.toDegrees(Math.acos(dotProd(n_1, n_2) / (vecLength(n_1) * vecLength(n_2))));
          
          if (angle > 180 - deltaR && angle < 180 + deltaR) {
            
            // rotate to x-axis (parallel to y-z-plane)
            // y and z rotation angles
            
            // clone vertices
            List<Double[]> f1_vertices = face1.getVertices().stream()
                .map(v -> new Double[] {v.getCoordinates().get(0), v.getCoordinates().get(1), v.getCoordinates().get(2)})
                .collect(Collectors.toList());
             
            List<Double[]> f2_vertices = face2.getVertices().stream()
                .map(v -> new Double[] {v.getCoordinates().get(0), v.getCoordinates().get(1), v.getCoordinates().get(2)})
                .collect(Collectors.toList());
            
            // translate vertices such that at least one is at origin
            translateToOrigin(f1_vertices, f2_vertices);
            
            
            // rotate cloned vertices. Use the same normal vector to 
            // rotate them into the same direction
            rotateToYZPlane(f1_vertices, n_1);
            rotateToYZPlane(f2_vertices, n_1);
            
            
            // to have an intersection, both rotated polygons must lie directly 
            // next to each other. Thus, their x distance must be zero
            boolean xDistanceZero = true;
            for (Double[] d1 : f1_vertices) {
              for (Double[] d2 : f2_vertices) {
                xDistanceZero &= Math.abs(d1[0] - d2[0]) <= deltaT;
              }
            }
            
            if (!xDistanceZero) {
              continue;
            }
            
            // project to 2D
            List<Double[]> f1_verts_2d = f1_vertices.stream()
                .map(c -> new Double[] {c[1], c[2]})
                .collect(Collectors.toList());
            
            List<Double[]> f2_verts_2d = f2_vertices.stream()
                .map(c -> new Double[] {c[1], c[2]})
                .collect(Collectors.toList());

            
            
            // TODO: start over with edges, as you need the original vertices' connection from their edges
            
            // if two line segments of the polygons intersect, we have an
            // overlap, and thus, a translational event
     
            boolean intersection = false;
            // TODO: line segment intersection of edges
            // here: pseudo edges
            for (int i = 0; i < f1_verts_2d.size(); i++) {
              for (int j = i+1; j < f1_verts_2d.size(); j++) {
                
                for (int k = 0; k < f2_verts_2d.size(); k++) {
                  for (int l = k+1; l < f2_verts_2d.size(); l++) {
                    
                    double[] p1 = ArrayUtils.toPrimitive(f1_verts_2d.get(i));
                    double[] p2 = ArrayUtils.toPrimitive(f1_verts_2d.get(j));
                    double[] p3 = ArrayUtils.toPrimitive(f2_verts_2d.get(k));
                    double[] p4 = ArrayUtils.toPrimitive(f2_verts_2d.get(l));
                    
                    intersection |= doIntersect(p1, p2, p3, p4);
                  }
                }
                
              }
            }
            
            if (intersection) {
              collisions.add(new TransaltionalRestriction(shell1, shell2, n_1));
              break;
            }
            
            // if one polygon lies completely inside another, there is an 
            // intersection of a line from "infinity" to a vertex with an
            // edge of the other polygon. To identify a vertex to lie inside
            // the other polygon, the number of intersections must be odd.
            
            for (int i = 0; i < f2_verts_2d.size(); i++) {
              int intersectionCount = 0;
              
              for (int j = 0; j < f1_verts_2d.size(); j++) {
                // point to check for
                double[] p1 = ArrayUtils.toPrimitive(f2_verts_2d.get(i));
                
                // edge to check against
                double[] p2 = ArrayUtils.toPrimitive(f1_verts_2d.get(j));
                double[] p3 = ArrayUtils.toPrimitive(f1_verts_2d.get((j + 1) % f1_verts_2d.size()));
                
                // count intersections
                intersectionCount += doIntersect(p1, INF_2D, p2, p3) ? 1 : 0;;
              }
              if (intersectionCount % 2 == 0) {
                intersection = true;
                break;
              }
            }

            if (intersection) {
              collisions.add(new TransaltionalRestriction(shell1, shell2, n_1));
              break;
            }
            
            
            for (int i = 0; i < f1_verts_2d.size(); i++) {
              int intersectionCount = 0;
              
              for (int j = 0; j < f2_verts_2d.size(); j++) {
                // point to check for
                double[] p1 = ArrayUtils.toPrimitive(f1_verts_2d.get(i));
                
                // edge to check against
                double[] p2 = ArrayUtils.toPrimitive(f2_verts_2d.get(j));
                double[] p3 = ArrayUtils.toPrimitive(f2_verts_2d.get((j + 1) % f2_verts_2d.size()));
                
                // count intersections
                intersectionCount += doIntersect(p1, INF_2D, p2, p3) ? 1 : 0;;
              }
              if (intersectionCount % 2 == 0) {
                intersection = true;
                break;
              }
            }
            
            if (intersection) {
              collisions.add(new TransaltionalRestriction(shell1, shell2, n_1));
              break;
            }

          }
    	  }
      }
    }
    
    return collisions;
  }
  
  
  
  /* ########## TODO: redo non-static ########## */
  
  //Given three collinear points p, q, r, the function checks if
  //point q lies on line segment 'pr'
  static boolean onSegment(double[] p, double[] q, double[] r) {
    if (q[0] <= Math.max(p[0], r[0]) && q[0] >= Math.min(p[0], r[0]) && q[1] <= Math.max(p[1], r[1]) && q[1] >= Math.min(p[1], r[1]))
      return true;
    
    return false;
  }
  
  //To find orientation of ordered triplet (p, q, r).
  //The function returns following values
  //0 --> p, q and r are collinear
  //1 --> Clockwise
  //2 --> Counterclockwise
  static int orientation(double[] p, double[] q, double[] r) {
    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
    // for details of below formula.
    double val = (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
    
    if (val == 0)
      return 0; // collinear
      
    return (val > 0) ? 1 : 2; // clock or counterclock wise
  }
  
  //The main function that returns true if line segment 'p1q1'
  //and 'p2q2' intersect.
  static boolean doIntersect(double[] p1, double[] q1, double[] p2, double[] q2) {
    // Find the four orientations needed for general and
    // special cases
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);
    
    // General case
    if (o1 != o2 && o3 != o4)
      return true;
      
    // Special Cases
    // p1, q1 and p2 are collinear and p2 lies on segment p1q1
    if (o1 == 0 && onSegment(p1, p2, q1))
      return true;
    
    // p1, q1 and q2 are collinear and q2 lies on segment p1q1
    if (o2 == 0 && onSegment(p1, q2, q1))
      return true;
    
    // p2, q2 and p1 are collinear and p1 lies on segment p2q2
    if (o3 == 0 && onSegment(p2, p1, q2))
      return true;
    
    // p2, q2 and q1 are collinear and q1 lies on segment p2q2
    if (o4 == 0 && onSegment(p2, q1, q2))
      return true;
    
    return false; // Doesn't fall in any of the above cases
  }
  
  /* ########## redo end ########## */

  
  protected void rotateToYZPlane(List<Double[]> vertices, double[] normal) {
    // rotate to x-axis (parallel to y-z-plane)
    // y and z rotation angles (are used reversed)
    double rotY = 90.0 - Math.toDegrees(Math.acos(dotProd(normal, VectorMath.Y) / (vecLength(normal) * vecLength(VectorMath.Y))));
    double rotZ = 90.0 - Math.toDegrees(Math.acos(dotProd(normal, VectorMath.Z) / (vecLength(normal) * vecLength(VectorMath.Z))));
    
    // rotate around y-axis
    for (int i = 0; i < vertices.size(); i++) {
      double[] v_prim = ArrayUtils.toPrimitive(vertices.get(i));
      v_prim = VectorMath.rotY(v_prim, rotZ);
      vertices.set(i, ArrayUtils.toObject(v_prim));
    }
    
    // rotate around z-axis
    for (int i = 0; i < vertices.size(); i++) {
      double[] v_prim = ArrayUtils.toPrimitive(vertices.get(i));
      v_prim = VectorMath.rotZ(v_prim, rotY);
      vertices.set(i, ArrayUtils.toObject(v_prim));
    }
  }
  
  protected void translateToOrigin(List<Double[]> vertices_f1, List<Double[]> vertices_f2) {
    // calculate translation vector to subtract from every vertex
    double[] transVec = ArrayUtils.toPrimitive(vertices_f1.get(0));
    
    // translate via vector subtraction for both faces
    for (int i = 0; i < vertices_f1.size(); i++) {
      double[] v = ArrayUtils.toPrimitive(vertices_f1.get(i));
      v = vecMinus(v, transVec);
      vertices_f1.set(i, ArrayUtils.toObject(v));
    }
    
    for (int i = 0; i < vertices_f2.size(); i++) {
      double[] v = ArrayUtils.toPrimitive(vertices_f2.get(i));
      v = vecMinus(v, transVec);
      vertices_f2.set(i, ArrayUtils.toObject(v));
    }
  }

}
