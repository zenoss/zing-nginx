include Makefile

IMAGE_NAME   := ${SERVICE_IMAGE}:${IMAGE_TAG}
REMOTE_IMAGE := ${REGISTRY}/${SERVICE_IMAGE}:$(if ${REMOTE_TAG},${REMOTE_TAG},${IMAGE_TAG})

.PHONY: IMAGE_NAME_VALID
IMAGE_NAME_VALID:
	@test ${SERVICE_IMAGE} || (echo "SERVICE_IMAGE must be defined"; false)
	@test ${IMAGE_TAG} || (echo "IMAGE_TAG must be defined"; false)

.PHONY: REMOTE_IMAGE_VALID
REMOTE_IMAGE_VALID:
	@test ${REGISTRY} || (echo "REGISTRY must be defined"; false)
	@test ${SERVICE_IMAGE} || (echo "SERVICE_IMAGE must be defined"; false)
	@test ${REMOTE_TAG}${IMAGE_TAG} || (echo "REMOTE_TAG or IMAGE_TAG must be defined"; false)

.PHONY: push
push: REMOTE_IMAGE_VALID IMAGE_NAME_VALID
	@docker tag $(IMAGE_NAME) $(REMOTE_IMAGE)
	@docker push $(REMOTE_IMAGE)

define VERSION_YAML
version: "3.2"
services:
    $${SERVICE_NAME}:
        image: $${ZING_REGISTRY}/$(IMAGE_NAME)
endef

ci/version.yml: export VERSION_YAML:=$(VERSION_YAML)
ci/version.yml: IMAGE_NAME_VALID
	@echo "$$VERSION_YAML" > $@

.PHONY: clean
clean::
	rm -f ci/version.yml
