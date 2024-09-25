package bouncing_balls;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;
	
	Ball [] balls;

	double g = 9.82;

	boolean collisionHasOccuredBetweenTheBalls = false;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT

		for (Ball b : balls) {
			boolean collisionHasOccured = false;
			// detect collision with other balls
			for(Ball b2 : balls) {
				if (b2 == b) continue;
				
				if (!checkBallCollision(b, b2)) continue;

				double angle = calcBaseAngle(b, b2);
				double[] b1v = calcCoordsNewBase(b.vx, b.vy, angle);
				double[] b2v = calcCoordsNewBase(b2.vx, b2.vy, angle);

				double u1 = Math.sqrt(b1v[0]*b1v[0] + b1v[1]*b1v[1]);
				double u2 = Math.sqrt(b2v[0]*b2v[0] + b2v[1]*b2v[1]);

				double I = b.m * u1 + b2.m * u2;
				double R = u2 - u1;

				double v1 = (I + (b2.m * R)) / (b.m + b2.m) ;
				double v2 = v1 - R;

				double v1x = Math.cos(angle) * v1;
				double v1y = Math.sin(angle) * v1;
				double v2x = Math.cos(angle) * v2;
				double v2y = Math.sin(angle) * v2;


				b.vx = v1x;
				b.vy = v1y;
				b2.vx = v2x;
				b2.vy = v2y;

				resolveOverlap(b, b2);

				collisionHasOccured = true;

				System.out.println(b.vx + " " + b.vy);
			}

			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {

				

				if(b.vx < 0) 
				{
					double overlap = Math.abs(0-(b.x - b.radius));
					b.x += overlap;
				}
				else {
					double overlap = Math.abs(areaWidth-(b.x + b.radius));
					b.x -= overlap;
				}

				b.vx *= -1; // change direction of ball
				
				collisionHasOccured = true;
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {

				if(b.vy < 0) 
				{
					double overlap = Math.abs(0-(b.y - b.radius));
					b.y += overlap;
				}
				else {
					double overlap = Math.abs(areaHeight-(b.y + b.radius));
					b.y -= overlap;
				}

				
				b.vy *= -1;
				collisionHasOccured = true;
			}
			
			// apply gravity
			b.vy -= g * deltaT;

 			//Change velocity according to the speed of the ball
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;

			
		}
	}

	void resolveOverlap(Ball b1, Ball b2) {
		// Calculate the distance between the centers of the two balls
		double dx = b2.x - b1.x;
		double dy = b2.y - b1.y;
		double distance = Math.sqrt(dx * dx + dy * dy);
		double overlap = (b1.radius + b2.radius) - distance;
	
		// Normalize the direction from b1 to b2
		if (distance > 0) {
			double nx = dx / distance;
			double ny = dy / distance;
	
			// Move the balls apart by half the overlap in opposite directions
			b1.x -= nx * overlap / 2;
			b1.y -= ny * overlap / 2;
			b2.x += nx * overlap / 2;
			b2.y += ny * overlap / 2;
		}
	}

	private double calcBaseAngle(Ball b, Ball b2){
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		return Math.atan2(dy, dx);
	}

	private double[] calcCoordsNewBase(double x, double y, double angle){
		double newX = x * Math.cos(angle) + y * Math.sin(angle);
		double newY = - x * Math.sin(angle) + y * Math.cos(angle);
		return new double[]{newX, newY};
	}

	private boolean checkBallCollision(Ball b, Ball b2)
	{
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		double distance = Math.sqrt(dx * dx + dy * dy);
		return distance <= b.radius + b2.radius;
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
			this.m = r*r*Math.PI; //Fake mass that is proportional to the area of the ball
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, m;
	}
}
