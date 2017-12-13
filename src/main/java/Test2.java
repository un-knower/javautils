import com.cenrise.utils.Const;
import org.dom4j.DocumentException;

import java.io.IOException;

public class Test2 {
    public static void main(String[] args) throws IOException, DocumentException {
        //定义正则表达式来搜索中文字符的转义符号
        /*Pattern compile = Pattern.compile("&#.*?;");
        //测试用中文字符
        String sourceString =
                "C&#x96C6;&#x56E2;&#x5929;c&#x6D25;&#x5927;&#x5510;&#x56FD;&#x9645;&#x76D8;&#x5C71;&#x53D1;&#x7535;&#x6709;&#x9650;&#x8D23;&#x4EFB;&#x516C;&#x53F8;";
        Matcher matcher = compile.matcher(sourceString);
        //循环搜索并转换替换
        while (matcher.find()) {
            String group = matcher.group();
            //获得16进制的码
            String hexcode = "0" + group.replaceAll("(&#|;)", "");
            //字符串形式的16进制码转成int并转成char并替换到源串中
            sourceString = sourceString.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");
        }
        System.out.println(sourceString);*/
        String sourceString =
                "C&#x96C6;&#x56E2;&#x5929;c&#x6D25;&#x5927;&#x5510;&#x56FD;&#x9645;&#x76D8;&#x5C71;&#x53D1;&#x7535;&#x6709;&#x9650;&#x8D23;&#x4EFB;&#x516C;&#x53F8;";
        Test2 test2 = new Test2();
        System.out.println(Const.transNCR(sourceString));
    }


}
