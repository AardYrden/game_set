package com.aard._2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {
    private final static Integer BLOCK_GAP = 10;
    private final static Integer BLOCK_SIZE = 84;
    private final static Integer BLOCK_ARC = 30;

    private final JLabel scoreLabel;

    int[][] val = new int[4][4];
    int score = 0;

    public GamePanel() {
        JFrame jFrame = new JFrame("2048小游戏");
        jFrame.setSize(400, 530);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        scoreLabel = new JLabel("得分: " + score, JLabel.CENTER);
        scoreLabel.setFont(new Font("黑体", Font.BOLD, 15));
        scoreLabel.setBounds(0, 0, 120, 30);

        jFrame.add(scoreLabel);

        JLabel tipsLabel = new JLabel("按 ↑ ↓ ← → 玩, esc 重置游戏");

        tipsLabel.setFont(new Font("宋体", Font.BOLD, 20));
        tipsLabel.setBounds(10, 60, 400, 20);
        jFrame.add(tipsLabel);

        jFrame.addKeyListener(this);
        jFrame.add(this);

        jFrame.setVisible(true);
        init();
    }

    public void init() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                val[i][j] = 0;
            }
        }
        createNum();
        createNum();
    }

    /**
     * 生成新数
     */
    private void createNum() {
        List<List<Integer>> emptyList = getEmptyList();

        if (!emptyList.isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(emptyList.size());
            val[emptyList.get(i).get(0)][emptyList.get(i).get(1)] = 2;
        }
    }

    /**
     * 获取空元素位置
     *
     * @return 空元素下标列表
     */
    private List<List<Integer>> getEmptyList() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (val[i][j] == 0) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        return result;
    }

    private boolean judge() {
        if (!getEmptyList().isEmpty()) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // 判断是否存在可合并的方格
                if (val[i][j] == val[i][j + 1] || val[i][j] == val[i + 1][j]) {
                    return false;
                }
            }
        }
        // 找第四行
        for (int j = 0; j < 3; j++) {
            if (val[3][j] == val[3][j + 1]) return false;
        }
        // 找第四列
        for (int i = 0; i < 3; i++) {
            if (val[i][3] == val[i + 1][3]) return false;
        }
        return true;
    }

    private void moveLeft() {
        for (int i = 0; i < 4; i++) {
            boolean merge = true;
            for (int j = 1; j < 4; j++) {
                if (val[i][j] != 0) {
                    int index = j - 1;
                    while (index >= 0) {
                        if (val[i][index] != 0) {
                            break;
                        }
                        index--;
                    }
                    if (index == -1) { // 前面没有非 0
                        val[i][0] = val[i][j];
                        val[i][j] = 0;
                    } else { // 前面有非 0
                        if (val[i][index] != val[i][j] || !merge) { // 不相同 / 已经合并过, 替换非 0 后一位
                            val[i][++index] = val[i][j];
                            if (index != j) {
                                val[i][j] = 0;
                            }
                        } else {
                            val[i][index] <<= 1;
                            score += val[i][index];
                            merge = false; // 每次移动只能合并一次
                            val[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private void moveRight() {
        for (int i = 0; i < 4; i++) {
            boolean merge = true;
            for (int j = 2; j >= 0; j--) {
                if (val[i][j] != 0) {
                    int index = j + 1;
                    while (index < 4) {
                        if (val[i][index] != 0) {
                            break;
                        }
                        index++;
                    }
                    if (index == 4) {
                        val[i][--index] = val[i][j];
                        val[i][j] = 0;
                    } else {
                        if (val[i][index] != val[i][j] || !merge) {
                            val[i][--index] = val[i][j];
                            if (index != j) {
                                val[i][j] = 0;
                            }
                        } else {
                            val[i][index] <<= 1;
                            score += val[i][index];
                            merge = false;
                            val[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private void moveDown() {
        for (int i = 0; i < 4; i++) {
            boolean merge = true;
            for (int j = 2; j >= 0; j--) {
                if (val[j][i] != 0) {
                    int index = j + 1;
                    while (index < 4) {
                        if (val[index][i] != 0) {
                            break;
                        }
                        index++;
                    }
                    if (index == 4) {
                        val[--index][i] = val[j][i];
                        val[j][i] = 0;
                    } else {
                        if (val[index][i] != val[j][i] || !merge) {
                            val[--index][i] = val[j][i];
                            if (index != j) {
                                val[j][i] = 0;
                            }
                        } else {
                            val[index][i] <<= 1;
                            score += val[index][i];
                            merge = false;
                            val[j][i] = 0;
                        }
                    }
                }
            }
        }
    }

    private void moveUp() {
        for (int i = 0; i < 4; i++) {
            boolean merge = true;
            for (int j = 1; j < 4; j++) {
                if (val[j][i] != 0) {
                    int index = j - 1;
                    while (index >= 0) {
                        if (val[index][i] != 0) {
                            break;
                        }
                        index--;
                    }
                    if (index == -1) {
                        val[++index][i] = val[j][i];
                        val[j][i] = 0;
                    } else {
                        if (val[index][i] != val[j][i] || !merge) {
                            val[++index][i] = val[j][i];
                            if (index != j) {
                                val[j][i] = 0;
                            }
                        } else {
                            val[index][i] <<= 1;
                            score += val[index][i];
                            merge = false;
                            val[j][i] = 0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0xBBADA0));
        g2d.fillRoundRect(3, 100, 380, 380, BLOCK_ARC, BLOCK_ARC);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g2d.setColor(new Color(0xeee4da));

                g2d.fillRoundRect(
                        BLOCK_GAP + (BLOCK_GAP + BLOCK_SIZE) * j,
                        BLOCK_GAP + 100 + (BLOCK_GAP + BLOCK_SIZE) * i,
                        BLOCK_SIZE, BLOCK_SIZE,
                        BLOCK_ARC, BLOCK_ARC
                );

                if (val[i][j] != 0) {
                    g2d.setFont(new Font("宋体", Font.BOLD, 20));

                    g2d.setColor(Color.BLACK);

                    g2d.drawString(val[i][j] + "",
                            BLOCK_GAP + BLOCK_SIZE / 2 + (BLOCK_GAP + BLOCK_SIZE) * j,
                            BLOCK_GAP + 100 + BLOCK_SIZE / 2 + (BLOCK_GAP + BLOCK_SIZE) * i
                    );
                }
            }
        }
        scoreLabel.setText("得分: " + score);

        // game over
        if (judge()) {
            g.setColor(new Color(64, 64, 64, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("黑体", Font.BOLD, 30));
            FontMetrics fms = getFontMetrics(new Font("黑体", Font.BOLD, 30));
            String value = "Game Over!";
            g.drawString(value, (getWidth()-fms.stringWidth(value)) / 2, getHeight() / 2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                moveUp();
                createNum();
            }
            case KeyEvent.VK_DOWN -> {
                moveDown();
                createNum();
            }
            case KeyEvent.VK_LEFT -> {
                moveLeft();
                createNum();
            }
            case KeyEvent.VK_RIGHT -> {
                moveRight();
                createNum();
            }
            case KeyEvent.VK_ESCAPE -> init();
        }
        /* 调用 paint() */
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
