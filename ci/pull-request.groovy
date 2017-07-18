#! groovy

//
// pull-request/Jenkinsfile - Jenkins script for initiating the {{Name}} microservice build
//
//  output files
//  version.yml         - docker-compose file that describes the version of the image to test
//

MAKE='make -f Makefile -f ci.mk'

node('docker') {
    currentBuild.displayName = "PR #${env.ghprbPullId}@${env.NODE_NAME}"
    configFileProvider([
        configFile(fileId: 'global', variable: 'GLOBAL'),
    ]) {
        global = load env.GLOBAL
    }

    checkout scm
    service = load 'service.groovy'
    SHA=sh(script: 'git rev-parse --short=8 HEAD | tee ci/.image_tag', returnStdout: true)
    sh("echo -n {{Name}}-${env.BUILD_NUMBER} > ci/.project_name")

    try	{
        stage('Run service tests') {
            sh("${MAKE} test")
        }

        stage('Build service image') {
            sh("${MAKE} build")
        }

        stage('Run service api tests') {
            sh("${MAKE} api-test")
        }
    } finally {
        stage ('Clean test environment') {
            sh("${MAKE} ci-clean")
        }
    }

    stage('Publish image') {
		def imageName = "${global.PUBLISHER_DEVELOP}/${service.DOCKER_IMAGE}:${SHA}"
		sh("${MAKE} push REMOTE_IMAGE=${imageName}")
    }

    stage('Promote to staging') {
        def imageName = "${global.PUBLISHER_STAGING}/${service.DOCKER_IMAGE}:${SHA}"
        sh("${MAKE} version.yaml REMOTE_IMAGE=${imageName}")
        archiveArtifacts artifacts: 'version.yaml'
        build job: env.GLOBAL_ACCEPTANCE_JOB, parameters: [
            text(name: 'VERSION', value: readFile('version.yaml'))
        ]
    }
}
