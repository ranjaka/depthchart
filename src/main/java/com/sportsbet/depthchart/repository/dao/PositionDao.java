package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.repository.PositionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionDao {

  @Autowired PositionRepository positionRepository;

  public Position savePosition(Position position) {

    return positionRepository.saveAndFlush(position);
  }

  public Optional<Position> getPositionByName(String name) {

    return positionRepository.findByName(name);
  }

  public List<Position> getAllPositionData() {
    return positionRepository.findAll();
  }
}
