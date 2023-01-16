package thePackmaster.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import javassist.CtBehavior;
import thePackmaster.potions.PackInAJar;
import thePackmaster.potions.SmithingOil;

import java.util.ArrayList;

public class PotionPatches {

    /*
    This patch is to make Smithing Oil be treated the same as Fruit Juice when dealing with 'get random potion', so
    it does not get generated by things like Alchemize.
     */

    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = {AbstractPotion.PotionRarity.class, boolean.class})
    public static class PotionPatch1 {
        public static AbstractPotion returnTo = null;


        @SpireInsertPatch(locator = Locator.class, localvars = {"temp"})
        public static void Insert(AbstractPotion.PotionRarity rarity, boolean limited, @ByRef AbstractPotion[] temp) {
            if (temp[0] instanceof SmithingOil) {
                returnTo = temp[0];
                temp[0] = new FruitJuice();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPotion.class, "ID");
                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

}