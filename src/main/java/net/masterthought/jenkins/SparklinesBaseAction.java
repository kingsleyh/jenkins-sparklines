package net.masterthought.jenkins;

import hudson.FilePath;
import hudson.PluginWrapper;
import hudson.model.AbstractItem;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.ProminentProjectAction;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public abstract class SparklinesBaseAction implements Action {
    
    public String getUrlName(){
        return "jenkins-sparklines";
    }
    
    public String getDisplayName(){
        return "Jenkins Sparklines";
    }
    
    public String getIconFileName(){
            return "/plugin/jenkins-sparklines/cuke.png";
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        DirectoryBrowserSupport dbs = new DirectoryBrowserSupport(this, new FilePath(this.dir()), this.getTitle(), "graph.gif", false);
        dbs.setIndexFileName("build-status.html");
        dbs.generateResponse(req, rsp, this);
    }

    protected abstract String getTitle();

    protected abstract File dir();
}










