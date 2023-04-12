import org.junit.Test;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class STEPParserTest {
  @Test
  public void testProductionGripper() throws IOException {
    String fileName = "src/test/resources/Production Gripper.STEP";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    assertFalse(stepParser.hasErrors());
  }

  @Test
  public void testProductionGripperTesting() throws IOException {
    String fileName = "src/test/resources/Production Gripper_testing.STEP";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    System.out.println(ast.get().getData(0).getInstanceList());

    assertFalse(stepParser.hasErrors());
  }

  @Test
  public void testHocker() throws IOException {
    String fileName = "src/test/resources/Hocker.STEP";
    STEPParser stepParser = new STEPParser();
    Optional<ASTStepExchangeFile> ast = stepParser.parse(fileName);
    assertTrue(ast.isPresent());
    System.out.println(ast.get().getData(0).getInstanceList());

    assertFalse(stepParser.hasErrors());
  }
}
