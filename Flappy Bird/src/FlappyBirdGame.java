import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FlappyBirdGame extends Application{
    private Label scoreLabel;
    private int width = 360;
    private int height = 640;
	Image birdImage;
	Image pipeImage;
	ImageView topPipeView;
	ImageView bottomPipeView;
	Image topPipe;
	Image bottomPipe;
	Image background = new Image("flappybirdbg.png");
	BackgroundImage bgImage = new BackgroundImage(background, null, null, null, null);
	Bird bird;
	boolean isInAir = false;
    int score;
    private Pane pane;
    private Scene scene;
    private int VELOCITY = 12;
    private int GRAVITY = 1; 
    private double birdVelocity = 0;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isSpacePressed;
    private AnimationTimer gameLoop;
    private long interval;
    private ScoreManager scoreManager;
    private ArrayList<Pipe> pipes;
    private long pipeSpawnTimer;
    private int pipeWidth;
    private boolean collisionDetected;
    private Label gameOverLabel;

	public static void main(String[] args) {
		launch();
	}

	
	@Override
	public void start(Stage stage) {
		pipeSpawnTimer = 0;
		stage.setTitle("Flappy Bird");
		scoreManager = new ScoreManager();

	    pane = new Pane();
	    pane.setBackground(new Background(bgImage));
	    scene = new Scene(pane, width, height);
	    
	    this.bird = new Bird(pane);
	    
	    scoreLabel = new Label();
	    scoreLabel.setTextFill(Color.BLACK);
	    CustomFont scoreLabelFont = new CustomFont(20);
	    scoreLabel.setFont(scoreLabelFont.getCustomFont());
	 
	    scoreLabel.setText(String.valueOf(scoreManager.getCurrentScore()));
	    scoreLabel.setLayoutX(10); // 10 is the margin
	    scoreLabel.setLayoutY(10);  // 10 pixels from the top
	    pane.getChildren().add(scoreLabel);
	    
	    pipes = new ArrayList<>();
	    //addPipe();
	    
	    gameOverLabel = new Label();
	    gameOverLabel.setTextFill(Color.BLACK);
	    gameOverLabel.setText("Game over!");
	    CustomFont gameOverFont = new CustomFont(40);
	    gameOverLabel.setFont(gameOverFont.getCustomFont());
	    gameOverLabel.setLayoutX(75);
	    gameOverLabel.setLayoutY(260);
	    
	    stage.setScene(scene);
	    stage.show();
		registerListeners(stage);
		setupGameLoop();
        gameLoop.start();
		
	}

    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= interval) {
                    update();
                    lastUpdate = now;
                }
            }
        };
    }
    

    public void stop() {
        gameLoop.stop();
    }
    
	
	private void registerListeners(Stage stage) {
		scene.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.SPACE) {
		    	isGameStarted = true;
		    	birdVelocity = -VELOCITY;
		    	isSpacePressed = true;
		    	isInAir = true;
		        System.out.println("pressed");
		    }
		});
		
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                isSpacePressed = false;
		        System.out.println("released");
            }
        });
	}
	
    private void update() {
        for (Pipe pipe : pipes) {
            if (bird.getBirdView().getX() > pipe.getX() + pipeWidth && !pipe.isPassed) {
                pipe.isPassed = true;  // Mark the pipe as passed
                scoreManager.updateScore(1);  // Increase score
                System.out.println("Bird passed through the pipe!");
            }
        }
    	
    	if (isGameStarted && !isGameOver) {
    		movePipes();
    		
            if (System.currentTimeMillis() - pipeSpawnTimer > 1500 && isGameStarted) { // 2 second interval
                addPipe();
                pipeSpawnTimer = System.currentTimeMillis(); 
            }
    	}
    	
        bird.getBirdView().setY(bird.getBirdView().getY() + birdVelocity);

        if (isInAir) {
            birdVelocity += GRAVITY;
        }

        if (bird.getBirdView().getY() > height - bird.getBirdView().getFitHeight()) {
            bird.getBirdView().setY(height - bird.getBirdView().getFitHeight());
            birdVelocity = 0;  
        }

        if (bird.getBirdView().getY() < 0) {
            bird.getBirdView().setY(0);
            birdVelocity = 0;
        }
    
        checkCollisions();
    
        if (birdHitGround()) {
        	gameOver();
        	stop();
 
        }
        
        updateScore();
    }
    
    public boolean birdHitGround() {
    	return bird.getY()+bird.BIRD_HEIGHT >= 640;
    }
    
	public void addPipe() {
		topPipe = new Image("toppipe.png");
		bottomPipe = new Image("bottompipe.png");
		
		int gap = 150;
		int pipeX = width;
		int pipeHeightMax = 220;
		int pipeHeightMin = 50;
		int pipeWidth = 50;
		int pipeY = 0;
		
		// randomize pipe height
		int topPipeHeight = new Random().nextInt(pipeHeightMax - pipeHeightMin) + pipeHeightMin;
		int bottomPipeHeight = height - (topPipeHeight - gap);
        
        //top pipe
		topPipeView = new ImageView(topPipe);
        topPipeView.setFitWidth(pipeWidth);
        topPipeView.setFitHeight(topPipeHeight);
        topPipeView.setX(pipeX);
        topPipeView.setY(pipeY);
        
        //bottom pipe
        bottomPipeView = new ImageView(bottomPipe);
        bottomPipeView.setFitWidth(pipeWidth); 
        bottomPipeView.setFitHeight(bottomPipeHeight);
        bottomPipeView.setX(pipeX);
        bottomPipeView.setY(topPipeHeight+gap);
        
        pane.getChildren().addAll(topPipeView, bottomPipeView);
        pipes.add(new Pipe(topPipeView, bottomPipeView, pane));
        
	    scoreLabel.toFront();
        
        System.out.println("pipe spawned at x = " + pipeX + ", y = " + pipeY + "\n");
	}
	
	public void movePipes() {
		for (Pipe pipe: pipes) {
			pipe.move(-3);
			
			if (pipe.getX() + pipeWidth < -50) {
				pipes.remove(pipe);
				System.out.println("pipe removed\n");
		        pane.getChildren().remove(pipe.getTopPipe());
		        pane.getChildren().remove(pipe.getBottomPipe());
			}
		}
	}
	
	public boolean checkCollision(Bird bird, Pipe pipe) {
		int bird_width = bird.BIRD_WIDTH;
		int bird_height = bird.BIRD_HEIGHT;
		double bird_x = bird.getX();
		double bird_y = bird.getY();
		
		// Top pipe info
	    double topPipeX = pipe.getTopPipe().getX();
	    double topPipeY = pipe.getTopPipe().getY();
	    double topPipeWidth = pipe.getTopPipe().getFitWidth();
	    double topPipeHeight = pipe.getTopPipe().getFitHeight();

	    // Bottom pipe info
	    double bottomPipeX = pipe.getBottomPipe().getX();
	    double bottomPipeY = pipe.getBottomPipe().getY();
	    double bottomPipeWidth = pipe.getBottomPipe().getFitWidth();
	    double bottomPipeHeight = pipe.getBottomPipe().getFitHeight();
	    
	    boolean isBirdWithinPipeX = bird_x + bird_width > topPipeX && bird_x < topPipeX + topPipeWidth;

	    // Check top pipe collision
	    boolean collisionWithTopPipe = isBirdWithinPipeX && bird_y < topPipeY + topPipeHeight;

	    // Check bottom pipe collision
	    boolean collisionWithBottomPipe = isBirdWithinPipeX && bird_y + bird_height > bottomPipeY;
		
		return collisionWithTopPipe || collisionWithBottomPipe;	
	}
	
	
	private void checkCollisions() {
	    for (Pipe pipe : pipes) {
	        if (checkCollision(bird, pipe)) {
	        	collisionDetected = true;
	            isGameOver = true;
	            System.out.println("Collision detected! Game over.");
	            triggerNoseDive();
	            break;
	        }
	    }
	}
	
	private void triggerNoseDive() {
		birdVelocity = VELOCITY * 2;
		bird.getBirdView().setRotate(90);
		isInAir = false;
	}
	
	
    public void restartGame() {
    	scoreManager.resetScore();
    }
    
    public void updateScore() {
        scoreLabel.setText("Score: " + scoreManager.getCurrentScore());
    }
    
    public void gameOver() {
    	if (!isGameOver) {
    		isGameOver = true;	
    	}
    	
    	if (!pane.getChildren().contains(gameOverLabel)) {
            pane.getChildren().add(gameOverLabel);
            gameOverLabel.toFront();
        }
    }

}