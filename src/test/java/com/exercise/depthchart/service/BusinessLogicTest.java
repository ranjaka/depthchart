package com.exercise.depthchart.service;

import com.exercise.depthchart.exceptions.BadRequestException;
import com.exercise.depthchart.repository.dto.CreatePlayerDTO;
import com.exercise.depthchart.repository.dto.SportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(value = "spring.main.lazy-initialization=true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusinessLogicTest {

  @Autowired BusinessLogic businessLogic;

  @MockBean CommandLineRunner commandLineRunner;

  @Test
  @Order(1)
  @DisplayName("when sport is new add sport to db then return new sport data")
  void createSport_1() throws JsonProcessingException {

    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Throwable thrown =
        Assertions.catchThrowable(
            () -> {
              businessLogic.createSport(sportDTO);
            });

    Assertions.assertThat(thrown).isNull();
  }

  @Test
  @Order(2)
  @DisplayName("when creating duplicate sport throw bad request exception")
  void createSport_2() {
    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "GG");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Assertions.assertThatThrownBy(
            () -> {
              businessLogic.createSport(sportDTO);
            })
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Sport already exists!");
  }

  @Test
  @Order(3)
  @DisplayName("given sport created when new player data create new player")
  void addPlayerToDepthChartTest_1() throws JsonProcessingException {
    CreatePlayerDTO bob = CreatePlayerDTO.builder().name("bob").position("WR").depth(0).build();
    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("alice").position("WR").build();
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("charlie").position("WR").depth(2).build();
    CreatePlayerDTO bob2 = CreatePlayerDTO.builder().name("Bob").position("KR").build();

    var out1 =
        businessLogic.addPlayerToDepthChart(bob.getName(), bob.getPosition(), bob.getDepth());
    var out2 =
        businessLogic.addPlayerToDepthChart(alice.getName(), alice.getPosition(), alice.getDepth());
    var out3 =
        businessLogic.addPlayerToDepthChart(
            charlie.getName(), charlie.getPosition(), charlie.getDepth());
    businessLogic.addPlayerToDepthChart(bob2.getName(), bob2.getPosition(), bob2.getDepth());

    Assertions.assertThat(out3.getPlayerIds().size()).isEqualTo(3);
  }

  @Test
  @Order(4)
  @DisplayName("given valid player details return players under player")
  void getPlayersUnderPlayerTest_1() throws JsonProcessingException {

    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("alice").position("WR").build();

    CreatePlayerDTO bob = CreatePlayerDTO.builder().name("bob").position("WR").depth(0).build();

    var out1 =
        businessLogic.getPlayersUnderPlayerInDepthChart(alice.getName(), alice.getPosition());

    Assertions.assertThat(out1.size()).isEqualTo(1);
  }

  @Test
  @Order(5)
  @DisplayName("given valid player details to remove proceed with remove")
  void removePlayerFromDepthChartTest_1() throws JsonProcessingException {
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("charlie").position("WR").depth(1).build();

    var out1 = businessLogic.removePlayerFromDepthChart(charlie.getName(), charlie.getPosition());

    System.out.println(out1);

    Assertions.assertThat(out1.getPlayerIds().size()).isEqualTo(2);
  }

  @Test
  @Order(6)
  @DisplayName("given non existing player details to remove proceed with remove")
  void removePlayerFromDepthChartTest_2() throws JsonProcessingException {
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("charlie").position("WR").depth(1).build();

    var out1 = businessLogic.removePlayerFromDepthChart(charlie.getName(), charlie.getPosition());

    System.out.println(out1);

    Assertions.assertThat(out1.getPlayerIds().size()).isEqualTo(2);
  }
}
