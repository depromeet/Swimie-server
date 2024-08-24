#!/bin/bash

user_id=appuappu
server_name=image-batch
container_name=image-batch
request_date=$(date +"%Y%m%d")

echo "----------------------------------------------------------------------"
echo "> 도커 이미지 pull 받기"
docker pull ${user_id}/${server_name}

echo "> 9090 포트로 PENDING 이미지 일괄 삭제 배치 작업 컨테이너 실행"
echo "> docker run -d --name ${container_name} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=invalidImageDeleteJob ${user_id}/${server_name}"
docker run -d --name ${container_name} -p 9090:9090 -e requestDate=${request_date} -e TZ=Asia/Seoul -e JOB_NAME=invalidImageDeleteJob ${user_id}/${server_name}
echo "----------------------------------------------------------------------"