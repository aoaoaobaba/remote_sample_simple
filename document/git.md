# GitHubに接続する方法についてメモ

## 概要

- SSH キーを生成（ホストマシン）
- GitHub に公開鍵を登録（ホストマシン）
- SSH キーを ssh-agent に追加（ホストマシン）
- Visual Studio Code の Remote Containers 拡張機能がホストの資格情報を共有してくれる（リモート用には何もしなくていい）

参考：
https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent?platform=windows


## 新しい SSH キーの生成（ホスト）

```
ssh-keygen -t ed25519 -C "your_email@example.com"
```

## SSH キーを ssh-agent に追加（ホストマシン）

PowerShell（管理者）で以下を実施
```
# ssh-agent を起動
Get-Service -Name ssh-agent | Set-Service -StartupType Manual
Start-Service ssh-agent
```

```
# 秘密キーを ssh-agent に追加
ssh-add /c/Users/YOU/.ssh/id_ed25519
```
