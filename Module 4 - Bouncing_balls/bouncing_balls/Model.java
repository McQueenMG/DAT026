package bouncing_balls;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish,
 * you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;

	Ball[] balls;

	double g = 9.82;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;

		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
	}

	void step(double deltaT) {
		// This method implements one step of simulation with a step deltaT

		for (Ball b : balls) {

			// Apply gravity
			b.vy -= g * deltaT;

			// Detect collision with other balls
			for (Ball b2 : balls) {
				if (b2 == b || !checkBallCollision(b, b2))
					continue;

				// Calculate the angle between the two balls
				double angle = calcBaseAngle(b, b2);

				// Change the base of the velocity vectors
				double[] b1v = calcCoordsNewBase(b.vx, b.vy, angle);
				double[] b2v = calcCoordsNewBase(b2.vx, b2.vy, angle);

				// Calculate the new velocities
				double u1 = b1v[0];
				double u2 = b2v[0];

				double I = b.m * u1 + b2.m * u2;
				double R = u2 - u1;

				double v1x = (I + (b2.m * R)) / (b.m + b2.m);
				double v2x = v1x - R;

				// Change the base of the velocity vectors back
				double[] v1 = calcCoordsOldBase(v1x, b1v[1], angle);
				double[] v2 = calcCoordsOldBase(v2x, b2v[1], angle);

				b.vx = v1[0];
				b.vy = v1[1];
				b2.vx = v2[0];
				b2.vy = v2[1];
				System.out.println("v1x: " + v1[0] + " v1y: " + v1[1]);

				// Resolve overlap
				resolveOverlap(b, b2);
			}

			// Detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {

				// Fix collision with the left border
				if (b.vx < 0) {
					double overlap = Math.abs(0 - (b.x - b.radius));
					b.x += overlap;
				}
				// Fix collision with the right border
				else {
					double overlap = Math.abs(areaWidth - (b.x + b.radius));
					b.x -= overlap;
				}

				b.vx *= -1; // Change direction of ball
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {

				// Fix collision with the top border
				if (b.vy < 0) {
					double overlap = Math.abs(0 - (b.y - b.radius));
					b.y += overlap;

				}
				// Fix collision with the bottom border
				else {
					double overlap = Math.abs(areaHeight - (b.y + b.radius));
					b.y -= overlap;
				}
				
				b.vy *= -1;
			}

			// Change velocity according to the speed of the ball
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
		}
	}

	void resolveOverlap(Ball b1, Ball b2) {
		// Calculate the distance between the centers of the two balls

		double dx = b2.x - b1.x;
		double dy = b2.y - b1.y;
		double distance = getBallDistance(b1, b2);
		double overlap = (b1.radius + b2.radius) - distance;

		// Normalize the direction from b1 to b2
		if (overlap > 0) {
			System.out.println("Resolving overlap");
			double nx = dx / distance;
			double ny = dy / distance;

			// Move the balls apart by "half" the overlap in opposite directions
			// We move by an additional arbitrary amount to avoid them sticking together as
			// often.
			b1.x -= nx * overlap / 1.5;
			b1.y -= ny * overlap / 1.5;
			b2.x += nx * overlap / 1.5;
			b2.y += ny * overlap / 1.5;
		}
	}

	// Method to calculate the angle between two balls
	private double calcBaseAngle(Ball b, Ball b2) {
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		return Math.atan2(dy, dx);
	}

	// Method to calculate the new coordinates of a point in a new base
	private double[] calcCoordsNewBase(double x, double y, double angle) {
		double newX = x * Math.cos(angle) + y * Math.sin(angle);
		double newY = -x * Math.sin(angle) + y * Math.cos(angle);
		return new double[] { newX, newY };
	}

	// Method to calculate the old coordinates of a point in the old base
	private double[] calcCoordsOldBase(double x, double y, double angle) {
		double newX = x * Math.cos(angle) - y * Math.sin(angle);
		double newY = x * Math.sin(angle) + y * Math.cos(angle);
		return new double[] { newX, newY };
	}

	// Method to check if two balls are colliding
	private boolean checkBallCollision(Ball b, Ball b2) {
		return getBallDistance(b, b2) < b.radius + b2.radius;
	}

	private double getBallDistance(Ball b, Ball b2) {
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Simple inner class describing balls.
	 */
	class Ball {

		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
			this.m = r * r * Math.PI; // Fake mass that is equal to the area of the ball
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other
		 * attributes.
		 */
		double x, y, vx, vy, radius, m;
	}
}
