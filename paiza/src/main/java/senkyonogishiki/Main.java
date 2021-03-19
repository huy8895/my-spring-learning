package senkyonogishiki;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int N = Integer.parseInt(line.split(" +")[0]);
        int K = Integer.parseInt(line.split(" +")[1]);
        ArrayList<Ghe> listDang = new ArrayList<>();
        int[] listGhe_done = new int[N];

        for (int id_dang = 0; id_dang < N; id_dang ++) {
            int so_phieu = sc.nextInt();
            listDang.add(new Ghe(so_phieu,id_dang,0));
        }
        listDang.sort((o1, o2) -> o2.so_phieu - o1.so_phieu);
        Integer min = listDang.get(listDang.size() - 1).so_phieu;
        int first = listDang.get(0).so_phieu;
        int id_hang = 1;
        List<Ghe> listGheAll = new ArrayList<>();
        while (first > min) {
            List<Ghe> listGhe = new ArrayList<>();
            for (int id_dang = 0; id_dang < listDang.size(); id_dang++) {
                int so_phieu = listDang.get(id_dang).so_phieu / id_hang;
                Ghe ghe = new Ghe(so_phieu, listDang.get(id_dang).id_dang, id_hang);
                listGhe.add(ghe);
                listGheAll.add(ghe);
            }
            id_hang++;
            first = listGhe.get(0).so_phieu;
        }

        listGheAll.stream()
                .sorted((o1, o2) -> {
                    if (o1.so_phieu == o2.so_phieu){
                        return o1.id_dang - o2.id_dang;
                    }
                    return o2.so_phieu - o1.so_phieu;
                        }
                )
                .limit(K)
                .sorted(Comparator.comparingInt(o -> o.id_dang))
        .forEach(ghe -> {
            int id_dang = ghe.id_dang;
            listGhe_done[id_dang] = listGhe_done[id_dang] + 1;
        });

        for (int i : listGhe_done) {
            System.out.println(i);
        }
    }

    static class Ghe{
        int so_phieu;
        int id_dang;
        int vi_tri_hang;

        public Ghe() {
        }

        public Ghe(int so_phieu, int id_dang, int vi_tri_hang) {
            this.so_phieu = so_phieu;
            this.id_dang = id_dang;
            this.vi_tri_hang = vi_tri_hang;
        }
    }
}


