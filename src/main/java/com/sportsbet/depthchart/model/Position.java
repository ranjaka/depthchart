package com.sportsbet.depthchart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
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
public class Position {
  @Id
  @Column(name = "position_name", unique = true)
  private String name;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "sport_name")
  @JsonBackReference
  private Sport sport;

  @OneToMany(targetEntity = Player.class, mappedBy = "position", fetch = FetchType.EAGER)
  @JsonManagedReference
  private List<Player> players = new ArrayList<>();
}
