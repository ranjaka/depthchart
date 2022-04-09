package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.repository.PositionRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionDao {

  @Autowired PositionRepository positionRepository;

  public void savePosition(Position position) {

    Position pos =
        Position.builder()
            .name(position.getName())
            .sport(position.getSport())
            .players(position.getPlayers())
            .build();

    positionRepository.saveAndFlush(pos);
  }

  public Optional<Position> getPositionByName(String name) {

    return positionRepository.findByName(name);
  }
}
