FROM node:21.5-bookworm-slim as base

# 3000 ポートを使用
EXPOSE 3000

# 作業ディレクトリ
WORKDIR /workspace

##############################
# 開発環境
##############################
FROM base as dev

ENV NODE_ENV=development

# # パッケージ定義ファイルをコピー
# COPY package.json yarn.lock* .nextjs/

# # 依存関係をインストール
# RUN if [ -f yarn.lock ]; then yarn --frozen-lockfile; \
#     else echo "Warning: Lockfile not found. It is recommended to commit lockfiles to version control." && yarn install; \
#     fi
