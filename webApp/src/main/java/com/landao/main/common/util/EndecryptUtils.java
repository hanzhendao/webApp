package com.landao.main.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.shiro.codec.Base64;

public final class EndecryptUtils {
	/**
	 * base64加密字节数组成字符串
	 * @param bytes
	 * @return
	 */
	public static String encodeBytesByBase64(byte[] bytes) {
		return Base64.encodeToString(bytes);
	}
	
	/**
	 * base64加密二进制数据
	 * @param binaryData 二进制数据
	 * @param isChunked	是否大量数据
	 * @return
	 */
	public static byte[] encodeBinaryDataByBase64(byte[] binaryData,boolean isChunked) {
		return Base64.encode(binaryData, isChunked);
	}
	
	/**
	 * 解密包含base64数据的字节数组
	 * @param base64Data
	 * @return
	 */
	public static byte[] decodeBinaryDataByBase64(byte[] base64Data) {
		return Base64.decode(base64Data);
	}
	
	/**
	 * 解密base64加密后的字符串
	 * @param base64Encoded
	 * @return
	 */
	public static byte[] decodeStringByBase64(String base64Encoded) {
		return Base64.decode(base64Encoded);
	}
	
	/**
	 * 解密base64加密后的字节数组
	 * @param base64Encoded
	 * @return
	 */
	public static String decodeBytesByBase64(byte[] base64Encoded) {
		return Base64.decodeToString(base64Encoded);
	}
	
	/**
	 * 解密base64加密后的字符串
	 * @param base64Encoded
	 * @return
	 */
	public static String decodeString2ByBase64(String base64Encoded) {
		return Base64.decodeToString(base64Encoded);
	}
	
	/**
	 * 检测是不是base64加密后的字节数组
	 * @param arrayOctect
	 * @return
	 */
	public static boolean isBase64(byte[] arrayOctect) {
		return Base64.isBase64(arrayOctect);
	}
	
	public static void main(String[] args) throws IOException {
		String test=encodeBytesByBase64("huawei".getBytes());		//加密后的字符串
		System.out.println(decodeString2ByBase64(test));
		File  file=new File("e:/软件.rar");
		InputStream isInputStream=new FileInputStream(file);
		byte[] data=new byte[isInputStream.available()];
		isInputStream.read(data);
		isInputStream.close();
		byte[] base64img=encodeBinaryDataByBase64(data, true);
		System.out.println(isBase64(base64img));
		byte[] forim=decodeBinaryDataByBase64(base64img);
		OutputStream outputStream=new FileOutputStream("e:/软糖.rar");
		outputStream.write(forim);
		outputStream.close();
	}
}
