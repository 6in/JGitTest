# JGitTest

## 概要

* JGitを使ったサンプルアプリです。
* Clone/Pull -> Add -> Commit -> Push を行います 


## ビルド方法

```
./gradlew clean build 
```

## 実行方法

```
./gradlew run \
  -Pusr=ユーザー名 \
  -Ppsw=パスワード \
  -Pemail=メールアドレス \
  -Premote=リモートリポジトリ(https)のURL \
  -Plocal=ローカルリポジトリへのパス

```



