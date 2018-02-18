package com.testmydata.util;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptAndDecrypt {

	Cipher cipher;

	// password for encryption
	final static String strPassword = "@D2ws675$sk+V#2N";

	// put this as key in AES
	static SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "AES");

	public EncryptAndDecrypt() {

	}

	public static String encryptData(String input) {
		String output = null;
		try {
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes());
			// Whatever you want to encrypt/decrypt
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// You can use ENCRYPT_MODE (ENCRYPTunderscoreMODE) or DECRYPT_MODE
			// (DECRYPT underscore MODE)
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			// encrypt data
			byte[] ecrypted = cipher.doFinal(input.getBytes());

			// encode data using standard encoder
			output = new BASE64Encoder().encode(ecrypted);

			// System.out.println("Orginal tring: " + input);
			// System.out.println("Encripted string: " + output);

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * @param input
	 * @return
	 */
	public static String decryptData(String input) {
		String doutput = null;
		try {
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes());
			// Whatever you want to encrypt/decrypt using AES /CBC padding
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// You can use ENCRYPT_MODE or DECRYPT_MODE
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			// decode data using standard decoder
			byte[] output = new BASE64Decoder().decodeBuffer(input);

			// Decrypt the data
			byte[] decrypted = cipher.doFinal(output);

			// System.out.println("Original string: " +new String(input));

			// decryptedData .;
			// System.out.println("Decrypted string: " + new String(decrypted));

			doutput = new String(decrypted);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doutput;

	}

	// public static void main(String args[]) {
	// // encryptData("jdbc:mysql://40.117.94.2:3306/");
	//
	// decryptData("q7dqpXcL8THRn+yh81MaKkDRHuayiDeQRB2ZhSnla/0=");
	// decryptData("te7bnf3dpnH28dAqPMEbxA==");
	// decryptData("ubzPqP5asE39q9gmRHMJVN2seBBJ0XlFYt8HX8fOlqg=");
	// decryptData("CdJmiO/TRAy2tnHpnkEkUe/HaBJb9Q7fFFvKcoNHlZQ=");
	// decryptData("q0Dw61lCAnjGvRpkx1Jrdg==");
	// decryptData("AuCnHeyrumCnIFdLtvi0Gg==");
	// }

}
