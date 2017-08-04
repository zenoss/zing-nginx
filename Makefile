include .env

SHELL                := /bin/bash
ROOTDIR              ?= $(CURDIR)
DOCKER_COMPOSE       := /usr/local/bin/docker-compose

DOCKER_COMPOSE_BASE  := $(DOCKER_COMPOSE)
ifdef PROJECT_NAME
DOCKER_COMPOSE_BASE  += -p $(PROJECT_NAME)
endif

.PHONY: docker-compose
docker-compose: $(DOCKER_COMPOSE)

$(DOCKER_COMPOSE): DOCKER_COMPOSE_VERSION := 1.14.0
$(DOCKER_COMPOSE):
	@if [ ! -w $(@D) ]; then echo 'No docker-compose found. Please run "sudo make docker-compose" to install it.'; exit 2; else true; fi
	@curl -L https://github.com/docker/compose/releases/download/$(DOCKER_COMPOSE_VERSION)/docker-compose-`uname -s`-`uname -m` > $@
	@chmod +x $@

.PHONY: build
build: export COMMIT_SHA ?= $(shell git rev-parse HEAD)
build: export GIT_BRANCH ?= $(shell git symbolic-ref HEAD | sed -e "s/^refs\/heads\///")
build: export PULL_REQUEST = ${ghprbPullLink}
build: $(DOCKER_COMPOSE)
	@$(DOCKER_COMPOSE_BASE) build nginx

.PHONY: run
run: vendor $(DOCKER_COMPOSE)
	@$(DOCKER_COMPOSE_BASE) up --build

.PHONY: clean
clean:
	rm -f **/junit.xml
	rm -f **/*.coverprofile
	rm -f hooks/*_hooks
	rm -f {hooks,ci}/docker-compose.yml
	rm -f ci/version.yml
	rm -rf $(COVERAGE_DIR)
	$(DOCKER_COMPOSE_BASE) down

.PHONY: mrclean
mrclean: clean
	rm -f resources/controller_reg.go
	rm -rf resources/app
	rm -rf swagger
	rm -rf glide.lock
	rm -rf vendor
