package com.example.demo.gateway.http;

import com.example.demo.domains.Image;
import com.example.demo.usecases.UploadImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/demoS3")
@Api(tags = "Operations for demo", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {

    private final UploadImage uploadImage;

    @Autowired
    public DemoController(final UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }

    @ApiOperation(value = "post to upload image")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = Image.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URI> upload(@RequestParam(name = "file") final MultipartFile file) {

        final URI uri = uploadImage.execute(file);

        return ResponseEntity.created(uri).build();
    }

}
