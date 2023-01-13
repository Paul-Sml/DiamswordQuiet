package thePackmaster.hats;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.ThePackmaster;
import thePackmaster.packs.AbstractCardPack;

import java.util.ArrayList;

import static thePackmaster.hats.Hats.currentHat;

public class HatMenu {

    public boolean isOpen = false;

    private final DropdownMenu dropdown;
    public final ArrayList<String> hats = new ArrayList<>();

    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(SpireAnniversary5Mod.makeID("HatMenu")).TEXT;
    private static final TextureRegion MENU_BG = new TextureRegion(ImageMaster.loadImage("img/ModPanelBg.png"));

    //positions
    private static final float BG_X_SCALE = Settings.scale * 0.31f;
    private static final float BG_Y_SCALE = Settings.scale * 0.8f;
    private static final float BG_X = 525f * Settings.xScale;
    private static final float BG_Y = Settings.HEIGHT - 40f * Settings.yScale - MENU_BG.getRegionHeight() * BG_Y_SCALE;
    private static final float DROPDOWN_X = 590f * Settings.xScale;
    private static final float DROPDOWN_Y = Settings.HEIGHT - 160f * Settings.yScale;
    private static final float PREVIEW_X = Settings.WIDTH / 2 - (200 * Settings.scale);
    private static final float PREVIEW_Y = Settings.HEIGHT / 2 + (100 * Settings.scale);

    private static AbstractPlayer dummy;

    private static final boolean UNLOCK_ALL_HATS = true;

    public HatMenu() {
        ArrayList<String> optionNames = new ArrayList<>();
        optionNames.add(TEXT[0]);
        if (UNLOCK_ALL_HATS) { //TODO: Check if a hat exists for this character
            for (AbstractCardPack s : SpireAnniversary5Mod.unfilteredAllPacks) {
                hats.add(s.packID);
                optionNames.add(s.name);
            }
        } else {
            for (String s : SpireAnniversary5Mod.getUnlockedHats()) {
                hats.add(s);
                optionNames.add(SpireAnniversary5Mod.packsByID.get(s).name);
            }
        }

        dropdown = new DropdownMenu(((dropdownMenu, index, s) -> setCurrentHat(index)),
                optionNames, FontHelper.tipBodyFont, Settings.CREAM_COLOR);

        setCurrentHat(0);

        dummy = BaseMod.findCharacter(ThePackmaster.Enums.THE_PACKMASTER);
        dummy.drawX = PREVIEW_X;
        dummy.drawY = PREVIEW_Y;

        dummy.animX = dummy.animY = 0;
    }

    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    private void open() {
        isOpen = true;
    }

    private void close() {
        isOpen = false;
    }

    public void setCurrentHat(int index) {
        currentHat = hats.get(index);

        if (index == 0) {
            //TODO: Remove hat
        } else {
            Skeleton skel = ReflectionHacks.getPrivate(dummy, AbstractCreature.class, "skeleton");
            Hats.addHat(skel, currentHat, getImagePathFromHatID(currentHat), 1, 1, 0, 0, 0);
        }
    }

    private static String getImagePathFromHatID(String hatID) {
        return SpireAnniversary5Mod.modID + "Resources/images/hats/" + hatID.replace(SpireAnniversary5Mod.modID + ":", "") + "Hat.png";
    }

    public void update() {
        dropdown.update();
        FontHelper.cardTitleFont.getData().setScale(1f);
    }

    public void render(SpriteBatch sb) {
        sb.draw(MENU_BG, BG_X, BG_Y, 0f, 0f, MENU_BG.getRegionWidth(), MENU_BG.getRegionHeight(), BG_X_SCALE, BG_Y_SCALE, 0f);

        dummy.renderPlayerImage(sb);

        dropdown.render(sb, DROPDOWN_X, DROPDOWN_Y);
    }

}
