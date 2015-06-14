package ro.diana.util;

/**
 * Created by tekin.omer on 6/7/2015.
 */
public class DUtils {
    public static String getExtension(String name) {
        return name.substring(name.lastIndexOf('.') + 1, name.length());
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        for(int i = 0; i < 1000; i++) {
            Thread.sleep(3);
            getExtension("dsadsa'd'sad'sa/d/sad/sa/d/sad/sa/d.satxt");
        }

        System.out.println("a durat " + (System.currentTimeMillis() - start) / 1000 + " secunde");
    }
}
