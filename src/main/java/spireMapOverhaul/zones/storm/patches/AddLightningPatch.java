package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.storm.StormUtil;
import spireMapOverhaul.zones.storm.vfx.LightningEffect;
import spireMapOverhaul.zones.storm.vfx.LightningOrbPassiveEffect;

import static spireMapOverhaul.util.Wiz.atb;


public class AddLightningPatch {

    @SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
    public static class AbstractRoomFields {
        public static SpireField<Float> timeToStrike = new SpireField<>(() -> 4.0f);
        public static SpireField<Float> timeSinceStrike = new SpireField<>(() -> 1.0f);
        public static SpireField<AbstractCreature> conduitTarget = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class GreaseLightning {

        private static float vfxTimer;
        private static float scale = 0.5F;
        private static float timeScaleStart = 0.1F;
        private static float timeScaleEnd = 0.3F;
        @SpirePrefixPatch()
        public static void Prefix() {
            if(StormUtil.isInStormZone()) {
                /* TODO: SFX with lightning? */
                if (AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) < 0.0f) {
                    float rand_y = MathUtils.random(((float) Settings.HEIGHT / 2) - 50.0f * Settings.scale, ((float) Settings.HEIGHT / 2) - 350.0f * Settings.scale);
                    boolean renderBehind = rand_y > (float) Settings.HEIGHT / 2 - 250.0f;
                    atb(new VFXAction(new LightningEffect(MathUtils.random(Settings.WIDTH), rand_y, renderBehind)));
                    if(Settings.AMBIANCE_ON) {
                        atb(new SFXAction(SpireAnniversary6Mod.THUNDER_KEY, 0.2f));
                    }
                    AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), MathUtils.random(3.5f, 10.0f));
                    AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), 0.0f);
                }
                AbstractRoomFields.timeToStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeToStrike.get(AbstractDungeon.getCurrRoom()) - Gdx.graphics.getDeltaTime());
                AbstractRoomFields.timeSinceStrike.set(AbstractDungeon.getCurrRoom(), AbstractRoomFields.timeSinceStrike.get(AbstractDungeon.getCurrRoom()) + Gdx.graphics.getDeltaTime());


                //TODO: Improve this vfx
                AbstractCreature conduitTarget = AbstractRoomFields.conduitTarget.get(AbstractDungeon.getCurrRoom());
                if(conduitTarget != null) {
                    vfxTimer -= Gdx.graphics.getDeltaTime();
                    if (vfxTimer < 0.0F) {
                        AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(MathUtils.random(conduitTarget.hb.cX + conduitTarget.hb.width * scale, conduitTarget.hb.cX - conduitTarget.hb.width * scale), MathUtils.random(conduitTarget.hb.cY + conduitTarget.hb.height * scale, conduitTarget.hb.cY - conduitTarget.hb.height * scale)));
                        if (MathUtils.randomBoolean()) {
                            AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(MathUtils.random(conduitTarget.hb.cX + conduitTarget.hb.width * scale, conduitTarget.hb.cX - conduitTarget.hb.width * scale), MathUtils.random(conduitTarget.hb.cY + conduitTarget.hb.height * scale, conduitTarget.hb.cY - conduitTarget.hb.height * scale)));
                        }
                        vfxTimer = MathUtils.random(timeScaleStart, timeScaleEnd);
                        scale *= 0.5F;
                        timeScaleStart -= 0.01F;
                        timeScaleEnd -= 0.03F;
                        if(!AbstractDungeon.actionManager.turnHasEnded) { //close into center if turn ends
                            scale = 0.5f;
                            timeScaleStart = 0.1f;
                            timeScaleEnd = 0.3F;
                        }
                    }
                }
            }
        }
    }
}
