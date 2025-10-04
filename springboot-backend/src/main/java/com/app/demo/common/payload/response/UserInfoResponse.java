package com.app.demo.common.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoResponse {
  private String id;
  private String username;
  private String email;
  private String role;
  private boolean isVerified;

}
