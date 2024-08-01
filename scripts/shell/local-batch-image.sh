./gradlew :module-batch:build

docker build --platform linux/amd64 -t appuappu/image-batch:latest -f ./scripts/Dockerfile-image .

docker push appuappu/image-batch:latest
