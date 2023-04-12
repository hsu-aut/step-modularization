import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import step.Entity;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import step.edge.Direction;
import tools.GraphBuilder;
import visitor.GeometryVisitor;

public class GeometryExportTest {
  
  @Test
  @Ignore
  public void test() {
    STEPParser stepParser = new STEPParser();
    Path path = Paths.get("src", "main", "resources", "step", "Pneumatikzylinder.stp");
    Optional<ASTStepExchangeFile> ast = null;
    try {
      ast = stepParser.parse(path.toAbsolutePath().toString());
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ast.isPresent());
    GeometryVisitor exporter = new GeometryVisitor();
    ast.get().accept(exporter);
    System.out.println(exporter.getMissing());
    
    exporter.resolveReferences();
    Map<String, List<String>> multipleReferences = exporter.findMultipleReferences();
    Map<String, Entity> entities = exporter.getEntities();
    Map<String, Entity> entitiesWithMultiRefs = new HashMap<>();
    for (String key : multipleReferences.keySet()) {
      Entity entity = entities.get(key);
      entitiesWithMultiRefs.put(key, entity);
    }
    
    Set<Set<String>> components = exporter.findComponents();
    
    GraphBuilder bg = new GraphBuilder();
    bg.createGraph(entitiesWithMultiRefs, multipleReferences);
  }
  
  @Test
  public void testComponent() {
    GeometryVisitor exporter = new GeometryVisitor();
    Direction one = new Direction("one", "1");
    Direction two = new Direction("two", "2");
    Direction three = new Direction("three", "3");
    Direction four = new Direction("four", "4");
    Direction five = new Direction("five", "5");
    Direction six = new Direction("six", "6");
    Direction seven = new Direction("seven", "7");
    Map<String, Entity> entityMap = new HashMap<>();
    entityMap.put("1", one);
    entityMap.put("2", two);
    entityMap.put("3", three);
    entityMap.put("4", four);
    entityMap.put("5", five);
    entityMap.put("6", six);
    entityMap.put("7", seven);
    Map<String, List<String>> refMap = new HashMap<>();
    List<String> oneRefs = new ArrayList<>();
    oneRefs.add("2");
    oneRefs.add("3");
    List<String> fourRefs = new ArrayList<>();
    fourRefs.add("5");
    List<String> fiveRefs = new ArrayList<>();
    fiveRefs.add("6");
    refMap.put("1", oneRefs);
    refMap.put("4", fourRefs);
    refMap.put("5", fiveRefs);
    exporter.setEntities(entityMap);
    exporter.setReferences(refMap);
    Set<Set<String>> components = exporter.findComponents();
    assertTrue(components.size() == 3);
  }
  
}
