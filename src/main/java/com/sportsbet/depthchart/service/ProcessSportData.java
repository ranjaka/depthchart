package com.sportsbet.depthchart.service;

import com.sportsbet.depthchart.exceptions.BadRequestException;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dao.PositionDao;
import com.sportsbet.depthchart.repository.dao.SportDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessSportData {

  @Autowired SportDao sportDao;

  @Autowired PositionDao positionDao;

  public void setupNewSport(String nameOfSport, List<String> positionNames) {

    // check whether the sport already exists in db
    var existingSportData = sportDao.getSportByName(nameOfSport);
    if (existingSportData.isEmpty()) {
      var sport = Sport.builder().name(nameOfSport).build();
      sportDao.saveSport(sport);
      positionNames.forEach(
          pn -> {
            var position = Position.builder().name(pn).sport(sport).build();
            positionDao.savePosition(position);
          });

    } else {
      throw new BadRequestException("Sport already exists!");
    }
  }
}
