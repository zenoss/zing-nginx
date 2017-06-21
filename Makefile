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


