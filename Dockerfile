# Build the application jar ignoring the tests
FROM lh-oci-registry.azercell.com/docker_proxy/library/maven:3-openjdk-17 as builder

ENV SRC_DIR=/usr/src/app

COPY pom.xml ${SRC_DIR}/
COPY src/ ${SRC_DIR}/src/

WORKDIR ${SRC_DIR}

RUN mvn package -DskipTests

# Build the application runtime image
FROM lh-oci-registry.azercell.com/docker_proxy/library/eclipse-temurin:17.0.5_8-jre

LABEL APPLICATION="book-app"

EXPOSE 8080/tcp

COPY --from=builder /usr/src/app/target/k8s-test-*.jar /app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
