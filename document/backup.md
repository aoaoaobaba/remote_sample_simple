# 試行錯誤のゴミ

```
# --------------------------------------------------
# タイムゾーンの設定
# --------------------------------------------------
# /etc/localtime を Asia/Tokyo タイムゾーンに対応する
# タイムゾーンファイルへのシンボリックリンクに置換
# これで、コンテナのシステムタイムゾーンが Asia/Tokyo に設定される
# --------------------------------------------------
ENV TZ "Asia/Tokyo"
RUN microdnf install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# RUN microdnf update -y && \
#     microdnf install -y tzdata && \
#     ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# --------------------------------------------------
# 日本語対応
# --------------------------------------------------
ENV LANG ja_JP.UTF-8
ENV LC_ALL ja_JP.UTF-8
RUN microdnf install -y glibc-locale-source && \
    localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8

```

```
# タイムゾーンの設定
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
```