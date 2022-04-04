package com.bootcamp.currentaccount.models.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Holder {

  @NotNull
  @NotBlank
  private String clientName;

  @NotNull
  @NotBlank
  private String dni;

  private String debitCard;
}
