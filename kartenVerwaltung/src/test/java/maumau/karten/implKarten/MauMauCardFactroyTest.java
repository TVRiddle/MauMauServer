package maumau.karten.implKarten;

import static org.junit.Assert.assertEquals;

import java.util.List;
import maumau.karten.interKarten.ICardFactory;
import maumau.karten.interKarten.module.Karte;
import org.junit.Before;
import org.junit.Test;

public class MauMauCardFactroyTest {

    ICardFactory factory;

    @Before
    public void init() {
        factory = new MauMauCardFactory();
    }

    @Test
    public void zweiunddreißigKartenErstellt() {
        List<Karte> cardSet = factory.createNewCardSet();
        assertEquals(32, cardSet.size());
    }
}
