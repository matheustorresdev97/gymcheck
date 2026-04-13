package com.matheustorres.gympass.tests;

import com.matheustorres.gympass.domain.models.Gym;

import java.math.BigDecimal;
import java.util.UUID;

public class GymFactory {

    public static Gym createGym() {
        Gym gym = new Gym();
        gym.setId(UUID.randomUUID().toString());
        gym.setTitle("JS Academy");
        gym.setDescription("The best gym in town");
        gym.setPhone("12345678");
        gym.setLatitude(new BigDecimal("-27.2092052"));
        gym.setLongitude(new BigDecimal("-49.6401091"));
        return gym;
    }
}
