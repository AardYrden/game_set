import org.junit.jupiter.api.Test;

public class Game2048Test {
    @Test
    public void test() {
        int[][] val = {
                {4, 2, 2, 0},
                {32, 4, 2, 2},
                {4, 0, 0, 4},
                {4, 0, 0, 2}
        };

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
                        if (val[i][index] != val[i][j] || !merge) { // 不相同, 替换非 0 后一位
                            val[i][++index] = val[i][j];
                            if (index != j) {
                                val[i][j] = 0;
                            }
                        } else {
                            val[i][index] <<= 1;
                            merge = false; // 每次移动智能合并一次
                            val[i][j] = 0;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(val[i][j] + " ");
            }
            System.out.println();
        }
    }
}
