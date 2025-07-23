import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private final String ipAddress;
    private final LocalDateTime dateTime;
    private final HttpMethod method;
    private final String requestPath;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final String userAgent;

    public LogEntry(String line) {
        String[] parts = line.split(" ");

        this.ipAddress = parts[0];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
        String dateTimeStr = parts[3].substring(1) + " " + parts[4].substring(0, parts[4].length() - 1);
        this.dateTime = LocalDateTime.parse(dateTimeStr, formatter);

        this.method = HttpMethod.valueOf(parts[5].substring(1).toUpperCase());
        this.requestPath = parts[6] + " " + parts[7].substring(0, parts[7].length() - 1);
        this.responseCode = Integer.parseInt(parts[8]);
        this.dataSize = Integer.parseInt(parts[9]);
        this.referer = parts[10];
        this.userAgent = parts.length > 11 ? String.join(" ", java.util.Arrays.copyOfRange(parts, 11, parts.length)).replace("\"", "") : "-";
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}
