package maumau.configuration;

import maumau.ascii.AsciiGen;
import maumau.karten.implKarten.MauMauCardFactory;
import maumau.karten.interKarten.ICardFactory;
import maumau.regel.implRegel.RegelFactoryImpl;
import maumau.regel.interRegel.IRegelFactory;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import maumau.spiel.model.implSpiel.SpielServiceImpl;
import maumau.spiel.model.interSpiel.ISpielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Erstellt Beans die benoetigt werden
 */
@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class Configuration {

    @Autowired
    ICardFactory cardFactory;
    @Qualifier("mauMauRegelFactory")
    @Autowired
    IRegelFactory regelFactory;

    @Autowired
    @Qualifier("achtAussetzen")
    private INaechsterSpielerVerhalten achtAussetzen;
    @Autowired
    @Qualifier("assNochmal")
    private INaechsterSpielerVerhalten assNochmal;
    @Autowired
    @Qualifier("assUndAcht")
    private INaechsterSpielerVerhalten assUndAcht;
    @Autowired
    @Qualifier("noneNaechster")
    private INaechsterSpielerVerhalten noneNaechster;

    @Autowired
    @Qualifier("bubeBube")
    private IGueltigeKarteVerhalten bubeBube;
    @Autowired
    @Qualifier("normalGueltig")
    private IGueltigeKarteVerhalten normalGueltig;

    @Autowired
    @Qualifier("noneMauMau")
    private IMauVerhalten noneMauMau;
    @Autowired
    @Qualifier("mauMau")
    private IMauVerhalten mauMau;

    /**
     * Swagger-Ui Bean, damit unter localhos:8080/swagger-ui.html die Schnittstellen gezeigt werden
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("maumau.aufrufer"))
            .paths(PathSelectors.any())
            .build();
    }

    /**
     * Setzt den initialen SpielService und stattet ihn mit den notwendigen Factories aus
     *
     * @return
     */
    @Bean
    public ISpielService getSpielSerice() {
        SpielServiceImpl spielService = new SpielServiceImpl();
        spielService.setCardFactory(cardFactory);
        spielService.setRegelFactory(regelFactory);
        return spielService;
    }

    /**
     * Erstellt eine CardFactory
     *
     * @return
     */
    @Bean
    public ICardFactory getCardFactory() {
        return new MauMauCardFactory();
    }

    /**
     * Erstellt eine RegelFactory
     *
     * @return
     */
    @Bean
    public IRegelFactory getRegelFactory() {
        return new RegelFactoryImpl(
            achtAussetzen, assNochmal, assUndAcht, noneNaechster, bubeBube, normalGueltig,
            noneMauMau, mauMau);
    }

    /**
     * Gibt beim Starten eine schoenes AsciPic aus
     */
    @Bean
    public void getAscii() {
        System.out.println("=========== !!!! - LET THE FUN START - !!!! ===========");
        System.out.println(new AsciiGen().getAscii());
        System.out.println("=========== !!!! - LET THE FUN START - !!!! ===========");
    }

}
