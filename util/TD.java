package util;

public class TD {
    public static Imagem transDistancia(Imagem in) {
        int alt = in.getAltura();
        int larg = in.getLargura();
        int matIn[][][] = in.getMatriz();
        int matOut[][][] = new int[1][alt][larg];
        int inc;

        for (int y = 0; y < alt; y++) {
            for (int x = 0; x < larg; x++) {
                inc = 0;
                if (matIn[0][y][x] == 0) matOut[0][y][x] = 0; // fundo da imagem
                else {
                    while (true) {
                        inc++;

                        if (matIn[0][y][x+inc] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((x-(x+inc))*((x-(x+inc))))));
                            break;
                        } else if (matIn[0][y][x-inc] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((x-(x-inc))*((x-(x-inc))))));
                            break;
                        } else if (matIn[0][y+inc][x] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((y-(y+inc))*((y-(y+inc))))));
                            break;
                        } else if (matIn[0][y-inc][x] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((y-(y-inc))*((y-(y-inc))))));
                            break;
                        } else if (matIn[0][y+inc][x+inc] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((x-(x+inc))*((x-(x+inc))))+((y-(y+inc))*((y-(y+inc))))));
                            break;
                        } else if (matIn[0][y-inc][x-inc] == 0) {
                            matOut[0][y][x] = (int)(Math.sqrt(((x-(x-inc))*((x-(x-inc))))+((y-(y-inc))*((y-(y-inc))))));
                            break;
                        }
                    }
                }
            }
        }
        return minMax(matOut);
    }

    public static Imagem minMax (int [][][]matIn) {
        int nCanais = matIn.length;
        int alt = matIn[0].length;
        int larg = matIn[0][0].length;
        int matOut[][][] = new int[nCanais][alt][larg];
        int min, max;

        for (int c = 0; c < nCanais; c++) {
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
            for (int y = 0; y < alt; y++) {
                for (int x = 0; x < larg; x++) {
                    if (matIn[c][y][x] < min) min = matIn[c][y][x];
                    if (matIn[c][y][x] > max) max = matIn[c][y][x];
                }
            }

            for (int y = 0; y < alt; y++) {
                for (int x = 0; x < larg; x++) {
                    if ((max-min) != 0) matOut[c][y][x] = (int)((matIn[c][y][x]-min)*255)/(max-min);
                    else matOut[c][y][x] = (int)((matIn[c][y][x]-min)*255);
                }
            }
        }
        return new Imagem(matOut);
    }
}
