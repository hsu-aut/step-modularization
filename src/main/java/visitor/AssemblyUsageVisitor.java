package visitor;

import step.topology.AssemblyUsage;
import step._ast.ASTInstance;
import step._ast.ASTList;
import step._ast.ASTNamedList;
import step._visitor.STEPVisitor;

import java.util.LinkedList;
import java.util.List;

public class AssemblyUsageVisitor implements STEPVisitor {
    private static String NEXT_ASSEMBLY_USAGE_OCCURRENCE = "NEXT_ASSEMBLY_USAGE_OCCURRENCE";
    private List<AssemblyUsage> assemblyUsages;

    public AssemblyUsageVisitor(){
        this.assemblyUsages = new LinkedList<AssemblyUsage>();
    }

    @Override
    public void visit(ASTInstance node){
        //System.out.println("visit called: " + node.getId().getDigits());
        if(!node.isPresentNamedList())
            return;
        ASTNamedList namedList = node.getNamedList();
        String name = node.getNamedList().getName().getName();
        if(name.equals(NEXT_ASSEMBLY_USAGE_OCCURRENCE)){
            ASTList list = node.getNamedList().getList();
            String user = list.getParameter(3).getId().getDigits();
            String used = list.getParameter(4).getId().getDigits();
            assemblyUsages.add(new AssemblyUsage(user,used));

        }
    }

    public List<AssemblyUsage> getAssemblyUsages(){
        return this.assemblyUsages;
    }
}
