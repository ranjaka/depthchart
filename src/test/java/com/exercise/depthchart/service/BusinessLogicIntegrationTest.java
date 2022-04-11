package com.exercise.depthchart.service;

import com.exercise.depthchart.exceptions.BadRequestException;
import com.exercise.depthchart.repository.dto.CreatePlayerDTO;
import com.exercise.depthchart.repository.dto.DeletePlayerDTO;
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
class BusinessLogicIntegrationTest {

  @Autowired BusinessLogic businessLogic;

  @MockBean CommandLineRunner commandLineRunner;

  @Order(1)
  @Test
  @DisplayName("given new sport data create in db and return created sport data")
  void testCreateSport_1() throws JsonProcessingException {

    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Throwable thrown =
        Assertions.catchThrowable(
            () -> {
              var response = businessLogic.createSport(sportDTO);
              Assertions.assertThat(response.getName()).isEqualTo(sportDTO.getName());
              Assertions.assertThat(response.getPositions())
                  .containsExactlyElementsOf(nflSupportedPositions);
            });

    Assertions.assertThat(thrown).isNull();
  }

  @Order(2)
  @Test
  @DisplayName("given duplicate sport throw bad request exception")
  void testCreateSport_2() {
    List<String> nflSupportedPositions = List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "GG");
    SportDTO sportDTO = SportDTO.builder().name("NFL").positions(nflSupportedPositions).build();

    Assertions.assertThatThrownBy(
            () -> {
              businessLogic.createSport(sportDTO);
            })
        .isInstanceOf(BadRequestException.class)
        .hasMessage("Sport already exists!");
  }

  @Order(3)
  @Test
  @DisplayName("given sport exists when adding players return player data")
  void testAddPlayerToDepthChart_1() throws JsonProcessingException {
    CreatePlayerDTO bob = CreatePlayerDTO.builder().name("bob").position("WR").depth(0).build();

    var responseBob =
        businessLogic.addPlayerToDepthChart(bob.getName(), bob.getPosition(), bob.getDepth());

    Assertions.assertThat(responseBob.getName()).isEqualTo(bob.getName());
    Assertions.assertThat(responseBob.getId()).isNotNull();
  }

  @Order(4)
  @Test
  @DisplayName("given player existing when adding duplicate throw bad request exception")
  void testAddPlayerToDepthChart_2() throws JsonProcessingException {
    CreatePlayerDTO bob = CreatePlayerDTO.builder().name("bob").position("WR").depth(0).build();

    Assertions.assertThatThrownBy(
            () -> {
              var responseBob =
                  businessLogic.addPlayerToDepthChart(
                      bob.getName(), bob.getPosition(), bob.getDepth());
            })
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Duplicate request");

    var depthChart = businessLogic.getFullDepthChart();

    var positionData =
        depthChart.stream().filter(x -> x.getName().equals(bob.getPosition())).findFirst();
    Assertions.assertThat(positionData).isPresent();

    Assertions.assertThat(positionData.get().getPlayerIds()).hasSize(1);
  }

  @Order(5)
  @Test
  @DisplayName("given existing player when adding new player to the same depth update depth")
  void testAddPlayerToDepthChart_3() {
    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("alice").position("WR").depth(0).build();

    var responseAliceUpdated =
        businessLogic.addPlayerToDepthChart(alice.getName(), alice.getPosition(), alice.getDepth());

    var depthChart = businessLogic.getFullDepthChart();

    var positionData =
        depthChart.stream().filter(x -> x.getName().equals(alice.getPosition())).findFirst();
    Assertions.assertThat(positionData).isPresent();

    Assertions.assertThat(positionData.get().getPlayerIds()).hasSize(2);
    Assertions.assertThat(positionData.get().getPlayerIds().get(0))
        .isEqualTo(responseAliceUpdated.getId());
  }

  @Test
  @Order(6)
  @DisplayName(
      "given non existing player when attempted to remove do nothing and return current position")
  void removePlayerFromDepthChart_1() {

    DeletePlayerDTO nonExistent = DeletePlayerDTO.builder().name("nE").position("WR").build();

    var positionData =
        businessLogic.removePlayerFromDepthChart(nonExistent.getName(), nonExistent.getPosition());
    Assertions.assertThat(positionData.getPlayerIds()).hasSize(2);
  }

  @Test
  @Order(7)
  @DisplayName("given position does not exist when remove player throw bad request exception")
  void removePlayerFromDepthChart_2() {

    DeletePlayerDTO nonExistent = DeletePlayerDTO.builder().name("bob").position("blahh").build();

    Assertions.assertThatThrownBy(
            () -> {
              businessLogic.removePlayerFromDepthChart(
                  nonExistent.getName(), nonExistent.getPosition());
            })
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Position data with name [blahh] does not " + "exist");
  }

  @Test
  @Order(8)
  @DisplayName("given existing player when remove request remove player")
  void removePlayerFromDepthChartTest_1() throws JsonProcessingException {
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("charlie").position("WR").depth(2).build();

    var dataOfCharlie =
        businessLogic.addPlayerToDepthChart(
            charlie.getName(), charlie.getPosition(), charlie.getDepth());

    var depthChart = businessLogic.getFullDepthChart();
    var posWrData =
        depthChart.stream().filter(x -> x.getName().equals(charlie.getPosition())).findFirst();

    Assertions.assertThat(posWrData.get().getPlayerIds()).contains(dataOfCharlie.getId());

    var positionData =
        businessLogic.removePlayerFromDepthChart(charlie.getName(), charlie.getPosition());

    Assertions.assertThat(positionData.getPlayerIds()).doesNotContain(dataOfCharlie.getId());
  }

  @Test
  @Order(9)
  @DisplayName("given existing player when remove request remove player")
  void testGetPlayersUnderPlayer_1() {
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("charlie").position("WR").depth(2).build();
    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("alice").position("WR").depth(0).build();

    DeletePlayerDTO deleteCharlie =
        DeletePlayerDTO.builder().name("charlie").position("WR").build();

    var dataOfCharlie =
        businessLogic.addPlayerToDepthChart(
            charlie.getName(), charlie.getPosition(), charlie.getDepth());

    var depthChart = businessLogic.getFullDepthChart();
    var posWrData =
        depthChart.stream().filter(x -> x.getName().equals(charlie.getPosition())).findFirst();

    Assertions.assertThat(posWrData.get().getPlayerIds()).contains(dataOfCharlie.getId());

    var playerData =
        businessLogic.getPlayersUnderPlayerInDepthChart(alice.getName(), alice.getPosition());

    Assertions.assertThat(playerData.size()).isEqualTo(2);
    Assertions.assertThat(playerData.get(1).getId()).isEqualTo(dataOfCharlie.getId());
  }
}
