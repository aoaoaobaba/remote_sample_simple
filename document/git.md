# RemoteContainer で Gitを使う＆GitHub接続する

## 大まかな流れ（SSH）

- SSH キーを生成 ＠ホスト（ローカル）マシン
- GitHub に公開鍵を登録 ＠ホスト（ローカル）マシン
- SSH キーを ssh-agent に追加 ＠ホスト（ローカル）マシン
- git をインストール ＠リモートマシン
  - Dockerfile にインストール処理を記述
- ssh-agent を起動 ＠リモートマシン
  - `~/.bash_profile` に自動的に起動する処理を追記
  - Visual Studio Code の Remote Containers 拡張機能がホストの資格情報を共有してくれるので、リモートマシンで SSH の再設定はしなくていい

### 参考
https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent?platform=windows

## 各手順メモ

### SSH キーを生成 ＠ホスト（ローカル）マシン

```powershell
ssh-keygen -t ed25519 -C "your_email@example.com"
```

### SSH キーを ssh-agent に追加 ＠ホスト（ローカル）マシン

```powershell
# ssh-agent を起動
Get-Service -Name ssh-agent | Set-Service -StartupType Manual
Start-Service ssh-agent
```

```powershell
# 秘密キーを ssh-agent に追加
ssh-add /c/Users/YOU/.ssh/id_ed25519
```

### git をインストール ＠リモートマシン

Dockerfile に以下を記述<br>
（パッケージによってバージョンが古くなる落とし穴があるようなので注意）
```
# Git
RUN yum install -y git
```

### ssh-agent を起動 ＠リモートマシン

`~/.bash_profile` に以下を記述することで、ログイン時に起動するようになる
```bash
if [ -z "$SSH_AUTH_SOCK" ]; then
   # Check for a currently running instance of the agent
   RUNNING_AGENT="`ps -ax | grep 'ssh-agent -s' | grep -v grep | wc -l | tr -d '[:space:]'`"
   if [ "$RUNNING_AGENT" = "0" ]; then
        # Launch a new instance of the agent
        ssh-agent -s &> $HOME/.ssh/ssh-agent
   fi
   eval `cat $HOME/.ssh/ssh-agent`
fi
```

Dockerfile で、~/.bash_profile に上記ロジックを追記する処理を記述
![Dockerfile](image.png)