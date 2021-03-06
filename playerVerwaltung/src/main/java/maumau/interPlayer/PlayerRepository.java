package maumau.interPlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface PlayerRepository extends JpaRepository<IPlayer, Integer> {

    IPlayer findByCookieId(String cookieId);
}
