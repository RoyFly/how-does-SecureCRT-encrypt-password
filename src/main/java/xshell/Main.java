package xshell;

/**
 * 测试类
 */
public class Main {
    public static void main(String[] args) {
        // 省略加密密码代码

        // 2.解密密码
        /* 加密后的密码 */
//        String pwd2 = "将密码放到这里";
        String pwd2 = "GUtjXiNlXDhft1hqAK4AzFXAo5UfuSxM";
        String str2 = ShellPasswordUtils.decrypt(pwd2);
        if (str2 != null) {
            System.out.println("解密结果: " + str2);
        }
    }
}