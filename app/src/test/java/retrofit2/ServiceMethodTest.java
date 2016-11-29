package retrofit2;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by 西野奈留NishinoNaru on 2016/11/22.
 */
public class ServiceMethodTest {

    @Test
    public void testString() {


        String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";

        System.out.println(PARAM);
    }

    @Test
    public void testRegularExpressions1() {
        String str = "13676485968 " +
                "15395847285 " +
                "17395847285 " +
                "16323243256";

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher("(13|15|17|16)\\d{9}");
        while (m.find()) {
            System.out.println(m.group());
        }
    }

    @Test
    public void testRegularExpressions2() {
        String str = "33 " +
                "13395847285 " +
                "15395847285 " +
                "17395847285 " +
                "16323243256";
        System.out.println("---------------------------");

        Pattern p = Pattern.compile("(13|15|17|16)\\d{9}");
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
        }
        System.out.println("---------------------------");

        Pattern p2 = Pattern.compile("(13|15)\\d{9}");
        Matcher m2 = p2.matcher(str);
        while (m2.find()) {
            System.out.println(m2.group());
        }
        System.out.println("---------------------------");

        Pattern p3 = Pattern.compile("15\\d{9}");
        Matcher m3 = p3.matcher(str);
        while (m3.find()) {
            System.out.println(m3.group());
        }
        System.out.println("---------------------------");
    }

    @Test
    public void testRegularExpressions3() {
        String str = "0 19395847285 " +
                "13495847285 " +
                "16323243256";
        Pattern p = Pattern.compile("\\d*");
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println(m.group());
        }
        System.out.println("---------------------------");
    }

    @Test
    public void testRegularExpression4() {
        String str = "abcmmmcc";
        System.out.println(str);
//        String str = "abc";
        Pattern p = Pattern.compile("abcmmm");
        Matcher m = p.matcher(str);
        while (m.find()) {
            System.out.println("result:");
            System.out.println(m.group());
        }
    }

    @Test
    public void testDaKuoHao() {
        String str = "\\{";
//        System.out.println(str);
//        System.out.println("{}");
        System.out.println("a\\nb");
    }

    @Test
    public void testDaKuoHao1() {
        String str = "spfdjsklah g rqep ygodashgj";
        System.out.println("\\(grqep\\)"); // print     \(grqep\)
        Pattern p = Pattern.compile("r\\w");
        System.out.println(p.matcher(str).group());
    }

    @Test
    public void testMail() {
        String str = "abc@qq.com mmmabc@qq.org ";
        System.out.println(str + "\n");
        Pattern p = Pattern.compile("\\w*@\\w+.(com|org)");
        Matcher m = p.matcher(str);
        while (m.find())
            System.out.println(m.group());
    }


}