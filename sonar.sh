#!/bin/bash

if ! docker container exec sonarqube echo 'SonarQube is ready'; then
    if ! docker container start sonarqube; then
        if ! docker run -d --name sonarqube -p 9000:9000 sonarqube:community; then
            exit 1
        fi
    fi
fi

echo "Starting analysis..."

./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=XXXXXXXXXXXXXXXXXXXX
