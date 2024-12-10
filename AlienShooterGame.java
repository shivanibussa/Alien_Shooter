import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class AlienShooterGame extends JPanel implements ActionListener, KeyListener {
    // Game settings
    private final Timer timer;
    private final int PLAYER_SPEED = 10;
    private final int BULLET_SPEED = 15;
    private final int ALIEN_SPEED = 2;
    
    // Game entities
    private final Player player;
    private final ArrayList<Bullet> bullets;
    private final ArrayList<Alien> aliens;

    public AlienShooterGame() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        player = new Player(400, 500);
        bullets = new ArrayList<>();
        aliens = new ArrayList<>();

        timer = new Timer(30, this);
        timer.start();

        spawnAliens();
    }

    private void spawnAliens() {
        for (int x = 100; x <= 700; x += 100) {
            aliens.add(new Alien(x, 50));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        // Draw player
        g.setColor(Color.GREEN);
        g.fillRect(player.x, player.y, player.width, player.height);

        // Draw bullets
        g.setColor(Color.YELLOW);
        for (Bullet bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        // Draw aliens
        g.setColor(Color.RED);
        for (Alien alien : aliens) {
            g.fillRect(alien.x, alien.y, alien.width, alien.height);
        }

        // Draw score and instructions
        g.setColor(Color.WHITE);
        g.drawString("Score: " + player.score, 10, 20);
        g.drawString("Press SPACE to shoot. Use arrow keys to move.", 10, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        // Update bullets
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.y -= BULLET_SPEED;
            if (bullet.y < 0) {
                bullets.remove(i);
                i--;
            }
        }

        // Update aliens
        for (Alien alien : aliens) {
            alien.y += ALIEN_SPEED;
        }

        // Check collisions
        checkCollisions();

        // Check game over condition
        for (Alien alien : aliens) {
            if (alien.y > 600) {
                JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + player.score);
                System.exit(0);
            }
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < aliens.size(); j++) {
                Alien alien = aliens.get(j);
                if (bullet.getBounds().intersects(alien.getBounds())) {
                    bullets.remove(i);
                    aliens.remove(j);
                    player.score += 10;
                    i--;
                    break;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Replace the lambda style with traditional switch-case
switch (e.getKeyCode()) {
    case KeyEvent.VK_LEFT:
        player.x = Math.max(player.x - PLAYER_SPEED, 0);
        break;
    case KeyEvent.VK_RIGHT:
        player.x = Math.min(player.x + PLAYER_SPEED, 800 - player.width);
        break;
    case KeyEvent.VK_SPACE:
        bullets.add(new Bullet(player.x + player.width / 2 - 2, player.y));
        break;
}

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Player class
    static class Player {
        int x, y, width = 50, height = 20, score = 0;

        Player(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Bullet class
    static class Bullet {
        int x, y, width = 5, height = 10;

        Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    // Alien class
    static class Alien {
        int x, y, width = 50, height = 20;

        Alien(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Alien Shooter Game");
        AlienShooterGame gamePanel = new AlienShooterGame();

        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
