package com.bootcamp.currentaccount.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

  private String name;

  private String clientIdType;

  private String clientIdNumber;

  private ClientType clientType;
}