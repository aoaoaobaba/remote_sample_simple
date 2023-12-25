FROM node:21.5-bookworm-slim as base

# 3000 ポートを使用
EXPOSE 3000

# 作業ディレクトリ
WORKDIR /workspace/nextjs

# パッケージリストを更新し、必要に応じてパッケージをアップグレード
RUN apt-get update -y && apt-get upgrade -y

##############################
# 開発環境
##############################
FROM base as dev

ENV NODE_ENV=development

#------------------------------
# 開発に必要なパッケージをインストール
#------------------------------
ARG DEV_PKG="tar"
RUN apt-get install -y sudo \
    git \
    openssh-server \
    openssh-client \
    ${DEV_PKG}

#------------------------------
# ユーザ作成
#------------------------------
# ビルド引数からユーザー情報を取得
# （引数が渡されたらその値、渡されなかったらここで指定した値が有効）
ARG USER_NAME=vscode
ARG USER_UID=550904
ARG USER_GID=$USER_UID
ARG HOME_DIR=/home/$USER_NAME

# ユーザーを追加し、sudo権限を付与
RUN groupadd --gid $USER_GID $USER_NAME && \
    useradd --uid $USER_UID --gid $USER_GID -m $USER_NAME && \
    echo "$USER_NAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/$USER_NAME

#------------------------------
# Git設定
#------------------------------
# Gitのグローバル設定を行う
RUN git config --global --add safe.directory /workspace

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

#------------------------------
# NEXT.JS 環境作成
#------------------------------
# パッケージ定義ファイルをコピー
COPY package.json yarn.lock* ./

# 依存関係をインストール
RUN if [ -f yarn.lock ]; then yarn --frozen-lockfile; \
    else echo "Warning: Lockfile not found. It is recommended to commit lockfiles to version control." && yarn install; \
    fi

# ユーザーを切り替え
USER $USER_NAME
