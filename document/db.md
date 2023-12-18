# MySQL & myBatisメモ

## MySQL

### やりたいこと ＆ 実現方法

- MySQLをインストール
    - Dockerfile の FROM で `FROM mysql:8` を指定
- データベース作成
    - データベース作成
        - 環境変数を設定すると、作成してくれる
        - 環境変数は db.env ファイルに記述し、そのファイル名を docker-compose.yml の `env_file` に指定している
        - [Image サイト](https://hub.docker.com/_/mysql) に、環境変数の解説がある
    - 文字コードや比較の方法等、MySQL の設定を行う
        - `mysqld.cnf` で、MySQL の設定を行う
        - `mysqld.cnf` は、ローカルで作成したものを Dockerfile でコンテナにコピー
        - Dockerfile でコピーする理由
            - Windows でボリューム割り当てするとファイルのパーミッションは 777 になるが、777 の `mysqld.cnf` は MySQL に無視される。コンテナに反映後、パーミッションを変更したいため、Dockerfile でコピーしている
    - データを永続化するための設定
        - DBの実データが格納される `/var/lib/mysql` を、docker-compose.yml で `volumes` に割り当てて永続化
        - `volumes`（名前付きボリューム）は Docker 内の永続化エリアで、DockerDesktop で確認や削除ができる
        - 永続化を行わないと、コンテナ作成の度にデータが消えてしまう
- データベースのユーザ作成
    - 環境変数を設定すると、作成してくれる
    - 環境変数は db.env ファイルに記述し、そのファイル名を docker-compose.yml の `env_file` に指定している
- データベースの初期設定（テーブル作成やデータ作成）
    - MySQLコンテナでは、DB初期化時に `/docker-entrypoint-initdb.d` に配置されたファイルが実行される
    - Dockerfile でこのパスに、ローカルで作成したファイルをコピーしている

### docker-compose.yml

- mysql コンテナの設定を追加
- java コンテナの depends_on に mysql コンテナを追加
    - Java コンテナは mysql コンテナを使うため、先に mysql を起動

```
version: "3"
services:
  # mysql
  mysql:
    container_name: ${COMPOSE_PROJECT_NAME}-mysql
    build:
      context: ./mysql/
    env_file:
      - ./mysql/db.env
    ports:
      - "${DB_PORT-3306}:3306"
    volumes:
      - mysql-local:/var/lib/mysql # 実データの永続化（名前付きボリュームを指定）
  # java
  java:
    container_name: ${COMPOSE_PROJECT_NAME}-java
    build:
      context: ./java/
      args:
        JAVA_VERSION: ${JAVA_VERSION}
        USER_NAME: ${USER_NAME}
        USER_UID: ${USER_UID}
        USER_GID: ${USER_GID}
    env_file:
      - ./mysql/db.env
    ports:
      - 8080:8080
    tty: true
    volumes:
      - ..:/workspace:cached
    working_dir: /workspace
    depends_on:
      - mysql # mysql の後で起動

# 名前付きボリューム
volumes:
  mysql-local:
```

### Dockerfile

- DB設定ファイルのコピー
- DB初期化DDLのコピー
- mysql イメージでは、yumもaptも使えず、microdnfを使う

```
FROM mysql:8

# 3306 ポートを使用
EXPOSE 3306

# --------------------------------------------------
# MySQL設定
# --------------------------------------------------
# Windows から VirtualBox 経由でマウントしているディレクトリ/ファイルは
# すべて パーミッションが 777 になるが、mysql は 777 の .cnf を読まない。
# このため、docker-compose.yml で volumes 指定するのではなく、
# Dockerfile でコピーしてから、パーミッションを変更する
# 参考：https://qiita.com/koyo-miyamura/items/4d1430b9086c5d4a58a5
# --------------------------------------------------
COPY ./config/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf
RUN chmod 644 /etc/mysql/mysql.conf.d/mysqld.cnf

# --------------------------------------------------
# データ初期化 DDL をコンテナにコピー
# --------------------------------------------------
# /docker-entrypoint-initdb.d に配置されたスクリプトやSQLファイルは、
# データベースの初回起動時に自動的に実行される
# --------------------------------------------------
COPY ./sql_init /docker-entrypoint-initdb.d
```

### mysqld.cnf

- DBの設定

```
[mysqld]

# PIDファイルの場所
pid-file=/var/run/mysqld/mysqld.pid
# UNIX ソケットファイルの場所
socket=/var/run/mysqld/mysqld.sock
# データベースのデータの場所
datadir=/var/lib/mysql

# シンボリックリンク禁止
symbolic-links=0

# 文字セットをUTF-8 の4バイト対応版（utf8mb4）に設定
character-set-server=utf8mb4
# 文字照合順序をバイナリにに設定（厳密な比較）
collation-server=utf8mb4_bin

# 一般ログを有効にする
general_log=1
general_log_file=/var/log/mysql/general.log

# エラーログ
log-error=/var/log/mysql/error.log

# ローカルホスト（同じマシン上のクライアント）からの接続のみを受け付ける
#bind-address=127.0.0.1

# クライアントのホスト名解決キャッシュ（DNSキャッシュ）を無効にする
host_cache_size=0

[client]

# クライアントの文字セットをUTF-8 の4バイト対応版（utf8mb4）に設定
default-character-set=utf8mb4
```

### db.env

- コンテナに設定する環境変数
- 特定の環境変数を設定しておくと、MySQLの設定を行ってくれる

```
# MySQL の root ユーザーのパスワード
MYSQL_ROOT_PASSWORD=root

# 自動的に作成されるデータベースの名前
MYSQL_DATABASE=sample_schema

# MySQL に新しく作成するユーザー
MYSQL_USER=dev_usr
MYSQL_PASSWORD=dev_usr_pass

# タイムゾーン
TZ=Asia/Tokyo
```

## MyBatis

### やりたいこと

- MySQL への接続
    - MySQL コネクタ の依存関係を追加
        - build.gradle で、依存関係に `mysql-connector-j` を追加
    - Spring の DataSource を追加
        - application.yml で 接続情報を追加

- MyBatis の設定
    - 依存関係の追加
        - build.gradle で、依存関係に `mybatis-spring-boot-starter` を追加
    - MyBatisの設定
        - application.yml で 設定を追加

### build.gradle

- MySQL コネクタと、MyBatis を追加

```
dependencies {
    ～ 略 ～

	// https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
	implementation 'com.mysql:mysql-connector-j:8.2.0'

	// https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter
	implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3'
}
```

### application.yml

- 接続情報を追加
    - dataSource の url の `mysql:3306/sample_schema` は `コンテナサービス名:ポート/データベース名`
- MyBatisの設定を追加

```
# DB接続情報
spring:
  dataSource:
    url: jdbc:mysql://mysql:3306/sample_schema
    username: dev_usr
    password: dev_usr_pass
    sql-script-encoding: utf-8
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  configuration:
    map-underscore-to-camel-case: true

```

### MyBatis 利用例（AccountDao.java）

```
package com.example.sbdemo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountDao {

    @Select("select * from account where account_id=#{accountId}")
    Account findById(Account account);

    @Select("select * from account")
    List<Account> findAll();
}
```