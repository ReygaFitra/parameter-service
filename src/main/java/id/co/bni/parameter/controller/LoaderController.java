package id.co.bni.parameter.controller;

import id.co.bni.parameter.cache.Loader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parameter")
public class LoaderController {
    private final Loader loader;

    @PostMapping(value = "/reload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseService create() {
        loader.load();
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, "", "Parameter Success Reload");
    }
}
