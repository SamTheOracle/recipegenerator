#!/usr/bin/env bash

./mvnw clean package

cd devops

docker build . -t oracolo/recipegenerator
docker push oracolo/recipegenerator