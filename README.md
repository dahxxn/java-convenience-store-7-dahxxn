# java-convenience-store-precourse

## ⭐ 미션 : 편의점

### 📢 프로그램 소개

> 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템

### 📢 시나리오

1. 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내함
    - 재고가 없으면 `재고없음`을 출력
2. 사용자에게 구매할 상품과 수량을 입력받음
    - 입력 형식 ) `[상품명-수량],[상품명-수량] ... ,[상품명-수량]`
3. 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산함
    - 최종 결제 금액은 상품별 가격과 수량을 곱하여 계산하고, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산축

    1) 프로모션 적용 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받음
        - 입력 형식) Y/N
    2) 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야하는 경우, 정가로 결제할지 여부를 입력받음
        - 입력 형식) Y/N
    3) 멤버십 할인 적용 여부를 입력받음
        - 입력 형식) Y/N
4. 구매 내역과 산출한 금액 정보를 영수증으로 출력함
5. 영수증 출력 후 추가 구매를 진행할지 또는 종료할지 선택할 수 있음
    1) 추가 구매 여부를 입력받음
        - 입력 형식) Y/N

#### 📦 재고 관리

- 상품 목록은 `src/main/resources/products.md` 파일에 저장되어 있음
- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인함
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리함
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공함

#### 💸 프로모션 할인

- 행사 목록은  `src/main/resources/promotions.md` 파일에 저장되어 있음
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용함
- 프로모션은 N개 구매시 1개 무료 증정의 형태로 진행
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에는 하나의 프로모션만 적용
- 프로모션 혜택은 프로모션 재고 내에서만 적용
- 프로모션 기간 중에는 프로모션 재고를 우선적으로 차감하고, 프로모션 재고가 부족하면 일반 재고를 활용
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내함
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내함

#### 💸 멤버십 할인

- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받음
- 프로모션 적용 후 남은 금액에 대한 멤버십 할인을 적용함
- 멤버십 할인의 최대 한도는 8,000원

#### 🧾 영수증 출력

- 영수증은 구매 내역과 할인을 요약하여 출력함
- 영수증 항목
    - 구매 상품 내역 : 구매한 상품명, 수량, 가격
    - 증정 상품 내역 : 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총 구매액 : 구매한 상품의 총 수량과 총 금액
        - 행사할인 : 프로모션에 의해 할인된 금액
        - 멤버십할인 : 멤버십에 의해 추가로 할인된 금액
        - 내실돈 : 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 할인

✅ 기능 구현 목록
---
> ❗ : 예외 발생 상황 ➕ : 추가 고려하여 개발한 상황

### 고객에게 입력받기

- [x] 고객에게 값 입력받기
    - [x] 구매할 상품과 수량 입력받기
        - ❗[상품명-수량],...,[상품명-수량] 형식에 맞지 않게 입력한 경우 에러 메시지 출력 후 다시 입력받기
        - ❗재고에 없는 상품명을 입력한 경우 에러 메시지 출력 후 다시 입력받기
        - ❗수량이 재고수량을 초과하여 입력한 에러 메시지 출력 후 다시 입력받기
        - ➕ 만약 [콜라-2], [사이다-3], [콜라-1] 이런식으로 입력이 된 경우 자동으로 누적하여 [콜라-3],[사이다-3] 으로 변환해주기
    - [x] 프로모션 상품에 대해 적게 가져온 경우, 추가 여부 입력받기
        - ❗ Y,N 이외의 값을 입력한 경우 에러 메시지 출력 후 다시 입력받기
    - [x] 프로모션 상품 재고가 부족하여 혜택없이 결제할 경우, 정가 결제에 대한 여부 입력받기
        - ❗ Y,N 이외의 값을 입력한 경우 에러 메시지 출력 후 다시 입력받기
    - [x] 멤버십 할인 적용 여부 입력받기
        - ❗ Y,N 이외의 값을 입력한 경우 에러 메시지 출력 후 다시 입력받기
    - [x] 추가 구매 여부 입력받기
        - ❗ Y,N 이외의 값을 입력한 경우 에러 메시지 출력 후 다시 입력받기
    - [x] 공통 예외 처리
        - ❗ 사용자가 값을 입력하지 않고 빈 문자열을 입력한 경우에러 메시지 출력 후 다시 입력받기

### 프로모션 정보 처리 및 관리

- [x] 프로모션 파일을 읽고 프로모션 정보를 프로모션 인벤토리에 저장하기
    -[x] 프로모션 이름, 구매조건, 증정량, 시작날짜 그리고 종료 날짜 정보 추출
    -[x] 추출된 정보로 하나의 프로모션 데이터 생성
    - [x] 프로모션 데이터를 프로모션 목록에 추가
- [x] 프로모션 데이터를 더 빠르게 찾기 위한 인덱스 사전 만들기
    - [x] 프로모션 이름으로 프로모션 목록에서의 해당 위치(인덱스)를 알 수 있는 사전 생성
    - 구조 : (프로모선이름, 위치), (프로모션이름,위치) ...
- [x] 프로모션 이름으로 해당 프로모션의 존재유무 확인하기

### 프로모션 혜택 확인하기

- [x] 프로모션 기간 확인하기
- [x] 프로모션 혜택 가능 여부 확인하기
    - [x] 구매 수량, 프로모션 재고 수량 그리고 프로모션 기간 여부, 프로모션 혜택 조건을 비교하여 확인
- [x] 프로모션 혜택 반영 후 남은 재고 및 수량 계산하기
    - [x] 한 프로모션에 대한 총 필요량을 바탕으로 반영 후 남은 재고 및 수량 계산하기
- [x] 추가 혜택이 가능한지 확인하기
    - [x] 남은 구매 수량과 남은 프로모션 재고를 바탕으로 추가 혜택이 제공될 수 있는 상황인지 확인하기

### 상품 정보 처리 및 관리

- [x] 상품 파일을 읽고 상품 정보를 상품 인벤토리에 저장하기
    - [x] 상품 이름, 재고량, 가격, 프로모션 이름(없으면 "null"이라고 저장)
    -[x] 추출된 정보로 하나의 상품 데이터 생성
    - [x] 상품 데이터를 상품 목록에 추가
        - [x] 프로모션 이름으로 프로모션 유무 확인
        - [x] 프로모션 상품이 아니면 일반 상품 목록에 상품 데이터 추가
        - [x] 프로모션 상품이면 프로모션 유효성 확인 후, 프로모션 상품 목록에 상품 데이터 추가
- [x] 상품 데이터를 더 빠르게 찾기 위한 인덱스 사전 만들기
    - [x] 상품 이름으로 상품 목록에서의 해당 위치(인덱스)를 알 수 있는 사전 생성
    - 구조 : (상품이름, 위치), (상품이름,위치) ...
    - 이 사전은 일반 상품 목록과 프로모션 상품목록 각각 따로 생성
- [x] 상품 이름으로 상품/프로모션 상품 데이터 가져오기
    - [x] 위의 인덱스 사전으로 상품 데이터 위치 찾기
    - [x] 찾은 데이터 위치로 상품 사전에서 상품 가져오기
- [x] 상품 이름으로 상품 존재 유무 확인하기
- [x] 상품 이름으로 프로모션 상품 존재 유무 확인하기
- [x] 상품 구매 가능 여부 확인하기
    - 구매 갯수가 일반 상품 재고와 프로모션 상품 재고의 합보다 작은지 확인하기
- [x] 상품 재고 빼기
    - [x] 상품 이름과 구매 갯수로 재고 업데이트 하기
    - ➕ 기능 요구 사항에서, 프로모션 진행 중이면 프로모션 재고를 우선 차감하라고 되어 있어서
      프로모션 진행 중이 아니라면 일반 재고를 우선 차감하도록 함
        - ➕ 프로모션 기간이 아닐때, 일반재고 우선차감 -> 일반재고 부족시 프로모션 재고 차감

### 재고 관리하기

- [x] 재고 정보 업데이트 하기
    - [x] 재고 추가하기
    - [x] 재고 삭제하기
- [x] 가격 정보 반환하기
- [x] 재고 정보 반환하기
- [x] 재고 상황에 따른 재고 정보 반환 문자열 생성하기
    - 재고가 0인 경우 "재고없음" 이 뜨도록 하기

### 주문 내역 및 멤버십 관리하기

- [x] 사용자로부터 입력받은 상품 및 구매 수량 정보 저장하기
    - [x] 상품이 장바구니에 중복되어 존재하면 수량을 누적
- [x] 상품 구매 수량 업데이트 하기
    - [x] 상품 수량 추가하기
    - [x] 상품 수량 제거하기
        - ➕ 수량이 0 이면 장바구니에서 삭제
- [x] 프로모션 수량 계산하기
    - [x] 프로모션 상품 명과 증정품 갯수 정보 저장하기
    - [x] 기존 증정품 갯수 정보가 있으면 수량을 누적
- [x] 멤버십 할인 여부 설정하기
- [x] 주문 상품 정보 조회하기
    - [x] 장바구니에 담긴 상품의 이름, 수량, 가격 정보를 조회
    - [x] 프로모션 상품의 정보를 별도로 조회
- [x] 할인 적용 전 총 상품 금액 계산하기
- [x] 프로모션 할인 금액 계산하기
    - [x] 증정품 갯수에 증점품 원가를 곱해 할인 금액 계산하기
- [x] 멤버십 할인 금액 계산하기
    - [x] 멤버십 적용을 안했을 경우 0원 할인
    - [x] 프로모션 상품을 제외한 가격에만 적용하기
    - [x] 최대 8000원 할인되게 하기
- [x] 내야할 돈 계산하기
    - [x] 총 상품 금액 계산
    - [x] 프로모션 할인 금액 계산
    - [x] 멤버십 할인 금액 계산
    - [x] 총 상품 금액 - 프로모션 할인 금액 - 멤버십 할인 금액으로 내야할 돈 계산하기
- [x] 재고 업데이트 하기
    - [x] 장바구니에 담겨있었던 상품과 갯수를 기반으로 재고 수정

### 할인 적용

- [x] 전체 프로모션 할인 적용하기
    - [x] 장바구니 정보를 바탕으로 프로모션 할인 적용
    - [x] 프로모션이 가능한지 확인 후 가능하면 증정품 수량 계산하여 할인 적용
        - [x] 일부 수량의 프로모션이 적용 불가능할 경우, 정가 결제에 대한 동의 구하기
            - [x] 비동의할 경우에는 해당 상품의 구매 수량 차감하기
        - [x] 프로모션 적용이 모두 가능한 경우, 추가 프로모션 혜택 적용 가능한지 확인하기
            - [x] 추가 혜택이 적용 가능한 경우, 증정품 추가 의사 물어보기
                - [x] 동의한 경우에는 해당 상품의 구매 수량 증가하기
- [x] 멤버십 할인 적용 유무 설정하기

### 고객에게 출력하기

- [x] 환영 인사 출력하기
- [x] 현재 보유 상품 내역 출력하기
    - [x] 상품명, 가격, 프로모션 이름, 재고 안내하기
    - [x] 재고가 0개이면 재고없음 출력하기
- [x] 프로모션 상품에 대해 적게 가져온 경우, 추가 여부에 대한 안내 메시지 출력하기
- [x] 프로모션 상품 재고가 부족하여 혜택 없이 결제할 경우, 정가 결제 여부에 대한 안내 메시지 출력하기
- [x] 멤버십 힐인 적용 여부 안내 메시지 출력하기
- [x] 영수증 출력하기
- [x] 추가 구매 여부 안내 메시지 출력하기
- [x] 잘못된 값 입력시, 오류 메시지와 함께 안내 메시지 출력하기
    - [x] 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
    - [x] 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
    - [x] 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
    - [x] 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

⭐ 개발 목표
---

#### 📍 이전 주차까지 제시된 프로그래밍 요구 사항 및 학습 목표를 지키면서 개발하기

- Java Style Guide 지키기
- Git Commit Convention 지키기 : 작은 단위로 자주 커밋하기
- 들여쓰기 깊이는 3이 넘지 않도록 하기
- 3항 연산자 쓰지 않기
- 함수가 한 가지 일만 하도록 작게 만들기
- JUnit5 & AssertJ 를 활용하여 테스트 코드로 확인하며 개발하기
- 배열 대신 컬렉션 사용하기
- Java 에서 제공하는 API 적극 활용하기
- 이름을 통해 의도 들어내기 & 축약하지 않기
- 의미없는 주석 달지 않기
- 관련 함수를 묶어 클래스를 만들고, 객체들이 협력하여 하나의 큰 기능을 수행하도록 하기
- 클래스와 함수에 대한 단위 테스트를 통해 의도한 대로 정확하게 작동하는 영역을 확보하기
- else 예약어 쓰지 않기
- Java Enum 을 적용하여 프로그램 구현하기

#### 📍 공통 피드백 반영하여 개발하기

- 비즈니스 로직과 UI 로직 분리하기
- 연관성이 있는 상수는 static final 대신 enum 활용하기
- final 키워드로 값의 변경 막기
- 객체의 상태 접근 제한하기
    - 인스턴스 변수 접근 제어자는 private으로 설정하여 외부에서 통제되지 않도록 함
- 객체는 객체답게 사용하기
    - 객체에서 데이터를 꺼내 사용 x -> 객체가 스스로 처리할 수 있도록 구조 변경
    - 객체가 자신의 데이터를 스스로 처리하고 메시지를 던지게 함
- 인스턴스 변수 수를 줄이도록 노력하기
- 예외 케이스도 테스트하기
- 테스트 코드도 코드이다
    - 리펙토링을 통해 중복되는 부분을 제거하고 유지 보수성을 높이고 가독성을 향상시키기
    - 파라미터 값만 바뀌는 경우 파라미터화된 테스트를 통해 중복을 줄이기
- 테스트 코드를 위한 코드는 구현 코드에서 분리하기
- 단위 테스트하기 어려운 코드를 단위 테스트하기
    - 테스트 하기 어려운 의존성을 외부에서 주입하거나 분리하여 테스트 가능한 상태로 만들기
- private 함수를 테스트 할때 클래스 분리를 고려하기
    - private 함수가 중요한 역할을 수행할때 클래스 분리하여 테스트 가능성을 높이기

#### 📍️ 4 주차 미션에 제시된 프로그래밍 요구 사항 및 학습 목표를 반영하여 개발하기

- 함수의 길이가 10라인을 넘어가지 않도록 구현하기
    - 함수가 한 가지 일만 잘 하도록 구현하기
- 입출력을 담당하는 클래스를 별도로 구현하기
- `camp.nextstep.edu.missionutils`에서 제공하는 `DateTimes` 및 `Console API`를 사용하여 구현하기
    - 현재 날짜와 시간을 가져오려면 `camp.nextstep.edu.missionutils.DateTimes`의 `now()`를 활용
    - 사용자가 입력하는 값은 `camp.nextstep.edu.missionutils.Console`의 `readLine()`을 활용