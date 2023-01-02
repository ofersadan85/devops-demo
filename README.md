# DevOps-Demo

This is a Jenkins server setup for running Jenkins on a local machine. It will run a pipeline that executes a Python script.

## Demo production server

[![Better Uptime Badge](https://betteruptime.com/status-badges/v1/monitor/krqy.svg)](https://betteruptime.com/?utm_source=status_badge)

For demonstration purposes, this Jenkins server is working and available at [jenkins.zero-ml.com](https://jenkins.zero-ml.com). Contact me to get a username and password if you wish to inspect it.

## Requirements

1. Tested this only on Linux, in a x86_64 architecture. You should be able to run it on other architectures, but I can't guarantee it.
2. You need to have Docker & Docker Compose installed on your machine.
3. Clone this repository to your machine and `cd` into it.

## Local Setup

To setup this server locally, accessible at [localhost:8080](http://localhost:8080), run the following commands:

```bash
docker-compose --env-file=example.env up -d
```

After that, you can run the python build pipeline by accessing [localhost:8080/job/example-app-python](http://localhost:8080/job/example-app-python) and clicking on `Build Now`. Watch the build logs to see the pipeline in action. Login details are in [example.env](example.env).

## Security

- The local server does not use TLS/SSL certificates(The production server does). The local server is accessible through HTTP on port 8080 (The production server is accessible through HTTPS on port 443).
- The server is pre-configured with matrix-based security. You can login with the credentials in [example.env](example.env).
- The server image is built from the official Jenkins image, but provides only a frozen subset of plugins. This is to prevent the server from being compromised by malicious plugins, or compromised plugin updates. See the [master's Dockerfile](master/Dockerfile).
- The host does not expose the docker socket to the Jenkins master container. Instead, this is done via `socat`. See the [docker-compose.yml](master/docker-compose.yml) file for more details. Ideally, the Jenkins master container should not have access to the docker socket at all, but this is not possible in a setting where running on local machines is needed.
- The Jenkins server does not run its own builds as a node. Instead, it provisions temporary docker containers as nodes, as requested by the pipeline. This is much safer and does not expose the server to arbitrary malicious code in the builds.
- The configured job only runs on pushes to the `main` branch, despite the fact that the task specifies that it should run on "merge requests". This is to prevent malicious code from being run on the server. The job can be easily modified to run on any branch, but this is not recommended.
- The server does not expose port 50000 used for agent nodes. This is because the agents are provisioned as docker containers dynamically.

## Maintainability

- The Jenkins server is built from a Dockerfile, with hardly any changes to the original, other than preloading it with plugins. This makes it very easy to maintain and update the server, after testing the updates in a staging environment.
- Likewise, the plugins themselves are all in the minimal set of plugins that come with the typical installation of Jenkins. The only exception to this is the Docker plugin.
- The server is easily customized through various `groovy` scripts that run on every server restart. This can be used to easily insert new jobs, security settings, users and more. See the [master's init.groovy.d](master/init.groovy.d) folder for more details.
- It's easy to configure the server as a proper production server, see the [production branch](https://github.com/ofersadan85/devops-demo/tree/production) in this repository to see how I do it in [jenkins.zero-ml.com](http://jenkins.zero-ml.com).

## Storage

| Image | Description | Size |
| --- | --- | --- |
| ofersadan85/jenkins-docker-master | Jenkins master image, alpine based with plugins installed | 541 MB |
| ofersadan85/jenkins-agent-python-docker | Python agent image, alpine based with python installed | 461 MB |
| alpine/socat | socat image, alpine based | 8.5 MB |

As you can see, storage requirements are not very high, thanks to the use of Alpine images. This can probably be further optimized by removing some of the default plugins, but that would make the server less usable for other things. I haven't looked into it thoroughly yet.

## Known Issues

- The primary user, defined in [example.env](example.env), is read by Jenkins as "ambiguous". This is **NOT** a security risk, as this is only a risk in servers that allow new users to sign up. This is not the case here, as the server is pre-configured with a single user. Furthermore, this is easily fixed in the UI with a single click. If I had more time, I would have fixed this, but so far I didn't 😊.
