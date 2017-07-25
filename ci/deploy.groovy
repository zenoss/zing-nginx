MAKE='make -f ci/Makefile'
node('docker') {
	git url: 'https://github.com/zenoss/zing-nginx.git'

    configFileProvider([
        configFile(fileId: 'global', variable: 'GLOBAL'),
    ]){
        global = load env.GLOBAL
    }

    COMMIT_SHA=sh(script: 'git rev-parse HEAD', returnStdout: true)
    IMAGE_TAG=COMMIT_SHA.substring(0,8)

    withEnv([
        "COMMIT_SHA=${COMMIT_SHA}",
        "IMAGE_TAG=${IMAGE_TAG}"]) {
		stage('Build image') {
			sh("${MAKE} build")
		}

		stage('Publish image') {
            sh("${MAKE} push REGISTRY=${global.PUBLISHER_DEVELOP}")
		}

		stage('Publish image as "latest"') {
            sh("${MAKE} push REGISTRY=${global.PUBLISHER_DEVELOP} REMOTE_TAG=latest")
		}
	}
}
