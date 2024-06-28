# Javascript

## Function

### Rest Parameter(나머지 파라미터)

- 함수의 마지막 파라미터에 마침표 3개 (...)를 붙여 사실상 무한대의 파라미터를 사용할 수 있게 합니다.

```js
function example(a, b, ....C) => {
 console.log(a,b,C)
}
```

- 위의 경우, C 가변인자는 무한대의 파라미터를 받을 수 있기 때문에 배열로 출력 됩니다.

---

### Arrow Function(화살표 함수)

- 자바의 람다 표현식과 유사한 것으로, 예시를 통해 쉽게 이해할 수 있습니다.

```js
function(a,b) {
    return a + b;
}

화살표 함수 적용

var example = (a,b) => {
    return a + b;
}

```

#### 화살표 함수의 주요 특징

- 함수 안에 super, this 바인딩이 없음.
- new 키워드 생성자를 사용할 수 있음.
- 블록 스코프를 사용.

> 화살표 함수 사용 시 유의사항

- => 화살표는 항상 변수 선언부와 동일한 row에 위치해야 합니다.

---

### 일급 함수, 고차 함수

> 일급 함수란, 다른 함수의 매개변수로 삽입되거나 return의 직접적인 대상이 될 수 있는 함수를 말합니다.

---

### 변수의 스코프와 스코프 체인

> 스코프(scope)란 ? 현재 실행 코드가 접근할 수 있는 선언되 변수의 범위를 말합니다. 반대로 말하면 변수가 영향을 미치는 코드의 범위입니다.

- 자바스크립트의 3개의 스코프 타입은 각각 다른 스코프를 사용합니다.

| 타입  |   스코프    |
| :---: | :---------: |
|  var  | 함수 스코프 |
|  let  | 블록 스코프 |
| const | 블록 스코프 |

- 함수 스코프 체인의 대표적인 예시

```js
var a = 1;
var b = 5;

function outerFunc() {
  function innerFunc() {
    a = b;
  }
  console.log(a); //1
  a = 3;
  b = 4;
  innerFunc();
  console.log(a); //4
  var b = 2;
}
outerFunc();
console.log(a, b); //4,5
```

### 함수 선언과 함수 표현

- 함수 선언 예시

```js
function example(a, b) {
  console.log(a);
  console.log(b);
}
```

- 함수 표현 예시

```js
let example = function funcName() {
  return "example";
};
```

---

### 함수 표현과 함수 선언의 착각

```js
let a = function nameFunc() {
  console.log("nameFunc!");
};
```

> 위의 경우는 함수 표현입니다.

- 함수 선언 몸체에 함수 이름이 있어도(nameFunc()) 변수에 대입을 하면(a) 함수 표현으로 선언됩니다.
- 즉, nameFunc()은 함수가 아니기 때문에 사용할 수 없고, 함수 표현 변수명으로 호출해야(a()) 함수가 실행됩니다.

---

### 함수 표현과 클로저

- 호이스팅이 지원되는 함수 선언을 사용하는것이 언뜻 보면 더 좋아보이지만, 이로 인해 여러개의 함수들이 호이스팅 되면 함수의 스코프 이슈를 피하기가 함들어집니다.
- 이와 더불어 함수 표현을 사용하는 이유는 함수 표현이 클로저를 지원하기 때문입니다.
