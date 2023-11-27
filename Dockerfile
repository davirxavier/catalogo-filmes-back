FROM alpine:3.14

RUN  apk update \
  && apk upgrade \
  && apk add --update openjdk11 tzdata curl unzip bash \
  && rm -rf /var/cache/apk/*

RUN mkdir /usr/local/app
COPY . /usr/local/app/
WORKDIR /usr/local/app

RUN ./mvnw clean package -DskipTests

ENV DB_HOST ''
ENV DB_NAME ''
ENV DB_USER ''
ENV DB_PASS ''
ENV ALLOWED_ORIGINS ''
ENV JWT_SECRET "u8x/A?D(G+KbPeSgVkYp3s6v9y\$B&E)H@McQfTjWmZq4t7w!z%C*F-JaNdRgUkXp"

ENTRYPOINT ["java","-jar","target/catalogodefilmes-1.0.1.jar"]