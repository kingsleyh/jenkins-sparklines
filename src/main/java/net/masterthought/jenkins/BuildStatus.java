package net.masterthought.jenkins;

public enum BuildStatus {

    SUCCESS("SUCCESS", "Passing", "#01DF01"),
    UNSTABLE("UNSTABLE", "Unstable", "#000000"),
    FAILURE("FAILURE", "Failing", "#FE2E64"),
    NOT_BUILT("NOT_BUILT", "NotBuilt", "#000000"),
    ABORTED("ABORTED", "Aborted", "#000000");

    private String status;
    private String text;
    private String colour;


    BuildStatus(String status, String text, String colour) {
        this.status = status;
        this.text = text;
        this.colour = colour;
    }

    public String getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public String getColour() {
        return colour;
    }

    public static BuildStatus valueForName(String text) {
        if (text != null) {
            for (BuildStatus b : BuildStatus.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

    public static BuildStatus fromStatus(String status) {
        if (status != null) {
            for (BuildStatus b : BuildStatus.values()) {
                if (status.equalsIgnoreCase(b.status)) {
                    return b;
                }
            }
        }
        return null;
    }

}
