MAKE='make -f Makefile -f ci/ci.mk'
node('docker') {
	git url: 'https://github.com/zenoss/zing-nginx.git'

    configFileProvider([
        configFile(fileId: 'global', variable: 'GLOBAL'),
    ]){
        global = load env.GLOBAL
    }
    service = load 'ci/service.groovy'

    stage('Build image') {
		sh("${MAKE} build")
    }

    stage('Publish image as "latest"') {
        dir('service') {
			def imageName = "${global.PUBLISHER_DEVELOP}/${service.DOCKER_IMAGE}:latest"
			sh("${MAKE} push REMOTE_IMAGE=${imageName}")
        }
    }
}
