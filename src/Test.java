import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            scan();
        }
    }

    public static void scan() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Nombre");
            System.out.print("> ");
            int a = sc.nextInt();
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
