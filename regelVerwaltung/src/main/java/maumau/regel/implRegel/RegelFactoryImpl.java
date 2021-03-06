package maumau.regel.implRegel;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.regel.interRegel.IRegelFactory;
import maumau.regel.interRegel.RegelServiceTemplate;
import maumau.regel.interRegel.module.RegelType;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("mauMauRegelFactory")
public class RegelFactoryImpl implements IRegelFactory {

    private final INaechsterSpielerVerhalten achtAussetzen;
    private final INaechsterSpielerVerhalten assNochmal;

    private final INaechsterSpielerVerhalten assUndAcht;
    private final INaechsterSpielerVerhalten noneNaechster;

    private final IGueltigeKarteVerhalten bubeBube;
    private final IGueltigeKarteVerhalten normalGueltig;

    private final IMauVerhalten noneMauMau;
    private final IMauVerhalten mauMau;


    public maumau.regel.interRegel.RegelServiceTemplate getRegelService(List<RegelType> regeln) {
        RegelServiceTemplate regelSercive = new RegelServiceImpl();
        regelSercive.setNaechsterSpielerVerhalten(
            getNachsterSpielerVerhalten(regeln.contains(RegelType.ACHT_AUSSETZEN),
                regeln.contains(RegelType.ASS_NOCHMAL)));
        regelSercive.setMauVerhalten(getMauVerhalten(regeln.contains(RegelType.MAU_REGEL)));
        regelSercive.setGueltigeKarteVerhalten(
            getGueltigeKarteVerhalten(regeln.contains(RegelType.BUBE_BUBE)));
        return regelSercive;
    }

    private IGueltigeKarteVerhalten getGueltigeKarteVerhalten(boolean isBubeBube) {
        if (isBubeBube) {
            return bubeBube;
        }
        return normalGueltig;
    }

    private IMauVerhalten getMauVerhalten(boolean mauRegel) {
        if (mauRegel) {
            return mauMau;
        }
        return noneMauMau;
    }

    private INaechsterSpielerVerhalten getNachsterSpielerVerhalten(
        boolean isAchtAussetzen, boolean isAssNochmal) {
        if (isAchtAussetzen && isAssNochmal) {
            return assUndAcht;
        }
        if (isAchtAussetzen) {
            return achtAussetzen;
        }
        if (isAssNochmal) {
            return assNochmal;
        }
        return noneNaechster;
    }

}
