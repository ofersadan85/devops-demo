import jenkins.model.Jenkins
import hudson.security.HudsonPrivateSecurityRealm
import hudson.security.GlobalMatrixAuthorizationStrategy

def env = System.getenv()

realm = new HudsonPrivateSecurityRealm(false)
admin = realm.createAccount(env.JENKINS_USER, env.JENKINS_PASS)
admin.save()

startegy = new GlobalMatrixAuthorizationStrategy()
startegy.add(Jenkins.ADMINISTER, env.JENKINS_USER)

def jenkins = Jenkins.getInstance()
jenkins.setSecurityRealm(realm)
jenkins.setAuthorizationStrategy(startegy)
jenkins.save()
