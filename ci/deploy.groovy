MAKE='make -f Makefile -f ci/ci.mk'
node('docker') {
	git url: 'https://github.com/zenoss/zing-nginx.git'

    configFileProvider([
        configFile(fileId: 'global', variable: 'GLOBAL'),
    ]){
        global = load env.GLOBAL
    }
    service = load 'ci/service.groovy'
    sh('git rev-parse --short=8 HEAD > ci/.image_tag')

    stage('Build image') {
		sh("${MAKE} build")
    }

    stage('Publish image as "latest"') {
		def imageName = "${global.PUBLISHER_DEVELOP}/${service.DOCKER_IMAGE}:latest"
		sh("${MAKE} push REMOTE_IMAGE=${imageName}")
    }
}
