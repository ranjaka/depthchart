package com.exercise.depthchart.repository.dao;

import com.exercise.depthchart.model.Position;
import com.exercise.depthchart.model.Sport;
import com.exercise.depthchart.repository.PositionRepository;
import com.exercise.depthchart.repository.SportRepository;
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

    return sportWithId;
  }

  public Sport getSportById(Integer id) {
    return sportRepository.getById(id);
  }

  public List<Sport> getSportsByName(String name) {
    return sportRepository.findByName(name);
  }
}
