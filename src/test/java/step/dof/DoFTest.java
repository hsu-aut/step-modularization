package step.dof;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ontology.DOF;
import ontology.EventToDOFConverter;
import ontology.MechanicalInterface;
import ontology.RotationalDOF;
import ontology.TranslationalDOF;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.analysis.Preprocessor;
import step.analysis.RotationAnalyzer;
import step.analysis.TranslationAnalyzer;
import step.event.Event;
import step.shell.ClosedShell;
import step.utility.VectorMath;

public class DoFTest {
  
  public static final String MODEL_PATH = "src/test/resources/";
  
  @Test
  public void test_tc1_ap214() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc1-ap214.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc1-ap214:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
    
    // expect two detected events
    assertEquals(2, events.size(), 0);
    
    // create interface list using converter
    EventToDOFConverter converter = new EventToDOFConverter(events, new ArrayList<ClosedShell>());         
    List<MechanicalInterface> mInterfaces = converter.getMInterfaceList();
    
    // expect one rotational dof
    assertEquals(1, mInterfaces.size(), 0);
    assertEquals(1, mInterfaces.get(0).getDOFS().size(), 0);
    
    DOF dof = mInterfaces.get(0).getDOFS().get(0);
    assertTrue(dof instanceof RotationalDOF);
    
    double[] expLoc = { 0.0, 0.0, 0.0 };
    double[] expDir1 = { 0.0, 1.0, 0.0 };
    double[] expDir2 = { 0.0, -1.0, 0.0 };
    
    RotationalDOF rotDof = (RotationalDOF) dof;
    
    assertTrue(VectorMath.vecEquals(rotDof.getLocationVector(), expLoc, 0.01));
    
    boolean correctDir = VectorMath.vecEquals(rotDof.getDirectionalVector(), expDir1, 0.01);
    correctDir |= VectorMath.vecEquals(rotDof.getDirectionalVector(), expDir2, 0.01);
    assertTrue(correctDir);
  }
  
//  @Test
  public void test_tc1_ap242() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc1-ap242.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc1-ap242:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
  }
  
  
  @Test
  public void test_tc2_ap214() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc2-ap214_normal_correction.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc2-ap214:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
    
    // expect three detected events
    assertEquals(3, events.size(), 0);
    
    // create interface list using converter
    EventToDOFConverter converter = new EventToDOFConverter(events, new ArrayList<ClosedShell>());         
    List<MechanicalInterface> mInterfaces = converter.getMInterfaceList();
    
    // expect no dofs
    assertEquals(1, mInterfaces.size(), 0);
    assertEquals(0, mInterfaces.get(0).getDOFS().size(), 0);
  }
  
//  @Test
  public void test_tc2_ap242() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc2-ap242.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc2-ap242:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
  }
  
  
  @Test
  public void test_tc3_ap214() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc3-ap214.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc3-ap214:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
    
    // expect three detected events
    assertEquals(3, events.size(), 0);
    
    // create interface list using converter
    EventToDOFConverter converter = new EventToDOFConverter(events, new ArrayList<ClosedShell>());         
    List<MechanicalInterface> mInterfaces = converter.getMInterfaceList();
        
    // expect one translational dof
    assertEquals(1, mInterfaces.size(), 0);
    assertEquals(1, mInterfaces.get(0).getDOFS().size(), 0);
    
    DOF dof = mInterfaces.get(0).getDOFS().get(0);
    assertTrue(dof instanceof TranslationalDOF);
    
    double[] expDir = { 0.0, 1.0, 0.0 };
    
    TranslationalDOF transDof = (TranslationalDOF) dof;
    
    assertTrue(VectorMath.vecEquals(transDof.getDirectionalVector(), expDir, 0.01));
    
  }
  
//  @Test
  public void test_tc3_ap242() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc3-ap242.stp";
    
    List<Event> events = new ArrayList<Event>();
    
    try {
      STEPParser stepParser = new STEPParser();
      Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
      
      if (ast.isPresent()) {
        Preprocessor prep = new Preprocessor(ast.get());
        prep.preprocess();
    	  
        TranslationAnalyzer transAn = new TranslationAnalyzer();
        RotationAnalyzer rotAn = new RotationAnalyzer();
        
        events.addAll(transAn.analyze(prep.getClosedShells()));
        events.addAll(rotAn.analyze(prep.getClosedShells()));
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("tc3-ap242:");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
  }
  
}
