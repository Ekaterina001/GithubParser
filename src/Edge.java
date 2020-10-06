import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Edge {
    private Integer devId1;
    private Integer devId2;
    private Integer indexInList1;
    private Integer indexInList2;
    private ConcurrentMap<Integer, AtomicInteger> numberOfCommonFilenamesByMonth;
    private ConcurrentMap<Integer, AtomicInteger> numberOfCommonPackagesByMonth;
    private ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> numberOfCommonExtensionsByMonth;

    public Edge(Integer devId1, Integer devId2, Integer indexInList1, Integer indexInList2, ConcurrentMap<Integer, AtomicInteger> numberOfCommonFilenamesByMonth, ConcurrentMap<Integer, AtomicInteger> numberOfCommonPackagesByMonth, ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> numberOfCommonExtensionsByMonth) {
        this.devId1 = devId1;
        this.devId2 = devId2;
        this.indexInList1 = indexInList1;
        this.indexInList2 = indexInList2;
        this.numberOfCommonFilenamesByMonth = numberOfCommonFilenamesByMonth;
        this.numberOfCommonPackagesByMonth = numberOfCommonPackagesByMonth;
        this.numberOfCommonExtensionsByMonth = numberOfCommonExtensionsByMonth;
    }

    public Integer getDevId1() {
        return devId1;
    }

    public Integer getDevId2() {
        return devId2;
    }

    public Integer getIndexInList1() {
        return indexInList1;
    }

    public Integer getIndexInList2() {
        return indexInList2;
    }

    public ConcurrentMap<Integer, AtomicInteger> getNumberOfCommonFilenamesByMonth() {
        return numberOfCommonFilenamesByMonth;
    }

    public ConcurrentMap<Integer, AtomicInteger> getNumberOfCommonPackagesByMonth() {
        return numberOfCommonPackagesByMonth;
    }

    public ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> getNumberOfCommonExtensionsByMonth() {
        return numberOfCommonExtensionsByMonth;
    }

}
