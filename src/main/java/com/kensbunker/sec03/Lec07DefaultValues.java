package com.kensbunker.sec03;

import com.kensbunker.models.sec03.Address;
import com.kensbunker.models.sec03.Car;
import com.kensbunker.models.sec03.Dealer;
import com.kensbunker.models.sec03.Library;
import com.kensbunker.models.sec03.School;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec07DefaultValues {
  private static final Logger LOG = LoggerFactory.getLogger(Lec07DefaultValues.class);

  public static void main(String[] args){

    var school = School.newBuilder().build();
    LOG.info("{}", school.getId());
    LOG.info("{}", school.getName());
    LOG.info("{}", school.getAddress());
    LOG.info("{}", school.getAddress().getCity());

    LOG.info("is default?: {}", school.getAddress().equals(Address.getDefaultInstance()));

    // has
    LOG.info("has address? {}", school.hasAddress());

    // collection
    var lib = Library.newBuilder().build();
    LOG.info("books: {}", lib.getBooksList());

    // map
    var dealer = Dealer.newBuilder().build();
    LOG.info("inventory: {}", dealer.getInventoryMap());

    // enum
    var car = Car.newBuilder().build();
    LOG.info("body style: {}", car.getBodyStyle());
  }
}
