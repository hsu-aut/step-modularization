package tools;

import java.io.IOException;

public class STEP_ToolingNames {

  public static void main(String[] args) throws IOException {
    GraphBuilder builder = new GraphBuilder();
    builder.createGraph();
//    STEPParser stepParser = new STEPParser();
//    List<Path> paths = new LinkedList<>();
//    paths.add(Paths.get("step-language","src", "main", "resources","step", "Pneumatikzylinder.stp"));
//    paths.add(Paths.get("step-language","src", "main", "resources","step", "Drehtisch.stp"));
//    for (Path path: paths) {
//      System.out.println(path.getFileName());
//      Optional<ASTStepExchangeFile> ast = stepParser.parse(path.toAbsolutePath().toString());
//      if (ast.isPresent()) {
//        ProductVisitor step_toolingNameFinder = new ProductVisitor();
//        ast.get().accept(step_toolingNameFinder);
//        for (Product product : step_toolingNameFinder.getProducts()) {
//          System.out.println(product.getName());
//        }
//      }else {
//        Log.error("AST is empty");
//      }
//    }
  }
}