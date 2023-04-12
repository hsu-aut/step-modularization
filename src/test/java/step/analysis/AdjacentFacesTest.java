package step.analysis;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import step.Entity;
import step._ast.ASTInstance;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.face.AdvancedFace;
import step.point.CartesianPoint;
import step.shell.ClosedShell;
import step.topology.AssemblyUsage;
import visitor.AssemblyUsageVisitor;
import visitor.GeometryVisitor;

public class AdjacentFacesTest {
  
  public static final String MODEL_PATH = "src/test/resources/step/analysis/";
  
  @Test
  public void testCubes() throws IOException {
    String fileName = MODEL_PATH + "cubes.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
    
    GeometryVisitor visitor = new GeometryVisitor();
    ast.get().accept(visitor);
    Map<String, Entity> entities = visitor.getEntities();
    List<AdvancedFace> faces = getFaces(entities.values());
    List<CartesianPoint> points = getCartesianPoints(entities.values());
    
    Map<CartesianPoint, List<CartesianPoint>> pos = pointsOnSamePos(points);
    System.out.println("Nico:");
    
    Map<String, List<String>> refs = visitor.getReferences();
    System.out.println(refs.containsKey("162"));
    List<String> f = refs.get("926");
    List<String> f1 = refs.get("927");
    List<CartesianPoint> erfP = getReferencedPoints("162", refs, points);
    // 781 529 (910) 916 552 923 924 531 934 940 579
    // 441 442 73 
    System.out.println(" ");
    CartesianPoint a = erfP.get(2);
//    List<Double> b = a.getCoordinates();
//    erfP.forEach(e -> System.out.println("#" + e.getId() + "           " + e.getCoordinates()));
    for (CartesianPoint e : erfP) {
      System.out.println("#" + e.getId() + ":");
      System.out.println(e.getCoordinates());
    }
    
    
    // get closed shells
    List<ClosedShell> shells = new ArrayList<ClosedShell>();
    for (Entity e : entities.values()) {
      if (e instanceof ClosedShell) {
        shells.add((ClosedShell) e);
      }
    }
    System.out.println();
    System.out.println("-------------------------------");
    System.out.println("shells");
    for (ClosedShell s : shells) {
      System.out.println("#" + s.getId());
    }
    System.out.println();
    System.out.println();
    
    ClosedShell cs = shells.get(1);
    
    List<String> faId = refs.get(cs.getId());
    
    faId.forEach(i -> System.out.println(i));
    
    System.out.println();
    System.out.println();
    
    List<AdvancedFace> myCsFaces = new ArrayList<AdvancedFace>();
    for (String ident : faId) {
      Entity potF = entities.get(ident);
      
      if (potF instanceof AdvancedFace) {
        myCsFaces.add((AdvancedFace) potF);
      }
    }
    
    System.out.println();
    System.out.println("ää");
    myCsFaces.forEach(fa -> System.out.println(fa.getId()));
    
    System.out.println();
    System.out.println();
    
    
    //Object sFaces = getFaces(shells.get(1));
    
    
    
    
//    System.out.println("################");
//    
//    InstanceHandler ih = new InstanceHandler();
//    ASTInstance sface = ih.find(ast.get(), "162");
//    
//    CartesianPointExtractor ex = new CartesianPointExtractor();
//    List<CartesianPoint> myPoints = ex.extractPoints(ast.get(), sface);
//    myPoints.forEach(e -> System.out.println(e.getCoordinates()));
//    
//    System.out.println("-------------------");
//    
//    List<CartesianPoint> cleanedPoints = Lists.newCopyOnWriteArrayList(myPoints);
//    for (int i = 0; i <= cleanedPoints.size(); i++) {
//      CartesianPoint ip = cleanedPoints.get(0);
//      
//      int j = i;
//      while (j <= cleanedPoints.size()) {
//        CartesianPoint jp = cleanedPoints.get(0);
//        if (ip != jp) {
//          List<Double> ic = ip.getCoordinates();
//          List<Double> jc = jp.getCoordinates();
//          
//          if (ic.get(0).equals(jc.get(0)) && ic.get(1).equals(jc.get(1)) && ic.get(2).equals(jc.get(2))) {
//            cleanedPoints.remove(jp);
//            continue;
//          }
//          
//        }
//        j++;
//      }
//    }
    
    
    
//    cleanedPoints.forEach(e -> System.out.println(e.getCoordinates()));
    
    // potentials
//    [1500.0, 500.0, 500.0]
//    [500.0, 500.0, 500.0]    

//    vs

//    [500.0, 500.0, 500.0]
//    [1146.446609407, 146.4466094067, 500.0]
//    [1146.446609407, 500.0, 146.44660940672]
//    [1500.0, 146.4466094067, 146.44660940672]
//    [853.55339059327, 500.0, 146.4466094067]
//    [1500.0, 500.0, 500.0]
  }
  
  @Test
  public void testSingleCube() throws IOException {
    String fileName = MODEL_PATH + "Wuerfel1.stp";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
    
    GeometryVisitor visitor = new GeometryVisitor();
    ast.get().accept(visitor);
    Map<String, Entity> entities = visitor.getEntities();
    List<AdvancedFace> faces = getFaces(entities.values());
    List<CartesianPoint> points = getCartesianPoints(entities.values());
    
    Map<CartesianPoint, List<CartesianPoint>> pos = pointsOnSamePos(points);
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
    
    System.out.println("Birte:");
    
    Map<String, List<String>> refs = visitor.getReferences();
    System.out.println(refs.containsKey("101"));
    List<CartesianPoint> erfP = getReferencedPoints("101", refs, points);
    // 781 529 (910) 916 552 923 924 531 934 940 579
    // 441 442 73 
    System.out.println(" ");
    CartesianPoint a = erfP.get(2);
//    List<Double> b = a.getCoordinates();
//    erfP.forEach(e -> System.out.println("#" + e.getId() + "           " + e.getCoordinates()));
    for (CartesianPoint e : erfP) {
      System.out.println("#" + e.getId() + ":");
      System.out.println(e.getCoordinates());
    }
  }
  
  @Test
  public void testCubesSpace() throws IOException {
    String fileName = MODEL_PATH + "cubes_space.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }
  
  @Test
  public void testCubesAngle() throws IOException {
    String fileName = MODEL_PATH + "cubes_angle.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }
  
  @Test
  public void testSmallCubes() throws IOException {
    String fileName = MODEL_PATH + "small_cubes.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }
  
  @Test
  public void testSmallCubesSpace() throws IOException {
    String fileName = MODEL_PATH + "small_cubes_space.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }
  
  @Test
  public void testSmallCubesAngle() throws IOException {
    String fileName = MODEL_PATH + "small_cubes_angle.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }
  
  public List<CartesianPoint> getReferencedPoints(String id, Map<String, List<String>> allRefs, List<CartesianPoint> points) {
    List<CartesianPoint> res = new ArrayList<CartesianPoint>();
    
    List<String> localRefs = allRefs.get(id);
    if (localRefs == null) {
//      System.out.println("#"+id);
      return res;
    }
    for (String ref : localRefs) {
      boolean isPoint = false;
      for (CartesianPoint p : points) {
        if (p.getId().equals(ref)) {
          res.add(p);
          isPoint = true;
        }
      }
      if (!isPoint) {
        res.addAll(getReferencedPoints(ref, allRefs, points));
      }
    }
    
    return res ;
  }
  
  
  public List<AdvancedFace> getFaces(Collection<Entity> entities) {
    List<AdvancedFace> faces = new ArrayList<AdvancedFace>();
    for (Entity e : entities) {
      if (e instanceof AdvancedFace) {
        faces.add((AdvancedFace) e);
      }
    }
    return faces;
  }
  
  public List<CartesianPoint> getCartesianPoints(Collection<Entity> entities) {
    List<CartesianPoint> CartesianPoints = new ArrayList<CartesianPoint>();
    for (Entity e : entities) {
      if (e instanceof CartesianPoint) {
        CartesianPoints.add((CartesianPoint) e);
      }
    }
    return CartesianPoints;
  }
  
  public Map<CartesianPoint, List<CartesianPoint>> pointsOnSamePos(List<CartesianPoint> input) {
    return pointsOnSamePos(input, 0.0);
  }
  
  public Map<CartesianPoint, List<CartesianPoint>> pointsOnSamePos(List<CartesianPoint> input, double delta) {
    Map<CartesianPoint, List<CartesianPoint>> res = new HashMap<CartesianPoint, List<CartesianPoint>>();
    
    for (CartesianPoint p1 : input) {
      List<CartesianPoint> samePos = new ArrayList<CartesianPoint>();
      for (CartesianPoint p2 : input) {
        List<Double> c1 = p1.getCoordinates();
        List<Double> c2 = p2.getCoordinates();
        if (p1.equals(p2) || c1.size() != c2.size() || c1.size() != 3) {
          continue;
        }
        
        double distance = Math.sqrt(Math.pow(c1.get(0) - c2.get(0), 2) + 
                                    Math.pow(c1.get(1) - c2.get(1), 2) + 
                                    Math.pow(c1.get(2) - c2.get(2), 2));
        if (distance <= delta) {
          samePos.add(p2);
        }
      }
      if (!samePos.isEmpty()) {
        res.put(p1, samePos);  
      }
    }
    return res;
  }
  
}
