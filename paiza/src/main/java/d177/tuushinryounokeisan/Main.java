package d177.tuushinryounokeisan;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int N = Integer.parseInt(line.split(" +")[0]);
        int K = Integer.parseInt(line.split(" +")[1]);
        System.out.println(N*K);
    }
}
