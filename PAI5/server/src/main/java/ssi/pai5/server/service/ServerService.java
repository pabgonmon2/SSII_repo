package ssi.pai5.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ssi.pai5.server.model.Certificado;
import ssi.pai5.server.model.Peticion;
import ssi.pai5.server.repository.CertificadoRepository;
import ssi.pai5.server.repository.PeticionRepository;

@Service
public class ServerService {

    @Autowired
    CertificadoRepository certificadoRepository;

    @Autowired
    PeticionRepository peticionRepository;

    public PublicKey getPublicKey(Long id) throws CertificateException, KeyStoreException {
        KeyStore keystore;
        try {
            File file = ResourceUtils.getFile("classpath:springboot.p12");
            FileInputStream is = new FileInputStream(file);
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "password".toCharArray());
            String alias = "springboot";

            Certificate cert = keystore.getCertificate(alias);

            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            return publicKey;

        } catch (KeyStoreException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Boolean verifyMessage(Map<String, String> params) throws NumberFormatException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PublicKey publicKey = this.getPublicKey(Long.valueOf(params.get("idEmpleado")));

        String mensaje = params.get("camas") + params.get("mesas") + params.get("sillas")
                + params.get("sillones") + params.get("idEmpleado") + params.get("nonce");
        Signature sg = Signature.getInstance("SHA256withRSA");
        sg.initVerify(publicKey);
        sg.update(mensaje.getBytes());
        Boolean verificacion = sg.verify(params.get("firma").getBytes());

        return verificacion;
    }

    public Peticion savePeticion(Peticion peticion) {
        return this.peticionRepository.save(peticion);
    }

    public Integer findNonce(String nonce) {
        return this.peticionRepository.findNonce(nonce);
    }

    public Integer countPeticionesEn4Horas(Date start, Date end) {
        return this.peticionRepository.countPeticionesEn4Horas(start, end);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    // public Map<String, Object> savePeticion(Map<String,String> params) {
    // Map<String, Object> response = new HashMap<>();
    // List<String> arrayErrors = new ArrayList<>();
    // response.put("errors", arrayErrors);

    // Boolean verificacion = false;
    // Integer camas = Integer.valueOf(params.get("camas"));
    // Integer mesas = Integer.valueOf(params.get("mesas"));
    // Integer sillas = Integer.valueOf(params.get("sillas"));
    // Integer sillones = Integer.valueOf(params.get("sillones"));

    // Peticion peticion = new Peticion(camas, mesas, sillas, sillones, new Date(),
    // verificacion, params.get("nonce"));

    // // Poner límite de 3 peticiones en 4 horas
    // Integer nPeticiones = this.peticionRepository.countPeticionesEn4Horas(new
    // Date(),
    // new Date(System.currentTimeMillis() - (3600 * 1000 * 4)));
    // if (nPeticiones > 4) {
    // arrayErrors.add("Limite de peticiones en 4 horas alcanzado");
    // } else {
    // if (params.containsKey("camas") && params.containsKey("mesas") &&
    // params.containsKey("sillas") &&
    // params.containsKey("sillones") && params.containsKey("idEmpleado") &&
    // params.containsKey("firma")
    // && params.containsKey("nonce")) {

    // // Validación de mensaje (entre 0 y 300)
    // if (camas < 0 || camas > 300 || mesas < 0 || mesas > 300 || sillas < 0 ||
    // sillas > 300 || sillones < 0
    // || sillones > 300) {
    // arrayErrors.add("Los campos debes estar entre 0 y 300");
    // }

    // // Comprobar nonce
    // if (this.peticionRepository.findNonce(params.get("nonce")) > 0) {
    // arrayErrors.add("Nonce repetido");
    // }

    // // Con el id coger el certificado
    // Certificado certificado =
    // this.certificadoRepository.findById(Long.valueOf(params.get("idEmpleado"))).orElse(null);
    // if (certificado != null) {
    // String clavePublica = certificado.getClavePublica();
    // byte[] publicBytes = Base64.decodeBase64(clavePublica);
    // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
    // KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    // PublicKey pubKey = keyFactory.generatePublic(keySpec);

    // String mensaje = params.get("camas") + params.get("mesas") +
    // params.get("sillas")
    // + params.get("sillones") + params.get("idEmpleado") + params.get("nonce");
    // Signature sg = Signature.getInstance("SHA256withRSA");
    // sg.initVerify(pubKey);
    // sg.update(mensaje.getBytes());
    // verificacion = sg.verify(params.get("firma").getBytes());

    // if (!verificacion) {
    // arrayErrors.add("La firma no es correcta");
    // }

    // response.put("ok", verificacion);

    // } else {
    // arrayErrors.add("No existe empleado con ID: " + params.get("idEmpleado"));
    // }

    // } else {
    // arrayErrors.add("Entrada invalida");
    // }
    // this.serverService.savePeticion(peticion);
    // }

    // return response;
    // }
}