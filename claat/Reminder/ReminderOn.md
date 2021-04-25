author: @gishi_yama
id: RemainderOn
categories: java
status: Draft
environments: Web
feedback link: mailto:cist-softeng-qa@googlegroups.com

# リマインダを登録する

## はじめに

ここでは、 **13:15に授業** などのように時間と用件をLINEBotに話しかけると、それをリマインダの登録要求として識別し、データベースに記録する部分を作成する。

まず、**ユーザのメッセージから、ユーザが何を要求しているのかの意図（これをインテントと呼ぶ）を識別する** 必要がある。インテントを識別したら、メッセージから **データベースに記録するデータを作** し、実際に **データベースのテーブルに記録** する。




### ポイント

1. **インテントをJavaの Enum(列挙型) で作成**する
1. インテントの識別は **初歩的な方法として正規表現で識別** をする
1. インテントを識別したメッセージから **時間と要件を分離し、データベースのテーブルに記録するためのデータを作る**
1. LINEBotの制御にも使っている Spring フレームワークの機能で、**テーブルを作成し、テーブルにデータを記録する**

## インテントのための Enum を作成する

このChatBotで識別できるIntentを定義する。

Javaでは、システム内で変わらない定義や定数に `Enum` （列挙型）と呼ばれる特別なクラスを使う。

### Enum を作成する

`com.example.linebot.replier` パッケージの中に、 `Intent` Enum を作る。

IntelliJ IDEA では、クラス作成時に **列挙型（Enum）** を選べば良い。

![Enum を作成する](RO0201.png)

作成されるIntent Enumは以下のようになる。通常、 `public class XXXX` と書くところが、`public enum XXXX` になっていることに注目。

![Enum の例](RO0301.png)

## インテントを識別する正規表現をEnumに作成する

Intent Enum の内容を次のように書き換える。

![正規表現をEnumに作成する](RO0302.png)

**^(\d{1,2}):(\d{1,2})に(.{1,32})$** は、**13:15に授業**、 **16:55にバス** などのように、**XX:YYに〇〇** という文字列かどうかを判別するための正規表現パターンである。

- XX, YY は最大2文字の数字。〇〇は最大32文字の文字列を想定
- 文字列のパターンに当てはまれば、メッセージが「リマインダを登録したい」ものだと判断する(`REMINDER`を使う)
- 文字列のパターンに当てはまらない時は、通常のメッセージだと判断する（`UNKNOWN` を使う）
- Javaコードでは \ が一つ増える:エスケープすることに注意

**makeIntent メソッドは static メソッド** のため、**インスタンス化しなくても呼び出せる** （詳しくは次ページ）。


Negative
: Javaの正規表現をより詳しく勉強したい場合は、[JavaDoc](https://docs.oracle.com/javase/jp/11/docs/api/java.base/java/util/regex/Pattern.html) や [Javaの正規表現](http://www.ne.jp/asahi/hishidama/home/tech/java/regexp.html) を参考にすると良い  
列挙型をより詳しく勉強したい場合は、[Java列挙型](https://www.ne.jp/asahi/hishidama/home/tech/java/enum.html) や [Typesafe Enum](https://www.javainthebox.net/laboratory/J2SE1.5/LangSpec/TypesafeEnum/TypesafeEnum.html) を参考にすると良い

##  