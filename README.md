# Green Gift
![소개 페이지](https://github.com/Green-GIft/db-term-backend/assets/84652886/57670380-35e4-4ce9-a8b6-55ecf768b674)
> 목차
> - [📌 프로젝트 소개](#프로젝트-소개)
> - [👩‍👩‍👧‍👧 팀원 소개](#팀원-소개)
> - [✏️ 주요 기능](#주요-기능)
> - [🔗 링크 모음(배포 주소 포함)](#링크-모음)
> - [📜 ERD](#erd)
> - [📄 API 모아보기](#api-모아보기)
> - [📁 파일 구조](#파일-구조)
> - [🚩 시작 가이드](#시작-가이드)
> - [©️ License: The MIT License (MIT)](#license)

## 프로젝트 소개

### 개발 동기 및 목적
코로나 19 이후 축제 등이 활발해지고 관광객이 늘어나면서 쓰레기를 무단으로 버리고 가 도시 미관을 해치는 문제가 빈번히 발생했다. 이 문제를 해결하기 위해 단순히 쓰레기통을 늘리는 것이 아닌, 사람들이 쓰레기를 버리기 위한 충분한 동기가 필요하다고 생각했다.  

따라서 쓰레기를 버리면 선물을 주는 서비스를 생각해보았다 .


<br>

### 서비스 소개
> 쓰레기를 주우면 로또처럼 추첨을 통해 선물을 주는 서비스

1. 🎉 축제 정보 관리
- 축제 참가자와 쓰레기 관리자는 축제에 참여하고 참여한 축제를 모아볼 수 있어요
 - 축제 관리자는 축제를 등록하고 추첨할 수 있어요
2. 🎁 상품 및 선물 관리
  - 축제 참가자는 축제 당첨시 선물을 받고 이를 선물함에서 확인할 수 있어요
  - 축제 관리자는 상품을 등록할 수 있어요
  - 마일리지로 선물을 구매할 수 있어요
3. 🚮 쓰레기 관리
 - 사용자가 쓰레기를 버렸는지 인증해줄 수 있어요
 - 해당 축제에서 쓰레기를 추가, 삭제, 조회할 수 있어요
 - 가장 쓰레기를 많이 버린 사람 3명을 확인할 수 있어요
4. 👤유저 정보 관리
  - 회원가입, 로그인


<br>

### 개발 기간
2023.11.29 - 12.10 (디비 텀프 기간)

<br>

## 팀원 소개

|   [남원정](https://github.com/1jeongg)      |
|:----------------------------------------------:|
|   <img src="https://github.com/1jeongg.png" width = 150> |
| `Backend`, `Android`, `Design` |

<br>

## 주요 기능

> 축제 참가자용 안드로이드 애플리케이션입니다. 축제 관리자와 쓰레기 관리자는 시간상의 이유로 API만 제작했습니다.

https://github.com/Green-GIft/db-term-backend/assets/84652886/7eb7ae47-f0e6-4eee-b99d-62e0d40dc9e1

### 공통 기능

1. 회원가입
  > - 닉네임, 이메일, 패스워드를 입력하여 사용자를 등록할 수 있다
  > - User `Insert`, `Transaction` 사용

2. 로그인
  > - 이메일과 패스워드를 통해 로그인 할 수 있다. 이때 토큰을 생성하여 유저 애플리케이션 내에 저장한다.
  > -  User `SFW`, `(where u.email = :email and u.password = :password)`

<br>

### 축제 참가자
<img width="493" alt="image" src="https://github.com/Green-GIft/db-term-backend/assets/84652886/4cb6b8a9-ca72-48ce-b989-02b0b128636c">

1. 등급 및 마일리지 조회
  > -  해당 유저의 등급과 마일리지를 조회할 수 있다.
  > -  User `SFW`, `(where u.id = :userId)`

2. 축제 참가
  > - 축제 이름을 등록하여 참가할 수 있다.
  > - Festival `SFW` `(where f.name = :name)` <br> UserFestival `Insert`, `Transaction`

3. 당첨 결과 확인
  > - 해당 축제에 대한 결과를 확인할 수 있다
  >    - 당첨시 -> 상품에 대한 정보
  >    - 실패시 -> null
  >    - 아직 추첨 진행 전일 시 -> error 반환
  > -  UserFestival `SFW`, `where f.id = :festivalId and p2.id = :userId and u.category = 'FESTIVAL'`

4. 참여한 축제 모아보기
  > - 사용자가 참가한 축제를 조회할 수 있다.
  > - UserFestival Select

5. 구매 가능한 상품 조회하기
  > - 등록된 모든 상품을 조회할 수 있다.
  > - Product Select

6. 상품 구매하기
  > - 1. 해당 상품의 남은 수량이 1개 이상인지 확인한다.
  >   2. 사용자의 마일리지가 가격보다 높은지 확인한다.
  >   3. 사용자의 마일리지를 가격만큼 감소시킨다.
  >   4. 해당 상품을 UserProduct 테이블에 insert한다.
  > - UserProduct Insert, Transaction

7. 내가 받은 상품 조회하기
  > - 내가 받은 선물을 모두 조회할 수 있다.
  > - UserProduct Select 

<br>

### 축제 관리자
<img width="494" alt="image" src="https://github.com/Green-GIft/db-term-backend/assets/84652886/ded9b0a7-7d08-4c55-99cf-59dd97415efe">

1. 축제 등록하기
  > - 이름, 시작 날짜, 끝나는 날짜, 이미지 정보를 받아와서 Festival 테이블에 저장한다.
  > - Transaction

2. 당첨 결과 추첨하기
> 1. 축제가 끝났는지 확인한다.
>   2. 상품이 3개 이상, 쓰레기 버림을 인증받은 유저가 3명 이상인지 확인한다.
>   3. 선물에 당첨된 사람을 선정한다.
>      - `where u.festival = :festival and u.status = 'READY' order by RAND() limit 3`
>   5. 선물을 3개 뽑는다.
>      - `where p.festival = :festival order by p.rank asc limit 3`
>   6. 선물에 당첨된 사람들에게 선물을 각각 부여하고 UserFestival의 상태를 업데이트 한다.
>      - `update UserFestival u set u.status = 'SUCCESS' where u in :userFestivalList`
>      - insert UserProduct, Transaction
>   7. 선물에 당첨되지 않은 사람들에겐 마일리지 10점을 주고 UserFestival 상태를 FAIL로 업데이트 한다.
>      - `update Participant p set p.mileage = p.mileage+10 where p.id in :nonSelected`
>   8. 축제의 상태를 끝남으로 바꾼다.
>      - is_finished = true
>  - 전체 단계에 대해 transaction 수행

3. 내가 추가한 축제 모아보기
> 축제 관리자가 등록한 축제를 모아볼 수 있다.
> Festival, SFW

5. 상품 등록하기
> 이름, 회사, 순위, 마감일자, 추가 수량, 이미지, 가격 정보를 담은 상품을 등록할 수 있다.
> insert Product, Transaction

7. 상품 수량 추가하기
> 상품의 수량을 업데이트 할 수 있다.
> update, Transaction

9. 내가 추가한 상품 조회하기
> 축제 관리자가 추가한 상품을 조회할 수 있다.
> select Festival

<br>

### 쓰레기 관리자
<img width="498" alt="image" src="https://github.com/Green-GIft/db-term-backend/assets/84652886/08d9e1ac-9ef6-4170-af5b-b6a458ad2e84">

1. 축제 참가
  > - 축제 이름을 등록하여 참가할 수 있다.
  > - Festival `SFW` `(where f.name = :name)` <br> UserFestival `Insert`, `Transaction`
2. 참여한 축제 모아보기
  > - 사용자가 참가한 축제를 조회할 수 있다.
  > - UserFestival Select
3. 쓰레기 인증
  > - 사용자 이메일과 festivalId를 통해 사용자가 쓰레기를 버렸음을 인증해준다.
  > - select User, update User status (Transaction)
5. 쓰레기 등록
  > - 쓰레기 정보 (이름, 분류)를 등록하여 해당 축제에 등록할 수 있다.
  > - insert trash
6. 쓰레기 삭제
  > - 선택한 쓰레기를 삭제할 수 있다.
  > - delete trash, Transaction
7. 내가 처리한 쓰레기 모아보기
  > - select Trash
8. 최고의 사원 확인하기
  > - 가장 쓰레기를 많이 처리한 우수 사원 3명의 이름과 그 횟수를 보여준다.
  > ```
  > select count(*), ut.username from trash_tb t
  > join user_festival_tb uf on t.user_festival_id = uf.id
  > join user_tb ut on uf.user_id = ut.id 
  > group by ut.id order by count(*) desc limit 3
  > ```

<br>

## 링크 모음
- [안드로이드 동작 영상](https://youtube.com/shorts/os4Lr7o9Y1g?feature=share)
- [백엔드 배포 URL](https://port-0-db-term-backend-57lz2alpu33yoq.sel4.cloudtype.app)
- [안드로이드 깃허브](https://github.com/Green-GIft/android-participant)

<br>

## ERD
<img width="657" alt="image" src="https://github.com/Green-GIft/db-term-backend/assets/84652886/d3f2987f-ddd2-4dc8-9c58-39264ec23b42">

<br>

## API 모아보기
|이름|url|사용자|메서드|
|:--|:--|:----|:-----|
| 이름                          | URL                                      | 사용자                          | 메서드 |
| ----------------------------- | ---------------------------------------- | ------------------------------- | ------ |
| **//회원//**                  |                                          |                                 |        |
| 회원가입                       | /api/user/signup                         | `쓰레기 관리자`, `참가자`, `축제 관리자` | `POST`   |
| 로그인                         | /api/user/login                          | `쓰레기 관리자`, `참가자`, `축제 관리자` | `POST`   |
| 등급 및 마일리지 조회          | /api/user/grade                          | `참가자`                           | `GET`    |
| **// 축제 //**                |                                          |                                 |        |
| 축제 등록                      | /api/festival                            | `축제 관리자`                      | `POST`   |
| 축제 참가                      | /api/festival/join                       | `쓰레기 관리자`, `참가자`             | `POST`   |
| 당첨 결과 추첨                 | /api/festival/random/{festivalId}        | `축제 관리자`                      | `GET`    |
| 당첨 결과 확인                 | /api/festival/result/{festivalId}        | `참가자`                           | `GET`    |
| 참여한 축제 모아보기           | /api/festival/all                        | `쓰레기 관리자`, `참가자`             | `GET`    |
| 내가 추가한 축제 모아보기      | /api/festival/manager                    | `축제 관리자`                      | `GET`    |
| **// 상품 //**                |                                          |                                 |        |
| 상품 등록                      | /api/product/{festivalId}                | `축제 관리자`                      | `POST`   |
| 상품 수량 추가하기             | /api/product/amount/{productId}          | `축제 관리자`                      | `POST`   |
| 상품 구매하기                  | /api/product/buy/{productId}             | `참가자`                           | `POST`   |
| 구매 가능한 상품 조회하기      | /api/product/all                         | `참가자`                           | `GET`    |
| 내가 받은 상품 조회하기        | /api/product/participant                 | `참가자`                           | `GET`    |
| 내가 추가한 상품 조회하기      | /api/product/manager/{festivalId}        | `축제 관리자`                      | `GET`    |
| **// 쓰레기 //**              |                                          |                                 |        |
| 쓰레기 인증                    | /api/trash/certificate/{festivalId}     | `쓰레기 관리자`                | `POST`   |
| 쓰레기 등록                    | /api/trash/{festivalId}                  | `쓰레기 관리자`                    | `POST`   |
| 쓰레기 삭제                    | /api/trash/{trashId}                     | `쓰레기 관리자`                    | `DELETE` |
| 내가 처리한 쓰레기 모아보기     | /api/trash                               | `쓰레기 관리자`                    | `GET`    |
| 최고의 사원 확인하기           | /api/trash/manager                       | `쓰레기 관리자`                    | `GET`    |


## 파일 구조
```
└───📂src
    ├───📂main
    │   ├───📂java
    │   │   └───📂com
    │   │       └───📂apiserver
    │   │           └───📂greengift
    │   │               ├───📂festival
    │   │               │   ├───📂constant
    │   │               │   └───📂user_festival
    │   │               ├───📂product
    │   │               │   ├───📂constant
    │   │               │   └───📂user_product
    │   │               ├───📂trash
    │   │               │   └───📂constant
    │   │               ├───📂user
    │   │               │   ├───📂constant
    │   │               │   ├───📂festival_manager
    │   │               │   ├───📂participant
    │   │               │   └──📂─trash_manager
    │   │               └───📂_core
    │   │                   ├───📂config
    │   │                   ├───📂errors
    │   │                   │   └───📂exception
    │   │                   ├───📂security
    │   │                   └───📂utils
    │   └───📂resources
    │       ├───📂static
    │       └───📂templates
    └───📂test
        └───📂java
            └───📂com
                ├───📂apiserver
                │   └───📂greengift
                └───📂greengift
```

폴더 내부 구조
```
- Entity.java
- DTOConverter.java
- JPARepository.java
- Request.java
- Response.java
- RestController.java
- Service.java 
- ServiceImpl.java
```
<br>


## 시작 가이드

> Settings: Java 17, Spring 3.1.4

1. 프로젝트 클론
```
git clone https://github.com/Green-GIft/db-term-backend.git
cd db-term-backend
cd greengift
```

2. 실행
```
./gradlew build
cd build
cd libs
java -jar api-server-0.0.1-SNAPSHOT.jar
```

## License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

