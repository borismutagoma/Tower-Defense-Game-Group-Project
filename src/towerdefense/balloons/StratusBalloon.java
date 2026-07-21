package towerdefense.balloons;

import towerdefense.game.GameConstants;
import towerdefense.path.Path;

/**
 * Representate a "StratusBalloon" in Bloons Tower Defense
 * Can be considered as the heavy balloon, is slower but more durable than the
 * normal model
 */
public class StratusBalloon extends Balloon {

    /** The default life point(s) of this type of balloon */
    private static final double DEFAULT_LIFE = GameConstants.STRATUS_LIFE;
    /** The default speed of this type of balloon */
    private static final double DEFAULT_SPEED = GameConstants.STRATUS_SPEED;

    /**
     * Construct a StratusBalloon following a path / trajectory
     * 
     * @param trajectory the path the balloon will follow
     */
    public StratusBalloon(Path trajectory) {
        super(trajectory);
    }

    /**
     * The StratusBalloon has <strong>2 hit point.</strong>
     * 
     * @see Balloon#getDefaultLife()
     */
    @Override
    public double getDefaultLife() {
        return DEFAULT_LIFE;
    }

    /**
     * The StratusBalloon has a <strong> speed of 0.5.</strong>
     * 
     * @see Balloon#getDefaultSpeed()
     */
    @Override
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
