# ハンズオンの内容

[line/line-bot-sdk-java](https://github.com/line/line-bot-sdk-java)を使って、Java アプリケーション（Spring Bootフレームワーク）でLINE BOTを作ってみる

**次の環境で動作確認しています**

- macOS 10.15.7
- AdoptOpenJDK (build 11.0.9.1+1)
- Maven 3.6.3 
- IntelliJ 2020.2.4
- ngrok 2.3.35
- Spring Boot 2.3.7
- line-bot-sdk-java 4.3.0


## 前準備

- [ngrokの準備と起動](doc/Settings/01.md)
- [Spring Bootのプロジェクトの準備](doc/Settings/02.md)
- [LINE Bot（Message API）の準備](doc/Settings/03.md)
- [Spring Bootのプロジェクトの編集](doc/Settings/04.md)

## 基本編

- [LINE Botの中身を作成](doc/Basics/05.md)
- [時間帯にあわせてあいさつする](doc/Basics/06.md)
- [おみくじBot](doc/Basics/07.md)
- [バイナリデータを受信する](doc/Binary/Binary.md)

## 応用編1 特別なやりとり

- [Botから話しかける](doc/Push/08.md)
- [Botからの話しかけを自動化する](doc/Push/09.md)
- [確認画面を表示し、ユーザの回答に対応する](doc/Push/10.md)

## 応用編2 ユーザーインターフェース

- [Flexメッセージを送信する](doc/Flex/Flex.md)
- [リッチメニューを作成・利用する](doc/RichMenu/RM.md)
- ❌ ~~[LIFFを動かす](doc/Liff/Liff_P1.md)~~
  - 現在、この資料の方法でのLIFFの利用は推奨されていません

## ハンズオン時専用

特別なハードウェア（Wio-Node, LINE Beacon）を利用する例です。

- [センサーとの連携](doc/HandsOn/ex01.md)
- [LINE Beaconに反応させる](doc/Beacon/Beacon.md)

## 参考資料

- [LINEのBot開発 超入門（前編） ゼロから応答ができるまで](https://qiita.com/nkjm/items/38808bbc97d6927837cd)
- [【LINE BOT】Java(Maven)+Heroku+SpringBootでラーメンBOTを作ってみたよ(1)](https://qiita.com/megaitai22/items/e3e130df1c044ec0f3fd)
- [line-bot-sdk-javaでごみ出しリマインダーを作る](https://qiita.com/aytdm/items/7b8692662a0b161c555c)
- [Spring Bootでtaskを定期実行する方法](https://qiita.com/rubytomato@github/items/4f0c64eb9a24eaceaa6e)
