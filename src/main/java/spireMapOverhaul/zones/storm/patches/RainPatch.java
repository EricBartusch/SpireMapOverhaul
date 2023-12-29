package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.scenes.TheBottomScene;

import static spireMapOverhaul.SpireAnniversary6Mod.makeShaderPath;

public class RainPatch {
    private static ShaderProgram shader = null;

    private static void initShader() {
        if (shader == null) {
            try {
                shader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("storm/rain/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("storm/rain/fragment.fs"))
                );
                if (!shader.isCompiled()) {
                    System.err.println(shader.getLog());
                }
                if (!shader.getLog().isEmpty()) {
                    System.out.println(shader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: rain shader:");
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(clz = TheBottomScene.class, method = "renderCombatRoomBg")
    public static class Shader {

        @SpirePrefixPatch
        public static void Prefix(TheBottomScene __instance, SpriteBatch sb) {
            initShader();
            sb.setShader(shader);
            shader.setUniformf("u_time", TheBottomSceneFields.time.get(__instance));
        }

        @SpirePostfixPatch
        public static void Postfix(TheBottomScene __instance, SpriteBatch sb) {
            sb.setShader(null);
        }
    }

    @SpirePatch2(clz = TheBottomScene.class, method = "update")
    public static class Timer {
        public static void Prefix(TheBottomScene __instance) {
            TheBottomSceneFields.time.set(__instance, TheBottomSceneFields.time.get(__instance) + Gdx.graphics.getDeltaTime());
        }
    }

    @SpirePatch(clz = TheBottomScene.class, method = SpirePatch.CLASS)
    public static class TheBottomSceneFields {
        public static SpireField<Float> time = new SpireField<>(() -> 0f);

    }
}
