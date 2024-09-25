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

			// detect collision with other balls
			for(Ball b2 : balls) {
				if (b2 == b) continue;

				if(!checkBallCollision(b, b2)) continue;

				double u1 = Math.sqrt(b.vx*b.vx + b.vy*b.vy);
				double u2 = Math.sqrt(b2.vx*b2.vx + b2.vy*b2.vy);

				double I = b.m * u1 + b2.m * u2;
				double R = u2 - u1;
				
				double v1 = (I + (b2.m * R)) / (b.m + b2.m) ;
				double v2 = v1 - R;

				b.vx = v1/2;
				b2.vx = v2/2;

				b.vy = v1/2;
				b2.vy = v2/2;
			}

			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
			}

			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -1;
			}
			
			// apply gravity
			b.vy -= g * deltaT;

 			//Change velocity according to the speed of the ball
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
		}
	}

	private void rectToPolar(Ball ball) {
		double fi = Math.acos(ball.x / Math.sqrt(ball.x * ball.x + ball.y * ball.y));
		ball.x = Math.cos(fi);
		ball.y = Math.sin(fi);

		// cos(x) = närliggande / hypotenusa
		// x = arccos(närliggande / hypotenusa)
	}

	private boolean checkBallCollision(Ball b, Ball b2)
	{
		double dx = b2.x - b.x;
		double dy = b2.y - b.y;
		double distance = Math.sqrt(dx * dx + dy * dy);
		return distance < b.radius + b2.radius;
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
