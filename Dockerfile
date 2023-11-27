FROM alpine:3.14

RUN  apk update \
  && apk upgrade \
  && apk add --update openjdk11 tzdata curl unzip bash \
  && rm -rf /var/cache/apk/*

RUN mkdir /usr/local/app
RUN mkdir /usr/local/app/.mvn
COPY pom.xml /usr/local/app/
COPY mvnw /usr/local/app/
COPY .mvn/ /usr/local/app/.mvn
WORKDIR /usr/local/app
RUN ./mvnw verify clean --fail-never

COPY . .
RUN ./mvnw clean package -DskipTests

ENV DB_HOST ''
ENV DB_NAME ''
ENV DB_USER ''
ENV DB_PASS ''
ENV ALLOWED_ORIGINS ''
ENV JWT_SECRET "u8x/A?D(G+KbPeSgVkYp3s6v9y\$B&E)H@McQfTjWmZq4t7w!z%C*F-JaNdRgUkXp"

EXPOSE 8080
ENTRYPOINT ["java","-jar","target/catalogodefilmes-1.0.1.jar"]