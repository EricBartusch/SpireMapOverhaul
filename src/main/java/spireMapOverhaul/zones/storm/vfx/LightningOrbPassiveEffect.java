package spireMapOverhaul.zones.storm.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LightningOrbPassiveEffect extends AbstractGameEffect {
    private Texture img = null;
    private int index = 0;
    private final float x;
    private final float y;
    private final boolean flipX;
    private final boolean flipY;
    private final float intervalDuration;

    public LightningOrbPassiveEffect(float x, float y) {
        this.renderBehind = false;
        this.x = x;
        this.y = y;
        this.color = Settings.LIGHT_YELLOW_COLOR.cpy();
        this.img = ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);
        this.scale = MathUtils.random(0.6F, 1.0F) * Settings.scale;
        this.rotation = MathUtils.random(360.0F);
        if (this.rotation < 120.0F) {
            this.renderBehind = true;
        }

        this.flipX = MathUtils.randomBoolean();
        this.flipY = MathUtils.randomBoolean();
        this.intervalDuration = MathUtils.random(0.03F, 0.06F);
        this.duration = this.intervalDuration;
    }

    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            ++index;
            if (index > ImageMaster.LIGHTNING_PASSIVE_VFX.size() - 1) {
                isDone = true;
                return;
            }

            img = ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);
            duration = this.intervalDuration;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x - 61.0F, y - 61.0F, 61.0F, 61.0F, 122.0F, 122.0F, scale, scale, rotation, 0, 0, 122, 122, flipX, flipY);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
