package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.repository.PositionRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionDao {

  @Autowired PositionRepository positionRepository;

  public Optional<Position> getPositionByName(String name) {

    return positionRepository.findByName(name);
  }
}
