
# DeleteMain

## 변경사항

* 책임별 메소드 분리
    * ex) 로그 생성, 파일 삭제, DB 데이터 삭제 등
* 하드코딩 소스 application.properties 설정파일에 등록
* Calender type -> LocalDateTime type으로 변경
* 프로그램 시작 시, dev 혹은 prod 환경설정을 읽어오지 않으면 프로그램 강제 종료 설정
