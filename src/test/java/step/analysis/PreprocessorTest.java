package step.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.event.Event;
import step.event.RotationalUnit;

public class PreprocessorTest {
  
  public static final String MODEL_PATH = "src/test/resources/step/analysis/";
  
  @Test
  public void testCubes() throws IOException {
    String fileName = MODEL_PATH + "bgr1.stp";
    
//    
//    Preprocessor tool = new Preprocessor();
//    
//    List<Restriction> rest = tool.analyze(fileName);
//    rest.forEach(r -> System.out.println(r.print()));
  }
  
}
