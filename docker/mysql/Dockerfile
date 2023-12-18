FROM mysql:8

RUN microdnf update -y

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
