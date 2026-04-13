package com.matheustorres.gympass.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_gyms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Gym {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
}
