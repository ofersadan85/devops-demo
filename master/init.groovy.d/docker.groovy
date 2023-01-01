import jenkins.model.Jenkins
import com.nirima.jenkins.plugins.docker.DockerCloud
import com.nirima.jenkins.plugins.docker.DockerTemplate
import com.nirima.jenkins.plugins.docker.DockerTemplateBase
import io.jenkins.docker.connector.DockerComputerAttachConnector

base = new DockerTemplateBase('ofersadan85/jenkins-agent-python-docker:latest')
base.setVolumesFrom2('socat')
dockerTemplate = new DockerTemplate(
    base,
    new DockerComputerAttachConnector(),
    'python-alpine', // Label
    '/home/jenkins', // Remote FS
    '2', // Instance cap (number of containers)
)

dockerCloud = new DockerCloud(
    'docker-internal', // Cloud name
    [dockerTemplate],
    'tcp://socat:2375', // Docker server URL
    2, // Container cap
    30, // Connect timeout
    40, // Read timeout
    '', // Credentials ID
    '', // Docker version
    '', // Docker hostname
)

jenkins = Jenkins.getInstance()
jenkins.clouds.add(dockerCloud)
jenkins.save()
