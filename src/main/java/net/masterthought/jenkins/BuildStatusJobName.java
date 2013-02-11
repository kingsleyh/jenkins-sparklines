package net.masterthought.jenkins;

public class BuildStatusJobName {

    public static String BuildStatus(String buildStatus, String pluginUrlPath, String jobPath, String jobName) {

        BuildStatus status = BuildStatus.fromStatus(buildStatus);
        int jobNameLength = jobName.length();
        double CHAR_LENGTH = 7.5;
        double itemLength = (jobNameLength * CHAR_LENGTH) + 5;
        int statusLength = 50;
        double canvasLength = itemLength + statusLength + 5;

        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<script type=\"text/javascript\" src=\"/" + pluginUrlPath + "plugin/jenkins-sparklines/processing.js\"></script>\n" +
                "</head>\n" +
                "<body>\t\n" +
                "<script type=\"application/processing\" data-processing-target=\"build-status\">\n" +
                "void setup()\n" +
                "{\n" +
                "size("+ canvasLength +", 19);\n" +
                "background(#FFFFFF);\n" +
                "stroke(#424242);\n" +
                "fill(#424242);\n" +
                "rect(0, 3, " + (itemLength + 5) + ", 15, 5, 0, 0, 5);\n" +
                "\n" +
                "font = loadFont(\"arial\"); \n" +
                "textFont(font); \n" +
                "\n" +
                "fill(#FFFFFF);\n" +
                "text(\"" + jobName + "\", 4, 15);\n" +
                "\n" +
                "stroke(" + status.getColour() + ");\n" +
                "fill(" + status.getColour() + ");\n" +
                "rect(" + itemLength + ",3,50,15, 0, 5, 5, 0);\n" +
                "\n" +
                "fill(#FFFFFF);\n" +
                "text(\"" + status.getText() + "\", " + (itemLength + 5)+ ", 15);\n" +
                "\n" +
                "}\n" +
                "void mousePressed() {"+
                "link(\"/"+ pluginUrlPath + jobPath + "\", \"_new\");" +
                "}" +
                "</script>\n" +
                "<canvas id=\"build-status\"></canvas>\n" +
                "</body>\n" +
                "</html> \n";
        return code;
    }

}
