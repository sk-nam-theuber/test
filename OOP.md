# OOP (Object-Oriented Programming)

## 1. 캡슐화(Encapsulation) = 정보 은닉

- 프로그램 내에서 같은 기능을 목적으로 작성된 코드를 모아서 다른 곳(클래스)에서 안보이게 숨기는 것.
- 사용자로 하여금 은닉된 사항을 알 필요가 없게 하여, 학습 양을 줄임으로서 간편하게 사용할 수 있게 해줌.
- 외부의 잘못된 접근으로 값이 변하는 보호 효과도 누릴 수 있음.

```
전자 레인지를 사용할 때, 어떻게 내부적으로 열이 발생하는지에 대한 지식을 알 필요 없이, 단순히 버튼만 누르면 조리가 가능하다.

전자레인지 원리 설계 정보를 은닉 함으로서 보안 효과를 누릴 수도 있지만, 소비자가 버튼만 누르면 작동하도록 단순화 한 것도 포함한다.
```

> ### String 클래스로 알아보는 캡슐화의 예제

실제 자바에서의 캡슐화의 대표적인 표본으로는 String 클래스를 들 수 있다.

실제 자바 코드에 구현 되어있는 String 클래스의 내부 모습이다.

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence, Constable, ConstantDesc {

    @Stable
    private final byte[] value;

    private final byte coder;

    private int hash;

    private boolean hashIsZero;

    ...

    public String() {
        this.value = "".value;
        this.coder = "".coder;
    }

    public String(String original) {
        this.value = original.value;
        this.coder = original.coder;
        this.hash = original.hash;
    }
}
```

클래스 자체는 public으로 선언 되어 있기 때문에, 어디서든 접근을 할 수 있다.
하지만 final로 선언되어 있어 상속은 불가능하다.<br>
또한 내부의 필드들은 private로 선언되어 있기 때문에, 직접 문자열 값에 접근해 변경할 수는 없다.

실제로 많은 프로그래머들은, 직접 객체 내부에 접근해서 값을 변경하는 것을 권장하지 않는다.
클래스의 속성(field)은 private 접근자를 적용하여 직접 값에 접근하는 것을 막고, 별도로 Set, Get 메소드를 정의하여 간접적으로 값을 가져오거나, 수정하는것을 권장한다.

<br>

> ### 정보 은닉은 개발 생산성을 향상시킨다

<br>

```java
ShapeFactory factory = new ShapeFactory();
Shape shape = factory.create_T();

shape.draw(); // "triangle"
```

factory에서 shape 객체를 만들었고, shape 인스턴스에서 메소드를 실행시켰더니 "triangle"이 출력되었다. 이유는?

```java
abstract class Shape {
    abstract public void draw();
}

class Rectangle extends Shape {
    public void draw(){
        rectangle();
    }
    private void rectangle() {
        System.out.println("rectangle");
    }
}

class Triangle extends Shape {
    public void draw() {
        triangle();
    }
    private void rectangle() {
        System.out.println("triangle");
    }
}

// Shape에 연관된 자식 객체들을 찍어내는 Factory라는 클래스를 새로 만든다.
class ShapeFactory {
    public Shape create_R() {
        return new Rectangle();
    }
    public Shape create_T() {
        return new Triangle();
    }
}

public void method() {
    Shape shape = new Triangle();
    shape.draw(); // "triangle"
}
```

Rectangle, Triangle 등 클래스 타입을 하나의 타입으로 통합 할 수 있는 Shape 라는 추상 클래스를 만들어 다형성의 효과를 이용해 구현 해 준다.

또한 객체의 생성, 메소드의 구현 모두 은닉시켰고, 추상 클래스의 추상 메소드를 통해 각기 다른 결과물을 출력시킬 수 있다.

위의 코드에서 shape.draw() 부분을 그대로 둔 채, 윗 줄의 변수 할당 부분만 new Rectangle(); 등으로 바꿔 주면 동적으로 기능 교체도 쉽게 가능하다.

**_이처럼 정보 은닉이 되면 될수록, 객체의 교체나 변경이 쉬워지게 되어 결과적으로 개발 생산성이 향상되게 된다._**

<br>
<br>

## 2. 상속(Inheritance) = 재사용 + 확장

- 공통된 속성과 기능을 추출(추상화) 하여 상위 클래스(부모 클래스)에 정의하고, extends 키워드를 통해 각각의 하위 클래스로 확장하여 해당 기능과 속성들을 매번 반복적으로 정의해야 하는 번거로움을 제거할 수 있음.
- 공통된 코드의 변경이 있는 경우, 상위 클래스에서 단 한번의 수정으로 모든 클래스에 변경 사항이 반영될 수 있도록 만들 수 있다.

<br>

> 상속(extends)과 구현(implements)의 차이

|                             |                 상속(extends)                  |             구현(implements)             |
| :-------------------------: | :--------------------------------------------: | :--------------------------------------: |
|     다중상속이 가능한가     |                     불가능                     |                   가능                   |
|      구현을 강제하는가      |            선택적 재정의, 구현 가능            | 구현이 강제됨(하위 클래스에서 정의 필요) |
| 상태 정의가 가능한가(field) | 클래스에는 field, method 정의가 모두 가능하다. | 인터페이스에는 상수와 method만 정의가능. |

<br>

**_따라서 상위 클래스가 확장할 목적으로 설계되었고, 문서화도 잘 되어 있는 경우에 상속을 사용하면 좋다._**

<br>

> ### 상속의 단점

<br>

1. 결합도가 높아진다.

OOP에서 결합도는 낮을수록, 응집도는 높을수록 좋다. 그래서 추상화에 의존 함으로서 다른 객체에 대한 결합도는 최소화 하고 응집도를 최대화 하여 변경 가능성을 최소화 시킬 수 있다.

하지만 상속을 하게되면 부모 클래스와 자식 클래스의 관계가 컴파일 시점에 관계가 결정되어, 결합도가 당연히 높아질 수 밖에 없다.

이에 실행 시점에 객체의 종류를 변경하는 것이 불가능하여, 유기적인 다형성 및 객체지향 기술을 사용할 수 없다.

2. 불필요한 기능 상속

동물 이라는 클래스를 상속받는 비둘기, 독수리, 호랑이가 있다고 하자. 부모 클래스인 동물에 fly() 라는 메소드를 추가한다면, 호랑이 클래스 에서는 작동하지 않는 불필요한 메소드가 되어 버린다.

3. 부모 클래스의 결함이 그대로 넘어옴.

상위 클래스에 결함이 있다면, 부모 클래스의 결함도 자식 클래스에게 넘어오게 된다.

결국 자식 클래스에서 아무리 구조적으로 잘 설계 하였다 하더라도, 애초에 부모 클래스에서 결함이 있기 때문에 자식 클래스도 문제가 터지게 된다.

## 3. 다형성(Polymorphism) = 사용편의

- 하나의 객체가 여러가지 형태를 가질 수 있는것.

<br>

> 다형성을 구현하기 위한 다양한 요소들

<br>

- 오버라이딩(Overriding)
  - 부모 클래스에 정의되어 있는 메서드를 자식 클래스에서 재정의 하여 사용하는 것.
- 오버로딩(Overloading)
  - 같은 이름을 가진 메서드를 인자값의 종류나 개수를 다르게 하여 다른 기능을 구현하도록 정의하는 것.
- 업 캐스팅
  - 하위 클래스가 상위 클래스로 형변환 되는 현상.
- 다운 캐스팅
  - 상위 클래스가 하위 클래스로 형변환 되는 현상. 단, 업 캐스팅이 이루어 진 이후에 원래 타입으로 돌려놓기 위한 다운 캐스팅이 허용됨.
- 인터페이스
  - 클래스들이 구현해야 하는 동작을 지정하는 용도로 사용되는 추상 자료형
- 추상메소드
  - 하위 클래스에서 구현을 강제하는 메소드. 인터페이스 내부에서 정의됨.
- 추상클래스
  - 구현을 강제한다는 점에서 인터페이스와 비슷하지만, 추상화의 정도가 인터페이스 보다 낮고 new를 통해 객체 생성이 불가능. 인터페이스와 다르게 non-static, non-final 필드 선언 가능.
