import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Developer {
    private final String name;
    private final String login;
    private final Integer id;
    private final Integer numContributions;
    private ConcurrentMap<Integer, AtomicInteger> commitsByMonth;
    private ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileNamesByMonth;
    private ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileExtensionsByMonth;
    private ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> packagesByMonth;

    public Developer(Integer id, Integer numContributions, String name, String login, ConcurrentMap<Integer, AtomicInteger> commitsByMonth, ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileNamesByMonth, ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileExtensionsByMonth, ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> packagesByMonth) {
        this.id=id;
        this.name = name;
        this.login = login;
        this.commitsByMonth = commitsByMonth;
        this.fileNamesByMonth = fileNamesByMonth;
        this.fileExtensionsByMonth = fileExtensionsByMonth;
        this.packagesByMonth = packagesByMonth;
        this.numContributions=numContributions;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public ConcurrentMap<Integer, AtomicInteger> getCommitsByMonth() {
        return commitsByMonth;
    }

    public ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> getFileNamesByMonth() {
        return fileNamesByMonth;
    }

    public ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> getFileExtensionsByMonth() {
        return fileExtensionsByMonth;
    }

    public ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> getPackagesByMonth() {
        return packagesByMonth;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumContributions() {
        return numContributions;
    }
}
