import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ContributorsGraph {
    private List<Developer> developersList;
    private List<Edge> devRelations = new ArrayList<>();
    private Map<Integer, Map<Integer,Edge>> adjMatrix = new HashMap<>();

    private void calculateRelations(){
        Integer month = 0;
        Integer commonNum = 0;
        Boolean edgeToPut = false;
        for(int i=0; i<this.developersList.size();i++){
            Developer dev1 = developersList.get(i);
            for(int j = i+1; j<this.developersList.size(); j++){
                Developer dev2 =developersList.get(j);
                edgeToPut=false;
                ConcurrentMap<Integer, AtomicInteger> numberOfCommonFilenamesByMonth = new ConcurrentHashMap<>();
                ConcurrentMap<Integer, AtomicInteger> numberOfCommonPackagesByMonth = new ConcurrentHashMap<>();
                ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> numberOfCommonExtensionsByMonth = new ConcurrentHashMap<>();

                for (Map.Entry<Integer, ConcurrentMap<String, AtomicInteger>> entry: dev1.getFileNamesByMonth().entrySet()){
                    month = entry.getKey();
                    if (dev2.getFileNamesByMonth().containsKey(month)){
                        commonNum = 0;
                        for (Map.Entry<String, AtomicInteger> entry1 : entry.getValue().entrySet()) {
                            if (dev2.getFileNamesByMonth().get(month).containsKey(entry1.getKey())){
                                commonNum+=Math.min(dev2.getFileNamesByMonth().get(month).get(entry1.getKey()).get(),entry1.getValue().get());
                            }
                        }
                        if(commonNum>0){
                            edgeToPut=true;
                            numberOfCommonFilenamesByMonth.put(month, new AtomicInteger(commonNum));
                        }
                    }
                }

                for (Map.Entry<Integer, ConcurrentMap<String, AtomicInteger>> entry: dev1.getPackagesByMonth().entrySet()){
                    month = entry.getKey();
                    if (dev2.getPackagesByMonth().containsKey(month)){
                        commonNum = 0;
                        for (Map.Entry<String, AtomicInteger> entry1 : entry.getValue().entrySet()) {
                            if (dev2.getPackagesByMonth().get(month).containsKey(entry1.getKey())){
                                commonNum+=Math.min(dev2.getPackagesByMonth().get(month).get(entry1.getKey()).get(),entry1.getValue().get());
                            }
                        }
                        if(commonNum>0){
                            edgeToPut=true;
                            numberOfCommonPackagesByMonth.put(month, new AtomicInteger(commonNum));
                        }
                    }
                }

                for (Map.Entry<Integer, ConcurrentMap<String, AtomicInteger>> entry: dev1.getFileExtensionsByMonth().entrySet()){
                    month = entry.getKey();
                    if (dev2.getPackagesByMonth().containsKey(month)){
                        for (Map.Entry<String, AtomicInteger> entry1 : entry.getValue().entrySet()) {
                            if (dev2.getFileExtensionsByMonth().get(month).containsKey(entry1.getKey())){
                                if(!numberOfCommonExtensionsByMonth.containsKey(month)){
                                    numberOfCommonExtensionsByMonth.put(month, new ConcurrentHashMap<>());
                                }
                                numberOfCommonExtensionsByMonth.get(month).put(entry1.getKey(), new AtomicInteger(Math.min(dev2.getFileExtensionsByMonth().get(month).get(entry1.getKey()).get(),entry1.getValue().get())));
                                edgeToPut = true;
                            }
                        }
                    }
                }

                if (edgeToPut){
                    Edge edge = new Edge(dev1.getId(), dev2.getId(), i, j, numberOfCommonFilenamesByMonth, numberOfCommonPackagesByMonth, numberOfCommonExtensionsByMonth);
                    devRelations.add(edge);
                    if(!adjMatrix.containsKey(i)){
                        adjMatrix.put(i, new HashMap<>());
                    }
                    if(!adjMatrix.containsKey(j)){
                        adjMatrix.put(j, new HashMap<>());
                    }
                    adjMatrix.get(i).put(j, edge);
                    adjMatrix.get(j).put(i, edge);
                }
            }
        }
    }




    ContributorsGraph(List<Developer> developersList){
        this.developersList=developersList;
        this.calculateRelations();
    }

    public List<Developer> getDevelopersList() {
        return developersList;
    }

    public List<Edge> getDevRelations() {
        return devRelations;
    }

    public Map<Integer, Map<Integer, Edge>> getAdjMatrix() {
        return adjMatrix;
    }
}
