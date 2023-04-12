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

public class RotationAnalyzerTest {
  
  public static final String MODEL_PATH = "src/test/resources/step/analysis/";
  
  @Test
  public void testCubes() throws IOException {
    String fileName = MODEL_PATH + "bgr1.stp";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
    
    Preprocessor prep = new Preprocessor(ast.get());
    prep.preprocess();
    RotationAnalyzer rotAn = new RotationAnalyzer();
    List<Event> restrictions = rotAn.analyze(prep.getClosedShells());
    
    assertEquals(1, restrictions.size(), 0);
    for (Event restriction : restrictions) {
      // check IDs
      assertEquals("Wrong assembly IDs.", "248", restriction.getAssemblies()[0].getId());
      assertEquals("Wrong assembly IDs.", "249", restriction.getAssemblies()[1].getId());
      
      // check axis origin
      double[] exOrigin = {0.0, 0.0, 0.0};
      assertArrayEquals(exOrigin, ((RotationalUnit) restriction).getAxisOrigin(), 0.00001);
      
      // check axis vector
      double[] exVector = {0.0, 1.0, 0.0};
      assertArrayEquals(exVector, ((RotationalUnit) restriction).getAxisVec(), 0.00001);
    }
    
    restrictions.forEach(r -> System.out.println(r.print()));
  }
  
}
