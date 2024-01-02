package spireMapOverhaul.zones.storm.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import javassist.*;
import org.clapper.util.classutil.*;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.storm.StormUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.*;

public class RainPatch {
    @SpirePatch(clz = AbstractScene.class, method = SpirePatch.CLASS)
    public static class AbstractSceneFields {
        public static SpireField<ShaderProgram> shader = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class ApplyRainShaderToAllCombatBGs {
        public static void Raw(CtBehavior ctBehavior) throws NotFoundException {
            ClassFinder finder = new ClassFinder();

            finder.add(new File(Loader.STS_JAR));

            for (ModInfo modInfo : Loader.MODINFOS) {
                if (modInfo.jarURL != null) {
                    try {
                        finder.add(new File(modInfo.jarURL.toURI()));
                    } catch (URISyntaxException e) {
                        // do nothing
                    }
                }
            }

            // Get all classes for AbstractScene
            AndClassFilter filter = new AndClassFilter(
                    new NotClassFilter(new AbstractClassFilter()),
                    new ClassModifiersClassFilter(Modifier.PUBLIC),
                    new OrClassFilter(
                            new SubclassClassFilter(AbstractScene.class),
                            (classInfo, classFinder) -> classInfo.getClassName().equals(AbstractScene.class.getName())
                    )
            );

            ArrayList<ClassInfo> foundClasses = new ArrayList<>();
            finder.findClasses(foundClasses, filter);

            for (ClassInfo classInfo : foundClasses) {
                CtClass ctClass = ctBehavior.getDeclaringClass().getClassPool().get(classInfo.getClassName());

                try {
                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    for (CtMethod m : methods) {
                        if (m.getName().equals("renderCombatRoomBg")) {
                            m.insertBefore("{" +
                                    "if(" + ApplyRainShaderToAllCombatBGs.class.getName() + ".inStormZone()) {" +
                                    ApplyRainShaderToAllCombatBGs.class.getName() + ".initShader($0);" +
                                    "$1.setShader(" + ApplyRainShaderToAllCombatBGs.class.getName() + ".getSceneShader($0));" +
                                    ApplyRainShaderToAllCombatBGs.class.getName() + ".getSceneShader($0).setUniformf(\"u_time\", " + ApplyRainShaderToAllCombatBGs.class.getName() + ".getTime());"  +
                                    "}}");
                            m.insertAfter("{" +
                                    "$1.setShader(null);" +
                                    "}");
                        }
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
        public static ShaderProgram getSceneShader(AbstractScene scene) {
            return AbstractSceneFields.shader.get(scene);
        }

        public static float getTime() {
            return time;
        }
        public static boolean inStormZone() {
            return StormUtil.isInStormZone();
        }

        public static void initShader(AbstractScene scene) {
            if (getSceneShader(scene) == null) {
                try {
                    AbstractSceneFields.shader.set(scene,new ShaderProgram(
                            Gdx.files.internal(makeShaderPath("storm/rain/vertex.vs")),
                            Gdx.files.internal(makeShaderPath("storm/rain/fragment.fs"))
                    ));
                    if (!getSceneShader(scene).isCompiled()) {
                        System.err.println(getSceneShader(scene).getLog());
                    }
                    if (!getSceneShader(scene).getLog().isEmpty()) {
                        System.out.println(getSceneShader(scene).getLog());
                    }
                } catch (GdxRuntimeException e) {
                    System.out.println("ERROR: snow shader:");
                    e.printStackTrace();
                }
            }
        }
    }
}