package com.sportsbet.depthchart.repository;

import com.sportsbet.depthchart.TestUtils;
import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dao.PlayerDao;
import com.sportsbet.depthchart.repository.dao.PositionDao;
import com.sportsbet.depthchart.repository.dao.SportDao;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(value = "spring.main.lazy-initialization=true")
class SportRepositoryTest {

  @Autowired PlayerDao playerDao;

  @Autowired SportDao sportDao;

  @Autowired PositionDao positionDao;

  @MockBean CommandLineRunner commandLineRunner;

  @Autowired SportRepository sportRepository;

  @Autowired PositionRepository positionRepository;

  @Autowired PlayerRepository playerRepository;

  @Test
  void testSportData() throws IOException {

    var sport =
        TestUtils.loadFromFile("src/test/resources/test-data/sample_sport.json", Sport.class);

    generateReturnSportData(sport);
  }

  private void generateReturnSportData(Sport sportData) {

    sportDao.saveSport(sportData);
    List<Position> positions = sportData.getPositions();
    positions.forEach(
        position -> {
          positionDao.savePosition(position);
        });

    positions.forEach(
        position -> {
          List<Player> players = position.getPlayers();
          players.forEach(player -> playerDao.savePlayer(player));
        });
  }
}
