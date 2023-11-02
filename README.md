Learn about Spring MVC

# 웹 애플리케이션 이해
### 모든 것이 HTTP
HTTP 메시지에 모든 것을 전송
- HTML, TEXT
- IMAGE, 음성, 영상, 파일
- JSON, XML (API)
- 거의 모든 형태의 데이터 전송 가능
- 서버간에 데이터를 주고 받을 때도 대부분 HTTP 사용
- *지금은 HTTP 시대!*

### 웹 서버(Web Server)
- HTTP 기반으로 동작
- 정적 리소스 제공, 기타 부가기능
- 정적(파일) HTML, CSS, JS, 이미지, 영상
- 예) NGINX, APACHE

![WEB_SERVER](https://github.com/keunoh/spring-mvc/assets/96904103/9c90c04c-0c25-4841-95b3-66c1ec12624e)


### 웹 애플리케이션 서버(WAS - Web Application Server)
- HTTP 기반으로 동작
- 웹 서버 기능 포함 + (정적 리소스 제공 가능)
- 프로그램 코드를 실행해서 애플리케이션 로직 수행
  - 동적 HTML, HTTP API(JSON)
  - 서블릿, JSP, 스프링 MVC
- 예) 톰캣(Tomcat) Jetty, Undertow

![WAS](https://github.com/keunoh/spring-mvc/assets/96904103/13dd68f3-46ed-47d7-a304-194755e2ea70)

### 웹 서버, 웹 애플리케이션 서버(WAS) 차이
- 웹 서버는 정적 리소스(파일), WAS는 애플리케이션 로직
- 사실은 둘의 용어도 경계도 모호함
  - 웹 서버도 프로그램을 실행하는 기능을 포함하기도 함
  - 웹 애플리케이션 서버도 웹 서버의 기능을 제공함
- 자바는 서블릿 컨테이너 기능을 제공하면 WAS
  - 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 있음
- WAS는 애플리케이션 코드를 실행하는데 더 특화

### 웹 시스템 구성 - WAS, DB
- WAS, DB 만으로 시스템 구성 가능
- WAS는 정적 리소스, 애플리케이션 로직 모두 제공 가능
- WAS가 너무 많은 역할을 담당, 서버 과부하 우려
- 가장 비싼 애플리케이션 로직이 정적 리소스 때문에 수행이 어려울 수 있음
- WAS 장애시 오류 화면도 노출 불가능

![WAS_DB](https://github.com/keunoh/spring-mvc/assets/96904103/6d3e6d1b-33a4-44c1-a49d-70cc55b6868b)

### 웹 시스템 구성 - WEB, WAS, DB
- 정적 리소스는 웹 서버가 처리
- 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
- WAS는 중요한 애플리케이션 로직 처리 전담
- 효율적인 리소스 관리
  - 정적 리소스가 많이 사용되면 Web 서버 증설
  - 애플리케이션 리소스가 많이 사용되면 WAS 증설
- 정적 리소스만 제공하는 웹 서버는 잘 죽지 않음
- 애플리케이션 로직이 동작하는 WAS 서버는 잘 죽음
- WAS, DB 장애시 WEB 서버가 오류 화면 제공 가능

![WEB_WAS_DB](https://github.com/keunoh/spring-mvc/assets/96904103/b704fd95-7fef-4c75-8506-ce09b24a6840)

### 서블릿
- 특징
  - ulrPatterns(/hello)의 URL이 호출되면 서블릿 코드가 실행
  - HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
  - HTTP 응답 정보를 편리하게 제공할 수 있는 HttpServletResponse
  - 개발자는 HTTP 스펙을 매우 편리하게 사용

- HTTP 요청, 응답 흐름
  - HTTP 요청 시
    - WAS는 Request, Response 객체를 새로 만들어서 서블릿 객체 호출
    - 개발자는 Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
    - 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력
    - WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성

![SERVLET](https://github.com/keunoh/spring-mvc/assets/96904103/1f264362-ad9a-4b17-8cf0-a2dff25f7228)

### 서블릿 컨테이너
- 톰캣처럼 서블릿을 지언하는 WAS를 서블릿 컨테이너라고 함
- 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 *싱글톤으로 관리*
  - 고객의 요청이 올 때 마다 계속 객체를 생성하는 것은 비효율
  - 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
  - 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
  - *공유 변수 사용 주의*
  - 서블릿 컨테이너 종료시 함께 종료
- JSP도 서블릿으로 변환 되어서 사용
- 동시 요청을 위한 멀티 쓰레드 처리 지원

# 동시 요청 - 멀티 쓰레드
### 쓰레드
- 애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 쓰레드
- 자바 메인 메서드를 처음 실행하면 main이라는 이름의 쓰레드가 실행
- 쓰레드가 없다면 자바 애플리케이션 실행이 불가능
- 쓰레드는 한번에 하나의 코드 라인만 수행
- 동시 처리가 필요하면 쓰레드를 추가로 생성

### 요청 마다 쓰레드 생성 [ 장단점 ]
- 장점
  - 동시 요청을 처리할 수 있다.
  - 리소스(CPU, 메모리)가 허용할 때까지 처리 가능
  - 하나의 쓰레드가 지연 되어도, 나머지 쓰레드는 정상 동작한다.
- 단점
  - 쓰레드는 생성 비용이 매우 비싸다.
    - 고객의 요청이 올 때마다 쓰레드를 생성하면, 응답 속도가 늦어진다.
  - 쓰레드는 컨텍스트 스위칭 비용이 발생한다.
  - 쓰레드 생성에 제한이 없다.
    - 고객 요청이 너무 많이 오면, CPU, 메모리 임게점을 넘어서 서버가 죽을 수 있다.

### 쓰레드 풀
요청마다 쓰레드 생성의 단점 보완
- 특징
  - 필요한 쓰레드를 쓰레드 풀에 보관하고 관리한다.
  - 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리한다. 톰캣은 최대 200개 기본 설정 (변경 가능)
- 사용
  - 쓰레드가 필요하면, 이미 생성되어 있는 쓰레드를 쓰레드 풀에서 꺼내서 사용한다.
  - 사용을 종료하면 쓰레드 풀에 해당 쓰레드를 반납한다.
  - 최대 쓰레드가 모두 사용중이어서 쓰레드 풀에 쓰레드가 없으면?
    - 기다리는 요청은 거절하거나 특정 숫자만큼만 대기하도록 설정할 수 있다.
- 장점
  - 쓰레드가 미리 생성되어 있으므로, 쓰레드를 생성하고 종료하는 비용(CPU)이 절약되고, 응답 시간이 빠르다.
  - 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리할 수 있다.

![THREAD_POOL](https://github.com/keunoh/spring-mvc/assets/96904103/5e113396-7edb-47f2-ba80-07e7cb47b8f0)

### 실무 팁
- WAS의 주요 튜닝 포인트는 최대 쓰레드(max thread) 수 이다.
- 이 값을 너무 낮게 설정하면?
  - 동시 요청이 많으면, 서버 리소스는 여유롭지만, 클라이언트는 금방 응답 지연
- 이 값을 너무 높게 설정하면?
  - 동시 요청이 많으면, CPU, 메모리 리소스 임계점 초과로 서버 다운
- 장애 발생시?
  - 클라우드면 일단 서버부터 늘리고, 이후에 튜닝
  - 클라우드가 아니면 열심히 튜닝

### 쓰레드 풀의 적정 숫자
- 적정 숫자는 어떻게 찾나요?
- 애플리케이션 로직의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 모두 다름
- 성능 테스트
  - 최대한 실제 서비스와 유사하게 성능 테스트 시도
  - 툴: 아파치 ab, 제이미터, nGrinder

### WAS의 멀티 쓰레드 지원 [ 핵심 ]
- 멀티 쓰레드에 대한 부분은 WAS가 처리
- `개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨`
- 개발자는 마치 `싱글 쓰레드 프로그래밍을 하듯이 편리하게 소스 코드를 개발`
- 멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용

# HTML, HTTP API, CSR, SSR
### 정적 리소스
- 고정된 HTML 파일, CSS, JS, 이미지, 영상 등을 제공
- 주로 웹 브라우저

### HTML 페이지
- 동적으로 필요한 HTML 파일을 생성해서 전달
- 웹 브라우저 : HTML 해석

### HTTP API
- HTML이 아니라 데이터를 전달
- 주로 JSON 형식 사용
- 다양한 시스템에서 호출
- 데이터만 주고 받음, UI 화면이 필요하면, 클라이언트가 별도 처리
- 앱, 웹 클라이언트, 서버 to 서버
  - UI 클라이언트 점점
    - 앱 클라이언트(아이폰, 안드로이드, PC 앱)
    - 웹 브라우저에서 자바스크립트를 통한 HTTP API 호출
    - React, Vue.js 같은 웹 클라이언트
  - 서버 to 서버
    - 주문 서버 -> 결제 서버
    - 기업간 데이터 통신

### 서버사이드 렌더링, 클라이언트 사이드 렌더링
- SSR - 서버 사이드 렌더링
  - HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
  - 주로 정적인 화면에 사용
  - 관련 기술 : JSP, 타임리프 -> `백엔드 개발자`
- CSR - 클라이언트 사이드 렌더링
  - HTML 겨로가를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성해서 적용
  - 주로 동적인 화면에 사용, 웹 환경을 마치 앱 처럼 필요한 부분부분 변경할 수 있음
  - 예) 구글 지도, Gmail, 구글 캘린더
  - 관련기술 : React, Vue.js -> `웹 프론트엔드 개발자`

![SSR](https://github.com/keunoh/spring-mvc/assets/96904103/479f2c06-dd75-4956-9742-be04cef2efc3)
![CSR](https://github.com/keunoh/spring-mvc/assets/96904103/b4974b19-f5f3-4076-b67c-47770b44a993)

### 어디까지 알아요 하나요?
백엔드 개발자 입장에서 UI 기술
- 백엔드 - 서버 사이드 렌더링 기술
  - JSP, 타임리프
  - 화면이 정적이고, 복잡하지 않을 때 사용
  - 백엔드 개발자는 서버 사이드 렌더링 기술 학습 필수
- 웹 프론트 엔드 - 클라이언트 사이드 렌더링 기술
  - React, Vue.js 
  - 복잡하고 동적인 UI 사용
  - 웹 프론트엔드 개발자의 전문 분야
- 선택과 집중
  - 백엔드 개발자의 웹 프론트엔드 기술 학습은 옵션
  - 백엔드 개발자는 서버, DB, 인프라 등등 수 많은 백엔드 기술을 공부해야 한다.
  - 웹 프론트엔드도 깊이있게 잘 하려면 숙련에 오랜 시간이 필요하다.

# 자바 백엔드 웹 기술 역사
- 과거 기술
  - 서블릿 - 1997
    - HTML 생성이 어려움
  - JSP - 1999
    - HTML 생성은 편리하지만, 비즈니스 로직까지 너무 많은 역할 담당
  - 서블릿, JSP 조합 MVC 패턴 사용
    - 모델, 뷰 컨트롤러로 역할을 나누어 개발
  - MVC 프레임워크 춘추 전국 시대 - 2000년 초 ~ 2010년 초
    - MVC 패턴 자동화, 복잡한 웹 기술을 편리하게 사용할 수 있는 다양한 기능 지원
    - 스트럿츠, 웹워크, 스프링 MVC(과거버전)
- 현재 사용 기술
  - `애노테이션 기반의 스프링 MVC 등장`
    - @Controller
    - MVC 프레임워크의 춘추 전국 시대 마무리
  - `스프링 부트의 등장`
    - 스프링 부트는 서버를 내장
    - 과거에는 서버에 WAS를 직접 설치하고, 소스는 War 파일을 만들어서 설치한 WAS에 배포
    - 스프링 부트는 빌드 결과(Jar)에 WAS 서버 포함 -> 빌드 배포 단순화
- 최신 기술 - 스프링 웹 기술의 분화
  - Web Servlet - Spring MVC
  - Web Reactive - Spring WebFlux
    - 특징
      - 비동기 넌 블러킹 처리
      - 최소 쓰레드로 최대 성능 - 쓰레드 컨텍스트 스위칭 비용 효율화
      - 함수형 스타일로 개발 - 동시처리 코드 효율화
      - 서블릿 기술 사용X
    - `그런데`
      - 웹 플럭스는 기술적 난이도 매우 높음
      - 아직은 RDB 지원 부족
      - 일반 MVC의 쓰레드 모델도 충분히 빠르다.
      - 실무에서 아직 많이 사용하지는 않음(전체 1% 이하)
- 자바 뷰 템플릿 역사 [ HTML을 편리하게 생성하는 뷰 기능 ]
  - JSP
    - 속도 느림, 기능부족
  - 프리마커(Freemarker), Velocity(벨로시티)
    - 속도 문제 해결, 다양한 기능
  - 타임리프(Thymeleaf)
    - 내추럴 템플릿 : HTML의 모양을 유지하면서 뷰 템플릿 적용 가능
    - 스프링 MVC와 강력한 기능 통합
    - `최선의 선택`, 단 성능은 프리마커, 벨로시티가 더 빠름

# 서블릿
### HttpServletRequest - 개요
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다.
서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다.
그리고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.
- *중요*
  - HttpServletRequest, HttpServletResponse를 사용할 떄 가장 중요한 점은 이 객체들이 HTTP 요청 메시지, HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점이다. 따라서 이 기능에 대해서 깊이 있는 이해를 하려면 "HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해"해야 한다.

### HTTP 요청 데이터 - 개요
HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법을 알아보자.
- 주로 다음 3가지 방법을 사용한다.
  - `GET - 쿼리 파라미터`
    - /url?username=hello&age=20
    - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
    - 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
  - `POST - HTML Form`
    - content-type: application/x-www-form-urlencoded
    - 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20
    - 예) 회원 가입, 상품 주문, HTML Form 사용
  - `HTTP message body`에 데이터를 직접 담아서 요청
    - HTTP API에서 주로 사용, JSON, XML, TEXT
    - content-type: application/json
    - 데이터 형식은 주로 JSON 사용
      - POST, PUT, PATCH
- *참고*
  - content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다. `GET URL 쿼리 파라미터 형식`으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다. `POST HTML Form 형식`으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다. 이렇게 폼으로 데이터를 전송하는 형식을 application/x-www-form-urlencoded라 한다. 
  - inputStream은 byte코드를 반환한다. byte 코드는 우리가 읽을 수 있는 문자(String)로 보려면 문자표(Charset)를 지정해주어야 한다. 여기서는 UTF_8 Charset을 지정해주었다.
  - JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다. 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리(ObjectMapper)를 함꼐 제공한다.
  - HTML form 데이터도 메시지 바디를 통해 전송되므로 직접 읽을 수 있다. 하지만 편리한 파라미터 조회기능(request.getParameter(...))을 이미 제공하기 때문에 파라미터 조회 기능을 사용하면 된다.

### HttpServletResponse - 기본 사용법
- 역할
  - HTTP 응답 메시지 생성
    - HTTP 응답코드 지정
    - 헤더 생성
    - 바디 생성

### HTTP 응답 데이터 - 단순 텍스트, HTML, JSON
HTTP 응답 메시지는 주로 다음 내용을 담아서 전달한다.
- 단순 텍스트 응답
  - 앞에서 살펴봄 (writer.println("ok);)
  - HTML 응답
  - HTTP API - MessageBody JSON 응답
- JSON 응답
  - HTTP 응답으로 JSON을 반환할 때는 content-type을 application/json로 지정해야 한다. Jackson 라이브러리가 제공하는 objectmapper.writeAsString()을 사용하면 객체를 JSON문자로 변경할 수 있다.

- *참고*
  - application/json은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 그래서 스펙에서 chaset=utf-8과 같은 추가 파라미터를 지원하지 않는다. 따라서 application/json 이라고만 사용해야지 application/json;charset=utf-8이라고 전달하는 것은 의미 없는 파라미터를 추가한 것이 된다. response.getWriter()를 사용하면 추가 파라미터를 자동으로 추가해버린다. 이때는 response.getOuputStream()으로 출력하면 그런 문제가 없다.

# 서블릿, JSP, MVC 패턴
### 템플릿 엔진으로
지금까지 서블릿과 자바 코드만으로 HTML을 만들어 보았다. 
서블릿 덕분에 동적으로 원하는 HTML을 마음껏 만들 수 있다.
정적인 HTML 문서라면 화면이 계속 달라지는 회원의 저장 결과라던가, 회원 목록 같은 동적인 HTML을 만드는 일은 불가능할 것이다.
그런데, 코드에서 보듯이 이것은 매우 복잡하고 비효율적이다.
자바 코드로 HTML을 만들어 내는 것보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것이다.
이것이 바로 템플릿 엔진이 나온 이유이다.
템플릿 엔진을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.
템플릿 엔진에는 JSP, Thymeleaf, Freemarker, Velocity 등이 있다.

### 서블릿과 JSP의 한계
서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다.
JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용했다.
그런데 이렇게 해도 해결되지 않는 몇 가지 고민이 남는다.

회원 저장 JSP를 보자. 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 회원 몬록의 경우에도 마찬가지다.
코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어있다. 
JSP가 너무 많은 역할을 한다. 이렇게 작은 프로젝트도 벌써 머리가 아파오는데, 수백 수천줄이 넘어가는 JSP를 떠올려보면 정말 지옥과 같을 것이다.

`MVC 패턴의 등장`   
비즈니스 로직은 서블릿처럼 다른 곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 하자.
과거 개발자들도 모두 비슷한 고민이 있었고, 그래서 MVC 패턴이 등장했다.
우리도 직접 MVC 패턴을 적용해서 프로제그를 리팩토링 해보자.

### MVC 패턴 - 개요
`너무 많은 역할`    
하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을 하게되고, 결과적으로 유지보수가 어려워진다.
비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당 코드를 손대야 하고, UI를 변경할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다.
HTML 코드 하나 수정해야 하는데, 수백줄의 자바 코드가 함께 있다고 상상해보라!
또는 비즈니스 로직을 하나 수정해야 하는데 수백 수천줄의 HTML 코드가 함꼐 있다고 상상해보라.

`변경의 라이프 사이클`  
사실 이게 정말 중요한데, 진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다.
예를 들어서 UI를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분 서로에게 영향을 주지 않는다.
이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은 유지보수하기 좋지 않다.
(물론 UI가 많이 변하면 함께 변경될 가능성도 있다.)

`기능 특화`   
특히 JSP 같은 뷰 템플릿은 화면을 렌더링하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이 가장 효과적이다.

`Model View Controller`   
MVC 패턴은 지금까지 학습한 것처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와 뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다.
웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.
- 컨트롤러 : HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
- 모델 : 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링하는 일에 집중할 수 있다.
- 뷰 : 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.

`참고`   
컨트롤러에 비즈니스 로직을 둘 수도 있지만, 이렇게 되면 컨트롤러가 너무 많은 역할을 담당한다.
그래서 일반적으로 비즈니스 로직은 서비스(Service)라는 계층을 별도로 만들어서 처리한다.
그리고 컨트롤러는 비즈니스 로직이 있는 서비스를 호출하는 역할을 담당한다.
참고로 비즈니스 로직을 변경하면 비즈니스 로직을 호출하는 컨트롤러의 코드도 변경될 수 있다.
앞에서는 이해를 돕기 위해 비즈니스 로직을 호출한다는 표현보다는, 비즈니스 로직이라 설명했다.
![MVC2](https://github.com/keunoh/spring-mvc/assets/96904103/c5c7f7c7-3ccc-45fe-b973-338eb7a809c8)

### MVC 패턴 - 적용
서블릿은 컨트롤러로 사용하고, JSP를 뷰로 사용해서 MVC 패턴을 적용해보자.
Model은 HttpServletRequest 개게를 사용한다. request는 내부에 데이터 저장소를 가지고 있는데,
request.setAttribute(), request.getAttribute()를 사용하면 데이터를 보관하고, 조회할 수 있다.

*/WEB-INF*   
이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다. 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.

`redirect vs forward`   
리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다, 클라이언트가 redirect 경로로 다시 요청한다.
따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다.
반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못 한다.

### MVC 패턴 - 한계
MVC 패턴을 적용한 덕분에 컨트롤러의 역할과 뷰를 렌더링하는 역할을 명확하게 구분할 수 있다.
특히 뷰는 화면을 그리는 역할에 충실한 덕분에, 코드가 깔끔하고 직관적이다.
단순하게 모델에서 필요한 데이터를 꺼내고, 화면을 만들면 된다.
그런데 컨트롤러는 딱 봐도 중복이 많고, 필요하지 않은 코드들도 많이 보인다.

- MVC 컨트롤러의 단점
  - 포워드 중복
    - View로 이동하는 코드가 항상 중복 호출되어야 한다. 물론 이 부분을 메서드로 공통화해도 되지만, 해당 메서드도 항상 직접 호출해야 한다.
  - ViewPath 중복
    - prefix: `/WEB-INF/views/`
    - suffix: `.jsp`
      - 그리고 만약 jsp가 아닌 thymeleaf 같은 다른 뷰로 변경한다면 전체 코드를 다 변경해야 한다.
  - 사용하지 않는 코드
    - 다음 코드를 사용할 때도 있고, 사용하지 않을 때도 있다. 특히 response는 현재 코드에서 사용되지 않는다.
    - HttpServletRequest request, HttpServletResponse response
    - 그리고 이런 HttpServletRequest, HttpServletResponse를 사용하는 코드는 테스트 케이스를 작성하기도 어렵다.
  - 공통 처리가 어렵다.
    - 기능이 복잡해 질수록 컨트롤러에서 공통으로 처리해야 하는 부분이 점점 더 많이 증가할 것이다. 단순히 공통 기능을 메서드로 뽑으면 될 것 같지만, 결과적으로 해당 메서드를 항상 호출해야 하고, 실수로 호출하지 않으면 문제가 될 것이다. 그리고 호출하는 것 자체도 중복이다.
  - 정리하면 공통 처리가 어렵다는 문제가 있다.
    - 이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다. 소위 `수문장 역할`을 하는 기능이 필요하다. `프론트 컨트롤러(Front Controller) 패턴`을 도입하면 이런 문제를 깔끔하게 해결할 수 있다. (입구를 하나로!) 스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.

# MVC 프레임워크 만들기
### 프론트 컨트롤러 패턴 소개
![FRONT_CONTROLLER](https://github.com/keunoh/spring-mvc/assets/96904103/caf71c7e-7274-43a5-ab8f-acfe2a5b2fab)

- *FrontController 패턴 특징*   
  - 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
  - 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
  - 입구를 하나로!
  - 공통 처리 기능
  - 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨
- *스프링 웹 MVC와 프론트 컨트롤러*
  - 스프링 웹 MVC의 핵심도 바로 `FrontController`
  - 스프링 웹 MVC의 `DispatcherServlet`이 FrontController 패턴으로 구현되어 있음

### 프론트 컨트롤러 도입 - v1
프론트 컨트롤러를 단계적으로 도입해보자.
이번 목표는 기존 코드를 최대한 유지하면서, 프론트 컨트롤러를 도입하는 것이다.
먼저 구조를 맞추어두고 점진적으로 리팩토링 해보자.

*V1 구조*  
![FRONT_CONTROLLER_V1](https://github.com/keunoh/spring-mvc/assets/96904103/df5aa14f-2c12-4f79-828a-c709d9ec9779)

서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다.
각 컨트롤러들은 이 인터페이스를 구현하면 된다.
프론트 컨트롤러는 이 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가져갈 수 있다.
이제 이 인터페이스를 구현한 컨트롤러를 만들어 보자.
지금 단계에서는 기존 로직을 최대한 유지하는게 핵심이다.

#### ⭐ TIP - 개발하다보면 아키텍처를 개선해야 될 일들이 생긴다. 구조적인 큰 거를 개선할 때랑 디테일한 부분을 개선할 때랑 준위가 다르다.  구조를 개선할 때는 일단 구조적인 것만 개선하고 기존 코드는 최대한 유지시켜야 한다. 그 다음에 구조를 바꿨는데 문제가 없으면 이제 세세한 부분들을 개선하는 식으로 해야한다. 구조적인 것 개선하고 커밋하고 배포해서 잘 됐는지 확인하고 완전히 끝낸다. 그 다음 단계로 세밀한 것을 개선한다.

### View 분리 - v2
모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다.
이 부분을 깔끔하게 분리하기 위해 별도로 뷰를 처리하는 객체를 만들자.

*V2 구조*  
![FRONT_CONTROLLER_V2](https://github.com/keunoh/spring-mvc/assets/96904103/ae809172-b0f9-4f0d-8a08-65f6b1823a4d)

### Model 추가 - v3
*서블릿 종속성 제거*  
컨트롤러 입장에서 HttpServletRequest, HttpServletResponse가 꼭 필요할까?
요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해 보자.
이렇게 하면 구현코드도 매우 단순해지고, 테스트 코드 작성도 쉽다.

*뷰 이름 중복 제거*  
컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
컨트롤러는 `뷰의 논리 이름`을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화하자.
이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.

![FRONT_CONTROLLER_V3](https://github.com/keunoh/spring-mvc/assets/96904103/31e7c77e-ddac-4a18-8f95-651311da5d6c)

*ModelView*  
지금까지 컨트롤러에서 서블릿에 종속적인 HttpServletRequest를 사용했다.
그리고 Model도 request.setAttribute()를 통해 데이터를 저장하고 뷰에 전달했다.
서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들어보자.
(이번 버전에서는 컨트롤러에서 HttpServletRequest를 사용할 수 없다. 따라서 직접 request.setAttribute()를 호출할 수도 없다. Model이 별도로 필요하다.)

*뷰 리졸버*  
컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다. 그리고 실제 물리 경로가 있는 MyView객체를 반환한다.

### 단순하고 실용적인 컨트롤러 - v4
앞서 만든 v3 컨트롤러는 서블릿 종속성을 제거하고 뷰 경로의 중복을 제거하는 등, 잘 설계된 컨트롤러이다.
그런데 실제 컨트롤러 인터페이스를 구현하는 개발자 입장에서 보면, 항상 ModelView 객체를 생성하고 반환해야 하는 부분이 조금은 번거롭다.
좋은 프레임워크는 아키텍처도 중요하지만, 그와 더불어 실제 개발하는 개발자가 단순하고 편리하게 사용할 수 있어야 한다. 
소위 실용성이 었어야 한다. 이번에는 v3를 조금 변경해서 실제 구현하는 개발자들이 매우 편리하게 개발할 수 있는 v4 버전을 개발해보자.

![FRONT_CONTROLLER_V4](https://github.com/keunoh/spring-mvc/assets/96904103/8c304037-a4bf-48bf-bc7a-3f19378d6f83)
- 기본적인 구조는 v3와 같다. 대신에 컨트롤러가 ModelView를 반환하지 않고, ViewName만 반환한다. 이번 버전은 인터페이스에 ModelView가 없다. model 객체는 파라미터로 전달되기 때문에 그냥 사용하면 되고, 결과로 뷰의 이름만 반환해주면 된다.

*정리*  
이번 버전의 컨트롤러는 매우 단순하고 실용적이다.
기존 구조에서 모델을 파라미터로 넘기고, 뷰의 논리 이름을 반환한다는 작은 아이디어를 적용했을 뿐인데,
컨트롤러를 구현하는 개발자 입장에서 보면 이제 군더더기 없는 코드를 작성할 수 있다.
또한 중요한 사실은 여기까지 한번에 온 것이 아니라는 점이다.
프레임워크가 점진적으로 발전하는 과정속에서 이런 방법도 찾을 수 있다.  
#### 프레임워크나 공통 기능이 수고로워야 사용하는 개발자가 편리해진다.



