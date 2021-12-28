# Taxi 배차 앱 API
> 택시 배차 서비스가 잘 동작할지 검증하기 위한 최소 기능 구현   
> DRAMA & COMPANY Server Development Recruiting Assingnment 과제
      
## Tech Stack
![Java](https://img.shields.io/badge/Java-e51f24.svg?&style=for-the-badge&logo=Java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F.svg?&style=for-the-badge&logo=SpringSecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169e1.svg?&style=for-the-badge&logo=PostgreSQL&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36.svg?&style=for-the-badge&logo=ApacheMaven&logoColor=white)
      
## API 환경 구성 가이드
1. Java 17 설치
   1. [Oracle Java](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://github.com/ojdkbuild/ojdkbuild) 설치
   
2. [Maven](https://maven.apache.org) 설치
   1. Maven Binary tar.gz이나 zip [다운로드](https://maven.apache.org/download.cgi)
   2. 다운로드한 Binary 압축파일 적절한 위치로 변경 후 unzip 진행
   
3. PostgreSQL 접속 Tool([Dbeaver](https://dbeaver.io/)) 설치
   1. OS환경에 맞는 Dbeaver 설치파일 [다운로드](https://dbeaver.io/download/)
   
4. Docker 설치
   1. Docker 설치 가이드 -> https://docs.docker.com/engine/install/
      
## 실행 가이드
### 1. DB 스키마 생성
1-1) Docker를 활용해 PostgreSQL Database 생성
```PowerShell
docker run -d
--name taxirequest \
-e POSTGRES_PASSWORD=appadmin \
-e POSTGRES_USER=appadmin \
-e POSTGRES_DB=taxiapp \
-e POSTGRES_INITDB_ARGS="--encoding=UTF-8" \
-p 5432:5432 \
postgres:14
```
1-2) PostgreSQL에 접속해 SQL Script를 실행할 수 있도록 Database Tool 설치 및 실행   
1-3) 아래 SQL Sciprt 실행
```SQL
-- 1. Schema 생성
create schema taxiapp;

-- 2. User [탐색 Schema] 옵션 변경
ALTER USER appadmin SET search_path = "$user", taxiapp, public;

-- 3. 새로 생성한 taxiapp Schema로 재접속

-- 4. Table ID Sequence 생성
CREATE SEQUENCE member_seq START 1 INCREMENT 1;
CREATE SEQUENCE request_seq START 1 INCREMENT 1;

-- 5. 사용자, 택시요청 관련 Table 생성
-- 사용자 Table
CREATE TABLE member (
 id integer not null,
 email varchar(255) not null,
 password varchar(255) not null,
 user_type varchar(1) not null,
 created_at timestamp,
 updated_at timestamp,
 primary key (id)
);

-- 택시요청 Table
CREATE TABLE request (
 id int8 not null,
 address varchar(100) not null,
 passenger_id int8 not null,
 driver_id int8,
 status varchar(255),
 created_at timestamp,
 accepted_at timestamp, 
 updated_at timestamp,
 primary key (id)
);
```

### 2. Project Build
1. 해당 Github Repository 로컬에 내려받기
```PowerShell
git clone https://github.com/perfectwan8765/TaxiRequest.git
```
2. Maven으로 Build 및 실행
```PowerShell
# 1. ${Maven_설치_path}/bin로 이동

# 2. Maven Build 명령어 실행
./mvn package -f "${Project_Path}\pom.xml"

# 3. Build 결과물 확인(${Project_Path}/target/taxiRequest-1.0.0.jar 파일 확인)

# 4. 실행
# taxiRequest-1.0.0.jar가 존재하는 path에서
java -jar taxiRequest-1.0.0.jar
```
     
## DB 스키마
1. 사용자 Table
   1. 테이블 명 : member
   2. 컬럼 List  

 |순서|컬럼명|데이터타입|길이|Nullable|PK여부|
 |:---:|------|-------|:----:|----|:--:|
 |1|id|integer||NotNull|Y|
 |2|email|varchar|255|NotNull||
 |3|password|varchar|255|NotNull||
 |4|user_type|varchar|1|NotNull||
 |5|created_at|timestamp||Nullable||
 |6|updated_at|timestamp||Nullable||
  

2. 택시 요청 Table
   1. 테이블 명 : request
   2. 컬럼 List

 |순서|컬럼명|데이터타입|길이|Nullable|PK여부|
 |:---:|------|-------|:----:|----|:--:|
 |1|id|integer||NotNull|Y|
 |2|address|varchar|100|NotNull||
 |3|passenger_id|integer||NotNull||
 |4|driver_id|integer||Nullable||
 |5|status|varchar|255|Nullable||
 |6|created_at|timestamp||Nullable||
 |7|acceptd_at|timestamp||Nullable||
 |8|updated_at|timestamp||Nullable||



