package com.exercise.depthchart;

import com.exercise.depthchart.repository.dto.CreatePlayerDTO;
import com.exercise.depthchart.repository.dto.SportDTO;
import com.exercise.depthchart.service.BusinessLogic;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

  ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Startup config to test you logic for the timebeing until a controller or other user input means
   * are added to this app
   *
   * @param businessLogic
   * @return
   */
  @Bean
  public CommandLineRunner setDbModelData(BusinessLogic businessLogic) {

    // --- create sports to add to db at runtime ---
    SportDTO nfl =
        SportDTO.builder()
            .name("NFL")
            .positions(List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR"))
            .build();

    SportDTO mlb =
        SportDTO.builder()
            .name("MLB")
            .positions(List.of("SP", "RP", "C", "1B", "2B", "3B", "SS", "LF", "RF", "CF", "DH"))
            .build();

    List<SportDTO> listOfSports = List.of(nfl, mlb);

    // --- create players to add to db at runtime ---
    CreatePlayerDTO bob = CreatePlayerDTO.builder().name("Bob").position("WR").depth(0).build();
    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("Alice").position("WR").depth(0).build();
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("Charlie").position("WR").depth(2).build();
    CreatePlayerDTO bob2 =
        CreatePlayerDTO.builder().name("Charlie").position("WR").depth(0).build();

    CreatePlayerDTO playersUnderPlayerCandidate = alice;

    List<CreatePlayerDTO> listOfPlayers = List.of(bob, alice, charlie, bob2);

    return args -> {
      try {

        listOfSports.forEach(businessLogic::createSport);
        listOfPlayers.forEach(
            createPlayerDTO -> {
              businessLogic.addPlayerToDepthChart(
                  createPlayerDTO.getName(),
                  createPlayerDTO.getPosition(),
                  createPlayerDTO.getDepth());
            });

        var depthChart = businessLogic.getFullDepthChart();
        var playerUnderPlayer =
            businessLogic.getPlayersUnderPlayerInDepthChart(
                playersUnderPlayerCandidate.getName(), playersUnderPlayerCandidate.getPosition());

        System.out.println("Depth Chart: " + objectMapper.writeValueAsString(depthChart));

        System.out.println(
            String.format(
                "Players under player [%s]: %s",
                playersUnderPlayerCandidate.getName(),
                objectMapper.writeValueAsString(playerUnderPlayer)));

        //                businessLogic.removePlayerFromDepthChart(alice.getName(),
        // alice.getPosition());

      } catch (Exception e) {
        System.err.println("Application error: " + e);
      }
    };
  }
}
