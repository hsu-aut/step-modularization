package step.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.event.Event;
import step.event.TransaltionalRestriction;
import step.face.AdvancedFace;
import step.point.CartesianPoint;

public class TranslationAnalyzerTest {
  
  public static final String MODEL_PATH = "src/test/resources/step/analysis/";
  
  protected AdvancedFace createTestFace() {
    AdvancedFace face = new AdvancedFace("", "1", true);
    
    CartesianPoint s1 = new CartesianPoint("", "2");
    s1.setCoordinates(Arrays.asList(new Double[] {0.0, 2.5, 0.0}));

    CartesianPoint s2 = new CartesianPoint("", "3");
    s2.setCoordinates(Arrays.asList(new Double[] {11.0, 2.5, 0.0}));
    
    CartesianPoint s3 = new CartesianPoint("", "4");
    s3.setCoordinates(Arrays.asList(new Double[] {0.0, 2.5, 6.0}));
    
    face.setVertices(Arrays.asList(new CartesianPoint[] {s1, s2, s3}));
    
    double[] n = { 0.0, 1.0, 0.0};
    face.setNormal(n);
    
    face.setPlane(true);
    
    return face;
  }
  
  @Test
  public void testCubes() throws IOException {
    String fileName = MODEL_PATH + "cubes.step";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
    
    Preprocessor prep = new Preprocessor(ast.get());
    prep.preprocess();
    TranslationAnalyzer ta = new TranslationAnalyzer();
    List<Event> restrictions = ta.analyze(prep.getClosedShells());
    double[] exRes = {-1.0, 0.0, 0.0};
    
    assertEquals(2, restrictions.size(), 0);
    for (Event restriction : restrictions) {
      // check IDs
      assertEquals("Wrong assembly IDs.", "522", restriction.getAssemblies()[0].getId());
      assertEquals("Wrong assembly IDs.", "16", restriction.getAssemblies()[1].getId());
      
      // check normal vector
      assertArrayEquals(exRes, ((TransaltionalRestriction) restriction).getTranslationVec(), 0.00001);
    }
    
  }
  
}
