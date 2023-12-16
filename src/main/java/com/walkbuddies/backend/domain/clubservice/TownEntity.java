package com.walkbuddies.backend.domain.clubservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "town")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TownId;

    private String townName;
}
