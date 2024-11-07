package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener {
    private final int DELAY = 80;
    public static final int TILE_SIZE = 50;
    public static final int ROWS = 10;
    public static final int COLUMNS = 10;
    private static final long serialVersionUID = 490905409104883233L;
    private Timer timer;
    private int ENEMY_NUM = 3;

    // OBJECT IN BOARD GAME
    private Player player;
    private ArrayList<Enemy> enemys;
    private ArrayList<Wall> walls;
    private Medical medical;
    private ArrayList<Point> wallsPos;

    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(232, 232, 232));

        // initialize the game state
        player = new Player();
        walls = CreateWalls();
        enemys = CreateEnemy();
        FindWallPosRand();
        CreateMedicals();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.tick();
        player.checkWall(walls); // Check wall

        // Check wall, bounded map and Random movement
        for(var enemy : enemys) {
            enemy.tick();
            enemy.RandomMovement();
            enemy.checkWall(walls);
        }

        CollectMed();
        CheckMedInWall(); // check medical spawn in wall

        CheckGameOver();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawScore(g);


        for (var wall : walls) {
            wall.draw(g, this);
        }

        if (medical != null) {
            medical.draw(g, this);
        }

        for (var enemy : enemys) {
            enemy.draw(g, this);
        }

        player.draw(g, this);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }

    private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(214, 214, 214));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // only color every other tile
                if ((row + col) % 2 == 1) {
                    // draw a square tile at the current row/column position
                    g.fillRect(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                    );
                }
            }
        }
    }

    private void drawScore(Graphics g) {
        String text = "Score: " + player.getScore();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));

        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle rect = new Rectangle(0, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;

        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g2d.drawString(text, x, y);
    }

    private ArrayList<Wall> CreateWalls() {
        var walls = new ArrayList<Wall>(){};

        // Walls Row 1
        var wr1_1 = new Wall(1, 1);
        var wr1_2 = new Wall(2, 1);
        var wr1_3 = new Wall(3, 1);
        var wr1_4 = new Wall(5, 1);
        var wr1_5 = new Wall(6, 1);
        var wr1_6 = new Wall(7, 1);
        var wr1_7 = new Wall(8, 1);

        // Walls Row 2
        var wr2_1 = new Wall(1, 2);
        var wr2_2 = new Wall(8, 2);

        // Walls Row 3
        var wr3_1 = new Wall(1, 3);
        var wr3_2 = new Wall(3, 3);
        var wr3_3 = new Wall(4, 3);
        var wr3_4 = new Wall(6, 3);
        var wr3_5 = new Wall(8, 3);

        // Walls Row 4
        var wr4_1 = new Wall(1,4);
        var wr4_2 = new Wall(3,4);
        var wr4_3 = new Wall(6,4);

        // Walls Row 5
        var wr5_1 = new Wall(3,5);
        var wr5_2 = new Wall(6,5);
        var wr5_3 = new Wall(8,5);

        // Walls Row 6
        var wr6_1 = new Wall(1, 6);
        var wr6_2 = new Wall(3, 6);
        var wr6_3 = new Wall(5, 6);
        var wr6_4 = new Wall(6, 6);
        var wr6_5 = new Wall(8, 6);

        // Walls Row 7
        var wr7_1 = new Wall(1, 7);
        var wr7_2 = new Wall(8, 7);

        // Walls Row 8
        var wr8_1 = new Wall(1, 8);
        var wr8_2 = new Wall(2, 8);
        var wr8_3 = new Wall(3, 8);
        var wr8_4 = new Wall(4, 8);
        var wr8_5 = new Wall(6, 8);
        var wr8_6 = new Wall(7, 8);
        var wr8_7 = new Wall(8, 8);

        Wall[] arrs = {wr1_1,wr1_2,wr1_3,wr1_4,wr1_5,wr1_6,wr1_7,
                        wr2_1,wr2_2,
                        wr3_1,wr3_2,wr3_3,wr3_4,wr3_5,
                        wr4_1,wr4_2,wr4_3,
                        wr5_1,wr5_2,wr5_3,
                        wr6_1,wr6_2,wr6_3,wr6_4,wr6_5,
                        wr7_1,wr7_2,
                        wr8_1,wr8_2,wr8_3,wr8_4,wr8_5,wr8_6,wr8_7,};

        for (var el : arrs) walls.add(el);
        return walls;
    }

    private void CreateMedicals() {
        Random rand = new Random();
        int medX = rand.nextInt(ROWS);
        int medY = rand.nextInt(COLUMNS);

        var point = new Point(medX, medX);
        for (var wall : wallsPos) {
            if (wall == point && medical == null) {
                medical = new Medical(9, 0);
                break;
            }

            if (wall != point && medical == null) {
                medical = new Medical(medX, medY);
                break;
            } else {
                medical = null;
            }
        }
    }

    private void CheckMedInWall() {
        if (wallsPos.contains(medical.getPos())) {
            medical = null;
            medical = new Medical(9, 0);
        }
    }

    private void CollectMed() {
        if (player.getPos().equals(medical.getPos())) {
            player.addScore(10);
            CreateMedicals();
        }
    }

    private void FindWallPosRand() {
        var pos = new ArrayList<Point>(){};

        for (var wall : walls) {
            pos.add(wall.getPos());
        }

        wallsPos = pos;
    }

    private ArrayList<Enemy> CreateEnemy() {
        var enemy = new ArrayList<Enemy>(){};

        for (int i = 0; i < ENEMY_NUM; i++) {
            enemy.add(new Enemy());
        }

        return enemy;
    }

    private void CheckGameOver() {
        for (var enemy : enemys) {
            if (player.getPos().equals(enemy.getPos())) {
                JOptionPane.showMessageDialog(null, "Game Over");
                this.setVisible(false);
                System.exit(0);
                break;
            }
        }
    }
}