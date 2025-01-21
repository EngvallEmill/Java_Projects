public abstract class Message {
    private boolean movedThisTimeStep;
    private boolean isDead;

    /**
     * Creates Message to be used
     */
    public Message() {
        this.movedThisTimeStep = false;
        this.isDead = false;
    }

    /**
     * Returns whether something has moved with a true or false
     * @return
     */
    public boolean hasMovedThisTimeStep() {
        return movedThisTimeStep;
    }

    /**
     * Changes the value of MovedThisTimeStep depending on parameter
     * @param moved
     */

    public void setMovedThisTimeStep(boolean moved) {
        this.movedThisTimeStep = moved;
    }

    /**
     * Returns true or false if its dead
     * @return
     */

    public boolean isDead() {
        return isDead;
    }

    /**
     * sets it as dead with a boolean
     * @param isDead
     */
    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    /**
     * Moves the message
     */

    public abstract void move();
}
