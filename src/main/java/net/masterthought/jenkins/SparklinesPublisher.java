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
import net.masterthought.sparklines.*;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class SparklinesPublisher extends Recorder {

    public final String pluginUrlPath;

    @DataBoundConstructor
    public SparklinesPublisher(String pluginUrlPath) {
        this.pluginUrlPath = pluginUrlPath;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {

        listener.getLogger().println("[SparklinesPublisher] Generating Sparklines Build Result Page ...");

        ArrayList<BuildInformation> jobHistory = aggregateJobHistory(build, listener);


        File targetBuildDirectory = new File(build.getRootDir(), "jenkins-sparklines");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
        }

        try {
            listener.getLogger().println("[SparklinesPublisher] Result is: " + build.getProject().getName());

            SparklinesGenerator sparklinesGenerator = new SparklinesGenerator(build.getResult().toString(), pluginUrlPath, build.getUrl(), targetBuildDirectory, build.getProject().getDisplayName(), jobHistory);
            sparklinesGenerator.generateBuildStatusBasic();
            sparklinesGenerator.generateBuildStatusJobName();
            sparklinesGenerator.generateJobStatusHistory();
            sparklinesGenerator.generateJobStatusHistoryPie();
            sparklinesGenerator.generateOverview();

        } catch (Exception e) {
            e.printStackTrace();
        }

        build.addAction(new SparklinesBuildAction(build));
        return true;
    }

    private ArrayList<BuildInformation> aggregateJobHistory(AbstractBuild<?, ?> build, BuildListener listener) {
        ArrayList<BuildInformation> buildInformationList = new ArrayList<BuildInformation>();
        Set historySet = build.getProject()._getRuns().entrySet();
        Iterator iterator = historySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry map = (Map.Entry) iterator.next();
            int key = (Integer) map.getKey();
            AbstractBuild historicalBuild = (AbstractBuild) map.getValue();
            buildInformationList.add(new BuildInformation(key, historicalBuild.getResult().toString(), historicalBuild.getDurationString()));
            listener.getLogger().println("[History] build: " + key + " result: " + historicalBuild.getResult());
        }
        return buildInformationList;
    }

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
