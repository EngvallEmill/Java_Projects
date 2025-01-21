import java.util.ArrayList;


class RandomRobot extends AbstractRobot {
	protected Position oldPos;

	public RandomRobot(Maze maze) {
		super(maze);
		this.oldPos = position;
	}

	/**
	 * Move this robot. Tries to move in a random direction.
	 * Avoids going back from where it came if possible
	 */
	@Override
	public void move() {
		ArrayList<Position> movable = getMovableNeighboursForward();
		movable.removeIf(p -> p.equals(oldPos));

		if (movable.isEmpty()) {
			stepBack();
		} else {
			moveToRandom(movable);
		}
	}

	/**
	 * Move to a random position from the list of movable positions
	 * @param movable the list of movable positions
	 */
	private void moveToRandom(ArrayList<Position> movable) {
		oldPos = position;
		position = movable.get((int) (Math.random() * movable.size()));
	}

	/**
	 * Step back to the previous position
	 */
	private void stepBack() {
		if (!position.equals(oldPos)) {
			Position temp = position;
			position = oldPos;
			oldPos = temp;
		}
	}

}