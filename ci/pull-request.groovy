#! groovy

MAKE='make -f ci/Makefile'

node('docker') {
    currentBuild.displayName = "PR #${env.ghprbPullId}@${env.NODE_NAME}"
    configFileProvider([
        configFile(fileId: 'global', variable: 'GLOBAL'),
    ]) {
        global = load env.GLOBAL
    }

    checkout scm

    withEnv([
        "COMMIT_SHA=${env.ghprbActualCommit}",
        "IMAGE_TAG=${env.ghprbActualCommit.substring(0,8)}",
        "PROJECT_NAME=cassandra-${env.BUILD_NUMBER}"]) {
        try    {
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
            sh("${MAKE} push REGISTRY=${global.PUBLISHER_DEVELOP}")
        }

        stage('Promote to staging') {
            sh("${MAKE} version.yaml REGISTRY=${global.PUBLISHER_STAGING}")
            archiveArtifacts artifacts: 'version.yaml'
            build job: env.GLOBAL_ACCEPTANCE_JOB, parameters: [
                text(name: 'VERSION', value: readFile('version.yaml'))
            ]
        }
    }
}
