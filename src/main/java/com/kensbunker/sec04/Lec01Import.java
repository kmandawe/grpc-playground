package com.kensbunker.sec04;

import com.kensbunker.models.common.Address;
import com.kensbunker.models.common.BodyStyle;
import com.kensbunker.models.common.Car;
import com.kensbunker.models.sec04.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Import {
  private static final Logger LOG = LoggerFactory.getLogger(Lec01Import.class);

  public static void main(String[] args) {
    var address = Address.newBuilder().setCity("atlanta").build();
    var car = Car.newBuilder().setBodyStyle(BodyStyle.COUPE).build();
    var person =
        Person.newBuilder().setName("sam").setAge(12).setAddress(address).setCar(car).build();
    LOG.info("has age? {}", person.hasAge());
    LOG.info("person: {}", person);
  }
}
