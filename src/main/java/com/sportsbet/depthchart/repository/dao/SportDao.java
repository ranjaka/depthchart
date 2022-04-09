package com.sportsbet.depthchart.repository.dao;

import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SportDao {

  @Autowired SportRepository sportRepository;

  public void saveSport(Sport sport) {
    sportRepository.saveAndFlush(sport);
  }

  public Sport getSportByName(String name) throws Exception {
    var sport = sportRepository.findByName(name);

    if (sport.isEmpty()) {
      throw new Exception("Sport not found for given name");
    } else return sport.get();
  }
}
