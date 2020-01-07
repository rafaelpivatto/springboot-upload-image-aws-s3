package com.example.demo.usecases;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class UploadImage {

    private final Logger LOG = LoggerFactory.getLogger(UploadImage.class);

    private final AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${img.size}")
    private int imageSize;

    @Autowired
    public UploadImage(final AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public URI execute(final MultipartFile multipartFile) {
        try {
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            final String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            final String fileName = uuid + "." + extension;
            final String contentType = multipartFile.getContentType();

            BufferedImage bf = ImageIO.read(multipartFile.getInputStream());

            bf = cropSquare(bf);
            bf = resize(bf);

            final InputStream is = getInputStream(bf, extension);

            return execute(is, fileName, contentType);
        } catch (final IOException e) {
            throw new FileException("IO error: " + e.getMessage());
        }
    }

    private URI execute(final InputStream is, final String fileName, final String contentType) {
        try {
            final ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            LOG.info("Iniciando upload");
            s3client.putObject(bucketName, fileName, is, meta);
            LOG.info("Iniciando finalizado");

            return s3client.getUrl(bucketName, fileName).toURI();
        } catch (final URISyntaxException e) {
            throw new FileException("Error to convert URL in URI");
        }
    }

    private InputStream getInputStream(final BufferedImage img, final String extension) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, extension, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    private BufferedImage cropSquare(final BufferedImage sourceImg) {
        final int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ?
                sourceImg.getHeight() : sourceImg.getWidth();
        return Scalr.crop(
                sourceImg,
                (sourceImg.getWidth() / 2) - (min / 2),
                (sourceImg.getHeight() / 2) - (min / 2),
                min,
                min);
    }

    private BufferedImage resize(final BufferedImage sourceImg) {
        return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, imageSize);
    }

}
