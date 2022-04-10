package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.PositionRepository;
import com.sportsbet.depthchart.repository.SportRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SportDao {

  @Autowired SportRepository sportRepository;

  @Autowired PositionRepository positionRepository;

  public Sport saveSport(Sport sport) {

    var positions = sport.getPositions();
    sport.setPositions(new ArrayList<>());
    var sportWithId = sportRepository.saveAndFlush(sport);

    for (Position position : positions) {
      sport.getPositions().add(positionRepository.saveAndFlush(position));
    }

    return sport;
  }

  public Sport getSportById(Integer id) {
    return sportRepository.getById(id);
  }

  public List<Sport> getSportsByName(String name) {
    return sportRepository.findByName(name);
  }
}
