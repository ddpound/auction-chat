package com.example.auctionchat.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int seller;

    // buyer 가 예약어인거 같다
    private int buyer;

    private int quantity;

    @CreationTimestamp
    private Timestamp createDate;

}
