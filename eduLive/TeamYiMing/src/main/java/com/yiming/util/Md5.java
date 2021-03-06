package com.yiming.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5绫�
 * @author jikai
 * @since 2014-6-6
 */
public class Md5 {
    
    // 鍏ㄥ眬鏁扮粍
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public Md5() {
    }

    /**
     * 澶勭悊瀛楄妭鍨嬫暟鎹�
     * @author jikai
     * @since 2014-6-6
     * @param bByte 瀛楄妭
     * @return 鏁板瓧璺熷瓧绗︿覆
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        	//System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }


    /**
     * 杞崲瀛楄妭鏁扮粍涓�16杩涘埗瀛椾覆
     * @author jikai
     * @since 2014-6-6
     * @param bByte 瀛楄妭鏁扮粍
     * @return 16杩涘埗瀛椾覆
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * md5鍔犲瘑
     * @author jikai
     * @since 2014-6-6
     * @param strObj 瀛楃涓�
     * @return md5鐮�
     */
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 璇ュ嚱鏁拌繑鍥炲�间负瀛樻斁鍝堝笇鍊肩粨鏋滅殑byte鏁扮粍
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}
