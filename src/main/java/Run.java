import com.springdev.Entity.RoleName;

public class Run {

    public static void main(String[] args) {

        String s = RoleName.ADMIN.name();
        int ss = RoleName.SELLER.ordinal();

        System.out.println(s);
        System.out.println(ss);

        char []c = {'e', 't'};
        String st = new String(c);

        System.out.println(st);
    }
}
