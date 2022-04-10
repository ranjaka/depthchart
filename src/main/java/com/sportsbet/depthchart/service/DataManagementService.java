package com.sportsbet.depthchart.service;

import com.sportsbet.depthchart.exceptions.BadRequestException;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dao.PlayerDao;
import com.sportsbet.depthchart.repository.dao.PositionDao;
import com.sportsbet.depthchart.repository.dao.SportDao;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.PlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import com.sportsbet.depthchart.repository.mapper.EntityDTOMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataManagementService {

  @Autowired SportDao sportDao;

  @Autowired PlayerDao playerDao;

  @Autowired PositionDao positionDao;

  @Autowired EntityDTOMapper entityDTOMapper;

  public SportDTO createSport(SportDTO sportDTO) {
    // check whether the sport already exists in db
    Sport sportToCreate = entityDTOMapper.sportDTOToEntity(sportDTO);
    var checkExisting = sportDao.getSportsByName(sportToCreate.getName());
    if (!checkExisting.isEmpty()) {
      throw new BadRequestException("Sport already exists!");
    }
    List<String> positionsNames = sportDTO.getPositions();
    List<Position> positions = new ArrayList<>();
    positionsNames.forEach(
        pn -> {
          var position = Position.builder().name(pn).sport(sportToCreate).build();
          positions.add(position);
        });
    sportToCreate.setPositions(positions);
    var sport = sportDao.saveSport(sportToCreate);
    return entityDTOMapper.sportToDTO(sport);
  }

  public PlayerDTO addPlayerToDepthChart(CreatePlayerDTO createPlayerDTO) {

    var playerToCreate = entityDTOMapper.createPlayerToEntity(createPlayerDTO);
    var positionName = createPlayerDTO.getPosition();

    var position = positionDao.getPositionByName(positionName);
    playerToCreate.setPosition(position.get());

    var playerId = playerDao.savePlayer(playerToCreate);

    var player = playerDao.getPlayerById(playerId);

    return entityDTOMapper.playerToDTO(player.get());
  }

  public void removePlayerFromDepthChart(CreatePlayerDTO createPlayerDTO) {}
}
