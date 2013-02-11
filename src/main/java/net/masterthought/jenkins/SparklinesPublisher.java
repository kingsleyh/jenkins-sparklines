package net.masterthought.jenkins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SparklinesPublisher extends Recorder {

    public final String pluginUrlPath;

    @DataBoundConstructor
    public SparklinesPublisher(String pluginUrlPath) {
        this.pluginUrlPath = pluginUrlPath;
    }

    private String[] findJsonFiles(File targetDirectory) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*.json"});
        scanner.setBasedir(targetDirectory);
        scanner.scan();
        return scanner.getIncludedFiles();
    }

    private void writeFile(File file, String content) throws Exception {
        Writer writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {


        // Make a location to store 2 pages - 1 page of sparklines and 1 page of iframe snippets pointing back to page 1

        // Generate the sparklines page based on build result

        // Generate the snippets page

        listener.getLogger().println("[SparklinesPublisher] Generating Sparklines Build Result Page ...");
        File targetBuildDirectory = new File(build.getRootDir(), "jenkins-sparklines");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
        }

        File buildStatusPage = new File(targetBuildDirectory, "build-status.html");
        File jobStatusSpark = new File(targetBuildDirectory, "job-status.html");
        try {
            listener.getLogger().println("[SparklinesPublisher] Result is: " + build.getProject().getName());

            writeFile(buildStatusPage, BuildStatusBasic.BuildStatus(build.getResult().toString(),pluginUrlPath,build.getUrl()));
            writeFile(jobStatusSpark, BuildStatusJobName.BuildStatus(build.getResult().toString(),pluginUrlPath,build.getUrl(),build.getProject().getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        build.addAction(new SparklinesBuildAction(build));
        return true;
    }

//    private List<String> fullPathToJsonFiles(String[] jsonFiles, File targetBuildDirectory) {
//        List<String> fullPathList = new ArrayList<String>();
//        for (String file : jsonFiles) {
//            fullPathList.add(new File(targetBuildDirectory, file).getAbsolutePath());
//        }
//        return fullPathList;
//    }

    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new SparklinesProjectAction(project);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        @Override
        public String getDisplayName() {
            return "Generate Jenkins Sparklines";
        }


        // Performs on-the-fly validation on the file mask wildcard.
        public FormValidation doCheck(@AncestorInPath AbstractProject project,
                                      @QueryParameter String value) throws IOException, ServletException {
            FilePath ws = project.getSomeWorkspace();
            return ws != null ? ws.validateRelativeDirectory(value) : FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
}
