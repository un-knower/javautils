import com.cenrise.utils.Const;

public class Test {
    @org.junit.Test
    public void testPattern01() {
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
        System.out.println(Const.transNCR(sourceString));
    }

    @org.junit.Test
    public void sysInfo() {
        System.out.println("Java运行时环境版本:\n" + System.getProperty("java.version"));
        System.out.println("Java 运行时环境供应商:\n" + System.getProperty("java.vendor"));
        System.out.println("Java 供应商的URL:\n" + System.getProperty("java.vendor.url"));
        System.out.println("Java安装目录:\n" + System.getProperty("java.home"));
        System.out.println("Java 虚拟机规范版本:\n" + System.getProperty("java.vm.specification.version"));
        System.out.println("Java 类格式版本号:\n" + System.getProperty("java.class.version"));
        System.out.println("Java类路径：\n" + System.getProperty("java.class.path"));
        System.out.println("加载库时搜索的路径列表:\n" + System.getProperty("java.library.path"));
        System.out.println("默认的临时文件路径:\n" + System.getProperty("java.io.tmpdir"));
        System.out.println("要使用的 JIT 编译器的名称:\n" + System.getProperty("java.compiler"));
        System.out.println("一个或多个扩展目录的路径:\n" + System.getProperty("java.ext.dirs"));
        System.out.println("操作系统的名称:\n" + System.getProperty("os.name"));
        System.out.println("操作系统的架构:\n" + System.getProperty("os.arch"));
        System.out.println("操作系统的版本:\n" + System.getProperty("os.version"));
        System.out.println("文件分隔符（在 UNIX 系统中是“/”）:\n" + System.getProperty("file.separator"));
        System.out.println("路径分隔符（在 UNIX 系统中是“:”）:\n" + System.getProperty("path.separator"));
        System.out.println("行分隔符（在 UNIX 系统中是“/n”）:\n" + System.getProperty("line.separator"));
        System.out.println("用户的账户名称:\n" + System.getProperty("user.name"));
        System.out.println("用户的主目录:\n" + System.getProperty("user.home"));
        System.out.println("用户的当前工作目录:\n" + System.getProperty("user.dir"));

        System.out.println("当前的classpath的绝对路径的URI表示法：---->> :\n" + Thread.currentThread().getContextClassLoader().getResource(""));

//        System.out.println("得到的是当前类FileTest.class文件的URI目录。不包括自己！:\n"+TestSystemproperty.class.getResource(""));
//        System.out.println("得到的是当前的classpath的绝对URI路径。:\n"+ TestSystemproperty.class.getResource("/"));


        String str = "/home/appgroup/kettle/pdi-ce-5.0.1.A-stable/data-integration/MyKtrs/";
        String strOne = "/home/appgroup/kettle/pdi-ce-5.0.1.A-stable/data-integration/MyKtrs/fdsafda/fsafdsa2231";

        System.out.println(strOne.replaceAll(str, ""));
    }
}
