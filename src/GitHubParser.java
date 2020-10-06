import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GitHubParser {

    static String token ="put_your_token_here";

    private static String getStringFromStream(InputStream stream) throws IOException {
        if (stream != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[2048];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                int counter;
                while ((counter = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, counter);
                }
            } finally {
                stream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }


    private static String readUrl(String urlString) throws IOException {
        String newUrl = urlString;
        try {
            URL myURL = new URL(newUrl);
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + token);
            return getStringFromStream(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Commit getCommitByApi(String url, String developerLogin) throws IOException{
        ConcurrentMap<String, AtomicInteger> fileNames = new ConcurrentHashMap<>();
        ConcurrentMap<String, AtomicInteger> fileExtensions = new ConcurrentHashMap<>();
        ConcurrentMap<String, AtomicInteger> packages = new ConcurrentHashMap<>();
        String data = readUrl(url);
        JSONObject commit = new JSONObject(data);
        String date = commit.getJSONObject("commit").getJSONObject("author").getString("date");
        Integer month = Integer.parseInt(date.substring(0,4)+date.substring(5,7));
        JSONArray files = commit.getJSONArray("files");
        for (int i=0; i< files.length(); i++) {
            String file = ((JSONObject) files.get(i)).getString("filename");
            String extension = "";
            String packageName = "!-root-!";
            String fileName="";
            if (file.contains("/")) {
                packageName = file.substring(0, file.lastIndexOf("/"));
                fileName = file.substring(file.lastIndexOf("/") + 1);
            }
            if (fileName.contains(".")){
                extension = fileName.substring(fileName.lastIndexOf("."));
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            fileExtensions.putIfAbsent(extension, new AtomicInteger(0));
            fileNames.putIfAbsent(fileName, new AtomicInteger(0));
            packages.putIfAbsent(packageName, new AtomicInteger(0));
            fileExtensions.get(extension).incrementAndGet();
            fileNames.get(fileName).incrementAndGet();
            packages.get(packageName).incrementAndGet();
        }
        return new Commit(developerLogin, month, fileNames, fileExtensions, packages);
    }

    private static Developer getDeveloperByApi(Integer id, Integer numContributions, String url, String login) throws IOException {

        String data = readUrl(url);
        JSONArray dev = new JSONArray(data);
        ConcurrentMap<Integer, AtomicInteger> commitsByMonth = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileNamesByMonth = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> fileExtensionsByMonth = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, ConcurrentMap<String, AtomicInteger>> packagesByMonth = new ConcurrentHashMap<>();
        String name = (String)((JSONObject) dev.get(0)).getJSONObject("commit").getJSONObject("author").get("name");
        for (int i=0; i< dev.length(); i++){
            String commitUrl = (String)((JSONObject) dev.get(i)).get("url");
            Commit commit = getCommitByApi(commitUrl, login);
            Integer month = commit.getMonth();
            commitsByMonth.putIfAbsent(month, new AtomicInteger(0));
            fileNamesByMonth.putIfAbsent(month, new ConcurrentHashMap<>());
            fileExtensionsByMonth.putIfAbsent(month, new ConcurrentHashMap<>());
            packagesByMonth.putIfAbsent(month, new ConcurrentHashMap<>());
            commitsByMonth.get(month).incrementAndGet();

            for (Map.Entry<String, AtomicInteger> entry: commit.getFileExtensions().entrySet()){
                fileExtensionsByMonth.get(month).put(entry.getKey(),
                        new AtomicInteger (fileExtensionsByMonth.get(month).getOrDefault(entry.getKey(),new AtomicInteger(0))
                                .addAndGet(entry.getValue().get())));
            }
            for (Map.Entry<String, AtomicInteger> entry: commit.getFileNames().entrySet()){
                fileNamesByMonth.get(month).put(entry.getKey(),
                        new AtomicInteger (fileNamesByMonth.get(month).getOrDefault(entry.getKey(),new AtomicInteger(0))
                                .addAndGet(entry.getValue().get())));
            }

            for (Map.Entry<String, AtomicInteger> entry: commit.getPackages().entrySet()){
                packagesByMonth.get(month).put(entry.getKey(),
                        new AtomicInteger (packagesByMonth.get(month).
                                getOrDefault(entry.getKey(),new AtomicInteger(0))
                                .addAndGet(entry.getValue().get())));
            }
        }

        return new Developer(id, numContributions, name, login, commitsByMonth, fileNamesByMonth, fileExtensionsByMonth, packagesByMonth);
    }

    private static List<Developer> getDevelopersList(String repoName) throws IOException{
        ArrayList<Developer> result = new ArrayList<>();
        String data = readUrl("https://api.github.com/repos/"+repoName+"/contributors?q=contributions&order=desc");
        JSONArray developers = new JSONArray(data);
        int maxSize=Math.min(50, developers.length());
        for (int i =0; i<maxSize; i++){
            JSONObject developer =(JSONObject)developers.get(i);
            String login = developer.getString("login");
            result.add(getDeveloperByApi(developer.getInt("id"), developer.getInt("contributions"),
                    "https://api.github.com/repos/"+repoName+"/commits?author="+login, login));

        }
        return result;
    }



    public static void main(String... args) throws IOException{
        ContributorsGraph graph = new ContributorsGraph(getDevelopersList("facebook/react"));
        GraphPrinter printer = new GraphPrinter(graph);
        printer.printDevelopersShort("saved_graph/dev_short.txt");
        printer.printDevelopersFull("saved_graph/dev_full.txt");
        printer.printEdges("saved_graph/edges.txt");
        printer.printAdjMatrix("saved_graph/adj_matrix.txt");
    }

}
