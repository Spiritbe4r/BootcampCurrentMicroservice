package com.bootcamp.currentaccount.models.bean;


import com.bootcamp.currentaccount.models.dto.ClientDTO;
import com.bootcamp.currentaccount.models.dto.Holder;
import com.bootcamp.currentaccount.models.dto.Signatory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(value = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

  @Id
  private String id;

  @Indexed(unique = true)
  private String accountNumber;

  private String typeOfAccount;

  private double amount;

  private String currency;

  private ClientDTO client;

  private int movementPerMonth;

  private int maxLimitMovementPerMonth;

  private String clientIdNumber;


  private List<Holder> holders= new ArrayList<>();

  private List<Signatory> signatory= new ArrayList<>();

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateOperation = LocalDateTime.now();

}
