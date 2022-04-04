package com.bootcamp.currentaccount.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Signatory {

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotBlank
  private String dni;

  private String phone;
}
