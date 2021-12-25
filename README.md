# Taxi 배차 앱 API
> 택시 배차 서비스가 잘 동작할지 검증하기 위한 최소 기능 구현   
> DRAMA & COMPANY Server Development Recruiting Assingnment 과제

## Tech Stack
![Java](https://img.shields.io/badge/Java-e51f24.svg?&style=for-the-badge&logo=Java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F.svg?&style=for-the-badge&logo=SpringSecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169e1.svg?&style=for-the-badge&logo=PostgreSQL&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36.svg?&style=for-the-badge&logo=ApacheMaven&logoColor=white)

### API 환경 구성 가이드

### 실행 가이드

### DB 스키마
PostgreSQL 사용

1. 사용자 Table   
테이블 명 : member   
컬럼   

 |순서|컬럼명|데이터타입|길이|Nullable|PK여부|
 |---|------|-------|----|----|--|
 |1|id|integer||NotNull|Y|
 |2|email|varchar|255|NotNull||
 |3|password|varchar|255|NotNull||
 |4|user_type|varchar|255|NotNull||
 |5|created_at|timestamp||Nullable||
 |6|updated_at|timestamp||Nullable||
  
2. 택시 요청 Table   
테이블 명 : request   
컬럼   

 |순서|컬럼명|데이터타입|길이|Nullable|PK여부|
 |---|------|-------|----|----|--|
 |1|id|integer||NotNull|Y|
 |2|address|varchar|100|NotNull||
 |3|passenger_id|integer||NotNull||
 |4|driver_id|integer||Nullable||
 |5|status|varchar|255|Nullable||
 |6|created_at|timestamp||Nullable||
 |7|acceptd_at|timestamp||Nullable||
 |8|updated_at|timestamp||Nullable||



