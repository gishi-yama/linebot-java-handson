## LINE Beaconを受信する

LINE Beacon と Bot を連携させることで、ビーコンを配置した特定のエリアに近づいた際に特別なメッセージを発話させたり、処理させることができる。

[公式ドキュメント：ビーコンを使う](https://developers.line.me/ja/docs/messaging-api/using-beacons/)

ビーコンは、[Webで購入できる](https://beacon.theshop.jp/items/6617930)。

もしくは、node.jsやBBC micro:bitでビーコンを自作することもできる。

-node.js用　[line/line-simple-beacon](https://github.com/line/line-simple-beacon)
-micro:bit用 [pizayanz/pxt-linebeacon](https://github.com/pizayanz/pxt-linebeacon)

ここでは、

1. micro:bit で LINE Beacon のビーコンを作成する
2. ビーコンのエリアに入った際にBotを反応させる

の手順で進める。

### LINE Beacon のビーコンを作成

[BBC micro:bit](https://microbit.org/ja/guide/) と [pizayanz/pxt-linebeacon](https://github.com/pizayanz/pxt-linebeacon) を使うと、ブロックプログラミングで用意にビーコンを準備できる。



-----

[戻る](../../README.md)
