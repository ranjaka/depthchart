package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.SportRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SportDao {

  @Autowired SportRepository sportRepository;

  public void saveSport(Sport sport) {
    Sport sp = Sport.builder().name(sport.getName()).build();
    sportRepository.saveAndFlush(sp);
  }

  public Optional<Sport> getSportByName(String name) {
    return sportRepository.findByName(name);
  }
}
