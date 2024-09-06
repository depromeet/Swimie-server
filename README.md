## 🏊 Swimie | 친구들의 응원과 함께하는 수영일기
![frame](./docs/design/frame.png)

## 🏗️ Architecture

### Infrastructure
![infrastructure](./docs/architecture/infrastructure.png)

### Software Architecture (Hexagonal)
![hexagonal](./docs/architecture/hexagonal.png)

<br>

## 📂 Module & Directory Structure
### Directory Structure
```
├── .github
├── module-presentation  # API 게이트웨이 서버
├── module-batch  # 배치 서버
├── module-independent  # 독립 모듈
├── module-domain  # 도메인 모듈
├── module-infrastructure  # 외부 모듈
│   └── persistence-database # 데이터베이스 모듈
│   └── persistence-redis # Redis 모듈
│   └── object-storage # 객체 저장소 모듈
│   └── google-spreadsheet # 구글 스프레드시트 모듈
└── docs # 데이터 관리용 폴더
```

### Multi Module Structure
- 멀티 모듈과 헥사고날 아키텍처를 적용하여 모듈 간 의존성을 분리하였습니다.
- Domain 모듈은 순수 자바 모듈로 구성되어 있으며, 외부 의존성을 가지지 않습니다.
- 각 모듈은 Domain 모듈에 대한 의존성을 가지고 있으며, 상위 모듈은 하위 모듈만을 의존하도록 설계하였습니다. 

<br>

## 🚗 How to start?
- presentation 모듈(API 게이트웨이 서버)을 실행하기 위해서는 메인 디렉토리에서 `./gradlew :module-presentation:build` 명령어를 수행합니다.
- `java -jar /module-presentation/build/libs/module-presentation.jar` 명령어를 통해 서버를 실행합니다.

## 💻 Tech Stack
- Java 21
- Gradle 8.8
- MySQL 8.0.35

#### Framework <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-social&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-social&logo=Gradle&logoColor=white">

#### Database <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-social&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-social&logo=Databricks&logoColor=white">

#### Auth - <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-social&logo=springsecurity&logoColor=white">  <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-social&logo=JSON Web Tokens&logoColor=white">

#### Business Logic Test - <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-sociak&logo=junit5&logoColor=white">

#### Performance Test - <img src="https://img.shields.io/badge/K6-7D64FF?style=for-the-sociak&logo=K6&logoColor=white"> <img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-sociak&logo=Apache JMeter&logoColor=white">

#### Cloud - <img src ="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-social&logo=amazonec2&logoColor=white">  <img src ="https://img.shields.io/badge/AWS S3-69A31?style=for-the-social&logo=amazons3&logoColor=white">  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-social&logo=amazonrds&logoColor=white">  <img src ="https://img.shields.io/badge/AWS Cloud Watch-FF4F8B?style=for-the-social&logo=amazoncloudwatch&logoColor=white"> <img src ="https://img.shields.io/badge/AWS Cloud Front-7B00FF?style=for-the-social&logo=icloud&logoColor=white"> <img src ="https://img.shields.io/badge/AWS Lambda-F9900?style=for-the-social&logo=awslambda&logoColor=white">

#### Monitoring - <img src="https://img.shields.io/badge/Sentry-362D59?style=for-the-social&logo=Sentry&logoColor=white">
<br>

## 📈 Database Schema
![ERD](./docs/database/ERD.png)

<br>

## Developers
### 🧑‍💻 Server Engineers
|                                                                                                                                                                                                                                              신민철                                                                                                                                                                                                                                               |                                                                                                                                               홍성주                                                                                                                                               |                                                                                                                                               양원채                                                                                                                                               |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/48898994?v=4" width="100" height="100" style="border-radius: 5%;"><br/><a href="https://github.com/its-sky" target="_blank"><img src="https://img.shields.io/badge/its-sky-181717?style=for-the-social&logo=github&logoColor=white"/></a> | <img src="https://avatars.githubusercontent.com/u/96187152?v=4" width="100" height="100" style="border-radius: 5%;"><br/><a href="https://github.com/penrose15" target="_blank"><img src="https://img.shields.io/badge/penrose15-181717?style=for-the-social&logo=github&logoColor=white"/></a> | <img src="https://avatars.githubusercontent.com/u/79977182?v=4" width="100" height="100" style="border-radius: 5%;"><br/><a href="https://github.com/ywonchae1" target="_blank"><img src="https://img.shields.io/badge/ywonchae1-181717?style=for-the-social&logo=github&logoColor=white"/></a> |

