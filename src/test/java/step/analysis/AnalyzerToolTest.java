package step.analysis;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import step.event.Event;
import step.event.TransaltionalRestriction;

public class AnalyzerToolTest {
  
  public static final String MODEL_PATH = "src/test/resources/";
  
  @Test
  public void testBgr1() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/bgr1.stp";
    
    AnalyzerTool tool = new AnalyzerTool();
    
    List<Event> rest = tool.analyze(fileName);
    rest.forEach(r -> System.out.println(r.print()));
  }
  
  @Test
  public void testCubes() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/cubes.step";
  
    AnalyzerTool tool = new AnalyzerTool();
    List<Event> rest = tool.analyze(fileName);
    
    assertEquals(2, rest.size());
    double[] restriction = { -1.0, 0.0, 0.0 };
    for (Event e : rest) {
      assertTrue(e instanceof TransaltionalRestriction);
      double[] trans = ((TransaltionalRestriction) e).getTranslationVec(); 
      assertArrayEquals(restriction, trans, 0.00001);
    }
    rest.forEach(r -> System.out.println(r.print()));
  }
  
  @Test
  public void testBgr5() throws IOException {
    String fileName = MODEL_PATH + "step/analysis/bgr5.stp";
  
    AnalyzerTool tool = new AnalyzerTool();
  
    List<Event> rest = tool.analyze(fileName);
    rest.forEach(r -> System.out.println(r.print()));
  }
  
  @Test
  public void testDrehtischAP203() throws IOException {
    String fileName = MODEL_PATH + "DrehtischAP203.stp";
    
    AnalyzerTool tool = new AnalyzerTool();
    
    List<Event> rest = tool.analyze(fileName);
    System.out.println("Result:");
    rest.forEach(r -> System.out.println(r.print()));
  }
  
@Test
public void testHocker() throws IOException {
  String fileName = MODEL_PATH + "Hocker.STEP";
  
  AnalyzerTool tool = new AnalyzerTool();
  
  List<Event> rest = tool.analyze(fileName);
  rest.forEach(r -> System.out.println(r.print()));
}
  
}
