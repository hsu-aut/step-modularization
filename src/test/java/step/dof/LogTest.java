package step.dof;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.analysis.Preprocessor;
import step.analysis.RotationAnalyzer;
import step.analysis.TranslationAnalyzer;
import step.event.Event;

public class LogTest {
  
  public static final String MODEL_PATH = "src/test/resources/";
  
  @Test
  public void test_tc1_p214() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc1-ap214.stp";
//    String fileName = MODEL_PATH + "step/analysis/tc1-ap242.stp";
    
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
    
    assertEquals(2, events.size(), 0);
    
    printLog(fileName, events);
  }
  
  @Test
  public void test_tc2_ap214() throws IOException {
//    String fileName = MODEL_PATH + "step/analysis/tc2-ap214.stp";
    String fileName = MODEL_PATH + "step/analysis/tc2-ap214_normal_correction.stp";
//    String fileName = MODEL_PATH + "step/analysis/tc2-ap242.stp";
    
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
    
    assertEquals(3, events.size(), 0);
    
    printLog(fileName, events);
  }
  
  @Test
  public void test_tc3ap214() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/tc3-ap214.stp";
//    String fileName = MODEL_PATH + "step/analysis/tc3-ap242.stp";
    
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
    
    assertEquals(3, events.size(), 0);
    
    printLog(fileName, events);
  }
  
  @Test
  public void test_drehtisch_verschoben() throws IOException {
    String fileName = MODEL_PATH + "drehtisch-mw-verschoben-ap214.stp";
    
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
    
    printLog(fileName, events);
  }
  
  protected void printLog(String fileName, List<Event> events) {
    System.out.println("events for " + fileName + ":");
    events.forEach(x -> System.out.println(x.print()));
    System.out.println();
  }
  
}
