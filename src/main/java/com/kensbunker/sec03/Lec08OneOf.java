package com.kensbunker.sec03;

import com.kensbunker.models.sec03.Credentials;
import com.kensbunker.models.sec03.Email;
import com.kensbunker.models.sec03.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec08OneOf {
  private static final Logger LOG = LoggerFactory.getLogger(Lec08OneOf.class);

  public static void main(String[] args){
    var email = Email.newBuilder().setAddress("sam@gmail.com").setPassword("admin").build();
    var phone = Phone.newBuilder().setNumber(123456789).setCode(123).build();

//    login(Credentials.newBuilder().setEmail(email).build());
//    login(Credentials.newBuilder().setPhone(phone).build());
    login(Credentials.newBuilder().setEmail(email).setPhone(phone).build());
  }

  private static void login(Credentials credentials) {
    switch (credentials.getLoginTypeCase()) {
      case EMAIL -> LOG.info("email -> {}", credentials.getEmail());
      case PHONE -> LOG.info("phone -> {}", credentials.getPhone());
    }
  }
}
