package com.everis.mscurrentaccount.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubType {
    String id;

    EnumSubType value;

    public enum EnumSubType{
        NORMAL, VIP, PYME
    }
}
