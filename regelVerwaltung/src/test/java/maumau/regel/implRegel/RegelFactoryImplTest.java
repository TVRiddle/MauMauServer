package maumau.regel.implRegel;

import static maumau.regel.interRegel.module.RegelType.ACHT_AUSSETZEN;
import static maumau.regel.interRegel.module.RegelType.ASS_NOCHMAL;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import maumau.regel.implRegel.verhalten.AchtAussetzenNaechsterSpieler;
import maumau.regel.implRegel.verhalten.AssNochmalNaechsterSpieler;
import maumau.regel.implRegel.verhalten.AssUndAchtNaechsterSpieler;
import maumau.regel.implRegel.verhalten.BubeAufBubeStinktVerhalten;
import maumau.regel.implRegel.verhalten.KeinMauVerhalten;
import maumau.regel.implRegel.verhalten.KeineSonderregelNaechsterSpieler;
import maumau.regel.implRegel.verhalten.MauVerhalten;
import maumau.regel.implRegel.verhalten.NormalGueltigVerhalten;
import maumau.regel.interRegel.RegelServiceTemplate;
import maumau.regel.interRegel.module.RegelType;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.junit.Before;
import org.junit.Test;

public class RegelFactoryImplTest {


    private RegelFactoryImpl regelFactory;

    private INaechsterSpielerVerhalten achtAussetzen;
    private INaechsterSpielerVerhalten assNochmal;
    private INaechsterSpielerVerhalten assUndAcht;
    private INaechsterSpielerVerhalten noneNaechster;
    private IGueltigeKarteVerhalten bubeBube;
    private IGueltigeKarteVerhalten normalGueltig;
    private IMauVerhalten noneMauMau;
    private IMauVerhalten mauMau;

    @Before
    public void init() {
        achtAussetzen = new AchtAussetzenNaechsterSpieler();
        assNochmal = new AssNochmalNaechsterSpieler();
        assUndAcht = new AssUndAchtNaechsterSpieler();
        noneNaechster = new KeineSonderregelNaechsterSpieler();
        bubeBube = new BubeAufBubeStinktVerhalten();
        normalGueltig = new NormalGueltigVerhalten();
        noneMauMau = new KeinMauVerhalten();
        mauMau = new MauVerhalten();

        regelFactory = new RegelFactoryImpl(
            achtAussetzen, assNochmal, assUndAcht, noneNaechster, bubeBube, normalGueltig,
            noneMauMau, mauMau);
    }

    @Test
    public void assUndAchtStrategy() {
        List<RegelType> regelList = Arrays.asList(ACHT_AUSSETZEN, ASS_NOCHMAL);
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(AssUndAchtNaechsterSpieler.class,
            regelService.getNaechsterSpielerVerhalten().getClass());
    }

    @Test
    public void achtAussetzenStrategy() {
        List<RegelType> regelList = Arrays.asList(ACHT_AUSSETZEN);
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(AchtAussetzenNaechsterSpieler.class,
            regelService.getNaechsterSpielerVerhalten().getClass());
    }

    @Test
    public void assNochmalStrategy() {
        List<RegelType> regelList = Arrays.asList(ASS_NOCHMAL);
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(AssNochmalNaechsterSpieler.class,
            regelService.getNaechsterSpielerVerhalten().getClass());
    }

    @Test
    public void keineNaechsterSpielerStrategy() {
        List<RegelType> regelList = Arrays.asList();
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(KeineSonderregelNaechsterSpieler.class,
            regelService.getNaechsterSpielerVerhalten().getClass());
    }

    @Test
    public void bubeAufBubeStinktStrategy() {
        List<RegelType> regelList = Arrays.asList(RegelType.BUBE_BUBE);
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(BubeAufBubeStinktVerhalten.class,
            regelService.getGueltigeKarteVerhalten().getClass());
    }

    @Test
    public void normalesGueltigkeitsVerhaltenStrategy() {
        List<RegelType> regelList = Arrays.asList();
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(NormalGueltigVerhalten.class,
            regelService.getGueltigeKarteVerhalten().getClass());
    }

    @Test
    public void mauRegelVerhaltenStrategy() {
        List<RegelType> regelList = Arrays.asList(RegelType.MAU_REGEL);
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(MauVerhalten.class,
            regelService.getMauVerhalten().getClass());
    }

    @Test
    public void keineMauRegelVerhaltenStrategy() {
        List<RegelType> regelList = Arrays.asList();
        RegelServiceTemplate regelService = regelFactory
            .getRegelService(regelList);
        assertEquals(KeinMauVerhalten.class,
            regelService.getMauVerhalten().getClass());
    }

}
