package com.sportsbet.depthchart.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Sport {

  @Id
  @Column(name = "sport_name", unique = true)
  private String name;

  @OneToMany(targetEntity = Position.class, mappedBy = "sport", fetch = FetchType.EAGER)
  @JsonManagedReference
  private List<Position> positions = new ArrayList<>();
}
