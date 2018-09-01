## LINE Beaconを受信する

LINE Beacon と Bot を連携させることで、ビーコンを配置した特定のエリアに近づいた際に特別なメッセージを発話させたり、処理させることができる。

[公式ドキュメント：ビーコンを使う](https://developers.line.me/ja/docs/messaging-api/using-beacons/)

ビーコンは、[Webで購入できる](https://beacon.theshop.jp/items/6617930)。

もしくは、node.jsやBBC micro:bitでビーコンを自作することもできる。

- node.js用　[line/line-simple-beacon](https://github.com/line/line-simple-beacon)
- micro:bit用 [pizayanz/pxt-linebeacon](https://github.com/pizayanz/pxt-linebeacon)

ここでは、

1. LINE Beacon の ハードウェアID の払い出し
2. micro:bit で LINE Beacon のビーコンを作成する
3. ビーコンのエリアに入った際にBotを反応させる

の手順で進める。

### LINE Beacon の ハードウェアID の払い出し

LINE Beaconを作成するためには ハードウェアID をLINEから払い出してもらう。

1. [LINE@マネージャーのビーコン登録ページ](https://admin-official.line.me/beacon/register) にアクセスする。
2. LINE Developers と同じユーザID・パスワードでログインする。
3. `LINE Simple BeaconのハードウェアID払い出し` ボタンを押す。<br />![beacon01](beacon01.png)
4. Beaconを利用するBotの `選択` ボタンを押す。<br />![beacon02](beacon02.png)
5. `ハードウェアID発行` ボタンを押す。<br />![beacon03](beacon03.png)
6. ハードウェアID（10桁数字）が発行される。メモ帳などにコピーしておく。<br />![beacon04](beacon04.png)

### micro:bit で LINE Beacon のビーコンを作成

[BBC micro:bit](https://microbit.org/ja/guide/) は教育用のブロックプログラミング可能な小型マイコンで、[スイッチサイエンス]()や[Amazon JP]()経由で購入できる。

ここでは手元に micro:bit の実機があり、開発しているPCとUSBケーブルで接続されていることを前提に進める。

1. [Microsoft MakeCode for micro:bit](https://makecode.microbit.org/) にアクセスする。<br />![beacon05](beacon05.png)
2. `高度なブロック` 欄をクリックする。<br />![beacon06](beacon06.png)
3. `+ パッケージを追加する` 欄をクリックする。<br />![beacon06](beacon07.png) 
4. `検索またはプロジェクトのURLを入力...` 欄に、[pizayanz/pxt-linebeacon](https://github.com/pizayanz/pxt-linebeacon) のURL `https://github.com/pizayanz/pxt-linebeacon` を入力し、虫眼鏡ボタンを押す。
5. `linebeacon`パッケージが表示されるので、選択する<br />![beacon08](beacon08.png)
6. `元のパッケージを削除して「linebeacon」を追加する` ボタンを押す。<br />![beacon09](beacon09.png)
7. メニューから `無線` 欄が消え、代わりに `Line Beacon` 欄と `Bluetooth` 欄が表示されていることを確認する。<br />![beacon10](beacon10.png)<br />※なお、元のエディタのメニューに戻したいときは `プロジェクト` → `新しいプロジェクト` すれば良い。
8. 以下の図のようにプログラミングする。<br />![beacon11](beacon11.png)
    - `Bluetooth 送信強度`　ブロックは、 `Bluetooth` 欄にある。<br />デフォルト値（最大値）は `7` だが、電波強度が高すぎる（+4dBm, 最大70m）なので、卓上で試すなら `0` もしくは `1` （-30dBm〜-20dBm）で十分。
    - `アイコンを表示` ブロックは、 `基本` 欄にある。 
    - `LINE Beacon start HWID is...` ブロックは、`LINE Beacon` 欄にある。<br />HWID の `xxxxxxxxxx` は、上の手順で払い出したLINE BeaconのハードウェアIDにする。<br />`with Device Message` は、0〜Fのによる16進数値（最大13バイト:26桁）にする。
    - `LINE Beacon stop` ブロックも、 `LINE Beacon` 欄にある。
9. エディタの `名称未設定` 欄に `linebeacon` と入力し、保存ボタンを押す。
10. ローカルフォルダに、 microbit-linebeacon.hex ファイルが保存されていることを確認する。
11. microbit-linebeacon.hex ファイルをUSBメモリとして認識されている micro:bit にコピーする。
12. micro:bit へのファイル書き込み後、実機のLEDに小さなハートマークが表示される。
    - 実機のAボタンを押すと、チェックマークが表示される。
    - 実機のBボタンを押すと、バツマークが表示される。

### ビーコンのエリアに入った際にBotを反応させる

LINEアプリを起動した端末をLINE Beaconに近づけると、ビーコンイベントがBotに送信される。

Messsage APIでは、このイベントに対するコールバック処理を作成する。

#### Callbackクラスを変更

ビーコンイベントに対応するメソッドを追加する。<br/>（必要であれば `com.linecorp.bot.model.event.BeaconEvent` を import の行につけくわえる）

```java
  // BeaconEventに対応する
  @EventMapping
  public Message handleBeacon(BeaconEvent event) {
    // Beaconイベントの内容を文字列に変換する
    String eventStr = event.getBeacon().toString();
    // eventStr をBotで返信する
    return reply(eventStr);
  }
```

### 動作確認

**必ずスマートフォン（タブレット）のLINEアプリで動作確認してください（本稿執筆時点でPC版は対応していない）**

1. LineBotApplication を一度停止して、再起動する
2. micro:bit を再起動する
3. LINEアプリを開いた状態で、micro:bitに近づける
4. micro:bit のAボタンを押す（ビーコン信号送信開始）
5. Botが、アプリが受信したビーコン信号の内容を投稿する<br />![beacon12](beacon12.png)
6. micro:bit のBボタンを押す（ビーコン送信停止）
7. しばらく待つとBotが、アプリが受信したビーコン信号の内容を投稿する<br />![beacon13](beacon13.png)

### 解説と補足

LINE BeaconはBluetooth（BLE）の電波で ハードウェアID と deviceMessage を周囲に送る。

この電波をLINEアプリがキャッチすると、 `enter` タイプとして認識されたBeaconイベントを、ハードウェアIDが登録されているBotに通知する。

電波がキャッチできなくなって一定時間がたつと、 `leave` タイプとして認識されたBeaconイベントを、ハードウェアIDが登録されているBotに通知する。

これによって、LINE Beaconに近づいた時（電波が届く範囲に入る： `enter` タイプのイベント）、LINE Beaconから離れた時（電波が届く範囲から出る： `leave` タイプのイベント）をBotが検知して処理を行うしくみを作れる。例えば「店に入るとBotがクーポンを表示する」といった仕組みに利用されている。

なお、[`leave` タイプのイベントは将来的に廃止予定](https://developers.line.me/ja/reference/messaging-api/#beacon-event-types)であり、今後挙動が変わる可能性もある。

-----

[戻る](../../README.md)
