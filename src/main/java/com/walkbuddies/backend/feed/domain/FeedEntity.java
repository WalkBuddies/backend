package com.walkbuddies.backend.feed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "feed")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedEntity {


}
