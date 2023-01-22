以下のソースを参考にさせていただき、maximumSessionsを入れた場合の動作を確認しようといている
https://github.com/MasatoshiTada/spring-security-intro


spring-session-jdbc を入れると、HttpSessionEventPublisher()してもセッションイベントが飛んでいない。

Redisは、イベント飛ばしている
https://github.com/spring-projects/spring-session/blob/1dbaffef5e69f731c4cca335e9ba10118a23749d/spring-session-data-redis/src/main/java/org/springframework/session/data/redis/RedisOperationsSessionRepository.java#L593

jdbcは、イベント飛ばしていない
https://github.com/spring-projects/spring-session/blob/1dbaffef5e69f731c4cca335e9ba10118a23749d/spring-session-jdbc/src/main/java/org/springframework/session/jdbc/JdbcOperationsSessionRepository.java#L66-L68

アプリがひとつなら
addLogoutHandler()でログアウトハンドラー使い、セッションIDをけす
明示的にログアウトしない場合のため、セッション切れのところにも入れる必要あり

アプリが複数ある場合はDB見ないとだめ

Spring Security Intro
=====================
Spring Securityのサンプルコードです。

# 環境
- JDK 17
- Spring Boot 3.0.0-RC2
- Spring Security 6.0.0-RC2
- Thymeleaf 3.1.0.RC1
- H2 Database

# 実行
[main()メソッド](src/main/java/com/example/springsecurityintro/SpringSecurityIntroApplication.java)から起動

または

```shell
mvn clean spring-boot:run
```
