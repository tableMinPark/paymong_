# paymong 리팩토링

### 1. 서비스 간 의존성 개선

### 1.1 의존 관계 분석

<img width="957" alt="image" src="https://github.com/tableMinPark/paymong_/assets/87235273/397fa894-30fc-4749-b02c-929bccfa1327">


- **6개의 서비스 간 9개의 의존 관계를 확인할 수 있다.**

  ```
  1. member - auth
  3. member - management
  4. member - collect
  5. member - common
  6. collect - common
  7. common - information
  8. information - member
  9. management - common
  10. management - collect
  ```
  
- **모든 서비스가 서로에 대한 의존 관계를 맺고 있기 때문에 독립적인 서비스 운영이 어려운 부분을 확인할 수 있다.**



- **기존의 불필요한 서비스 간 요청을 없애고 의존 관계를 끊어내 최대한 독립적으로 서비스가 운영될 수 있는 환경을 구축하는 것이 중요하다.**

---

### 1.2 기존 프로젝트 재설계 (DDD)
#### 1.2.1 도메인 이벤트 작성

![image](https://github.com/tableMinPark/paymong_/assets/87235273/4e6928c6-8903-4735-addb-63bf3fb5bcc0)

```
- mobildLogin         : 로그인 (토큰 발급)
- watchLogin          : 웨어러블 기기 로그인 (토큰 발급)
- ressuie             : 토큰 재발급
- findCollectMap      : 사용자 도감 맵 조회
- findCollectMong     : 사용자 도감 몽 조회
- findMong            : 몽 상태 조회
- findMongINfo        : 몽 정보 조회
- findMongStatus      : 몽 스텟 조회
- registerMong        : 몽 생성
- mongStroke          : 몽 쓰다듬기 내역 등록
- mongSleep           : 몽 수면 상태 수정
- mongDeletePoop      : 몽 배변 치움 내역 등록
- mongFeedFood        : 몽 밥 먹임 내역 등록
- mongFeedSnack       : 몽 간식 먹임 내역 등록
- mongFoodSnack       : 밥, 간식 카테고리 조회
- mongTraining        : 몽 훈련 내역 등록
- mongWalking         : 몽 산책 내역 등록
- mongEvolution       : 몽 진화 내역 등록
- mongGraduation      : 몽 졸업 내역 등록
- registerPay         : 삼성페이 결제 내역 등록
- findMember          : 회원 정보 조회
- findPayPoint        : 페이포인트 내역 조회
- findThings          : 삼성싱즈 등록 내역 조회
- registerThings      : 삼성싱즈 등록
- deleteThings        : 삼성싱즈 등록 내역 삭제
- findThingsAble      : 삼성싱즈 등록 가능한 기기 조회
- registerThingActive : 삼성싱즈 동작 내역 등록
```

#### 1.2.2 애그리거트 도출

<img width="1129" alt="image" src="https://github.com/tableMinPark/paymong_/assets/87235273/beac118e-6663-4053-aebc-817f415769d2">

```
- Auth
  ㄴ mobildLogin         : 로그인 (토큰 발급)
  ㄴ watchLogin          : 웨어러블 기기 로그인 (토큰 발급)
  ㄴ ressuie             : 토큰 재발급

- Collect
  ㄴ findCollectMap      : 사용자 도감 맵 조회
  ㄴ findCollectMong     : 사용자 도감 몽 조회

- Mong
  ㄴ findMong            : 몽 상태 조회
  ㄴ findMongINfo        : 몽 정보 조회
  ㄴ findMongStatus      : 몽 스텟 조회
  ㄴ registerMong        : 몽 생성

- Management
  ㄴ mongStroke          : 몽 쓰다듬기 내역 등록
  ㄴ mongSleep           : 몽 수면 상태 수정
  ㄴ mongDeletePoop      : 몽 배변 치움 내역 등록
  ㄴ mongFeedFood        : 몽 밥 먹임 내역 등록
  ㄴ mongFeedSnack       : 몽 간식 먹임 내역 등록
  ㄴ mongFoodSnack       : 밥, 간식 카테고리 조회
  ㄴ mongTraining        : 몽 훈련 내역 등록
  ㄴ mongWalking         : 몽 산책 내역 등록
  ㄴ mongEvolution       : 몽 진화 내역 등록
  ㄴ mongGraduation      : 몽 졸업 내역 등록

- PayPoint
  ㄴ registerPay         : 삼성페이 결제 내역 등록
  ㄴ findMember          : 회원 정보 조회
  ㄴ findPayPoint        : 페이포인트 내역 조회

- Things
  ㄴ findThings          : 삼성싱즈 등록 내역 조회
  ㄴ registerThings      : 삼성싱즈 등록
  ㄴ deleteThings        : 삼성싱즈 등록 내역 삭제
  ㄴ findThingsAble      : 삼성싱즈 등록 가능한 기기 조회
  ㄴ registerThingActive : 삼성싱즈 동작 내역 등록
```

#### 1.2.3 바운디드 컨텍스트 도출

<img width="1155" alt="image" src="https://github.com/tableMinPark/paymong_/assets/87235273/e45294b7-31bf-4eb0-b46b-96041becf207">

```
- Auth (Service)
  ㄴ Auth
    ㄴ mobildLogin         : 로그인 (토큰 발급)
    ㄴ watchLogin          : 웨어러블 기기 로그인 (토큰 발급)
    ㄴ ressuie             : 토큰 재발급

- Collect (Service)
  ㄴ Collect
    ㄴ findCollectMap      : 사용자 도감 맵 조회
    ㄴ findCollectMong     : 사용자 도감 몽 조회

- Mong (Service)
  ㄴ Mong
    ㄴ findMong            : 몽 상태 조회
    ㄴ findMongINfo        : 몽 정보 조회
    ㄴ findMongStatus      : 몽 스텟 조회
    ㄴ registerMong        : 몽 생성
  
  ㄴ Management
    ㄴ mongStroke          : 몽 쓰다듬기 내역 등록
    ㄴ mongSleep           : 몽 수면 상태 수정
    ㄴ mongDeletePoop      : 몽 배변 치움 내역 등록
    ㄴ mongFeedFood        : 몽 밥 먹임 내역 등록
    ㄴ mongFeedSnack       : 몽 간식 먹임 내역 등록
    ㄴ mongFoodSnack       : 밥, 간식 카테고리 조회
    ㄴ mongTraining        : 몽 훈련 내역 등록
    ㄴ mongWalking         : 몽 산책 내역 등록
    ㄴ mongEvolution       : 몽 진화 내역 등록
    ㄴ mongGraduation      : 몽 졸업 내역 등록

- PayPoint (Service)
  ㄴ PayPoint
    ㄴ registerPay         : 삼성페이 결제 내역 등록
    ㄴ findMember          : 회원 정보 조회
    ㄴ findPayPoint        : 페이포인트 내역 조회

- Things (Service)
  ㄴ Things
    ㄴ findThings          : 삼성싱즈 등록 내역 조회
    ㄴ registerThings      : 삼성싱즈 등록
    ㄴ deleteThings        : 삼성싱즈 등록 내역 삭제
    ㄴ findThingsAble      : 삼성싱즈 등록 가능한 기기 조회
    ㄴ registerThingActive : 삼성싱즈 동작 내역 등록
```

#### 1.2.4 컨텍스트 매핑을 통한 컨텍스트 간 관계 표현

<img width="1430" alt="image" src="https://github.com/tableMinPark/paymong_/assets/87235273/28695e84-6b10-47a8-aec4-598c6b2c17e6">

- **기존 9개의 의존 관계에서 3개로 감소시켰다.**
- **gateway 를 통한 내부 토큰 구조를 도입하여 중복 요청을 감소시켰다.**

#### 1.2.5 전체 시스템 의존 관계

<img width="1493" alt="image" src="https://github.com/tableMinPark/paymong_/assets/87235273/2a157950-3885-46d7-8189-96bbcd456cd3">

- **Mong 데이터베이스의 Mong 테이블을 gateway 데이터베이스에 뷰 테이블로 생성하여 서비스 간 요청없이 데이터베이스를 조회할 수 있도록 했다.**
