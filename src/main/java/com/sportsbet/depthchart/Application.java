package com.sportsbet.depthchart;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dao.PlayerDao;
import com.sportsbet.depthchart.repository.dao.PositionDao;
import com.sportsbet.depthchart.repository.dao.SportDao;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner addDemoData(
      SportDao sportDao, PositionDao positionDao, PlayerDao playerDao) {
    Sport nfl = Sport.builder().name("NFL").build();

    Position qb = Position.builder().name("QB").sport(nfl).build();
    Position wr = Position.builder().name("WR").sport(nfl).build();

    Player bob = Player.builder().id(1).name("bob").position(wr).build();
    Player alice = Player.builder().id(2).name("alice").position(wr).build();
    Player charlie = Player.builder().id(3).name("chalie").position(wr).build();

    List<Sport> sports = List.of(nfl);
    List<Position> positions = List.of(qb, wr);
    List<Player> players = List.of(bob, alice, charlie);

    return args -> {
      sports.forEach(sp -> sportDao.saveSport(nfl));
      positions.forEach(positionDao::savePosition);
      players.forEach(playerDao::savePlayer);
    };
  }
}
