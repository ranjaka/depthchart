package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionDao {

  @Autowired PositionRepository positionRepository;

  public void savePosition(Position position) {
    positionRepository.saveAndFlush(position);
  }

  public Position getPositionByName(String name) throws Exception {

    var position = positionRepository.findByName(name);

    if (position.isEmpty()) {
      throw new Exception("Position not found for the given name");
    } else return position.get();
  }
}
