import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> existingPages;
    private HashMap<String, Integer> osFrequency;
    private int totalOsCount;
    private HashSet<String> nonExistingPages;
    private HashMap<String, Integer> browserFrequency;
    private int totalBrowserCount;
    private int totalVisits = 0;
    private int errorRequests = 0;
    private Set<String> uniqueUserIPs = new HashSet<>();
    private List<LogEntry> logEntries = new ArrayList<>();

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;

    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();

        if (entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }

        if (entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }

        if (entry.getResponseCode() == 200) {
            existingPages.add(entry.getRequestPath());
        }

        if (entry.getResponseCode() == 404) {
            nonExistingPages.add(entry.getRequestPath());
        }

        String os = entry.getUserAgent().getOsType();
        if (osFrequency.containsKey(os)){
            osFrequency.put(os, osFrequency.get(os) + 1);
        } else {
            osFrequency.put(os, 1);
        }
        totalOsCount++;

        String browser = entry.getUserAgent().getBrowser();
        if (browserFrequency.containsKey(browser)){
            browserFrequency.put(browser, browserFrequency.get(browser) + 1);
        } else {
            browserFrequency.put(browser, 1);
        }
        totalBrowserCount++;

        if (!entry.getUserAgent().getAllUserAgent().toLowerCase().contains("bot")) {
            totalVisits++;
            uniqueUserIPs.add(entry.getIpAddress());
        }

        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorRequests++;
        }
        logEntries.add(entry);
    }

    public double getTrafficRate() {
        long hoursDifference = java.time.Duration.between(minTime, maxTime).toHours();

        if (hoursDifference == 0) {
            return 0;
        }

        return (double) totalTraffic / hoursDifference;
    }

    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public HashSet<String> getNonExistingPages() {
        return nonExistingPages;
    }

    public HashMap<String, Double> getOsFrequency() {

        HashMap<String, Double> osStatistics = new HashMap<>();
        for (Map.Entry<String, Integer> entry: osFrequency.entrySet()){
            String os = entry.getKey();
            int count = entry.getValue();
            double proportion = (double)count/totalOsCount;
            osStatistics.put(os, proportion);
        }

        return osStatistics;
    }

    public HashMap<String, Double> getBrowserFrequency() {

        HashMap<String, Double> browserStatistics = new HashMap<>();
        for (Map.Entry<String, Integer> entry: browserFrequency.entrySet()){
            String browser = entry.getKey();
            int count = entry.getValue();
            double proportion = (double)count/totalBrowserCount;
            browserStatistics.put(browser, proportion);
        }

        return browserStatistics;
    }
    public double averageVisitsPerHour() {
        long hours = logEntries.stream()
                .map(LogEntry::getDateTime)
                .map(timestamp -> timestamp.toInstant(ZoneOffset.ofHours(3)).truncatedTo(ChronoUnit.HOURS))
                .distinct()
                .count();
        return hours > 0 ? (double) totalVisits / hours : 0;
    }

    public double averageErrorsPerHour() {
        long hours = logEntries.stream()
                .map(LogEntry::getDateTime)
                .map(timestamp -> timestamp.toInstant(ZoneOffset.ofHours(3)).truncatedTo(ChronoUnit.HOURS))
                .distinct()
                .count();

        return hours > 0 ? (double) errorRequests / hours : 0;
    }

    public double averageVisitsPerUser() {
        long realUsersCount = uniqueUserIPs.size();
        return realUsersCount > 0 ? (double) totalVisits / realUsersCount : 0;
    }
}
