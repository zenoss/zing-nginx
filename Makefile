DOCKER_COMPOSE         := /usr/local/bin/docker-compose
DOCKERCOMPOSE_RUN_OPTS ?= -d

.PHONY: default
default: build

.PHONY: test
test:
	echo "No tests exist for this service; skipping"

.PHONY: run
run:
	docker-compose up --build ${DOCKERCOMPOSE_RUN_OPTS}

