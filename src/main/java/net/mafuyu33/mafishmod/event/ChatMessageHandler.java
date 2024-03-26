package net.mafuyu33.mafishmod.event;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.ClientChatEvent;


public class ChatMessageHandler {
    private static int number = 999999999;

    public ChatMessageHandler(int number) {
        ChatMessageHandler.number = number;
    }
    public static void register() {
        // 注册客户端接收聊天消息事件监听器
        ClientChatEvent.RECEIVED.register((parameters, message) -> {


            // 在这里可以处理接收到的聊天消息, 获取“<玩家名称> 玩家输入的文字” 后面的文字
            String string = message.getString();
            int index = string.indexOf('>');
            String textAfterArrow = "";
            if (index != -1 && index + 1 < string.length()) {
                textAfterArrow = string.substring(index + 1).trim();
            }


            // 尝试将文本内容转换为整数
            try {
                number = Integer.parseInt(textAfterArrow);
                System.out.println("转换后的数字为: " + number);
            } catch (NumberFormatException e) {
                System.out.println("文本内容不是一个有效的数字。");
            }

            // 注意：这里有两个参数，但我们将忽略第一个参数，因为它是消息类型参数

            // 返回 CompoundEventResult 来保持返回类型一致
            return CompoundEventResult.pass();
        });
    }

    public static int getNumber() {
        return number;
    }
}
