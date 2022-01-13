package util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class Imagem {

    public static final String JPEG = "JPEG";
    public static final String PNG = "PNG";
    public static final int GRAY = BufferedImage.TYPE_BYTE_GRAY;
    public static final int RGB = BufferedImage.TYPE_INT_RGB;
    private BufferedImage image = null;
    private WritableRaster raster = null;
    private int tipo;
    private int altura;
    private int largura;

    public Imagem(String path) {
        try {
            image = ImageIO.read(new File(path));
            raster = image.getRaster();
            tipo = image.getType();
            altura = image.getHeight();
            largura = image.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Imagem(int altura, int largura, int tipo) {
        image = new BufferedImage(largura, altura, tipo);
        raster = image.getRaster();
        this.tipo = tipo;
        this.altura = altura;
        this.largura = largura;
    }

    public Imagem(int mat[][][]) {
        this.tipo = mat.length == 1 ? GRAY : RGB;
        this.altura = mat[0].length;
        this.largura = mat[0][0].length;
        image = new BufferedImage(largura, altura, tipo);
        raster = image.getRaster();
        this.setMatriz(mat);
    }

    public Imagem toGray() {
        int matIn[][][] = getMatriz();
        int matOut[][][] = new int[1][altura][largura];
        int R, G, B, gray;
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                R = matIn[0][y][x];
                G = matIn[1][y][x];
                B = matIn[2][y][x];
                gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                matOut[0][y][x] = gray;
            }
        }
        return new Imagem(matOut);
    }

    public Imagem toRGB() {
        int matIn[][][] = getMatriz();
        int matOut[][][] = new int[3][altura][largura];
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                matOut[0][y][x] = matIn[0][y][x];
                matOut[1][y][x] = matIn[0][y][x];
                matOut[2][y][x] = matIn[0][y][x];
            }
        }
        return new Imagem(matOut);
    }

    public int getAltura() {
        return altura;
    }

    public int getLargura() {
        return largura;
    }

    public int getNCanais() {
        return tipo < 10 ? 3 : 1;
    }

    public int[][][] getMatriz() {
        int nCanais = getNCanais();
        int m[][][] = new int[nCanais][altura][largura];
        for (int canal = 0; canal < nCanais; canal++) {
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    m[canal][y][x] = raster.getSample(x, y, canal);
                }
            }
        }
        return m;
    }

    public void setMatriz(int[][][] mat) {
        int nCanais = getNCanais();
        int pixel[] = new int[nCanais];
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                for (int c = 0; c < nCanais; c++) {
                    pixel[c] = mat[c][y][x];
                }
                raster.setPixel(x, y, pixel);
            }
        }
    }

    protected BufferedImage getBuffer() {
        return image;
    }

    public void salvar(String nome, String tipoSaida) {
        try {
            ImageIO.write(image, tipoSaida, new File(nome));
        } catch (IOException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrar(String titulo) {
        new IFrame(titulo, this).setVisible(true);
    }

    public void mostrar() {
        new IFrame("", this).setVisible(true);
    }
}

class IFrame extends JFrame {

    private static final long serialVersionUID = 4074185320511690112L;
    static int incremento = 0;

    private int xIni = 0;
    private int yIni = 0;
    private int xFim = 0;
    private int yFim = 0;
    JScrollPane scroll = null;

    final class GCanvas extends JPanel {

        private final Imagem img;

        public GCanvas(Imagem img) {
            this.img = img;
            setSize(getPreferredSize());
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    scrollMouseClicked(evt);
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    scrollMousePressed(evt);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollMouseReleased(evt);
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(img.getLargura(), img.getAltura());
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(img.getBuffer(), 0, 0, this);
        }

        public void scrollMouseClicked(MouseEvent e) {
        }

        public void scrollMousePressed(MouseEvent e) {
            xIni = e.getX();
            yIni = e.getY();
        }

        public void scrollMouseReleased(MouseEvent e) {
            xFim = e.getX();
            yFim = e.getY();
            int dx = xFim - xIni;
            int dy = yFim - yIni;
            int posAtual = scroll.getHorizontalScrollBar().getValue();
            scroll.getHorizontalScrollBar().setValue(posAtual + dx);
            posAtual = scroll.getVerticalScrollBar().getValue();
            scroll.getVerticalScrollBar().setValue(posAtual + dy);
        }

        public void scrollMouseEntered(MouseEvent e) {
        }

        public void scrollMouseExited(MouseEvent e) {
        }
    }

    public IFrame(String titulo, Imagem img) //constructor
    {
        super(titulo);
        scroll = new JScrollPane(new GCanvas(img));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setMaximumSize(new Dimension(img.getLargura(), img.getAltura()));
        getContentPane().add(scroll);
        setSize(img.getLargura() + 20, img.getAltura() + 45);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(incremento, incremento);
        setVisible(true);
        incremento += 20;//desloca as várias imagens na diagonal ao serem mostradas
    }
}
