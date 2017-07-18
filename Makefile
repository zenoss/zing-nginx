DOCKERCOMPOSE_RUN_OPTS ?= -d

.PHONY: default
default: build

.PHONY: test
test:
	echo "No tests exist for this service; skipping"

.PHONY: build
build:
	docker-compose build

.PHONY: run
run:
	docker-compose up --build ${DOCKERCOMPOSE_RUN_OPTS}

