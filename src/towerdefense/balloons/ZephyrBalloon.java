package towerdefense.balloons;

import towerdefense.game.GameConstants;
import towerdefense.path.Path;

/**
 * Representate a "ZephyrBalloon" in Bloons Tower Defense
 * Can be considered as the light balloon, faster but weaker than the standard
 * model
 */
public class ZephyrBalloon extends Balloon {

    /** The default life point(s) of this type of balloon */
    private static final double DEFAULT_LIFE = GameConstants.ZEPHYR_LIFE;
    /** The default speed of this type of balloon */
    private static final double DEFAULT_SPEED = GameConstants.ZEPHYR_SPEED;

    /**
     * Construct a ZephyrBalloon following a path / trajectory
     * 
     * @param trajectory the path the balloon will follow
     */
    public ZephyrBalloon(Path trajectory) {
        super(trajectory);
    }

    /**
     * The ZephyrBalloon has <strong>0.8 hit point.</strong>
     * 
     * @see Balloon#getDefaultLife()
     */
    @Override
    public double getDefaultLife() {
        return DEFAULT_LIFE;
    }

    /**
     * The ZephyrBalloon has a <strong> speed of 1.5.</strong>
     * 
     * @see Balloon#getDefaultSpeed()
     */
    @Override
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
