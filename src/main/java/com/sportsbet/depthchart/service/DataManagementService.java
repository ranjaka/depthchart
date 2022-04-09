package com.sportsbet.depthchart.service;

import com.sportsbet.depthchart.exceptions.ApplicationException;
import com.sportsbet.depthchart.exceptions.BadRequestException;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dao.PlayerDao;
import com.sportsbet.depthchart.repository.dao.PositionDao;
import com.sportsbet.depthchart.repository.dao.SportDao;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import com.sportsbet.depthchart.repository.mapper.EntityDTOMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataManagementService {

  @Autowired SportDao sportDao;

  @Autowired PositionDao positionDao;

  @Autowired PlayerDao playerDao;

  @Autowired EntityDTOMapper entityDTOMapper;

  public void createSport(SportDTO sportDTO) {

    // check whether the sport already exists in db
    Sport sportToCreate = entityDTOMapper.sportToEntity(sportDTO);
    var existingSportData = sportDao.getSportByName(sportToCreate.getName());
    if (existingSportData.isEmpty()) {
      sportDao.saveSport(sportToCreate);
      List<String> positionsNames = sportDTO.getPositions();
      positionsNames.forEach(
          pn -> {
            var position = Position.builder().name(pn).sport(sportToCreate).build();
            positionDao.savePosition(position);
          });

    } else {
      throw new BadRequestException("Sport already exists!");
    }
  }

  public Position addPlayerToDepthChart(CreatePlayerDTO createPlayerDTO) {

    var playerToCreate = entityDTOMapper.createPlayerToEntity(createPlayerDTO);

    var existingPosition = positionDao.getPositionByName(createPlayerDTO.getPosition());

    if (existingPosition.isEmpty()) {
      // position is not created in db so cannot create player profile
      throw new BadRequestException(
          String.format(
              "Position [%s] does not exist. Unable to create player profile",
              createPlayerDTO.getName()));
    }

    // create player
    existingPosition.get().setPlayers(new ArrayList<>());
    positionDao.savePosition(existingPosition.get());

    playerToCreate.setPosition(existingPosition.get());
    var createdPlayer = playerDao.savePlayer(playerToCreate);

    var depthChart = positionDao.getPositionByName(createPlayerDTO.getPosition());

    if (depthChart.isEmpty()) {
      throw new ApplicationException("issue");
    }

    return depthChart.get();
  }
}
