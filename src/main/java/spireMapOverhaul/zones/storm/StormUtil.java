package spireMapOverhaul.zones.storm;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.storm.cardmods.DampModifier;

import java.util.stream.Collectors;

import static spireMapOverhaul.util.Wiz.getCurZone;

public class StormUtil {

    public static long rainSoundId;
    public static boolean activePlayerLightning;
    public static float brightTime;

    public static FrameBuffer createBuffer() {
        return createBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static FrameBuffer createBuffer(int sizeX, int sizeY) {
        return new FrameBuffer(Pixmap.Format.RGBA8888, sizeX, sizeY, false, false);
    }

    public static void beginBuffer(FrameBuffer fbo) {
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
    }

    public static TextureRegion getBufferTexture(FrameBuffer fbo) {
        TextureRegion texture = new TextureRegion(fbo.getColorBufferTexture());
        texture.flip(false, true);
        return texture;
    }

    public static boolean cardValidToMakeDamp(AbstractCard c) {
        return !CardModifierManager.hasModifier(c, DampModifier.ID);
    }

    public static int countValidCardsInHandToMakeDamp() {
        return (int) AbstractDungeon.player.hand.group.stream().filter(StormUtil::cardValidToMakeDamp).count();
    }

    public static boolean isInStormZone() {
        return getCurZone() instanceof StormZone;
    }
}
