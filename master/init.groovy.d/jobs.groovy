import jenkins.model.Jenkins
import jenkins.branch.BranchSource
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject
import com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger

// I don't like this wildcard import, but it's the only way to get the traits for some reason
import org.jenkinsci.plugins.github_branch_source.*

jenkins = Jenkins.instance
job = new WorkflowMultiBranchProject(jenkins, 'example-app-python')
job.addTrigger(new PeriodicFolderTrigger('3m'))
github = new GitHubSCMSource(
    'ofersadan85',
    'devops-demo',
    'https://github.com/ofersadan85/devops-demo',
    true
)
traits = [
    new BranchDiscoveryTrait(BranchDiscoveryTrait.ALL_BRANCHES),
    new OriginPullRequestDiscoveryTrait(OriginPullRequestDiscoveryTrait.MERGE),
    new ForkPullRequestDiscoveryTrait(
        ForkPullRequestDiscoveryTrait.MERGE,
        new ForkPullRequestDiscoveryTrait.TrustNobody()
    )
]
github.setTraits(traits)
job.getProjectFactory().setScriptPath('example-app-python/Jenkinsfile')
job.getSourcesList().add(new BranchSource(github))

jenkins.reload()
