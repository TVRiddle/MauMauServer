package maumau.aufrufer.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ContactApi {

    /**
     * Wird aufgerufen, wenn der User unsere Kontaktdaten erhalten moechte
     *
     * @return View der Kontaktseite
     */
    @GetMapping(value = {"/contact"})
    public ModelAndView contact() {
        return new ModelAndView("contactApi");
    }
}
