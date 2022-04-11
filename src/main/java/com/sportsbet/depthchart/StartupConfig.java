package com.sportsbet.depthchart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import com.sportsbet.depthchart.service.BusinessLogic;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

  ObjectMapper objectMapper = new ObjectMapper();

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
    CreatePlayerDTO bob2 = CreatePlayerDTO.builder().name("Bob").position("KR").build();

    List<CreatePlayerDTO> listOfPlayers = List.of(bob, alice, charlie, bob2);

    return args -> {
      listOfSports.forEach(businessLogic::createSport);
      listOfPlayers.forEach(
          createPlayerDTO -> {
            businessLogic.addPlayerToDepthChart(
                createPlayerDTO.getName(),
                createPlayerDTO.getPosition(),
                createPlayerDTO.getDepth());
          });

      var depthChart = businessLogic.getFullDepthChart();
      //      businessLogic.removePlayerFromDepthChart(bob.getName(), bob.getPosition());
      var playerUnderPlayer =
          businessLogic.getPlayersUnderPlayerInDepthChart(alice.getName(), alice.getPosition());

      System.out.println("PUP: " + objectMapper.writeValueAsString(playerUnderPlayer));
    };
  }
}
