package com.exercise.depthchart.repository;

import com.exercise.depthchart.model.Position;
import com.exercise.depthchart.repository.dao.PlayerDao;
import com.exercise.depthchart.repository.dao.SportDao;
import com.exercise.depthchart.repository.dto.SportDTO;
import com.exercise.depthchart.repository.mapper.EntityDTOMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(value = "spring.main.lazy-initialization=true")
class SportDaoTest {

  @Autowired PlayerDao playerDao;

  @Autowired SportDao sportDao;

  @Autowired EntityDTOMapper entityDTOMapper;

  @MockBean CommandLineRunner commandLineRunner;

  @Test
  @DisplayName("when valid sport data generate sport data in db")
  void testSportData() throws IOException {

    generateReturnSportData();
  }

  private void generateReturnSportData() {

    SportDTO nfl =
        SportDTO.builder()
            .name("NFL")
            .positions(List.of("QB", "WR", "RB", "TE", "K", "P", "KR", "PR"))
            .build();

    var createSport = entityDTOMapper.sportDTOToEntity(nfl);

    List<Position> positions = new ArrayList<>();
    nfl.getPositions()
        .forEach(
            pn -> {
              var position = Position.builder().name(pn).sport(createSport).build();
              positions.add(position);
            });
    createSport.setPositions(positions);

    var sport = sportDao.saveSport(createSport);

    Assertions.assertThat(sport).isNotNull();
    Assertions.assertThat(sport.getName()).isEqualTo(nfl.getName());
    Assertions.assertThat(sport.getPositions()).hasSameSizeAs(nfl.getPositions());
  }
}
