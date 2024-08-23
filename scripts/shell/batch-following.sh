#!/bin/bash

user_id=appuappu
server_name=image-batch # imageDelete 배치와 같은 도커 이미지를 사용할 예정입니다. (module-batch에 imageDelete 배치와 followingMemoryLogDelete 배치가 같이 있음)
container_name=following-batch
request_date=$(date +"%Y%m%d")

echo "----------------------------------------------------------------------"
echo "> 도커 이미지 pull 받기"
docker pull ${user_id}/${server_name}

echo "> 9090 포트로 100일 이전의 팔로우 소식 일괄 삭제 배치 작업 컨테이너 실행"
echo "> docker run -d --name ${container_name} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=followingLogDeleteJob ${user_id}/${server_name}"
container=$(docker run -d --name ${container_name} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=followingLogDeleteJob ${user_id}/${server_name})

echo ">docker wait $container"
docker wait $container

echo ">docker rm $container"
docker rm $container

echo "----------------------------------------------------------------------"