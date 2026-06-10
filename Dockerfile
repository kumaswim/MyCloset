FROM eclipse-temurin:21-jdk-alpine

# 事前にビルドされたJARファイルをコンテナ内に app.jar としてコピー
COPY build/libs/*.jar app.jar

# コンテナ起動時に実行するコマンド
ENTRYPOINT ["java", "-jar", "/app.jar"]