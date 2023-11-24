package com.example.linebot.replier;


import com.linecorp.bot.messaging.model.Message;

import java.util.Arrays;

public class BubbleSample implements Replier {
  @Override
  public Message reply() {
    return null;
  }

//  @Override
//  public Message reply() {
//    var hello = Text.builder()
//      .text("Hello")
//      .build();
//
//    var world = Text.builder()
//      .text("world")
//      .weight(Text.TextWeight.BOLD)
//      .size(FlexFontSize.XL)
//      .align(FlexAlign.CENTER)
//      .color("#FF0000")
//      .build();
//
//    var separator = Separator.builder().build();
//
//    var box = Box.builder()
//      .layout(FlexLayout.HORIZONTAL)
//      .contents(Arrays.asList(hello, separator, world))
//      .build();
//
//    var bubble = com.linecorp.bot.model.message.flex.container.Bubble.builder()
//      .body(box)
//      .build();
//
//    return new FlexMessage("BubbleSample", bubble);
//  }

}
