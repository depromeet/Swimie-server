./gradlew :module-batch:build

docker build --platform linux/amd64 -t appuappu/swimie-batch:latest -f ./scripts/Dockerfile-image .

docker push appuappu/swimie-batch:latest
