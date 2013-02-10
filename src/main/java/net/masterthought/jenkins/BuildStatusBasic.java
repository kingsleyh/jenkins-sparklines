package net.masterthought.jenkins;

public class BuildStatusBasic {

    public static String BuildStatus(String buildStatus, String pluginUrlPath) {

        BuildStatus status = BuildStatus.fromStatus(buildStatus);

        String code = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<script type=\"text/javascript\" src=\"/" + pluginUrlPath + "plugin/jenkins-sparklines/processing.js\"></script>\n" +
                "</head>\n" +
                "<body>\t\n" +
                "<script type=\"application/processing\" data-processing-target=\"build-status\">\n" +
                "void setup()\n" +
                "{\n" +
                "size(120, 19);\n" +
                "background(#FFFFFF);\n" +
                "stroke(#424242);\n" +
                "fill(#424242);\n" +
                "rect(0, 3, 79, 15, 5, 0, 0, 5);\n" +
                "\n" +
                "font = loadFont(\"arial\"); \n" +
                "textFont(font); \n" +
                "\n" +
                "fill(#FFFFFF);\n" +
                "text(\"build status\", 4, 15);\n" +
                "\n" +
                "stroke(" + status.getColour() + ");\n" +
                "fill(" + status.getColour() + ");\n" +
                "rect(69,3,50,15, 0, 5, 5, 0);\n" +
                "\n" +
                "fill(#FFFFFF);\n" +
                "text(\"" + status.getText() + "\", 73, 15);\n" +
                "\n" +
                "}\n" +
                "</script>\n" +
                "<canvas id=\"build-status\"></canvas>\n" +
                "</body>\n" +
                "</html> \n";
        return code;
    }

}
