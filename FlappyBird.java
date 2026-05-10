import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//import java.util.ArrayList;
//import java.util.Random.*;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener , KeyListener {

    int boardwidth = 360;
    int boardheight = 640;

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    Timer gameLoop;
    Timer PlacePipesTimer;

    boolean gameOver = false;
    double score = 0;

    // bird
    int birdX = boardwidth / 8;
    int birdY = boardheight / 2;
    int birdwidth = 34;
    int birdheight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdwidth;
        int height = birdheight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // pipes
    int PipeX =boardwidth;
    int PipeY = 0;
    int pipewidth = 64;
    int pipeheight = 512;

    class pipe {
        int x = PipeX;
        int y = PipeY;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

        pipe (Image img){
            this.img = img;
        }
    }

    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<pipe> pipes;
    Random random = new Random();



    FlappyBird() {

        setPreferredSize(new Dimension(boardwidth, boardheight));
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
        setBackground(Color.blue);

        backgroundImg = new ImageIcon("C:/Users/Administrator/Downloads/java Games/flappybirdbg.png").getImage();

        birdImg = new ImageIcon("C:/Users/Administrator/Downloads/java Games/flappybird.png").getImage();

        topPipeImg = new ImageIcon("C:/Users/Administrator/Downloads/java Games/toppipe.png").getImage();

        bottomPipeImg = new ImageIcon("C:/Users/Administrator/Downloads/java Games/bottompipe.png").getImage();

         bird = new Bird(birdImg);
         pipes = new ArrayList<pipe>();

         PlacePipesTimer =  new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
         });
         PlacePipesTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void placePipes() {

    int randomPipeY = (int) (PipeY - pipeheight / 4 - Math.random() * (pipeheight / 2));
    int openingSpace = boardheight/4;
    pipe topPipe = new pipe(topPipeImg);
    topPipe.y = randomPipeY;

    pipes.add(topPipe);

    pipe bottomPipe = new pipe(bottomPipeImg);
    bottomPipe.y = topPipe.y + pipeheight + openingSpace;
    pipes.add(bottomPipe);
}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
      System.out.println("draw");
        g.drawImage(backgroundImg, 0, 0, boardwidth, boardheight, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for(int i = 0; i <pipes.size(); i++)
        {
            pipe p = pipes.get(i);
            g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over : "+ (int) score , 10 , 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10 , 35);
        }

        
    }
    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        

        //pipes
        for(int i = 0 ; i <pipes.size(); i++){
            pipe p = pipes.get(i);
            p.x += velocityX;


            if (!p.passed  && bird.x > p.x + p.width) {
                p.passed = true;
                score += 0.5;
            }
            if (collision(bird, p)) {
            gameOver = true;               
        }
        }
        if (bird.y > boardheight){
            gameOver = true;
        }
    }

    public boolean collision(Bird a ,pipe b){
       return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            PlacePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;

            if (gameOver){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                PlacePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}