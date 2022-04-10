package com.sportsbet.depthchart;

import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import com.sportsbet.depthchart.service.DataManagementService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public CommandLineRunner setDbModelData(DataManagementService dataManagementService) {

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
    CreatePlayerDTO alice = CreatePlayerDTO.builder().name("Alice").position("WR").depth(1).build();
    CreatePlayerDTO charlie =
        CreatePlayerDTO.builder().name("Charlie").position("WR").depth(2).build();

    List<CreatePlayerDTO> listOfPlayers = List.of(bob, alice, charlie);

    return args -> {
      listOfSports.forEach(dataManagementService::createSport);
      listOfPlayers.forEach(dataManagementService::addPlayerToDepthChart);
    };
  }
}
