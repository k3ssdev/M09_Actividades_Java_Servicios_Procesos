package Actividad_3_ejercicio_1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class encriptar {
  
   public static void main(String[] args) throws Exception {
      // Generamos el par de claves.
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      PublicKey publicKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();

      // Guardamos la clave publica en un fichero y la recuperamos
      guardarLlave(publicKey, "clavepublica.dat");
      publicKey = leerLlavePublica("clavepublica.dat");

      // Guardamos la clave privada en un fichero y la recuperamos
      guardarLlave(privateKey, "claveprivada.dat");
      privateKey = leerLlavePrivada("claveprivada.dat");

      // Creamos el objeto cifrador para encriptar/desencriptar
      Cipher cifrador = Cipher.getInstance("RSA");

      // mensaje  a encriptar
      String mensaje = "Mensaje a encriptar";
      System.out.println("Mensaje a encriptar: "+mensaje);


      // Se encripta
      cifrador.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encriptado = cifrador.doFinal(mensaje.getBytes());

      // Escribimos el encriptado con caracteres visibles
      System.out.println("Mensaje encriptado en formato hexadecimal");
      for (byte b : encriptado) {
         System.out.print(Integer.toHexString(0xFF & b));
      }
      System.out.println();

      // Desencriptar
      cifrador.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] bytesDesencriptados = cifrador.doFinal(encriptado);
      String mensajeDesencriptado = new String(bytesDesencriptados);

      // Se escribe el mensajeo desencriptado
      System.out.println("Mensaje Desencriptado: "+mensajeDesencriptado);

   }

   private static PublicKey leerLlavePublica(String nombreFichero) throws Exception {
      FileInputStream fis = new FileInputStream(nombreFichero);
      int numBtyes = fis.available();
      byte[] bytes = new byte[numBtyes];
      fis.read(bytes);
      fis.close();

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      KeySpec keySpec = new X509EncodedKeySpec(bytes);
      PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
      return keyFromBytes;
   }

   private static PrivateKey leerLlavePrivada(String nombreFichero) throws Exception {
      FileInputStream fis = new FileInputStream(nombreFichero);
      int numBtyes = fis.available();
      byte[] bytes = new byte[numBtyes];
      fis.read(bytes);
      fis.close();

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
      PrivateKey keyFromBytes = keyFactory.generatePrivate(keySpec);
      return keyFromBytes;
   }

   private static void guardarLlave(Key key, String nombreFichero) throws Exception {
      byte[] publicKeyBytes = key.getEncoded();
      FileOutputStream fos = new FileOutputStream(nombreFichero);
      fos.write(publicKeyBytes);
      fos.close();
   }
}
