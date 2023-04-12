package tools;

import io.github.livingdocumentation.dotdiagram.GraphvizDotWriter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import step.Entity;
import step.ManifoldSolidBrep;
import step.RepresentationItem;
import step.topology.AssemblyUsage;
import step.topology.Product;
import step._ast.ASTStepExchangeFile;
import step._parser.STEPParser;
import visitor.AssemblyUsageVisitor;
import visitor.ProductVisitor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GraphBuilder {
//    Graph<Product, DefaultEdge> graph;

    public void createGraph(){
        STEPParser stepParser = new STEPParser();
        Path path = Paths.get("src", "main", "resources","step", "Station.stp");
        System.out.println(path.getFileName());
        Optional<ASTStepExchangeFile> ast = null;
        try {
            ast = stepParser.parse(path.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProductVisitor productVisitor = new ProductVisitor();
        ast.get().accept(productVisitor);
        List<Product> products = productVisitor.getProducts();

        AssemblyUsageVisitor assemblyUsageVisitor = new AssemblyUsageVisitor();
        ast.get().accept(assemblyUsageVisitor);
        List<AssemblyUsage> assemblyUsages = assemblyUsageVisitor.getAssemblyUsages();

        Graph<Product, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        Map<String,Product> stringProductMap = new HashMap<>();
        for(Product product: products){
            graph.addVertex(product);
            stringProductMap.put(product.getId(),product);
        }
        for(AssemblyUsage assemblyUsage: assemblyUsages){
            graph.addEdge(stringProductMap.get(assemblyUsage.getParent()),stringProductMap.get(assemblyUsage.getChild()));
        }




        DOTExporter<Product, DefaultEdge> exporter =
                new DOTExporter<>(v -> v.toString().replace("Product@","").replace(".",""));
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getName()));
            return map;
        });
        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);

        System.out.println(writer.toString());
    }

    public void createGraph(Map<String,Entity> nodes, Map<String,List<String>> edges){
        Graph graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for(Entity node: nodes.values()){
            graph.addVertex(node);
        }
        for(String parent: edges.keySet()){
            Entity v = nodes.get(parent);
            for (String child: edges.get(parent)) {
                Entity entity = nodes.get(child);
                if(entity != null) {
                    graph.addEdge(v, entity);
                }
            }
        }

        DOTExporter<RepresentationItem, DefaultEdge> exporter =
                new DOTExporter<>(v -> v.getEntityLabel()+v.getId());
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.getEntityLabel()+v.getId()));
            if(v.getEntityLabel().equals(ManifoldSolidBrep.LABEL)){
                map.put("color", DefaultAttribute.createAttribute("red"));
            }
            return map;
        });
        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        System.out.println(writer.toString());

    }

    private void drawImage(){
        Path dot = Paths.get("C:", "Program Files", "Graphviz", "bin");
        Path output = Paths.get("src", "main", "resources", "img");
        String format = "png";
        String gcmd =  "dot -Tpng";

        GraphvizDotWriter imgWriter = new GraphvizDotWriter(output.toAbsolutePath().toString(),dot.toAbsolutePath().toString(),format, gcmd);
        try {
            imgWriter.render("graph.dot");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
