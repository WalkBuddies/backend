package com.walkbuddies.backend.club.domain;

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
    private Long townId;

    private String townName;
}
