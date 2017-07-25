include .env
include Makefile

IMAGE_NAME   := ${SERVICE_IMAGE}:${IMAGE_TAG}
REMOTE_IMAGE := ${REGISTRY}/${SERVICE_IMAGE}:$(if ${REMOTE_TAG},${REMOTE_TAG},${IMAGE_TAG})

.PHONY: default

.PHONY: IMAGE_NAME_VALID
IMAGE_NAME_VALID:
	@test ${SERVICE_IMAGE} || (echo "SERVICE_IMAGE must be defined"; false)
	@test ${IMAGE_TAG} || (echo "IMAGE_TAG must be defined"; false)

.PHONY: REMOTE_IMAGE_VALID
REMOTE_IMAGE_VALID:
	@test ${REGISTRY} || (echo "REGISTRY must be defined"; false)
	@test ${SERVICE_IMAGE} || (echo "SERVICE_IMAGE must be defined"; false)
	@test ${REMOTE_TAG}${IMAGE_TAG} || (echo "REMOTE_TAG or IMAGE_TAG must be defined"; false)

.PHONY: PROJECT_NAME_VALID
PROJECT_NAME_VALID:
	@test ${PROJECT_NAME} || (echo "PROJECT_NAME must be defined"; false)


.PHONY: api-test
api-test: $(DOCKER_COMPOSE)
	@echo "Not implemented"

.PHONY: push
push: REMOTE_IMAGE_VALID IMAGE_NAME_VALID
	@docker tag $(IMAGE_NAME) $(REMOTE_IMAGE)
	@docker push $(REMOTE_IMAGE)

version.yaml: REMOTE_IMAGE_VALID
	@cat ci/version-template.yaml | REMOTE_IMAGE=$(REMOTE_IMAGE) envsubst > version.yaml

.PHONY: ci-clean
ci-clean: PROJECT_NAME_VALID
	$(DOCKER_COMPOSE) -p $(PROJECT_NAME) down

.PHONY: ci-mrclean
ci-mrclean: ci-clean
	rm -f version.yaml
