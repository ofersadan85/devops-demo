import jenkins.model.JenkinsLocationConfiguration

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()

jenkinsLocationConfiguration.setAdminAddress('Jenkins Zero-ML <jenkins@zero-ml.com>')
jenkinsLocationConfiguration.setUrl('https://jenkins.zero-ml.com/')
jenkinsLocationConfiguration.save()
