import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> existingPages;
    private HashMap<String, Integer> osFrequency;
    private int totalOsCount;

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

        String os = entry.getUserAgent().getOsType();
        if (osFrequency.containsKey(os)){
            osFrequency.put(os, osFrequency.get(os) + 1);
        } else {
            osFrequency.put(os, 1);
        }
        totalOsCount++;
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
}
