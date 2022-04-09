package com.sportsbet.depthchart.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerRepositoryTest {

  static ObjectMapper objectMapper = new ObjectMapper();
  @Autowired PlayerRepository playerRepository;
  @Autowired PositionRepository positionRepository;
  @Autowired SportRepository sportRepository;

  @Test
  void addPlayer_1() throws JsonProcessingException {

    Sport nfl = Sport.builder().name("NFL").build();

    Position wb = Position.builder().name("WB").sport(nfl).build();

    Player bob = new Player(1, "Bob", wb);

    sportRepository.saveAndFlush(nfl);

    positionRepository.saveAndFlush(wb);

    playerRepository.saveAndFlush(bob);

    var result = playerRepository.findById(bob.getId());

    System.out.println("player data: " + objectMapper.writeValueAsString(result.get()));
  }
}
