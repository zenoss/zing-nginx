DOCKER_COMPOSE         := /usr/local/bin/docker-compose
DOCKERCOMPOSE_RUN_OPTS ?= -d

.PHONY: default
default: build

.PHONY: build
build: export COMMIT_SHA ?= $(shell git rev-parse HEAD)
build: export GIT_BRANCH ?= $(shell git symbolic-ref HEAD | sed -e "s/^refs\/heads\///")
build: export PULL_REQUEST = ${ghprbPullLink}
build: $(DOCKER_COMPOSE)
	@$(DOCKER_COMPOSE) build nginx 

.PHONY: test
test:
	echo "No tests exist for this service; skipping"

.PHONY: run
run:
	docker-compose up --build ${DOCKERCOMPOSE_RUN_OPTS}

