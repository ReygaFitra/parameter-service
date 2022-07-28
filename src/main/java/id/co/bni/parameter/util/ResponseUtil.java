package id.co.bni.parameter.util;

import id.co.bni.parameter.dto.ResponseService;

public class ResponseUtil {
    private ResponseUtil() {
    }

    public static ResponseService setResponse(RestConstants.RESPONSE response, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatus(response.getDescription());
        res.setStatusCode(response.getCode());
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponse(ResponseService responseService, RestConstants.RESPONSE response, Object obj, String message) {
        responseService.setStatusCode(response.getCode());
        responseService.setStatus(response.getDescription());
        responseService.setData(obj);
        responseService.setMessage(message);
        return responseService;
    }

    public static ResponseService setResponseSuccess(RestConstants.RESPONSE response, String reffNumber, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(response.getCode());
        res.setStatus(" Data berhasil disimpan, No.Referensi : " + reffNumber);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseSuccess(RestConstants.RESPONSE response, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(response.getCode());
        res.setStatus(" Data berhasil disimpan ");
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseSuccessCustom(RestConstants.RESPONSE response, Object obj, String status, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(response.getCode());
        res.setStatus(status);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseError(RestConstants.RESPONSE response, String desc, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(response.getCode());
        res.setStatus(" Gagal : " + desc);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseError(String code, String desc, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(code);
        res.setStatus(desc);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseErrorCustom(ResponseService res, RestConstants.RESPONSE response, String desc, Object obj, String message) {
        res.setStatusCode(response.getCode());
        res.setStatus(" Gagal : " + desc);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }
}
