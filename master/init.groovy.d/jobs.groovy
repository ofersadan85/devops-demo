import jenkins.model.Jenkins
import hudson.plugins.git.GitSCM
import hudson.plugins.git.BranchSpec
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

def scm = new GitSCM('https://github.com/ofersadan85/devops-demo')
scm.branches = [new BranchSpec('refs/heads/main')]
jenkins = Jenkins.instance
job = new WorkflowJob(jenkins, 'example-app-python')
job.definition = new CpsScmFlowDefinition(scm, 'example-app-python/Jenkinsfile')
jenkins.reload()
