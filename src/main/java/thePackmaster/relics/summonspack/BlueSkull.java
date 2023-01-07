package thePackmaster.relics.summonspack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.relics.AbstractPackmasterRelic;

import static thePackmaster.util.Wiz;

public class BlueSkull extends AbstractPackmasterRelic {
    public static final String ID = SpireAnniversary5Mod.makeID(BlueSkull.class.getSimpleName());
    private static final int FOCUS_AMOUNT = 2;
    private boolean isActive = false;

    public BlueSkull() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    public void atBattleStart() {
        isActive = false;
        addToBot(new AbstractGameAction() {
            public void update() {
                if (!isActive && adp().isBloodied) {
                    flash();
                    pulse = true;
                    adp().addPower(new FocusPower(adp(), FOCUS_AMOUNT));
                    att(new RelicAboveCreatureAction(adp(), BlueSkull.this));
                    isActive = true;
                    AbstractDungeon.onModifyPower();
                }
                isDone = true;
            }
        });
    }

    public void onBloodied() {
        flash();
        pulse = true;
        if (!isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            att(new ApplyPowerAction(adp(), adp(), new FocusPower(adp(), FOCUS_AMOUNT), FOCUS_AMOUNT));
            att(new RelicAboveCreatureAction(adp(), this));
            isActive = true;
            adp().hand.applyPowers();
        }

    }

    public void onNotBloodied() {
        if (isActive && Wiz.isInCombat())
            att(new ApplyPowerAction(adp(), adp(), new FocusPower(adp(), -FOCUS_AMOUNT), -FOCUS_AMOUNT));

        stopPulse();
        isActive = false;
        adp().hand.applyPowers();
    }

    public void onVictory() {
        pulse = false;
        isActive = false;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FOCUS_AMOUNT + DESCRIPTIONS[1];
    }
}