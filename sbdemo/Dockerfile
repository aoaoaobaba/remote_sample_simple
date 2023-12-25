# Amazon CorrettoのJavaをベースイメージとして使用
ARG JAVA_VERSION=21
FROM amazoncorretto:${JAVA_VERSION} as base

# 作業ディレクトリの設定
WORKDIR /workspace

# パッケージを最新の状態にアップデート
RUN yum update -y

##############################
# 開発環境
##############################
FROM base as dev

# 開発に必要なパッケージをインストール
ARG DEV_PKG="tar"
RUN yum install -y sudo \
    git \
    openssh-server \
    openssh-clients \
    ${DEV_PKG}

# ビルド引数からユーザー情報を取得
# （引数が渡されたらその値、渡されなかったらここで指定した値が有効）
ARG USER_NAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG HOME_DIR=/home/$USER_NAME

# ユーザーを追加し、sudo権限を付与
RUN groupadd --gid $USER_GID $USER_NAME && \
    useradd --uid $USER_UID --gid $USER_GID -m $USER_NAME && \
    echo "$USER_NAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/$USER_NAME

# GitHub用のSSH接続設定
RUN { \
    echo ''; \
    echo '# Launch ssh-agent'; \
    echo 'if [ -z "$SSH_AUTH_SOCK" ]; then'; \
    echo '   # Check for a currently running instance of the agent'; \
    echo '   RUNNING_AGENT="`ps -ax | grep '"'"'ssh-agent -s'"'"' | grep -v grep | wc -l | tr -d '"'"'[:space:]'"'"'`"'; \
    echo '   if [ "$RUNNING_AGENT" = "0" ]; then'; \
    echo '        # Launch a new instance of the agent'; \
    echo '        ssh-agent -s &> $HOME/.ssh/ssh-agent'; \
    echo '   fi'; \
    echo '   eval `cat $HOME/.ssh/ssh-agent`'; \
    echo 'fi'; \
    } >> $HOME_DIR/.bash_profile

# ユーザーを切り替え
USER $USER_NAME

# Spring Boot の場合は Gradle ラッパーが含まれているため Gradle のインストールは不要
