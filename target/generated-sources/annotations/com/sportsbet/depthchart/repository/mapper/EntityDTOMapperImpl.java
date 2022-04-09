package com.sportsbet.depthchart.repository.mapper;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.PlayerDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-09T17:37:36+1000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
)
@Component
public class EntityDTOMapperImpl extends EntityDTOMapper {

    @Override
    public PlayerDTO playerToDTO(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setId( player.getId() );
        playerDTO.setName( player.getName() );

        playerToDTOAfterMapping( player, playerDTO );

        return playerDTO;
    }

    @Override
    public Player playerToEntity(PlayerDTO playerDTO) {
        if ( playerDTO == null ) {
            return null;
        }

        Player player = new Player();

        player.setName( playerDTO.getName() );

        return player;
    }

    @Override
    public Player createPlayerToEntity(CreatePlayerDTO createPlayerDTO) {
        if ( createPlayerDTO == null ) {
            return null;
        }

        Player player = new Player();

        player.setName( createPlayerDTO.getName() );

        return player;
    }

    @Override
    public Sport sportToEntity(SportDTO sportDTO) {
        if ( sportDTO == null ) {
            return null;
        }

        Sport sport = new Sport();

        sport.setName( sportDTO.getName() );

        return sport;
    }

    @Override
    public SportDTO sportToDTO(Sport sport) {
        if ( sport == null ) {
            return null;
        }

        SportDTO sportDTO = new SportDTO();

        sportDTO.setName( sport.getName() );

        sportToDTOAfterMapping( sport, sportDTO );

        return sportDTO;
    }
}
