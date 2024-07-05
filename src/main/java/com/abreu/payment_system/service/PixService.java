package com.abreu.payment_system.service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.abreu.payment_system.model.dto.pix.PixChargeRequestDTO;
import com.abreu.payment_system.pix.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PixService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;


    public JSONObject pixDetailCharge(String txid){

        JSONObject options = configuringJsonObject();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("txid", txid);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixDetailCharge", params, new JSONObject());

            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<JSONObject> pixListCharges(){

        JSONObject options = configuringJsonObject();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("inicio", "2021-04-01T16:01:35Z");
        params.put("fim", "2021-04-21T16:01:35Z");

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixListCharges", params, new JSONObject());
            System.out.println(response);
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public JSONObject pixCreateEVP(){

        JSONObject options = configuringJsonObject();

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateEvp", new HashMap<String,String>(), new JSONObject());
            System.out.println(response.toString());

            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public JSONObject pixCreateCharge(PixChargeRequestDTO pixChargeRequest){

        JSONObject options = configuringJsonObject();

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject().put("cpf", "12345678909").put("nome", "Francisco da Silva"));
        body.put("valor", new JSONObject().put("original", pixChargeRequest.valor()));
        body.put("chave", pixChargeRequest.chave());

        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(new JSONObject().put("nome", "Campo 1").put("valor", "Informação Adicional1 do PSP-Recebedor"));
        infoAdicionais.put(new JSONObject().put("nome", "Campo 2").put("valor", "Informação Adicional2 do PSP-Recebedor"));
        body.put("infoAdicionais", infoAdicionais);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateImmediateCharge", new HashMap<String,String>(), body);

            int idFromJson= response.getJSONObject("loc").getInt("id");
            pixGenerateQRCode(String.valueOf(idFromJson));

            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    private void pixGenerateQRCode(String id){

        JSONObject options = configuringJsonObject();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi= new EfiPay(options);
            Map<String, Object> response = efi.call("pixGenerateQRCode", params, new HashMap<String, Object>());

            System.out.println(response);

            File outputfile = new File("qrCodeImage.png");
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(((String) response.get("imagemQrcode")).split(",")[1]))), "png", outputfile);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(outputfile);
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private JSONObject configuringJsonObject(){
        Credentials credentials = new Credentials();

        JSONObject options = new JSONObject();
        options.put("client_id", clientId);
        options.put("client_secret", clientSecret);
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        return options;
    }
}