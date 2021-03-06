package mage.cards.b;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfCombatTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.continuous.GainAbilityAllEffect;
import mage.abilities.keyword.HasteAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.permanent.TokenPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.BrudicladTelchorMyrToken;
import mage.players.Player;
import mage.target.common.TargetControlledPermanent;
import mage.util.functions.EmptyApplyToPermanent;

/**
 *
 * @author spjspj
 */
public final class BrudicladTelchorEngineer extends CardImpl {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("creature tokens you control");

    static {
        filter.add(new TokenPredicate());
    }

    public BrudicladTelchorEngineer(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT, CardType.CREATURE}, "{4}{U}{R}");

        addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.ARTIFICER);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Creature tokens you control have haste.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new GainAbilityAllEffect(HasteAbility.getInstance(), Duration.WhileOnBattlefield, filter, true)));

        // At the beginning of combat on your turn, create a 2/1 blue Myr artifact creature token. Then you may choose a token you control. If you do, each other token you control becomes a copy of that token.
        this.addAbility(new BeginningOfCombatTriggeredAbility(new BrudicladTelchorCombatffect(), TargetController.YOU, false));
    }

    public BrudicladTelchorEngineer(final BrudicladTelchorEngineer card) {
        super(card);
    }

    @Override
    public BrudicladTelchorEngineer copy() {
        return new BrudicladTelchorEngineer(this);
    }
}

class BrudicladTelchorCombatffect extends OneShotEffect {

    private static final FilterControlledPermanent filter = new FilterControlledPermanent(" token you control. If you do, each other token you control becomes a copy of that token");

    static {
        filter.add(new TokenPredicate());
    }

    public BrudicladTelchorCombatffect() {
        super(Outcome.Sacrifice);
        this.staticText = " you may choose a token you control. If you do, each other token you control becomes a copy of that token";
    }

    public BrudicladTelchorCombatffect(final BrudicladTelchorCombatffect effect) {
        super(effect);
    }

    @Override
    public BrudicladTelchorCombatffect copy() {
        return new BrudicladTelchorCombatffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        CreateTokenEffect effect = new CreateTokenEffect(new BrudicladTelchorMyrToken(), 1);

        if (effect.apply(game, source)) {
            TargetControlledPermanent target = new TargetControlledPermanent(0, 1, filter, true);
            target.setNotTarget(true);
            if (controller.choose(Outcome.Neutral, target, source.getSourceId(), game)) {
                Permanent toCopyFromPermanent = game.getPermanent(target.getFirstTarget());

                if (toCopyFromPermanent != null) {
                    for (Permanent toCopyToPermanent : game.getBattlefield().getAllActivePermanents(filter, source.getControllerId(), game)) {
                        if (!toCopyToPermanent.equals(toCopyFromPermanent)) {
                            game.copyPermanent(toCopyFromPermanent, toCopyToPermanent.getId(), source, new EmptyApplyToPermanent());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
