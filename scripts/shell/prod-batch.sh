#!/bin/bash

user_id=appuappu
server_name=swimie-batch
image_batch_container=image-batch
following_batch_container=following-batch
request_date=$(date +"%Y%m%d")

echo "----------------------------------------------------------------------"
echo "> 도커 이미지 pull 받기"
docker pull ${user_id}/${server_name}

echo "> 9090 포트로 PENDING 이미지 일괄 삭제 배치 작업 컨테이너 실행"
echo "> docker run -d --name ${image_batch_container} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=invalidImageDeleteJob ${user_id}/${server_name}"
container1=$(docker run -d --name ${image_batch_container} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=invalidImageDeleteJob ${user_id}/${server_name})

echo ">docker wait $container1"
docker wait $container1

echo ">docker rm $container1"
docker rm $container1

echo "----------------------------------------------------------------------"

echo "> 9090 포트로 100일 이전의 팔로우 소식 일괄 삭제 배치 작업 컨테이너 실행"
echo "> docker run -d --name ${following_batch_container} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=followingLogDeleteJob ${user_id}/${server_name}"
container2=$(docker run -d --name ${following_batch_container} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=followingLogDeleteJob ${user_id}/${server_name})

echo ">docker wait $container2"
docker wait $container2

echo ">docker rm $container2"
docker rm $container2

echo "----------------------------------------------------------------------"