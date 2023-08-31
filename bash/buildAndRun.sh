#!/bin/bash

docker-compose up -d --build

cd ../ && mvn clean install

mvn spring-boot:run

