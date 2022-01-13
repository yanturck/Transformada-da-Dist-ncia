import util.Imagem;
import util.TD;

public class Main {
    static String IMAGEM_A = "img/a.png";
    static String IMAGEM_B = "img/b.png";

    public static void main(String args[]) {
        td();
    }

    public static void td() {
        Imagem img1 = new Imagem(Main.IMAGEM_A);
        Imagem img2 = new Imagem(Main.IMAGEM_B);
        img1.mostrar("Imagem Original");
        img2.mostrar("Imagem Original");
        TD.transDistancia(img1).mostrar("Transformada da Distância");
        TD.transDistancia(img2).mostrar("Transformada da Distância");
    }
}