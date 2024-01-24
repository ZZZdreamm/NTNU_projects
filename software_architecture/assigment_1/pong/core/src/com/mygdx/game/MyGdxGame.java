// change number of points need to win from 3 to 21!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font;

	private final int WIDTH = 800;
	private final int HEIGHT = 480;
	private final int PADDLE_WIDTH = 20;
	private final int PADDLE_HEIGHT = 80;
	private final int BALL_SIZE = 20;
	private final int RESTART_BUTTON_WIDTH = 110;
	private final int RESTART_BUTTON_HEIGHT = 40;

	private Rectangle paddle1;
	private Rectangle paddle2;
	private Rectangle ball;
	private Rectangle restartButton;

	private int paddleSpeed = 5;
	private Vector2 ballVelocity;

	private int player1Score = 0;
	private int player2Score = 0;

	private boolean gameRunning = true;
	private boolean gameRestarting = false;

	private int consecutivePassCounter = 0;
	private final int CONSECUTIVE_PASS_THRESHOLD = 5;

	@Override
	public void create() {
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		camera.update();

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();

		paddle1 = new Rectangle(10, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle2 = new Rectangle(WIDTH - PADDLE_WIDTH - 10, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
		ball = new Rectangle(WIDTH / 2 - BALL_SIZE / 2, HEIGHT / 2 - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);
		ballVelocity = new Vector2(MathUtils.randomSign() * 3, MathUtils.randomSign() * 3);

		restartButton = new Rectangle(WIDTH / 2 - RESTART_BUTTON_WIDTH / 2, HEIGHT / 4, RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT);

		// Set gameRunning to true to start the game automatically
		gameRunning = true;
	}

	@Override
	public void render() {
		if (gameRunning) {
			if (!gameRestarting) {
				handleInput();
				update();
			}
			draw();
		} else {
			drawGameOver();
			checkForRestart();
		}
	}

	private void handleInput() {
		// Control paddles with keyboard input
		if (Gdx.input.isKeyPressed(Input.Keys.W) && paddle1.y < HEIGHT - PADDLE_HEIGHT) {
			paddle1.y += paddleSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && paddle1.y > 0) {
			paddle1.y -= paddleSpeed;
		}

		// Control paddles vertically
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && paddle2.y < HEIGHT - PADDLE_HEIGHT) {
			paddle2.y += paddleSpeed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && paddle2.y > 0) {
			paddle2.y -= paddleSpeed;
		}
	}

	private void update() {
		if (!gameRestarting) {
			// Update ball position using velocity
			ball.x += ballVelocity.x;
			ball.y += ballVelocity.y;

			// Check collision with paddles
			if (ball.overlaps(paddle1) || ball.overlaps(paddle2)) {
				ballVelocity.x = -ballVelocity.x;
				consecutivePassCounter++; // Reset the counter on collision
			}

			// Check if ball missed paddles
			if (ball.x < 0) {
				// Player 2 scores
				player2Score++;
				checkGameOver();
				resetGame();
			} else if (ball.x > WIDTH - BALL_SIZE) {
				// Player 1 scores
				player1Score++;
				checkGameOver();
				resetGame();
			}

			// Check collision with walls
			if (ball.y < 0 || ball.y > HEIGHT - BALL_SIZE) {
				ballVelocity.y = -ballVelocity.y;
			}

			// Check consecutive passes
			if (consecutivePassCounter >= CONSECUTIVE_PASS_THRESHOLD) {
				// Increase velocity by a factor of 10
				ballVelocity.scl(1.5f);
				consecutivePassCounter = 0; // Reset the counter
			}
		}

	}

	private void checkGameOver() {
		if (player1Score == 3 || player2Score == 3) {//////////////////////////////////////////////////////change to 21
			gameRunning = false;
		}
	}

	private void resetGame() {
		if (!gameRunning) {
			// Game is over, set gameRestarting to true
			gameRestarting = true;
		}

		consecutivePassCounter = 0;
		ballVelocity = new Vector2(MathUtils.randomSign() * 3, MathUtils.randomSign() * 3);

		// Reset paddles position
		paddle1.y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
		paddle2.y = HEIGHT / 2 - PADDLE_HEIGHT / 2;

		// Reset ball position to the middle
		ball.x = WIDTH / 2 - BALL_SIZE / 2;
		ball.y = HEIGHT / 2 - BALL_SIZE / 2;

		if (gameRestarting) {
//			ballVelocity.scl(10f);
			// Shoot the ball in a random direction
			ballVelocity.set(MathUtils.randomSign() * 2, MathUtils.randomSign() * 2);
			gameRestarting = false;
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);

		// Draw paddles
		shapeRenderer.rect(paddle1.x, paddle1.y, paddle1.width, paddle1.height);
		shapeRenderer.rect(paddle2.x, paddle2.y, paddle2.width, paddle2.height);

		// Draw ball
		shapeRenderer.circle(ball.x + ball.width / 2, ball.y + ball.height / 2, ball.width / 2);

		shapeRenderer.end();

		batch.begin();
		font.setColor(Color.WHITE);
		font.draw(batch, player1Score + " | " + player2Score, WIDTH / 2 - 20, HEIGHT - 20);
		batch.end();
	}

	private void drawGameOver() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.GREEN); // Change the color to yellow
		shapeRenderer.rect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
		shapeRenderer.end();

		batch.begin();
		font.setColor(Color.WHITE);                           //////////////////////////////////////////////////////change to 21
		font.getData().setScale(2);
		font.draw(batch, "Game Over!", WIDTH / 2 - 80, HEIGHT / 2 + 70); // Adjusted position
		font.draw(batch, "Player " + (player1Score == 3 ? "1" : "2") + " wins!", WIDTH / 2 - 90, HEIGHT / 2 + 20); // Adjusted position

		font.getData().setScale(1.5f);
		font.draw(batch, "Score: " + (player1Score == 3 ? player1Score : player2Score) + " : " + (player1Score == 3 ? player2Score : player1Score), WIDTH / 2 - 60, HEIGHT / 2 - 40);

		font.getData().setScale(2f); // Increase the scale for bigger text
		font.setColor(Color.BLACK); // Change the color to black
		font.draw(batch, "Restart", WIDTH / 2 - 45, HEIGHT / 4 + 30); // Adjusted position

		batch.end();
	}


	private void checkForRestart() {
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			if (restartButton.contains(touchPos.x, touchPos.y)) {
				// Restart the game
				gameRunning = true;
				resetGame();
				player1Score = 0;
				player2Score = 0;
			}
		}
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}
}



