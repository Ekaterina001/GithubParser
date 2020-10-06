import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphPrinter {
    ContributorsGraph graph;
    String separator;

    GraphPrinter(ContributorsGraph graph, String separator){
        this.graph=graph;
        this.separator = separator;
    }


    GraphPrinter(ContributorsGraph graph){
        this.graph=graph;
        this.separator = ",";
    }

    public void printDevelopersShort(String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.print("[");
        Boolean needSeparator=false;
        List<Developer> developersList = graph.getDevelopersList();
        for (int i=0; i<developersList.size(); i++){
            Developer dev = developersList.get(i);
            if(needSeparator){
                writer.print(separator);
            }
            needSeparator=true;
            writer.print("{num_in_list:"+ i);
            writer.print(separator +"id:"+ dev.getId());
            writer.print(separator+"name:"+ dev.getName());
            writer.print(separator+"login:"+ dev.getLogin());
            writer.print(separator+"num_contributions:" + dev.getNumContributions()+"}");
        }
        writer.print("]");
        writer.close();
    }



    private <T> void printShortMap(ConcurrentMap<T, AtomicInteger> mapToPrint, PrintWriter writer){
        Boolean needSeparator=false;
        for (Map.Entry<T, AtomicInteger> entry : mapToPrint.entrySet()){
            if(needSeparator) {
                writer.print(separator);
            }
            needSeparator=true;
            writer.print(entry.getKey()+":"+entry.getValue());
        }

    }

    private  void printLongMap(Map<Integer, ConcurrentMap<String, AtomicInteger>> mapToPrint, PrintWriter writer){
        boolean needSeparator=false;
        boolean needSeparatorInner = false;
        for (Map.Entry<Integer, ConcurrentMap<String, AtomicInteger>> entry : mapToPrint.entrySet()){
            if(needSeparator){
                writer.print(separator);
            }
            needSeparator=true;
            writer.print(entry.getKey()+":{");
            needSeparatorInner=false;
            for (Map.Entry<String, AtomicInteger> entry1: entry.getValue().entrySet()){
                if(needSeparatorInner){
                    writer.print(separator);
                }
                needSeparatorInner=true;
                writer.print(entry1.getKey()+":"+entry1.getValue());
            }
            writer.print("}");
        }

    }

    public void printDevelopersFull(String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.print("[");
        Boolean needSeparator=false;
        List<Developer> developersList = graph.getDevelopersList();
        for (int i=0; i<developersList.size(); i++){
            Developer dev = developersList.get(i);
            if(needSeparator){
                writer.print(separator);
            }
            needSeparator=true;
            writer.print("{num_in_list:"+ i);
            writer.print(separator +"id:"+ dev.getId());
            writer.print(separator+"name:"+ dev.getName());
            writer.print(separator+"login:"+ dev.getLogin());
            writer.print(separator+"num_contributions:" + dev.getNumContributions());
            writer.print(separator+"commits_by_month:{");
            printShortMap(dev.getCommitsByMonth(), writer);
            writer.print("}"+separator+"file_extensions_by_month:{");
            printLongMap(dev.getFileExtensionsByMonth(), writer);
            writer.print("}"+separator+"packages_by_month:{");
            printLongMap(dev.getPackagesByMonth(), writer);
            writer.print("}"+separator+"file_names_by_month:{");
            printLongMap(dev.getFileNamesByMonth(), writer);
            writer.print("}}");
        }
        writer.print("]");
        writer.close();
    }

    public void printEdges(String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.print("[");
        Boolean needSeparator=false;
        List<Edge> edgeList = graph.getDevRelations();
        for (int i=0; i<edgeList.size(); i++){
            if(needSeparator){
                writer.print(separator);
            }
            needSeparator=true;

            Edge edge = edgeList.get(i);
            writer.print("{dev1:"+edge.getDevId1());
            writer.print(separator+ "dev1_in_list:"+edge.getIndexInList1());
            writer.print(separator+"dev2:"+edge.getDevId2());
            writer.print(separator+ "dev2_in_list:"+edge.getIndexInList2());
            writer.print(separator+ "file_extensions:{");
            printLongMap(edge.getNumberOfCommonExtensionsByMonth(), writer);
            writer.print("}"+separator+ "directories:{");
            printShortMap(edge.getNumberOfCommonPackagesByMonth(), writer);
            writer.print("}"+separator+ "files:{");
            printShortMap(edge.getNumberOfCommonFilenamesByMonth(), writer);
            writer.print("}}");

        }
        writer.print("]");
        writer.close();
    }

    public void printAdjMatrix(String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.print("[");
        Boolean needSeparator=false;
        Integer numDevs = graph.getDevelopersList().size();
        for (int i=0; i<numDevs; i++){
            for(int j=0; j<numDevs; j++){
                if((i==j)||(!graph.getAdjMatrix().containsKey(i))||(!graph.getAdjMatrix().get(i).containsKey(j))){
                    continue;
                }
                if(needSeparator){
                    writer.print(separator);
                }
                needSeparator=true;
                Edge edge = graph.getAdjMatrix().get(i).get(j);
                if(i<j) {
                    writer.print("{dev1:" + edge.getDevId1());
                    writer.print(separator + "dev1_in_list:" + edge.getIndexInList1());
                    writer.print(separator + "dev2:" + edge.getDevId2());
                    writer.print(separator + "dev2_in_list:" + edge.getIndexInList2());
                } else{
                    writer.print("{dev1:" + edge.getDevId2());
                    writer.print(separator + "dev1_in_list:" + edge.getIndexInList2());
                    writer.print(separator + "dev2:" + edge.getDevId1());
                    writer.print(separator + "dev2_in_list:" + edge.getIndexInList1());
                }
                writer.print(separator+ "file_extensions:{");
                printLongMap(edge.getNumberOfCommonExtensionsByMonth(), writer);
                writer.print("}"+separator+ "directories:{");
                printShortMap(edge.getNumberOfCommonPackagesByMonth(), writer);
                writer.print("}"+separator+ "files:{");
                printShortMap(edge.getNumberOfCommonFilenamesByMonth(), writer);
                writer.print("}}");

            }
        }
        writer.print("]");
        writer.close();
    }

}
