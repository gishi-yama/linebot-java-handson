package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.action.URIAction.AltUri;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;


@LineMessageHandler
public class Callback {

  private LineMessagingClient client;

  @Autowired
  public Callback(LineMessagingClient client) {
    this.client = client;
  }

  private static final Logger log = LoggerFactory.getLogger(Callback.class);

  // フォローイベントに対応する
  @EventMapping
  public TextMessage handleFollow(FollowEvent event) {
    // 実際の開発ではユーザIDを返信せず、フォロワーのユーザIDをデータベースに格納しておくなど
    String userId = event.getSource().getUserId();
    return reply("あなたのユーザIDは " + userId);
  }

  // 返答メッセージを作る
  private TextMessage reply(String text) {
    return new TextMessage(text);
  }


  // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
  @EventMapping
  public Message handleMessage(MessageEvent<TextMessageContent> event) {
    TextMessageContent tmc = event.getMessage();
    String text = tmc.getText();
    switch (text) {
      case "やあ":
        return greet();
      case "おみくじ":
        return replyOmikuji();
      case "部屋":
        return replyRoomInfo();
      case "バブル":
        return replyBubble();
      case "カルーセル":
        return replyCarousel();
      case "クイックリプライ":
        return get();
      default:
        return reply(text);
    }
  }

  // あいさつする
  private TextMessage greet() {
    LocalTime lt = LocalTime.now();
    int hour = lt.getHour();
    if (hour >= 17) {
      return reply("こんばんは！");
    }
    if (hour >= 11) {
      return reply("こんにちは！");
    }
    return reply("おはよう！");
  }

  // 画像メッセージを作る
  private ImageMessage replyImage(String url) {
    // 本来は、第一引数が実際の画像URL、第二画像がサムネイルのurl
    return new ImageMessage(url, url);
  }

  // ランダムにおみくじ画像を返す
  private ImageMessage replyOmikuji() {
    int ranNum = new Random().nextInt(3);
    switch (ranNum) {
      case 2:
        return replyImage("https://3.bp.blogspot.com/-vQSPQf-ytsc/T3K7QM3qaQI/AAAAAAAAE-s/6SB2q7ltxwg/s1600/omikuji_daikichi.png");
      case 1:
        return replyImage("https://2.bp.blogspot.com/-27IG0CNV-ZE/VKYfn_1-ycI/AAAAAAAAqXw/fr6Y72lOP9s/s400/omikuji_kichi.png");
      case 0:
      default:
        return replyImage("https://4.bp.blogspot.com/-qCfF4H7YOvE/T3K7R5ZjQVI/AAAAAAAAE-4/Hd1u2tzMG3Q/s1600/omikuji_kyou.png");
    }
  }

  // センサーの値をWebから取得して、CO2クラスのインスタンスにいれる(******の所は、別途指示します）
  private TextMessage replyRoomInfo() {
    String key = "736862004b81b1abeed5c716cacbf048";
    String url = "https://us.wio.seeed.io/v1/node/GroveUltraRangerD1/range_in_cm?access_token=";
    URI uri = URI.create(url + key);
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    try {
      //②二酸化炭素検知センサを用いて空気中の二酸化炭素の量を計測する
           /* CO2 co2 = restTemplate.getForObject(uri, CO2.class);
            return reply("二酸化炭素は"
                    + co2.getConcentration()
                    + "ppm、温度は"
                    + co2.getTemperature()
                    + "度です。よって部屋の利用者数は"+ co2.getConcentration()/500+"人です。");*/
      //①超音波センサを用いた障害物との距離を計測する
      Range range = restTemplate.getForObject(uri, Range.class);
      return reply("障害物までの距離は" + range.getRange_cm() + "cmです。");
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return reply("センサーに接続できていません");
    }
  }

  // 画像のメッセージイベントに対応する
  @EventMapping
  public Message handleImg(MessageEvent<ImageMessageContent> event) {
    // ①画像メッセージのidを取得する
    String msgId = event.getMessage().getId();
    Optional<String> opt = Optional.empty();
    try {
      // ②画像メッセージのidを使って MessageContentResponse を取得する
      MessageContentResponse resp = client.getMessageContent(msgId).get();
      log.info("get content{}:", resp);
      // ③ MessageContentResponse からファイルをローカルに保存する
      // ※LINEでは、どの解像度で写真を送っても、サーバ側でjpgファイルに変換される
      opt = makeTmpFile(resp, ".jpg");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    // ④ ファイルが保存できたことが確認できるように、ローカルのファイルパスをコールバックする
    // 運用ではファイルパスを表示することは避けましょう
    String path = opt.orElseGet(() -> "ファイル書き込みNG");
    return reply(path);
  }

  // MessageContentResponseの中のバイト入力ストリームを、拡張子を指定してファイルに書き込む。
  // また、保存先のファイルパスをOptional型で返す。
  private Optional<String> makeTmpFile(MessageContentResponse resp, String extension) {
    // tmpディレクトリに一時的に格納して、ファイルパスを返す
    try (InputStream is = resp.getStream()) {
      Path tmpFilePath = Files.createTempFile("linebot", extension);
      Files.copy(is, tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
      return Optional.ofNullable(tmpFilePath.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  // PostBackEventに対応する
  @EventMapping
  public Message handlePostBack(PostbackEvent event) {
    String actionLabel = event.getPostbackContent().getData();
    switch (actionLabel) {
      case "CY":
        return reply("イイね！");
      case "CN":
        return reply("つらたん");
      case "DT":
        return reply(event.getPostbackContent().getParams().toString());
      default:
        return reply("?");
    }
  }
    return Optional.empty();
}


  // BeaconEventに対応する
  @EventMapping
  public Message handleBeacon(BeaconEvent event) {
    // Beaconイベントの内容を文字列に変換する
    String eventStr = event.getBeacon().toString();
    // eventStr をBotで返信する
    return reply(eventStr);
  }

  private FlexMessage replyBubble() {
    Text hello = Text.builder()
      .text("Hello")
      .build();

    Text world = Text.builder()
      .text("world")
      .weight(Text.TextWeight.BOLD)
      .size(FlexFontSize.XL)
      .align(FlexAlign.CENTER)
      .color("#FF0000")
      .build();

    Separator separator = Separator.builder().build();

    Box box = Box.builder()
      .layout(FlexLayout.HORIZONTAL)
      .contents(Arrays.asList(hello, separator, world))
      .build();

    Bubble bubble = Bubble.builder()
      .body(box)
      .build();

    return new FlexMessage("BubbleSample", bubble);
  }

  private FlexMessage replyCarousel() {
    Text currentTitle = Text.builder()
      .text("今日のイベントはこちら")
      .build();

    Box currentHeader = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(currentTitle))
      .build();

    Image currentImage = Image.builder()
      .url("https://connpass-tokyo.s3.amazonaws.com/thumbs/3e/b8/3eb8be3f66515598c47c76bd65e3ebb2.png")
      .size(Image.ImageSize.FULL_WIDTH)
      .aspectMode(Image.ImageAspectMode.Fit)
      .build();

    Text currentText = Text.builder()
      .text("LINE Messaging API for Java でLINE Botを作ってみませんか？\n" +
        "エントリーを考えている方・考えていない方、社会人、学生の皆さん、誰でも大歓迎です！")
      .wrap(true)
      .build();

    Button currentBtn = Button.builder()
      .style(Button.ButtonStyle.SECONDARY)
      .action(new URIAction("表示",
        "https://javado.connpass.com/event/97107/",
        new AltUri(URI.create("https://javado.connpass.com/event/97107/"))))
      .build();

    Box currentBody = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(currentText, currentBtn))
      .build();

    Bubble currentBbl = Bubble.builder()
      .header(currentHeader)
      .hero(currentImage)
      .body(currentBody)
      .build();

    Text nextTitle = Text.builder()
      .text("次回のイベントはこちら")
      .build();

    Box nextHeader = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(nextTitle))
      .build();

    Image nextImage = Image.builder()
      .url("https://connpass-tokyo.s3.amazonaws.com/thumbs/9a/82/9a82ae80521b1f119cc6ed1e3e5edac0.png")
      .size(Image.ImageSize.FULL_WIDTH)
      .aspectMode(Image.ImageAspectMode.Fit)
      .build();

    Text nextText = Text.builder()
      .text("待ちに待ったスキルの開発環境・CEK(Clova Extension Kit)がお目見えしました!!\n" +
        "Clovaスキルを作ってみたい！Clovaと触れ合いたい！とお考えの皆さんのためにCEKのハンズオンを行います。")
      .wrap(true)
      .build();

    Button nextBtn = Button.builder()
      .style(Button.ButtonStyle.PRIMARY)
      .action(new URIAction("申し込み",
        "https://linedev.connpass.com/event/96793/",
        new AltUri(URI.create("https://linedev.connpass.com/event/96793/"))))
      .build();

    Box nextBody = Box.builder()
      .layout(FlexLayout.VERTICAL)
      .contents(Arrays.asList(nextText, nextBtn))
      .build();

    Bubble nextBbl = Bubble.builder()
      .header(nextHeader)
      .hero(nextImage)
      .body(nextBody)
      .build();

    Carousel carousel = Carousel.builder()
      .contents(Arrays.asList(currentBbl, nextBbl))
      .build();

    return new FlexMessage("カルーセル", carousel);
  }

  public Message get() {
    var items = Arrays.asList(
      QuickReplyItem.builder()
        .action(new MessageAction("メッセージ", "メッセージ"))
        .build(),
      QuickReplyItem.builder()
        .action(CameraAction.withLabel("カメラ"))
        .build(),
      QuickReplyItem.builder()
        .action(CameraRollAction.withLabel("カメラロール"))
        .build(),
      QuickReplyItem.builder()
        .action(LocationAction.withLabel("位置情報"))
        .build(),
      QuickReplyItem.builder()
        .action(PostbackAction.builder()
          .label("PostbackAction")
          .text("PostbackAction clicked")
          .data("{PostbackAction: true}")
          .build())
        .build()
    );

    var quickReply = QuickReply.items(items);

    return TextMessage
      .builder()
      .text("Message with QuickReply")
      .quickReply(quickReply)
      .build();
  }


}
