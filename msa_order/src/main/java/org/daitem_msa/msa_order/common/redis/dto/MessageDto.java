package org.daitem_msa.msa_order.common.redis.dto;

import lombok.Data;

@Data
public class MessageDto {
    private static final long serializeId = 1L;

    private String message;
    private String sender; //메세지 발신자
    private String recipient; //메세지 수신자

}
