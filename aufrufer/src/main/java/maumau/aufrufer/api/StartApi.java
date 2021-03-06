package maumau.aufrufer.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StartApi {

    /**
     * Erster Screen der gezeigt wird und dem User die Moeglichkeit gibt ein Spiel zu starten
     *
     * @return HomeView
     */
    @GetMapping(value = {"/"})
    public ModelAndView getFirstMenu() {
        log.info("Neuer Startbildschirm wurde aufgerufen");
        return new ModelAndView("start");
    }


}
