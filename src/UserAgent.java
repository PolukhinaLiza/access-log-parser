public class UserAgent {
    private final String osType;
    private final String browser;

    public UserAgent(String userAgent) {

        this.osType = extractOsType(userAgent);
        this.browser = extractBrowser(userAgent);
    }

    public String getOsType() {
        return osType;
    }

    public String getBrowser() {
        return browser;
    }

    private String extractOsType(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Other";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        }
        return "Other";
    }
}
