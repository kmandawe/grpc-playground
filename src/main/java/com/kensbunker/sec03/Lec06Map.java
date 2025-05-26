package com.kensbunker.sec03;

import com.kensbunker.models.sec03.Car;
import com.kensbunker.models.sec03.Dealer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec06Map {

  private static final Logger LOG = LoggerFactory.getLogger(Lec06Map.class);

  public static void main(String[] args) {
    var car1 = Car.newBuilder().setMake("honda").setModel("civic").setYear(2000).build();
    var car2 = Car.newBuilder().setMake("honda").setModel("accord").setYear(2002).build();

    var dealer =
        Dealer.newBuilder()
            .putInventory(car1.getYear(), car1)
            .putInventory(car2.getYear(), car2)
            .build();

    LOG.info("dealer: {}", dealer);

    LOG.info("2002 ? : {}", dealer.containsInventory(2002));
    LOG.info("2003 ? : {}", dealer.containsInventory(2003));

    LOG.info("2002 model: {}", dealer.getInventoryOrThrow(2002).getModel());
  }
}
