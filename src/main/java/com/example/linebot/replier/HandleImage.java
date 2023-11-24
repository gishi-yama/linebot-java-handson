package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class HandleImage implements Replier {
  @Override
  public Message reply() {
    return null;
  }

//  private MessageContentResponse resp;
//
//  public HandleImage(MessageContentResponse resp) {
//    this.resp = resp;
//  }
//
//  @Override
//  public Message reply() {
//    // MessageContentResponse からファイルをローカルに保存する
//    // ※LINEでは、どの解像度で写真を送っても、サーバ側でjpgファイルに変換される
//    String path = makeTmpFile(resp, ".jpg")
//      .orElse("ファイル書き込みNG");
//    return new TextMessage(path);
//  }
//
//  // MessageContentResponseの中のバイト入力ストリームを、拡張子を指定してファイルに書き込む。
//  // また、保存先のファイルパスをOptional型で返す。
//  private Optional<String> makeTmpFile(MessageContentResponse resp, String extension) {
//    // tmpディレクトリに一時的に格納して、ファイルパスを返す
//    try (InputStream is = resp.getStream()) {
//      Path tmpFilePath = Files.createTempFile("linebot", extension);
//      Files.copy(is, tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
//      return Optional.ofNullable(tmpFilePath.toString());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return Optional.empty();
//  }

}
