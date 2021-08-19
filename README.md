# w3-SpringBoot_Part_A

## Mission 1 내용
---
바우처 관리 Command-line Application을 만들어본다.
(https://dzone.com/articles/interactive-console-applications-in-java)
CommandLineApplication 클래스를 작성한다.
AnnotationConfigApplicationContext 를 이용해서 IoC 컨테이너를 생성하고 서비스, 레 포지토리를 빈으로 등록한다.
프로그램이 시작하면 다음과 같이 지원가능한 명령어를 알려준다.

```=== Voucher Program ===
Type exit to exit the program.
Type create to create a new voucher.
Type list to list all vouchers.
```
- create / list 커맨드를 지원한다.
- create 커맨드를 통해 바우처를 생성할수 있습니다. (FixedAmountVoucher,
PercentDiscountVoucher)
- list 커맨드를 통해 만들어진 바우처를 조회할 수 있습니다.


## Mission 2 내용

---
- 컴포넌트 스캔을 통해서 빈이 등록되게 해보세요.
- 메모리 관리가 아닌 파일로 관리가 되는 Repository를 한번 만들어보세요.
- 기존 메모리 레포지토리는 지우지 말아주세요.
