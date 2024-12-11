package com.sici.chat.model.chat.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: qiyu-live-app
 * @package: com.sici.chat.model.chat.message.dto
 * @author: 20148
 * @description:
 * @create-date: 11/27/2024 6:18 PM
 * @version: 1.0
 */

@Data
@AllArgsConstructor
public class TextMessageDto extends MessageDto{
    private String content;
}
