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

				double[] v1Coords = calcCoordsOldBase(v1x, v1y, angle);

				double v2x = Math.cos(angle) * v2;
				double v2y = Math.sin(angle) * v2;

				double[] v2Coords = calcCoordsOldBase(v2x, v2y, angle);

				b.vx = v1Coords[0];
				b.vy = v1Coords[1];
				b2.vx = -v2Coords[0];
				b2.vy = -v2Coords[1];

				// double u1 = Math.sqrt(b.vx*b.vx + b.vy*b.vy);
				// double u2 = Math.sqrt(b2.vx*b2.vx + b2.vy*b2.vy);

				// double I = b.m * u1 + b2.m * u2;
				// double R = u2 - u1;
				
				// double v1 = (I + (b2.m * R)) / (b.m + b2.m) ;
				// double v2 = v1 - R;
				
				// double a1 = calcAngle(b); 
				// double v1x = Math.cos(a1) * v1;
				// double v1y = Math.sin(a1) * v1;

				// double a2 = calcAngle(b2);
				// double v2x = Math.cos(a2) * v2;
				// double v2y = Math.sin(a2) * v2;

				// b.vx = v1x;
				// b.vy = v1y;
				// b2.vx = -v2x;
				// b2.vy = -v2y;

				collisionHasOccured = true;
				System.out.println(b.vx + " " + b.vy);
			}

			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
				collisionHasOccured = true;
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -1;
				collisionHasOccured = true;
			}
			
			// apply gravity
			if(!collisionHasOccured) b.vy -= g * deltaT;

 			//Change velocity according to the speed of the ball
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;

			
		}
	}

	private double calcBaseAngle(Ball b, Ball b2){
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		return Math.atan2(dy, dx);
	}

	private double[] calcCoordsNewBase(double x, double y, double angle){
		double newX = x * Math.cos(angle) - y * Math.sin(angle);
		double newY = - x * Math.sin(angle) + y * Math.cos(angle);
		return new double[]{newX, newY};
	}

	private double[] calcCoordsOldBase(double x, double y, double angle){
		double oldX = x * Math.cos(angle) - y * Math.sin(angle);
		double oldY = x * Math.sin(angle) + y * Math.cos(angle);
		return new double[]{oldX, oldY};
	}

	// private void rectToPolar(Ball ball) {
		
	// 	double r = Math.sqrt(ball.x * ball.x + ball.y * ball.y);
	// 	double fi = Math.acos(ball.x / r);
	// 	ball.x = r * Math.cos(fi);
	// 	ball.y = r * Math.sin(fi);

	// 	// cos(x) = närliggande / hypotenusa
	// 	// x = arccos(närliggande / hypotenusa)
	// }

	// private double calcAngle(Ball b){
	// 	double hypotenuse = Math.sqrt(b.x * b.x + b.y * b.y);
	// 	return Math.acos(b.x / hypotenuse);
	// }

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
