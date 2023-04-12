import org.junit.Test;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import tools.GraphBuilder;
import visitor.AssemblyUsageVisitor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class GraphExport {

    @Test
    public void test(){
        STEPParser stepParser = new STEPParser();
        Path path = Paths.get("src", "main", "resources","step", "Drehtisch.stp");
        Optional<ASTStepExchangeFile> ast = null;
        try {
            ast = stepParser.parse(path.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(ast.isPresent());
        AssemblyUsageVisitor exporter = new AssemblyUsageVisitor();
        ast.get().accept(exporter);

    }

    @Test
    public void test2() {
        GraphBuilder builder = new GraphBuilder();
        builder.createGraph();
    }
}


