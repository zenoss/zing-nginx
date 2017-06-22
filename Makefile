DOCKERCOMPOSE_RUN_OPTS ?= -d

.PHONY: default
default: app

.PHONY: app
app:
	echo "This repo does require building an app"

.PHONY: build
build:
	docker-compose build

.PHONY: run
run:
	docker-compose up --build ${DOCKERCOMPOSE_RUN_OPTS}

PHONY: ci-push
ci-push: GIT_COMMIT:=$(shell git rev-parse --short HEAD)
ci-push: IMAGE:=registry.zing.zenoss.eng/zenoss/zing-nginx
ci-push:
	CI_IMAGE=${IMAGE}:${GIT_COMMIT} docker-compose build
	docker tag ${IMAGE}:${GIT_COMMIT} ${IMAGE}:latest
	docker push ${IMAGE}:${GIT_COMMIT}
	docker push ${IMAGE}:latest
