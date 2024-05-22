# Build a Quarkus native image

## Build the native executable

```shell
./mvnw package -Pnative
```

## Build the docker image using the Dockerfile.native-micro

```shell
docker build -f Dockerfile.native-micro -t quarkus/copilot-demo .
```

## Run the docker image

```shell
docker run -i --rm -p 8080:8080 quarkus/copilot-demo
```

## Test the application

```shell
curl -v http://localhost:8080/hello?key=world
```


