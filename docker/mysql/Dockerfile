FROM mysql:8

# タイムゾーンの設定
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# 3306 ポートを使用
EXPOSE 3306

# --------------------------------------------------
# データ初期化 DDL をコンテナにコピー
# --------------------------------------------------
# /docker-entrypoint-initdb.d に配置されたスクリプトやSQLファイルは、
# データベースの初回起動時に自動的に実行される
# --------------------------------------------------
COPY ./sql_init /docker-entrypoint-initdb.d
