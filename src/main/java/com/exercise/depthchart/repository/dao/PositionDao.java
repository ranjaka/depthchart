package com.exercise.depthchart.repository.dao;

import com.exercise.depthchart.model.Position;
import com.exercise.depthchart.repository.PositionRepository;
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
