package finallShellActive;

import cn.hutool.crypto.digest.MD5;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * FinallShell 离线激活
 */
public class FinalShellActive {
    /**
     * Main方法
     * 操作步骤：随意输入一个用户名和密码，选择离线激活
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        System.out.print("请输入FinalShell的离线机器码：");
        Scanner reader = new Scanner(System.in);
        String machineCode = reader.nextLine();
        generateKey(machineCode);
    }

    /**
     * 生成激活码
     * @param hardwareId 硬件Id（机器码）
     * @throws NoSuchAlgorithmException
     */
    public static void generateKey(String hardwareId) throws NoSuchAlgorithmException {
        String proKey = transform(61305 + hardwareId + 8552);
        String pfKey = transform(2356 + hardwareId + 13593);
        System.out.println("高级版激活码：请将此行复制到离线激活中：" + proKey);
        System.out.println("专业版激活码："+pfKey);
    }

    /**
     * 转换
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String transform(String str) throws NoSuchAlgorithmException {
        String md5 = MD5.create().digestHex(str);
        return md5.substring(8, 24);
    }

}