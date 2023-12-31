package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.zones.storm.StormUtil;
import spireMapOverhaul.zones.storm.vfx.LightningEffect;

import static spireMapOverhaul.util.Wiz.atb;


public class AddLightningPatch {

    @SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
    public static class AbstractRoomFields {
        public static SpireField<Float> timeToStrike = new SpireField(() -> 4.0f);
        public static SpireField<Float> timeSinceStrike = new SpireField(() -> 1.0f);
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class Strike {

        @SpirePrefixPatch()
        public static void Prefix() {
            if(StormUtil.isInStormZone()) {
                if (AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) < 0.0f) {
                    float rand_y = MathUtils.random((Settings.HEIGHT / 2) - 50.0f * Settings.scale, (Settings.HEIGHT / 2) - 350.0f * Settings.scale);
                    boolean renderBehind = rand_y > Settings.HEIGHT / 2 - 250.0f;
                    atb(new VFXAction(new LightningEffect(MathUtils.random(Settings.WIDTH), rand_y, renderBehind)));
                    AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), MathUtils.random(3.5f, 10.0f));
                    AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), 0.0f);
                }
                AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) - Gdx.graphics.getDeltaTime());
                AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeSinceStrike.get(AbstractDungeon.getCurrRoom()) + Gdx.graphics.getDeltaTime());
            }
        }
    }
}
