0201

```java
package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;

public class HelloPython {

  String message;

  @JsonCreator
  public HelloPython(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
```

0301

```java
import com.example.linebot.value.HelloPython;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class PythonGreet implements Replier {

  private HelloPython helloPython;

  public PythonGreet(HelloPython helloPython) {
    this.helloPython = helloPython;
  }

  @Override
  public Message reply() {
    String body = String.format("Pythonプログラムから: %s", helloPython.getMessage());
    return new TextMessage(body);
  }
}
```

0403

```java
// ↓ExternalServiceについて追加
@Autowired
public Callback(ReminderService remainderService, 
                  ExternalService externalService) {
  this.remainderService = remainderService;
  this.externalService = externalService;
}
```