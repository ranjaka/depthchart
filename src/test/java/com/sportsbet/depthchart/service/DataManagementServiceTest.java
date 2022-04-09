package com.sportsbet.depthchart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsbet.depthchart.exceptions.BadRequestException;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(value = "spring.main.lazy-initialization=true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataManagementServiceTest {

  @Autowired DataManagementService dataManagementService;

  @MockBean CommandLineRunner commandLineRunner;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @Order(1)
  @DisplayName("when sport is new add sport to db then return new sport data")
  void createSport_1() throws JsonProcessingException {

    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Throwable thrown =
        Assertions.catchThrowable(
            () -> {
              dataManagementService.createSport(sportDTO);
            });

    Assertions.assertThat(thrown).isNull();
  }

  @Test
  @Order(2)
  @DisplayName("when duplicate sport then throw BadRequestException")
  void createSport_2() {
    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Assertions.assertThatThrownBy(
            () -> {
              dataManagementService.createSport(sportDTO);
            })
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Sport already exists!");
  }

  @Test
  @Order(3)
  @DisplayName("given sport created when new player data create new player")
  void createPlayer() throws JsonProcessingException {

    CreatePlayerDTO createPlayerDTO = CreatePlayerDTO.builder().name("bob").position("WR").build();

    var playerProfile = dataManagementService.createPlayer(createPlayerDTO);

    System.out.println("player: " + objectMapper.writeValueAsString(playerProfile));
  }
}