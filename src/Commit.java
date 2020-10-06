import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Commit {
    private String developerLogin;
    private Integer month;
    private ConcurrentMap<String, AtomicInteger> fileNames;
    private ConcurrentMap<String, AtomicInteger> fileExtensions;
    private ConcurrentMap<String, AtomicInteger> packages;

    public Commit(String developerLogin, Integer month, ConcurrentMap<String, AtomicInteger> fileNames, ConcurrentMap<String, AtomicInteger> fileExtensions, ConcurrentMap<String, AtomicInteger> packages) {
        this.developerLogin = developerLogin;
        this.month = month;
        this.fileNames = fileNames;
        this.fileExtensions = fileExtensions;
        this.packages = packages;
    }

    public String getDeveloperLogin() {
        return developerLogin;
    }

    public Integer getMonth() {
        return month;
    }

    public ConcurrentMap<String, AtomicInteger> getFileNames() {
        return fileNames;
    }

    public ConcurrentMap<String, AtomicInteger> getFileExtensions() {
        return fileExtensions;
    }

    public ConcurrentMap<String, AtomicInteger> getPackages() {
        return packages;
    }
}
