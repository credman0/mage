package mage.cards.c;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.abilities.effects.common.DrawCardTargetEffect;
import mage.abilities.effects.common.UntapSourceEffect;
import mage.abilities.effects.mana.AddManaOfAnyColorEffect;
import mage.abilities.mana.SimpleManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author TheElk801
 */
public final class CovetedJewel extends CardImpl {

    public CovetedJewel(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{6}");

        // When Coveted Jewel enters the battlefield, draw three cards.
        this.addAbility(new EntersBattlefieldTriggeredAbility(
                new DrawCardSourceControllerEffect(3)
        ));

        // {T}: Add three mana of any one color.
        this.addAbility(new SimpleManaAbility(
                Zone.BATTLEFIELD,
                new AddManaOfAnyColorEffect(3),
                new TapSourceCost()
        ));

        // Whenever one or more creatures an opponent controls attack you and aren't blocked, that player draws three cards and gains control of Coveted Jewel. Untap it.
        this.addAbility(new CovetedJewelTriggeredAbility());
    }

    public CovetedJewel(final CovetedJewel card) {
        super(card);
    }

    @Override
    public CovetedJewel copy() {
        return new CovetedJewel(this);
    }
}

class CovetedJewelTriggeredAbility extends TriggeredAbilityImpl {

    public CovetedJewelTriggeredAbility() {
        super(Zone.BATTLEFIELD, new DrawCardTargetEffect(3), false);
        this.addEffect(new CovetedJewelEffect());
        this.addEffect(new UntapSourceEffect());
    }

    public CovetedJewelTriggeredAbility(final CovetedJewelTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public CovetedJewelTriggeredAbility copy() {
        return new CovetedJewelTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.DECLARE_BLOCKERS_STEP;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        Player player = game.getPlayer(this.getControllerId());
        if (player == null) {
            return false;
        }
        for (UUID attacker : game.getCombat().getAttackers()) {
            Permanent creature = game.getPermanent(attacker);
            if (creature != null
                    && player.hasOpponent(creature.getControllerId(), game)
                    && player.getId().equals(game.getCombat().getDefendingPlayerId(attacker, game))
                    && !creature.isBlocked(game)) {
                this.getEffects().setTargetPointer(new FixedTarget(this.getControllerId(), game));
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever one or more creatures an opponent controls attack you "
                + "and aren't blocked, that player draws three cards "
                + "and gains control of {this}. Untap it.";
    }
}

class CovetedJewelEffect extends ContinuousEffectImpl {

    public CovetedJewelEffect() {
        super(Duration.Custom, Layer.ControlChangingEffects_2, SubLayer.NA, Outcome.GainControl);
    }

    public CovetedJewelEffect(final CovetedJewelEffect effect) {
        super(effect);
    }

    @Override
    public CovetedJewelEffect copy() {
        return new CovetedJewelEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (permanent == null) {
            return false;
        }
        return permanent.changeControllerId(source.getFirstTarget(), game);
    }
}
