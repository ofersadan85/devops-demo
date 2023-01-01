FROM jenkins/jenkins:lts-alpine
COPY plugins /var/jenkins_home/plugins
USER root
RUN chown -R jenkins:jenkins /var/jenkins_home/plugins
USER jenkins
