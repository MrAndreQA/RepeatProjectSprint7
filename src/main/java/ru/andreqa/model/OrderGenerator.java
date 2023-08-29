package ru.andreqa.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Random;

public class OrderGenerator {
    public static Order getRandom() {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String metroStation = RandomStringUtils.randomAlphabetic(10);
        String phone = RandomStringUtils.randomAlphabetic(10);
        int rentTime = Integer.parseInt(RandomStringUtils.randomNumeric(1));
        String deliveryDate = "2020-06-06";
        String comment = RandomStringUtils.randomAlphabetic(10);
        ArrayList color = null;
        return new Order (firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
