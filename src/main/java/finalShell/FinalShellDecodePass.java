package finalShell;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

public class FinalShellDecodePass {

    //todo DIR_NAME改为你的目录位置
    public static String DIR_NAME = "E:\\GreenSoft\\finalshell\\conn\\";

    /**
     * FinalShell解析密码入口文件
     *
     * @param args
     */
    public static void main(String[] args) {
        DIR_NAME = (ArrayUtil.isNotEmpty(args)) ? args[0] : DIR_NAME;
        ArrayList<ConnPath> pathFiles = getConnPathSubItems(DIR_NAME);
        printPassword(pathFiles);
    }

    /**
     * 打印密码
     *
     * @param pathFiles
     */
    public static void printPassword(ArrayList<ConnPath> pathFiles) {
        for (ConnPath connPath : pathFiles) {
            if (connPath.isFile()) {
                //文件
                String fileText = readFileText(connPath.getFullName());
                JSONObject jsonObject = JSONObject.parseObject(fileText);
                String password = "";
                try {
                    password = decodePass(jsonObject.getString("password"));
                } catch (Exception ignored) {
                    password = "密码解析错误";
                }
                String host = jsonObject.getString("host");
                System.out.println(host + ": " + password);
            } else {
                //文件夹
                ArrayList<ConnPath> innerPathFiles = getConnPathSubItems(connPath.getFullName());
//                System.out.println("begin...");
                printPassword(innerPathFiles);
//                System.out.println("end...");
            }
        }
    }

    /**
     * 读取文件夹下所有直接子文件夹及文件
     *
     * @param dirname 目录
     * @return 文件列表
     */
    public static ArrayList<ConnPath> getConnPathSubItems(String dirname) {
        File dir = new File(dirname);
        File[] files = dir.listFiles();
        ArrayList<ConnPath> connPathArrayList = new ArrayList<>();
        assert files != null;
        ConnPath connPath;
        for (File file : files) {
            if (file.isDirectory()) {
                //目录
                connPath = new ConnPath(file.getAbsolutePath(), ConnPath.PathType.DIR);
            } else {
                //文件
                connPath = new ConnPath(file.getAbsolutePath(), ConnPath.PathType.FILE);
            }
            connPathArrayList.add(connPath);
        }
        return connPathArrayList;
    }

    //读取json文件
    public static String readFileText(String fileName) {
        String fileText = FileUtil.readString(new File(fileName), Charset.defaultCharset());
        return fileText;
    }

    public static byte[] desDecode(byte[] data, byte[] head) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(head);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(data);
    }

    public static String decodePass(String data) throws Exception {
        if (data == null) {
            return null;
        } else {
            String rs = "";
            byte[] buf = Base64.getDecoder().decode(data);
            byte[] head = new byte[8];
            System.arraycopy(buf, 0, head, 0, head.length);
            byte[] d = new byte[buf.length - head.length];
            System.arraycopy(buf, head.length, d, 0, d.length);
            byte[] bt = desDecode(d, ranDomKey(head));
            rs = new String(bt);

            return rs;
        }
    }

    public static byte[] ranDomKey(byte[] head) {
        long ks = 3680984568597093857L / (long) (new Random((long) head[5])).nextInt(127);
        Random random = new Random(ks);
        int t = head[0];

        for (int i = 0; i < t; ++i) {
            random.nextLong();
        }

        long n = random.nextLong();
        Random r2 = new Random(n);
        long[] ld = new long[]{(long) head[4], r2.nextLong(), (long) head[7], (long) head[3], r2.nextLong(), (long) head[1], random.nextLong(), (long) head[2]};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        long[] var15;
        var15 = ld;
        int var14 = ld.length;

        for (int var13 = 0; var13 < var14; ++var13) {
            long l = var15[var13];

            try {
                dos.writeLong(l);
            } catch (IOException var18) {
                var18.printStackTrace();
            }
        }

        try {
            dos.close();
        } catch (IOException var17) {
            var17.printStackTrace();
        }

        byte[] keyData = bos.toByteArray();
        keyData = md5(keyData);
        return keyData;
    }

    public static byte[] md5(byte[] data) {
        String ret = null;
        byte[] res = null;

        try {
            MessageDigest m;
            m = MessageDigest.getInstance("MD5");
            m.update(data, 0, data.length);
            res = m.digest();
            ret = new BigInteger(1, res).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }
}
