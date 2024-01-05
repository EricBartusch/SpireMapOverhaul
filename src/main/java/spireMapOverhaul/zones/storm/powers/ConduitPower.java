package spireMapOverhaul.zones.storm.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class ConduitPower extends AbstractSMOPower {
    public static final String POWER_ID = makeID("ConduitPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //TODO: Maybe damage adheres to vuln/other damage effects?
    private int damage;
    private final AbstractCreature target;

    public ConduitPower(AbstractCreature target) {
        super(POWER_ID, NAME, NeutralPowertypePatch.NEUTRAL, false, target, -1);
        this.target = target;
    }

    @Override
    public void onInitialApplication() {
        this.damage = target instanceof AbstractPlayer ? target.maxHealth / 20 : target.maxHealth / 10;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + damage + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        atb(new VFXAction(new LightningEffect(owner.drawX, owner.drawY)));
        atb(new RemoveSpecificPowerAction(owner, owner, this));
        atb(new DamageAction(owner, new DamageInfo(null, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
        atb(new SFXAction("ORB_LIGHTNING_EVOKE"));
    }
}

