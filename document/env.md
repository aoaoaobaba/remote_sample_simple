# 変数メモ

devcontainer.json、docker-compose.yml、Dockerfile に登場する変数についてまとめ

以下を整理
- 引数か、環境変数か
- ホスト（ローカル）マシンの環境変数か、リモートマシンの環境変数か

## RemoteContainer の devcontainer.json で扱う変数

### 参考
- [VSCodeのdevcontainer.jsonのremoteEnvでハマった話](https://medium.com/galactic1969/vscode%E3%81%AEdevcontainer-json%E3%81%AEremoteenv%E3%81%A7%E3%83%8F%E3%83%9E%E3%81%A3%E3%81%9F%E8%A9%B1-b46158e76d2f)

### リモートマシンの環境変数を設定

- `devcontainer.json` で、リモートマシンのログイン Shell に環境変数を設定できる
    ```
    // RunArgs
      "runArgs": ["--env-file",".env"],
      "remoteEnv": {
        "HOGE": "${containerEnv:HOGE}",
        "FUGA": "${containerEnv:FUGA}"
      },
    ```

    ```:.env
    HOGE=hoge
    FUGA="fuga"
    ```

## docker-compose.yml で扱う変数

### 参考

- [Compose における環境変数](https://matsuand.github.io/docs.docker.jp.onthefly/compose/environment-variables/)
- [環境変数のデフォルトをファイル内に定義する](https://matsuand.github.io/docs.docker.jp.onthefly/compose/env-file/)
- [docker-composeのenv_fileと.envファイルの違い](https://qiita.com/SolKul/items/989727aeeafcae28ecf7)

### ホスト（ローカル）マシンの環境変数を参照

- `docker-compose.yml` では、ホスト（ローカル）マシンで設定された環境変数を参照できる
  ```
  web:
    image: "webapp:${TAG}"
  ```

- 以下のようなデフォルト指定、必須指定が可能
  ```
  # USER_NAME がセットされていない or 空文字の場合、default
  ${USER_NAME:-default}

  # USER_NAME がセットされていない場合のみ、default
  ${USER_NAME-default}

  # USER_NAME がセットされていない or 空文字の場合、エラー
  ${USER_NAME:?err}

  # USER_NAME がセットされていない場合のみ、エラー
  ${USER_NAME?err}
  ```

- `.env` ファイルで、デフォルト値を設定することもできる
  - ホスト（ローカル）マシンにすでに環境変数が存在する場合は、そちらが優先
  - `.env` ファイルは、プロジェクトのベースディレクトリに置く
    - プロジェクトのベースディレクトリは、環境変数 `COMPOSE_FILE` で明示的に定義
    - 定義がない場合は `docker compose` コマンドを実行したカレントのワーキングディレクトリ
    ```
    # 値はクオーテーションなどで囲ってはいけない
    USER_NAME=AOAOAOBA
    ```

- 各方法の優先順位は以下のとおり
  1. `docker-compose.yml` ファイル
  1. ホスト（ローカル）マシン内の環境変数
  1. `.env` ファイル

### リモートマシンの環境変数を設定

- `docker-compose.yml` 内で `environment` キー を使って、環境変数を設定できる
  ```
  web:
    environment:
      - USER_NAME=user01
  ```

- `docker-compose.yml` 内の `env_file` で、環境変数のファイルを指定することもできる
  ```
  web:
    env_file:
      - my-variables.env
  ```

## Dockerfile で扱う変数

### 参考
- [Dockerfile リファレンス > ARG](https://docs.docker.jp/v19.03/engine/reference/builder.html#arg)

### ビルド時に受け取った引数を参照（ARG）

- イメージをビルドする際にホスト（ローカル）マシンの環境変数を利用したい場合は、引数として渡してもらう

- `ARG` で、ビルド時に指定された引数を変数に取得できる
  - `ARG` で指定された変数は、ビルドイメージ（リモートマシン）の中には保持されない
  - `ARG` で指定された変数のスコープは、`ARG` 宣言以降（Dockerfile内のみ）
  - `ARG` では、デフォルト値を指定できる

    ```
    # user1 が指定されなかった場合は"someuser"とする
    ARG user1=someuser
    ```

- ビルド時に指定する方法は以下２つ

  - `docker build` 時に `--build-arg <varname>=<value>` フラグで指定
    ```
    $ docker build --build-arg user=what_user Dockerfile
    ```

  - `docker-compose.yml` 内で、Dockerfileの `build` の引数 `args` として指定
    ```
    build:
      context: .
      dockerfile: Dockerfile
      args:
        JAVA_VERSION: 21
    ```
