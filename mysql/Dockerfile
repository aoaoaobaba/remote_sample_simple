FROM mysql:8 as base

# 3306 ポートを使用
EXPOSE 3306

##############################
# 開発環境
##############################
FROM base as dev

# --------------------------------------------------
# MySQL の設定ファイルをリモートにコピー
# --------------------------------------------------
# chmod する理由：
# Windows から VirtualBox 経由でマウントしているディレクトリ/ファイルは
# すべて パーミッションが 777 になるが、mysql は 777 の .cnf を読まない。
# このため、Dockerfile でコピーしてから、パーミッションを変更する
# docker-compose.yml の volumes 指定でのコピーでは、うまくいかない。
# 参考：https://qiita.com/koyo-miyamura/items/4d1430b9086c5d4a58a5
# --------------------------------------------------
COPY ./config/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf
RUN chmod 644 /etc/mysql/mysql.conf.d/mysqld.cnf

# --------------------------------------------------
# データ初期化 DDL をリモートにコピー
# --------------------------------------------------
# /docker-entrypoint-initdb.d に配置されたスクリプトやSQLファイルは、
# データベースの初回起動時に自動的に実行される
# --------------------------------------------------
COPY ./init_sql /docker-entrypoint-initdb.d
