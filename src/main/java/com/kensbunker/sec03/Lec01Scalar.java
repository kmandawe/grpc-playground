package com.kensbunker.sec03;

import com.kensbunker.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Scalar {
  private static final Logger LOG = LoggerFactory.getLogger(Lec01Scalar.class);

  public static void main(String[] args) {
    var person =
        Person.newBuilder()
            .setLastName("sam")
//            .setAge(12)
//            .setEmail("sam@gmail.com")
//            .setEmployed(true)
//            .setSalary(1000.2345)
//            //.setBankAccountNumber(123456789012L)
//            .setBalance(-10000)
            .build();

    LOG.info("{}", person);
  }
}
